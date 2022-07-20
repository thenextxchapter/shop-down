package com.shopdown.admin.security;

import com.shopdown.admin.user.UserRepository;
import com.shopdown.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopDownUserDetailsService implements UserDetailsService {

	/* This class is called by Spring Security when it is performing the authentication process */
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);

		if (user != null) {
			return new ShopDownUserDetails(user);
		}

		throw new UsernameNotFoundException("Could not find user with email: " + email);
	}
}
