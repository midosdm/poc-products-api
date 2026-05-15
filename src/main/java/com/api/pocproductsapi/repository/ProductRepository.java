package com.api.pocproductsapi.repository;

import com.api.pocproductsapi.dto.product.ProductSummaryResponse;
import com.api.pocproductsapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(
            """
	SELECT new com.api.pocproductsapi.dto.product.ProductSummaryResponse(

		p.id,
		p.name,
		p.code,
		p.description,
		p.price,
		p.category,
		p.image,
		p.quantity,
		p.rating
	)
	FROM Product p
WHERE (
		COALESCE(:search, '') = ''
		OR
		LOWER(p.name)
			LIKE LOWER(CONCAT('%', :search, '%'))
		OR
		LOWER(p.code)
			LIKE LOWER(CONCAT('%', :search, '%'))
	)

	AND (
		COALESCE(:category, '') = ''
		OR
		LOWER(p.category)
			LIKE LOWER(CONCAT('%', :category, '%'))
	)
""")
    Page<ProductSummaryResponse> listProducts(
            @Param("search") String search, @Param("category") String category, Pageable pageable);

    boolean existsByCode(String code);
}
