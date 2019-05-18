package inc.myself.fo.repos;

import inc.myself.fo.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByTag(@NonNull String tag);
}
