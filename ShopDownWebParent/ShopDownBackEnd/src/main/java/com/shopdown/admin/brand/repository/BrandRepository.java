package com.shopdown.admin.brand.repository;

import com.shopdown.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

	Long countById(Integer id);

	Brand findByName(String name);
}
