package info.bluefoot.winter.controller;

import info.bluefoot.winter.model.WinterUser;

import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    public static WinterUser getCurrentLoggedUser() {
        return (WinterUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
