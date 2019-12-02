package ua.tss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
	
	@GetMapping("/")
	public String index() {
		
		return "index";
	}

	@GetMapping("/login")
	public String login() {	
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		}
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/";
	}
	
	
	
	private Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private boolean isCurrentAuthenticationAnonymous() {
		return getCurrentAuthentication().getPrincipal().equals("anonymousUser");
	}

}
