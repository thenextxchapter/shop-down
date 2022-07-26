package com.shopdown.admin.category.repository;

import java.util.List;

import com.shopdown.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	List<Category> listRootCategories();

	Category findByName(String name);

	Category findByAlias(String alias);
}
