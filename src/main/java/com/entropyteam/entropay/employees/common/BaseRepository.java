package com.entropyteam.entropay.employees.common;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, K> extends JpaRepository<T, K> {
    List<T> findAllByDeletedIsFalse();
}
