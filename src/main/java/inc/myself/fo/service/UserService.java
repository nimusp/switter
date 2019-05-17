package inc.myself.fo.service;

import inc.myself.fo.domain.Role;
import inc.myself.fo.domain.User;
import inc.myself.fo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(final User user) {
        final User foundUser = userRepo.findByUsername(user.getUsername());
        if (foundUser != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        sendActivationMessage(user);

        return true;
    }

    public boolean activateUser(final String code) {
        final User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(final Long userId, final String username, final Map<String, String> form) {
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
    }

    public User findById(Long userId) {
        return userRepo.findById(userId).get();
    }

    public void updateProfile(final User user, final String email, final String password) {
        final String userEmail = user.getEmail();

        final boolean isEmailChanged = email != null && !email.equals(userEmail) ||
                userEmail != null && !userEmail.equals(email);
        if (isEmailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        userRepo.save(user);

        if (isEmailChanged) {
            sendActivationMessage(user);
        }
    }

    private void sendActivationMessage(final User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            final String message = String.format(
                    "Hello, %s!\n" +
                            "Welcome to Switter. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailSenderService.send(user.getEmail(), "Activation code", message);
        }
    }
}
