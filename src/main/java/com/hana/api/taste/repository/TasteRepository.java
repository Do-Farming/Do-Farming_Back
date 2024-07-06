package com.hana.api.taste.repository;

import com.hana.api.taste.entity.Taste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteRepository extends JpaRepository<Taste, Long> {
    @Query("SELECT t FROM taste t where t.tasteCategory = :category")
    List<Taste> findTasteByTasteCategory(@Param(value = "category") String category);
}