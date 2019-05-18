package inc.myself.fo.controller;

import inc.myself.fo.domain.User;
import inc.myself.fo.domain.dto.CaptchaResponseDto;
import inc.myself.fo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify" + "?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @GetMapping("/registration")
    public String registration(final Model model) {
        model.addAttribute("message", "");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("g-recaptcha-response") final String recaptchaResponse,
            @Valid final User user,
            final BindingResult bindingResult,
            final Model model) {
        final String requestUrl = String.format(CAPTCHA_URL, recaptchaSecret, recaptchaResponse);
        final CaptchaResponseDto captchaResponse = restTemplate.postForObject(requestUrl, Collections.emptyList(), CaptchaResponseDto.class);
        if (!captchaResponse.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }

        final String password = user.getPassword();
        final String passwordConfirmation = user.getPasswordConfirmation();
        if (password != null && passwordConfirmation != null && !password.equals(passwordConfirmation)) {
            model.addAttribute("passwordConfirmationError", "Passwords are different!");
        }

        if (bindingResult.hasErrors() || !captchaResponse.isSuccess()) {
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
