package inc.myself.fo.controller;

import inc.myself.fo.domain.Role;
import inc.myself.fo.domain.User;
import inc.myself.fo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(final Model model) {
        final List<User> userList = userService.findAll();
        model.addAttribute("users", userList);
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public String editUser(@PathVariable final Long userId, final Model model) {
        final User foundUser = userService.findById(userId);
        model.addAttribute("user", foundUser);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam final String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") final Long userId) {
        userService.saveUser(userId, username, form);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal final User user, final Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal final User user,
            @RequestParam final String email,
            @RequestParam final String password) {
        userService.updateProfile(user, email, password);
        return "redirect:/user/profile";
    }
}
