package inc.myself.fo.repos;

import inc.myself.fo.domain.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
