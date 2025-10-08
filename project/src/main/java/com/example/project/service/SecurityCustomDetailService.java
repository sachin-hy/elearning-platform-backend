package com.example.project.service;

import lombok.extern.slf4j.Slf4j;
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

import java.util.Optional;


@Service
@Slf4j
public class SecurityCustomDetailService implements UserDetailsService{


    @Autowired
	 private  UsersRepository usersRepo;


	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Users user = usersRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No User Present With Email id"));

         log.info("user login using email :{} " , user.getEmail());

		return User.builder()
		            .username(user.getEmail())
		            .password(user.getPassword())
		            .roles(user.getAccountType().name())  // This should be like "USER", "ADMIN", etc.
		            .build();
	}

}
