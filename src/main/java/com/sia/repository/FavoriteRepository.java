package com.sia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sia.entity.Favorite;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    Page<Favorite> findByUser_Id(Integer userId, Pageable pageable);

    Optional<Favorite> findByUser_IdAndAd_Id(Integer userId, Integer AdId);

    void deleteByUser_IdAndAd_Id(Integer userId, Integer AdId);
}
