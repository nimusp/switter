package inc.myself.fo.controller;

import inc.myself.fo.domain.Role;
import inc.myself.fo.domain.User;
import inc.myself.fo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> map) {
        final User foundUser = userRepo.findByUsername(user.getUsername());
        if (foundUser != null) {
            map.put("message", "User exists!");
            return "registration";
        }
        
        foundUser.setActive(true);
        foundUser.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
