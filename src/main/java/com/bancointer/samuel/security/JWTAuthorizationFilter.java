package com.bancointer.samuel.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bancointer.samuel.utils.MessageUtils;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	
	private MessageUtils messageUtils;
	
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService,MessageUtils messageUtils) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		this.messageUtils=messageUtils;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
		
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}else {
	            response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            response.setContentType(MediaType.APPLICATION_JSON_VALUE); 
	            response.getWriter().append(json(request));
	            return;
			}
		}
		chain.doFilter(request, response);
	}
    private String json(HttpServletRequest request) {
        return "{\"timestamp\": " + new Date().getTime() + ", "
            + "\"status\": 401, "
            + "\"error\": \""+messageUtils.getMessageEnglish("authentication.exception.title")+"\", "
            + "\"message\": \""+messageUtils.getMessageEnglish("authentication.exception")+"\", "
            + "\"details\":  \""+messageUtils.getMessageEnglish("authentication.exception.details")+"\", "
            + "\"path\": \"/"+request.getRequestURL().toString()+"\"}";
    }
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}
