package ua.tss.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.tss.model.User;
import ua.tss.model.enums.Role;
import ua.tss.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		if (isCurrentAuthenticationAnonymous()) {
			model.addAttribute("user", new User());
			return "signup";
		}
		return "redirect:/";
	}

	@PreAuthorize("isAnonymous()")
	@PostMapping("/signup")
	public String create(@Valid User user, BindingResult result, Model model) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		if (result.hasErrors()) {return "signup";}
		user.setUsername(user.getPhoneNumber());
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.CUSTOMER));

		if (userService.findByUsername(user.getUsername()) != null) {
			FieldError existError = new FieldError("user", "username", "User Exists!");
			result.addError(existError);
			return "signup";
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userService.save(user);
		return "redirect:/login";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/update/{id}")
	public String updateForm(@PathVariable("id") long id, Model model) {
		User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("user", user);
		return "user-update";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@PostMapping("/update/{id}")
	public String update(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "user-update";
		}
		userService.save(user);
		model.addAttribute("users", userService.findAll());
		return "redirect:/user/list";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("users", userService.findAll());
		return "user-list";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id, Model model) {
		User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userService.delete(user);
		return "redirect:/user/list";
	}

	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/update")
	public String profile(Model model) {
		Object principal = getCurrentAuthentication().getPrincipal();
		Long id;
		User user;
		if (principal instanceof User) {
			id = ((User) principal).getId();
			user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			model.addAttribute("user", user);
			return "user-update";
		}
		return "redirect:/";
	}

	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PostMapping("/update")
	public String updateProfile(@Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {return "user-update";}
		user.setRoles(Collections.singleton(Role.CUSTOMER));
		userService.save(user);
		return "user-update";
	}

	private Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private boolean isCurrentAuthenticationAnonymous() {
		return getCurrentAuthentication().getPrincipal().equals("anonymousUser");
	}
}