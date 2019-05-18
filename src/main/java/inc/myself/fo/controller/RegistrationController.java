package inc.myself.fo.controller;

import inc.myself.fo.domain.User;
import inc.myself.fo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(final Model model) {
        model.addAttribute("message", "");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid final User user, final BindingResult bindingResult, final Model model) {
        final String password = user.getPassword();
        final String passwordConfirmation = user.getPasswordConfirmation();
        if (password != null && passwordConfirmation != null && !password.equals(passwordConfirmation)) {
            model.addAttribute("passwordConfirmationError", "Passwords are different!");
        }

        if (bindingResult.hasErrors()) {
            final Map<String, String> errorMap = ControllerUtils.getErrorMap(bindingResult);
            model.mergeAttributes(errorMap);
            return "registration";
        }

        final boolean isUserAdded = userService.addUser(user);
        if (!isUserAdded) {
            model.addAttribute("userError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable final String code, final Model model) {
        final boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message","User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
