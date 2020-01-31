package ua.tss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.tss.model.Cart;
import ua.tss.service.CartService;

@Controller
public class MainController extends SuperController {

	@Autowired
	private CartService cartService;


	@GetMapping("/")
	public String index(Model model, HttpServletRequest request) {
		Cart cart = cartService.findBySessionId(request.getSession(true).getId());
		model.addAttribute("cart",cart);
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

	private void printAllActiveCarts() {
		System.out.println("************************************ ACTIVE CARTS START *************************************");
		cartService.getAllCarts().forEach((c)->{System.out.println(c.getSessionId());});
		System.out.println("************************************* ACTIVE CARTS END **************************************");
	}

}
