package com.shopdown.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.shopdown.admin.user.repository.RoleRepository;
import com.shopdown.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repo;

	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Manages Everything");
		Role savedRole = repo.save(roleAdmin);

		assertThat(savedRole.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Sales Person", "Manages product price, "
				+ "customers, shipping, orders and sales report");

		Role roleEditor = new Role("Editor", "Manages categories, brands, "
				+ "products, articles and menus");

		Role roleShipper = new Role("Shipper", "View products, view orders "
				+ "and update order status");

		Role roleAssistant = new Role("Assistant", "Manages questions and reviews");

		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
}
