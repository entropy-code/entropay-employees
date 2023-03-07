package com.entropyteam.entropay.common;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;
import java.util.Comparator;
import java.util.Collections;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.auth.AppRole;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;

public abstract class BaseService<Entity extends BaseEntity, DTO, Key> implements CrudService<DTO, Key> {

    public static final String ID = "id";

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<Entity> entityClass;
    private final ReactAdminMapper mapper;
    public static final String SEARCH_TERM_KEY = ReactAdminMapper.SEARCH_TERM_KEY;

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

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), false));
            predicates.addAll(buildIdPredicates(root, filter));
            predicates.addAll(buildEntityPredicates(root, filter, cb));
            predicates.addAll(buildEntityRelatedPredicates(root, filter, cb));
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

            // Get entity response
            List<DTO> entitiesResponse = query.getResultList().stream().map(this::toDTO).collect(Collectors.toList());

            // Count query
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Entity> rootForCount = countQuery.from(entityClass);
            countQuery.select(cb.count(rootForCount))
                    .where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            Long count = session.createQuery(countQuery).getSingleResult();

            return new PageImpl<DTO>(entitiesResponse, Pageable.unpaged(), count);

        } catch (Exception e) {
            throw new InvalidRequestParametersException("Bad params error: invalid params", e);
        }
    }

    private Collection<Predicate> buildIdPredicates(Root<Entity> root, Filter filter) {
        if (MapUtils.isEmpty(filter.getGetByIdsFilter())) {
            return CollectionUtils.emptyCollection();
        }

        return filter.getGetByIdsFilter().entrySet().stream()
                .map(f -> root.get(f.getKey()).in(f.getValue())).toList();
    }

    private Collection<Predicate> buildEntityPredicates(Root<Entity> root, Filter filter, CriteriaBuilder cb) {
        if (MapUtils.isEmpty(filter.getGetByFieldsFilter())) {
            return CollectionUtils.emptyCollection();
        }
        Collection<Predicate> predicates = filter.getGetByFieldsFilter().entrySet().stream().filter(f -> f.getKey() != SEARCH_TERM_KEY)
                .map(f -> cb.equal(root.get(f.getKey()), f.getValue())).collect(Collectors.toSet());


        if(filter.getGetByFieldsFilter().containsKey(SEARCH_TERM_KEY)){
            String searchInput = filter.getGetByFieldsFilter().get(SEARCH_TERM_KEY).toLowerCase();
            ArrayList<Predicate> searchPredicates = new ArrayList<>();
            for(String column: getColumnsForSearch()){
                Predicate searchContainsColumn = cb.like(cb.lower(root.get(column)), "%"+searchInput+"%");
                searchPredicates.add(searchContainsColumn);
            }
            predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
        }
        return predicates;
    }

    private Collection<Predicate> buildEntityRelatedPredicates(Root<Entity> root, Filter filter, CriteriaBuilder cb) {
        if (MapUtils.isEmpty(filter.getGetByRelatedFieldsFilter())) {
            return CollectionUtils.emptyCollection();
        }

        return filter.getGetByRelatedFieldsFilter().entrySet().stream()
                .map(f -> cb.equal(root.get(f.getKey()).get(ID), f.getValue())).collect(Collectors.toSet());
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

    public AppRole getUserRole() {
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication()
                        .getAuthorities();
        Optional<AppRole> appRole = authorities.stream().map(a -> AppRole.getByValue(a.getAuthority()))
                .min(Comparator.comparing(r -> r.score));
        return appRole.orElseThrow();
    }

    @Transactional
    public List<String> getColumnsForSearch() {
        return Collections.emptyList();
    }
}
