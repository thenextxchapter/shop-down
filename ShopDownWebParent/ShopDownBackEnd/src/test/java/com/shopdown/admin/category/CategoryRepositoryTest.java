package com.shopdown.admin.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Set;

import com.shopdown.admin.category.repository.CategoryRepository;
import com.shopdown.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false) // Because it's a spring data JPA test
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Because we want to run the
// test against the real database, not the in-memory database, which is used by default
@Rollback(false)  // Because we want to keep the data persisted in the database
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;

	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Clothing");
		Category savedCategory = repo.save(category);

		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory() {
		/* We need to retrieve the parent category by Id */
		Category parent = new Category(7);
		Category subCategory = new Category("iPhone", parent);

		Category savedCategory = repo.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testGetCategory() {
		Category category = repo.findById(1).get();
		System.out.println(category.getName());

		Set<Category> children = category.getChildren();

		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}

		assertThat(children.size()).isGreaterThan(0);
	}

	@Test
	public void tesPrintHierarchicalCategories() {
		Iterable<Category> categories = repo.findAll();

		for (Category category : categories) {
			if (category.getParent() == null) {
				/* Parent category */
				System.out.println(category.getName());

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					System.out.println("  " + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}

	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.print("  ");
			}
			System.out.println(subCategory.getName());

			printChildren(subCategory, newSubLevel);
		}
	}

	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repo.listRootCategories(Sort.by("name").ascending());
		rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}

	@Test
	public void testFindByName() {
		String name = "Computers1";
		Category category = repo.findByName(name);

		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}

	@Test
	public void testFindByAlias() {
		String alias = "Electronics";
		Category category = repo.findByAlias(alias);

		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);
	}
}
