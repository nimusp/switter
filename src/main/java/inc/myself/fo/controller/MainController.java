package inc.myself.fo.controller;

import inc.myself.fo.domain.Message;
import inc.myself.fo.domain.User;
import inc.myself.fo.repos.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") final String filter, final Model model) {
        final Iterable<Message> messageList;

        if (filter != null && !filter.isEmpty()) {
            messageList = messageRepository.findByTag(filter);
        } else {
            messageList = messageRepository.findAll();
        }
        model.addAttribute("messages", messageList);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal final User user,
            @RequestParam("file") MultipartFile file,
            @Valid final Message message,
            final BindingResult bindingResult,
            final Model model
    ) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            final Map<String, String> errorMap = ControllerUtils.getErrorMap(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);
        } else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                final File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                final String uuidString = UUID.randomUUID().toString();
                final String resultFileName = uuidString + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFileName));
                message.setFilename(resultFileName);
            }
            messageRepository.save(message);
            model.addAttribute("message", null);
        }
        final Iterable<Message> messageList = messageRepository.findAll();
        model.addAttribute("messages", messageList);
        return "main";
    }
}
