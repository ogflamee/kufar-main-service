package com.sia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sia.entity.Category;

import java.util.Optional;

/**
 * Репозиторий для работы с категориями.
 * Обеспечивает доступ к данным в БД.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);
}
