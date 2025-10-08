package com.example.project.filter;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.project.service.SecurityCustomDetailService;
import com.example.project.utilis.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;


@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private  SecurityCustomDetailService securityCustomDetailService;
    @Autowired
    private  JwtUtil jwtUtil;


	
	private Set<String> paths = Set.of("/auth/*","/login","/signup","/sendotp","/categories","/categories/*/courses","/course/courses/size","/chat/**","/app/**","/topic/**");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	    String path = request.getServletPath();
	   
	    AntPathMatcher matcher = new AntPathMatcher();
	    
	    for(String s:paths)
	    {
	    	if(matcher.match(s,path))
	    	{
	    		 
	    		 filterChain.doFilter(request, response);
		    	 return;
	    	}
	    	
	    }
	   
	    try {
	    	
	    	String authHeader = request.getHeader("Authorization");
	    	String token = null;
	    	String username = null;
	    	
	    	if(authHeader != null && authHeader.startsWith("Bearer"))
	    	{
	    		token = authHeader.substring(7);
	    		username=jwtUtil.extractUsername(token);
	    	}else
		    {
		    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    	response.getWriter().write("Missing or invalid Authorization header");
		    	return;
		    }
	    	
	    	if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
	    	{
	    		UserDetails userDetails = securityCustomDetailService.loadUserByUsername(username);
	    		
	    		if(jwtUtil.validateToken(token, userDetails))
	    		{
	    			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	    			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	    		}else {

	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("Invalid token");
	                return;
	            }
	    		
	    		
	    	}
	    	filterChain.doFilter(request, response);
	    	
	    }catch(ExpiredJwtException e)
	    {
	    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    	response.setContentType("application/json");
	    	response.getWriter().write("{\"error\": \"Token has expired\"}");
	    }
	    catch(SignatureException | MalformedJwtException e)
	    {
	    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"error\": \"Invalid token\"}");
	    }catch(Exception e)
	    {
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"error\": \"An unexpected error occurred\"}");
	    }
	    	
	    	
	    
		
	}
	
	
}
