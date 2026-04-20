package com.sia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sia.entity.Ad;
import com.sia.entity.AdStatus;

import java.math.BigDecimal;

/**
 * Репозиторий для работы с объявлениями.
 * Обеспечивает доступ к данным в БД.
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    Page<Ad> findByUser_Id(Integer userId, Pageable pageable);

    Page<Ad> findByCategory_Id(Integer categoryId, Pageable pageable);

    Page<Ad> findByStatus(AdStatus status, Pageable pageable);

    Page<Ad> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    Page<Ad> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
