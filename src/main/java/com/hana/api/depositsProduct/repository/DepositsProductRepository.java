package com.hana.api.depositsProduct.repository;

import com.hana.api.depositsProduct.entity.DepositsProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositsProductRepository extends JpaRepository<DepositsProduct, Long> {
    @Query("select s from deposits_product s where s.finPrdtNm like %:searchword%")
    List<DepositsProduct> findByserchwordLike(@Param("searchword") String searchword);
}