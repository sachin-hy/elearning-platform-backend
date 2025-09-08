package com.example.project.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.CourseResponseDto;
import com.example.project.dto.LoginDto;
import com.example.project.dto.LoginResponseDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;
import com.example.project.repository.UsersRepository;
import com.example.project.utilis.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;




@Service
public class UsersService {

	private final UsersRepository userRepo;
    private final AuthenticationManager authManager;
    private final SecurityCustomDetailService securityCustomDetailService;
    private final JwtUtil jwtUtil;

    public UsersService(UsersRepository userRepo, AuthenticationManager authManager,
                        SecurityCustomDetailService securityCustomDetailService, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.securityCustomDetailService = securityCustomDetailService;
        this.jwtUtil = jwtUtil;
    }
    
    
    
    
	@Transactional
	public UserResponseDto findUserByEmail(String email)
	{
		
		Users user = userRepo.findByEmail(email);
		
		return new UserResponseDto(user);
		
		
	}


	@Transactional
	public Users findByEmail(String email)
	{
	     return userRepo.findByEmail(email);	
	}
	

	
	
	@Transactional
	public String getPassword(String email) {
		
		String dbpassword = userRepo.findPasswordByEmail(email);
		return dbpassword;
	}

	
	
	@Transactional
	public String updatePassword(String email, String newPassword)  {
		
		 userRepo.updatePasswordByEmail(email,newPassword);
		
		 return newPassword;
	}


	@Transactional
	public Users findBytoken(String token) {
		
		return userRepo.findByToken(token);
	}



	@Transactional
	
	public void deleteByEmail(String email) {
		// TODO Auto-generated method stub
		userRepo.deleteByEmail(email);
	}






	@Transactional
	public List<CourseResponseDto> findCoursesEnrolled(String email) {
		// TODO Auto-generated method stub
		List<Courses> list = userRepo.findCoursesEnrolled(email);
		 
		return list.stream().map(course -> new CourseResponseDto(course)).collect(Collectors.toList());
	}



    @Transactional
	public void setUserToken(String email,String token) {
		// TODO Auto-generated method stub
		Users user = userRepo.findByEmail(email);
		 user.setToken(token);
	     user.setResetPasswordExpires(LocalDateTime.now().plusMinutes(10));
	}


    
    @Transactional
     public LoginResponseDto checkCredentials(LoginDto logindto,HttpServletRequest request)
     {
    	   LoginResponseDto res = null;
    	    String email =logindto.email();
			String password = logindto.password();
			
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
			String jwt="";
			
			if(auth.isAuthenticated())
			{
				
				UserDetails userDetails = securityCustomDetailService.loadUserByUsername(email);
			    
				
				
				jwt = jwtUtil.generateToken(userDetails.getUsername());
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
				
			
				Users user = userRepo.findByEmail(email);
			
				UserResponseDto u = new UserResponseDto(user);
				
				res = new LoginResponseDto(u,jwt);
				
			
			}
			
			return res;
			
     }


	
	
}
