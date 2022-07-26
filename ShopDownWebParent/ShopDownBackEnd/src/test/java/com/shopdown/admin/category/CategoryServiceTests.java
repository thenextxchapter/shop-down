package com.shopdown.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.shopdown.admin.category.repository.CategoryRepository;
import com.shopdown.admin.category.service.CategoryService;
import com.shopdown.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@MockBean
	private CategoryRepository repo;
	/* At runtime, the Mockito framework will inspect the category repository
	* interface and then create a fake object of the CategoryRepository type */

	@InjectMocks
	private CategoryService service;
	/* At runtime, Mockito will create the fake categoryRepository object and inject
	* it into the categoryService because in the actual categoryService class we have
	* a reference to the categoryRepository */

	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id = null; // Creating a new category mode
		String name = "Computers";
		String alias = "abc";

		/* Creating a new category object */
		Category category = new Category(id, name, alias);

		/* Specify the return value for the findByName method of the mock object
		* So when the repo.findByName(name) method with the given name is called,
		* we return the category object
		* */
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlias() {
		Integer id = null; // Creating a new category mode
		String name = "NameABC";
		String alias = "Books";

		Category category = new Category(id, name, alias);

		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	}

	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null; // Creating a new category mode
		String name = "NameABC";
		String alias = "Books";

//		Category category = new Category(id, name, alias);

		/* We're checking for when no existing category in the database
		* has the given name or alias */
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		Integer id = 1; // Creating a new category mode
		String name = "Computers";
		String alias = "abc";

		Category category = new Category(2, name, alias);

		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlias() {
		Integer id = 1; // Creating a new category mode
		String name = "NameABC";
		String alias = "Books";

		Category category = new Category(2, name, alias);

		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	}

	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1; // Creating a new category mode
		String name = "NameABC";
		String alias = "Books";

		Category category = new Category(1, name, alias);

		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("OK");
	}


}
