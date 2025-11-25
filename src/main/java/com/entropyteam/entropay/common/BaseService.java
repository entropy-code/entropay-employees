package com.entropyteam.entropay.common;

import static com.entropyteam.entropay.common.ReactAdminMapper.DATE_FROM_TERM_KEY;
import static com.entropyteam.entropay.common.ReactAdminMapper.DATE_TO_TERM_KEY;
import static com.entropyteam.entropay.common.ReactAdminMapper.SEARCH_TERM_KEY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.entropyteam.entropay.employees.models.Contract;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class BaseService<Entity extends BaseEntity, DTO, Key> implements CrudService<DTO, Key> {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ID = "id";

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<Entity> entityClass;
    private final ReactAdminMapper mapper;

    protected BaseService(Class<Entity> clazz, ReactAdminMapper mapper) {
        this.entityClass = clazz;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Optional<DTO> findOne(Key id) {
        return getRepository().findById(id)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public Page<DTO> findAllActive(ReactAdminParams params) {
        try {
            Filter filter = mapper.buildFilter(params, entityClass);
            Pageable pageable = mapper.buildPageable(params, entityClass);
            Session session = entityManager.unwrap(Session.class);
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Entity> entityQuery = cb.createQuery(entityClass);
            Root<Entity> root = entityQuery.from(entityClass);

            List<Predicate> predicates = getPredicates(cb, root, filter);
            entityQuery.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

            Optional<Order> order = pageable.getSort().stream().findFirst();
            if (order.isPresent()) {
                String sortField = order.get().getProperty();
                entityQuery.orderBy(order.get().getDirection().isAscending() ? cb.asc(root.get(sortField))
                        : cb.desc(root.get(sortField)));
            }

            Query<Entity> query = session.createQuery(entityQuery);
            if (pageable.isPaged()) {
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
            }

            configureEntityGraph(query);

            // Get entity response
            List<DTO> entitiesResponse = query.getResultList().stream().map(this::toDTO).collect(Collectors.toList());

            // Count query
            CriteriaBuilder countBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = countBuilder.createQuery(Long.class);
            Root<Entity> rootForCount = countQuery.from(entityClass);
            root.getJoins().forEach(join -> rootForCount.join(join.getAttribute().getName()));
            List<Predicate> countPredicates = getPredicates(countBuilder, rootForCount, filter);
            countQuery.select(countBuilder.count(rootForCount))
                    .where(countBuilder.and(countPredicates.toArray(new Predicate[0])));

            Long count = session.createQuery(countQuery).getSingleResult();

            return new PageImpl<>(entitiesResponse, Pageable.unpaged(), count);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidRequestParametersException("Bad params error", e);
        }
    }

    private void configureEntityGraph(Query<Entity> query) {
        getEntityGraphName().ifPresent(entityGraphName -> {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph(entityGraphName);
            query.setHint("jakarta.persistence.fetchgraph", entityGraph);
        });
    }

    private List<Predicate> getPredicates(CriteriaBuilder cb, Root<Entity> root, Filter filter) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("deleted"), false));
        predicates.addAll(buildIdPredicates(root, filter));
        predicates.addAll(buildEntityPredicates(root, filter, cb));
        predicates.addAll(buildEntityRelatedPredicates(root, filter, cb));
        predicates.addAll(buildCustomFieldsPredicates(root, filter, cb));
        return predicates;
    }

    private Collection<Predicate> buildIdPredicates(Root<Entity> root, Filter filter) {
        if (MapUtils.isEmpty(filter.getGetByIdsFilter())) {
            return CollectionUtils.emptyCollection();
        }

        return filter.getGetByIdsFilter().entrySet().stream()
                .map(f -> root.get(f.getKey()).in(f.getValue())).toList();
    }

    private Collection<Predicate> buildEntityPredicates(Root<Entity> root, Filter filter, CriteriaBuilder cb) {
        if (MapUtils.isEmpty(filter.getGetByFieldsFilter()) && MapUtils.isEmpty(filter.getGetByDateFieldsFilter())) {
            return CollectionUtils.emptyCollection();
        }
        Collection<Predicate> predicates = filter.getGetByFieldsFilter().entrySet().stream()
                .filter(f -> !SEARCH_TERM_KEY.equals(f.getKey()))
                .map(f -> cb.equal(root.get(f.getKey()), f.getValue()))
                .collect(Collectors.toSet());

        if (filter.getGetByDateFieldsFilter().containsKey(DATE_TO_TERM_KEY) && filter.getGetByDateFieldsFilter()
                .containsKey(DATE_FROM_TERM_KEY)) {
            ArrayList<Predicate> searchPredicates = new ArrayList<>();
            for (String column : getDateColumnsForSearch()) {
                LocalDate dateFrom = filter.getGetByDateFieldsFilter().get(DATE_FROM_TERM_KEY);
                LocalDate dateTo = filter.getGetByDateFieldsFilter().get(DATE_TO_TERM_KEY);
                Predicate predicate = cb.between(root.get(column), dateFrom, dateTo);
                searchPredicates.add(predicate);
            }
            if (CollectionUtils.isNotEmpty(searchPredicates)) {
                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }
        }

        ArrayList<Predicate> searchPredicates = new ArrayList<>();
        if (filter.getGetByFieldsFilter().containsKey(SEARCH_TERM_KEY)) {
            String searchInput = filter.getGetByFieldsFilter().get(SEARCH_TERM_KEY).toString().toLowerCase();
            for (String column : getColumnsForSearch()) {
                Predicate searchContainsColumn = cb.like(cb.lower(root.get(column)), "%" + searchInput + "%");
                searchPredicates.add(searchContainsColumn);
            }
            if (CollectionUtils.isNotEmpty(searchPredicates)) {
                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }
        }

        return predicates;
    }

    private Collection<Predicate> buildEntityRelatedPredicates(Root<Entity> root, Filter filter, CriteriaBuilder cb) {
        if (MapUtils.isEmpty(filter.getGetByRelatedFieldsFilter()) && MapUtils.isEmpty(filter.getGetByFieldsFilter())) {
            return CollectionUtils.emptyCollection();
        }

        Collection<Predicate> predicates = filter.getGetByRelatedFieldsFilter().entrySet().stream()
                .map(f -> cb.equal(root.get(f.getKey()).get(ID), f.getValue())).collect(Collectors.toSet());
        ArrayList<Predicate> searchPredicates = new ArrayList<>();
        if (filter.getGetByFieldsFilter().containsKey(SEARCH_TERM_KEY)) {
            String searchInput = filter.getGetByFieldsFilter().get(SEARCH_TERM_KEY).toString().toLowerCase();
            for (Map.Entry<String, List<String>> relatedEntity : getRelatedColumnsForSearch().entrySet()) {
                Join<?, Contract> join = root.join(relatedEntity.getKey());
                for (String column : relatedEntity.getValue()) {
                    Predicate searchContainsColumn = cb.like(cb.lower(join.get(column)), "%" + searchInput + "%");
                    searchPredicates.add(searchContainsColumn);
                }
            }
            if (CollectionUtils.isNotEmpty(searchPredicates)) {
                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }
        }

        return predicates;
    }

    @Override
    @Transactional
    public DTO delete(Key id) {
        Entity entity = getRepository().findById(id).orElseThrow();
        entity.setDeleted(true);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public DTO create(DTO entity) {
        Entity entityToCreate = toEntity(entity);
        Entity savedEntity = getRepository().save(entityToCreate);
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public DTO update(Key id, DTO entity) {
        Entity entityToUpdate = toEntity(entity);
        entityToUpdate.setId((UUID) id);
        Entity savedEntity = getRepository().save(entityToUpdate);
        return toDTO(savedEntity);
    }

    protected abstract BaseRepository<Entity, Key> getRepository();

    protected abstract DTO toDTO(Entity entity);

    protected abstract Entity toEntity(DTO entity);

    protected List<String> getColumnsForSearch() {
        return Collections.emptyList();
    }

    protected Map<String, List<String>> getRelatedColumnsForSearch() {
        return Collections.emptyMap();
    }

    protected List<String> getDateColumnsForSearch() {
        return Collections.emptyList();
    }

    protected Optional<String> getEntityGraphName() {
        return Optional.empty();
    }

    protected Collection<Predicate> buildCustomFieldsPredicates(Root<Entity> root, Filter filter, CriteriaBuilder cb) {
        return CollectionUtils.emptyCollection();
    }
}
