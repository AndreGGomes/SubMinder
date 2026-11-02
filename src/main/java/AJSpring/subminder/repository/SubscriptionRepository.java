package AJSpring.subminder.repository;

import AJSpring.subminder.entity.Subscription;
import AJSpring.subminder.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserOrderByIdAsc(User user);

    Optional<Subscription> findByIdAndUser(Long id, User user);
}
