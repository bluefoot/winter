package info.bluefoot.winter.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import info.bluefoot.winter.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    public static User getCurrentLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    public static Collection<? extends GrantedAuthority> getDefaultUserAuthorities() {
        return new HashSet<SimpleGrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
