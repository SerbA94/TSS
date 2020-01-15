package ua.tss.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ua.tss.model.User;
import ua.tss.service.UserService;

public abstract class SuperController {

	protected Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected boolean isCurrentAuthenticationAnonymous() {
		return getCurrentAuthentication().getPrincipal().equals("anonymousUser");
	}

	protected User getCurrentUser(UserService userService) {
		Object principal = getCurrentAuthentication().getPrincipal();
		if (principal instanceof User) {
			Long id = ((User) getCurrentAuthentication().getPrincipal()).getId();
			return userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		}
		return null;
	}

}
