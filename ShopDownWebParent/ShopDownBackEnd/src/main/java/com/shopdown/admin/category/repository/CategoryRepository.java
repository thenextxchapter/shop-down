package com.shopdown.admin.category.repository;

import java.util.List;

import com.shopdown.common.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	List<Category> listRootCategories(Sort sort);

	Long countById(Integer id);

	Category findByName(String name);

	Category findByAlias(String alias);

	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	void updateEnabledStatus(Integer id, boolean enabled);
}
