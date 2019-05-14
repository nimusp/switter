package inc.myself.fo.controller;

import inc.myself.fo.domain.Role;
import inc.myself.fo.domain.User;
import inc.myself.fo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(final Model model) {
        final List<User> userList = userRepo.findAll();
        model.addAttribute("users", userList);
        return "userList";
    }

    @GetMapping("{userId}")
    public String editUser(@PathVariable final Long userId, final Model model) {
        final User foundUser = userRepo.findById(userId).get();
        model.addAttribute("user", foundUser);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam final String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") final Long userId) {
        final User user = userRepo.findById(userId).get();
        user.setUsername(username);

        final Set<String> roleSet = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roleSet.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
        return "redirect:/user";
    }
}
