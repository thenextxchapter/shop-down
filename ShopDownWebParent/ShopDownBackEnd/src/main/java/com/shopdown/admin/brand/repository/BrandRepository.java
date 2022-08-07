package com.shopdown.admin.brand.repository;

import java.util.List;

import com.shopdown.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

	Long countById(Integer id);

	Brand findByName(String name);

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	Page<Brand> findAll(String keyword, Pageable pageable);

	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();
}
