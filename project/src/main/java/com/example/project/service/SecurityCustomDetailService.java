package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.Users;
import com.example.project.enums.AccountType;
import com.example.project.repository.UsersRepository;


@Service
public class SecurityCustomDetailService implements UserDetailsService{

	
	 private final UsersRepository usersRepo;

	    public SecurityCustomDetailService(UsersRepository usersRepo) {
	        this.usersRepo = usersRepo;
	    }
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Users user = usersRepo.findByEmail(email);
		System.out.println("users data = " + user.getEmail() + user.getFirstName() + user.getLastName() + user.getPassword());
		System.out.println("=----------------after user---------");
		return User.builder()
		            .username(user.getEmail())
		            .password(user.getPassword())
		            .roles(user.getAccountType().name())  // This should be like "USER", "ADMIN", etc.
		            .build();
	}

}
