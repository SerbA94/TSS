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
	public String mainPage() {
		
		return "mainPage";
	}

	@GetMapping("/login")
	public String loginPage() {	
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		}
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String signOutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/";
		
	}
	
	private boolean isCurrentAuthenticationAnonymous() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    return auth.getPrincipal().equals("anonymousUser");
	}

}
