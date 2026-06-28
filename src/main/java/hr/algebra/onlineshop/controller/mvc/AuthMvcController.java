package hr.algebra.onlineshop.controller.mvc;

import hr.algebra.onlineshop.dto.authentication.AuthRequestDTO;
import hr.algebra.onlineshop.dto.authentication.RegisterDTO;
import hr.algebra.onlineshop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc/auth")
public class AuthMvcController {
    private final AuthService authService;
    private static final String REGISTER = "register";
    public AuthMvcController(AuthService authService)
    {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("authRequest", new AuthRequestDTO());
        model.addAttribute("loginError", error != null);//sending error to front
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("registerDTO", new RegisterDTO());

        return REGISTER;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterDTO dto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("registerDTO", dto);
            return REGISTER;
        }

        try {
            authService.register(dto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("username", "error.username", e.getMessage());
            return REGISTER;
        }

        return "redirect:/mvc/auth/login";
    }
}
