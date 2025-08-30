package com.example.project.service;








import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.AllRatingResponseDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.dto.LoginDto;
import com.example.project.dto.LoginResponseDto;
import com.example.project.dto.SectionResponseDto;
import com.example.project.dto.Signupdto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.dto.UpdateProfileRequest;
import com.example.project.dto.UserResponseDto;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Courses;
import com.example.project.entity.Profile;
import com.example.project.entity.RatingAndReviews;
import com.example.project.entity.Users;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.RatingAndReviewsRepository;
import com.example.project.repository.UsersRepository;
import com.example.project.utilis.JwtUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;




@Service
public class UsersService {

	@Autowired
	private UsersRepository userRepo;
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RatingAndReviewsRepository ratingAndReviewsRepo;
	
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private SecurityCustomDetailService securityCustomDetailService; 
	@Autowired
	private JwtUtil jwtUtil;
	
	@Transactional
	public UserResponseDto findUserByEmail(String email)
	{
		System.out.println("find user by email is called =====");
		Users user = userRepo.findByEmail(email);
		
		return new UserResponseDto(user);
		
		
	}


	@Transactional
	public Users findByEmail(String email)
	{
	     return userRepo.findByEmail(email);	
	}
	
//	@Transactional
//	public UserResponseDto saveUsersDetials(Signupdto signupdto) {
//		// TODO Auto-generated method stub
//		
//		Users user = new Users();
//	
//		user.setImage("https://api.dicebear.com/5.x/initials/svg?seed="+signupdto.firstName()+" "+signupdto.lastName());
//	    user.setFirstName(signupdto.firstName());
//	    user.setLastName(signupdto.lastName());
//	    user.setEmail(signupdto.email());
//	    user.setPassword(passwordEncoder.encode(signupdto.password()));
//	    user.setAccountType(null);
//	    user.setAccountType(signupdto.accountType());
//	    userRepo.save(user);
//	    return new UserResponseDto(user);
//	}

	
	
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





//	@Transactional
//	public List<CourseResponseDto> findInstructorCourseByEmail(String email) {
//		// TODO Auto-generated method stub
//		 List<Courses> list = userRepo.findInstructorCoursesByEmail(email);
//		 
//		 return list.stream().map(course -> new CourseResponseDto(course)).collect(Collectors.toList());
//	}


	@Transactional
	public List<CourseResponseDto> findCoursesEnrolled(String email) {
		// TODO Auto-generated method stub
		List<Courses> list = userRepo.findCoursesEnrolled(email);
		 
		return list.stream().map(course -> new CourseResponseDto(course)).collect(Collectors.toList());
	}



//	@Transactional
//	public List<CourseResponseDto> enrollStudentInCourse(Long courseId,String email) {
//		// TODO Auto-generated method stub
//		
//		
//		return null;
//	}


	

//    @Transactional
//	public void saveRatingAndReview(String rating, String review, Users user, Courses course) {
//		// TODO Auto-generated method stub
//		
//		 RatingAndReviews ratingAndReviews = new RatingAndReviews();
//		 
//		 ratingAndReviews.setRating(Integer.parseInt(rating));
//		 ratingAndReviews.setReview(review);
//		 ratingAndReviews.setUser(user);
//		 ratingAndReviews.setCourse(course);
//		 RatingAndReviews savedRating = ratingAndReviewsRepo.save(ratingAndReviews);
//		 
//		 user.getRatingAndReviews().add(savedRating);
//		 course.getRatingAndReviews().add(savedRating);
//		 
//		 userRepo.save(user);
//	     courseRepo.save(course);
//		 
//	}


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
//    @Transactional
//    public List<Map<String, Object>> ChatRooms(String email) {
//        List<ChatRoom> list = userRepo.findChatRoomsByEmail(email);
//
//        System.out.println("chat room list isze in userservic chatroom = " + list.size());
//        return list.stream()
//            .map(room -> {
//                Map<String, Object> map = new HashMap<>();
//                map.put("roomId", room.getId());
//                map.put("roomName", room.getRoomName());
//                map.put("roomImageUrl", room.getCourseImageUrl());
//                return map;
//            })
//            .collect(Collectors.toList());
//    }

	

	
	
}
