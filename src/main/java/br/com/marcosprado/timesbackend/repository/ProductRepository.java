package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("""
            SELECT DISTINCT p FROM Product p
            WHERE (:brand IS NULL OR p.brand = :brand)
            AND (:style IS NULL OR p.style = :style)
            AND (:mechanism IS NULL OR p.mechanism = :mechanism)
            AND (:boxMaterial IS NULL OR p.boxMaterial = :boxMaterial)
            AND (:boxFormat IS NULL OR p.boxFormat = :boxFormat)
            AND (:dial IS NULL OR p.dial = :dial)
            AND (:line IS NULL OR p.line = :line)
           """)
    List<Product> findByPartialCharacteristics(
            @Param("brand") String brand,
            @Param("style") String style,
            @Param("mechanism") String mechanism,
            @Param("boxMaterial") String boxMaterial,
            @Param("boxFormat") String boxFormat,
            @Param("dial") String dial,
            @Param("line") String line
    );
}
