package inc.myself.fo.controller;

import inc.myself.fo.domain.Role;
import inc.myself.fo.domain.User;
import inc.myself.fo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(final Model model) {
        model.addAttribute("message", "");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(final User user, final Model model) {
        final User foundUser = userRepo.findByUsername(user.getUsername());
        if (foundUser != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        model.addAttribute("message", "");
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
