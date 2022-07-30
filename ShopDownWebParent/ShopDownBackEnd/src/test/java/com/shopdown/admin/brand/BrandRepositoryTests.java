package com.shopdown.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import com.shopdown.admin.brand.repository.BrandRepository;
import com.shopdown.common.entity.Brand;
import com.shopdown.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {

	@Autowired
	BrandRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateBrandWithOneCategory() {
		Category category = entityManager.find(Category.class, 6);
		Brand acer = new Brand("Acer", "acer.png");
		acer.addCategory(category);

		Brand savedBrand = repo.save(acer);
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrandWithTwoCategories() {
		Brand samsung = new Brand("Samsung", "samsung.png");
		Category memory = new Category(10);
		Category drive = new Category(19);

		samsung.addCategory(memory);
		samsung.addCategory(drive);

		Brand savedBrand = repo.save(samsung);
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllBrands() {
		Iterable<Brand> listBrands = repo.findAll();
		listBrands.forEach(System.out::println);
	}

	@Test
	public void testGetBrandById() {
		Brand brand = repo.findById(1).get();
		System.out.println(brand);
		assertThat(brand).isNotNull();
	}

	@Test
	public void testUpdateBrandDetails() {
		Brand brand = repo.findById(3).get();
		brand.setName("Samsung Electronics");

		repo.save(brand);
		System.out.println(brand.getName());
	}

	@Test
	public void testDeleteBrand() {
		Integer brandId = 2;
		repo.deleteById(brandId);
		assertThat(repo.findById(2)).isEmpty();
	}
}
