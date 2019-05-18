package inc.myself.fo.repos;

import inc.myself.fo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(@NonNull String username);

    User findByActivationCode(@NonNull String code);
}
