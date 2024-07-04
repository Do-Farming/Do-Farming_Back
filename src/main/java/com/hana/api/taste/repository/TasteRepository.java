package com.hana.api.taste.repository;

import com.hana.api.taste.entity.Taste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteRepository extends JpaRepository<Taste, Long> {

}