package com.shopdown.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.shopdown.common.entity.Role;
import com.shopdown.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ShopDownUserDetails implements UserDetails {

	private User user;

	public ShopDownUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		/* Will return a collection of no granted authority or a list of
		* roles assigned to this user */
		Set<Role> roles = user.getRoles();

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		/* This method will be used by spring security to get the list of
		assigned roles to the user */
		return authorities;
	}

	@Override
	public String getPassword() {
		/* Here we return the value of the password of the user */
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		/* Meaning that the account is not expired */
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public String getFirstName() {
		return this.user.getFirstName();
	}

	public void setFirstName(String firstName) {
		this.user.setFirstName(firstName);
	}
}
