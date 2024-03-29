package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Credentials;
import it.uniroma3.siw.efitness.efitnesswebapp.model.User;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CredentialsService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.UserService;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.CredentialsValidator;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @Autowired private UserValidator userValidator;

    @Autowired private CredentialsValidator credentialsValidator;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm () {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "index";
    }

    @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
    public String registerUser(
            @ModelAttribute("user") User user, BindingResult userBindingResult,
            @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult) {

        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);

        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            return "login";
        }
        return "register";
    }

    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof DefaultOidcUser) {
            model.addAttribute("user", this.userService.getUserByEmail(((DefaultOidcUser) principal).getEmail()));
            return "user/personalArea/personalArea";
        }
        Credentials credentials = this.credentialsService.getCredentials(((UserDetails) principal).getUsername());
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "admin/home";
        }
        model.addAttribute("user", credentials.getUser());
        return "user/personalArea/personalArea";
    }

    @RequestMapping(value = "/defaultGoogle", method = RequestMethod.GET)
    public String defaultAfterLoginGoogle(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User googleUser =  this.userService.getUserByEmail(((DefaultOidcUser) principal).getEmail());
        if (googleUser == null) {
            googleUser = new User();
        }
        googleUser.setEmail(((DefaultOidcUser) principal).getEmail());
        googleUser.setName(((DefaultOidcUser) principal).getGivenName());
        googleUser.setSurname(((DefaultOidcUser) principal).getFamilyName());
        userService.saveUser(googleUser);
        return defaultAfterLogin(model);
    }
}
