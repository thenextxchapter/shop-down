package com.shopdown.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.shopdown.admin.user.repository.UserRepository;
import com.shopdown.common.entity.Role;
import com.shopdown.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

	@Autowired
	UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userNony = new User("chinonso.ata@gmail.com", "redcarpet", "Chinonso", "Ata");
		userNony.addRole(roleAdmin);

		User savedUser = repo.save(userNony);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateUserWithTwoRoles() {
		User userKosi = new User("keloasuzu@gmail.com", "kosi123", "Kosisochukwu", "Asuzu");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		userKosi.addRole(roleEditor);
		userKosi.addRole(roleAssistant);

		User savedUser = repo.save(userKosi);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(System.out::println);
	}

	@Test
	public void testGetUserById() {
		User userNony = repo.findById(1).get();
		System.out.println(userNony);
		assertThat(userNony).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		User userNony = repo.findById(1).get();
		userNony.setEnabled(true);
		userNony.setEmail("chinonsoata@yahoo.com");

		repo.save(userNony);
	}

	@Test
	public void testUpdateUserRoles() {
		User userKosi = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);

		userKosi.getRoles().remove(roleEditor);
		userKosi.addRole(roleSalesPerson);

		repo.save(userKosi);
	}

	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "chinonsoata@yahoo.com";
		User user = repo.getUserByEmail(email);

		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);

		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDiableUser() {
		Integer id = 4;
		repo.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUsers() {
		String keyword = "Bruce";

		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isGreaterThan(0);
	}
}
