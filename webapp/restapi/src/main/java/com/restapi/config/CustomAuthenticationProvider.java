package com.restapi.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.restapi.model.User;
import com.restapi.services.LoginService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    public CustomAuthenticationProvider() {
        super();
    }
    
 @Autowired
 private LoginService loginService;
 


@Override
public Authentication authenticate(Authentication auth) 
{
	System.out.println("inside CustomAuthenticationProvider");
	try
	{
		String username="";
		String password="";
		//UsernamePasswordAuthenticationToken resultAuth = new UsernamePasswordAuthenticationToken(username, password);
		try
		{
			  username = String.valueOf(auth.getName());
			  password = String.valueOf(auth.getCredentials().toString());
			  System.out.println(username+"   "+password);
			
		}
		catch (Exception e) {
			System.out.println("Exception in parsing password:"+e.getMessage());
		}
	   
		try
		{
		    boolean authenticate = this.loginService.checkUser(username, password);
		    System.out.println("loginService result:"+authenticate);
		    if (authenticate)
		    {
//		    	return new UsernamePasswordAuthenticationToken(username, password);
		    	Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		    	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password , authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return authenticationToken;
		    }
		    else
		    	throw new BadCredentialsException("invalid credentials");
		}
		catch (Exception e) {
			System.out.println("Exception in validating  CustomAuthenticationProvider:"+e.getMessage());
			throw new BadCredentialsException("invalid provider");
		}

	    
	}
	catch(Exception e){
		System.out.println("Exception in CustomAuthenticationProvider:"+e.getMessage());
		throw new BadCredentialsException("invalid provider");
	}

    
}

@Override
public boolean supports(Class<? extends Object> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
}




}