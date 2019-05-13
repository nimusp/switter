package inc.myself.fo.controller;

import inc.myself.fo.domain.Message;
import inc.myself.fo.domain.User;
import inc.myself.fo.repos.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

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
            @RequestParam final String text,
            @RequestParam final String tag, final Model model) {
        final Message message = new Message(text, tag, user);
        messageRepository.save(message);
        final Iterable<Message> messageList = messageRepository.findAll();
        model.addAttribute("messages", messageList);
        model.addAttribute("filter", "");
        return "main";
    }
}
