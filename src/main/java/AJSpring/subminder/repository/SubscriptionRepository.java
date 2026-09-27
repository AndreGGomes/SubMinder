package AJSpring.subminder.repository;

import AJSpring.subminder.entity.Subscription;
import AJSpring.subminder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserOrderByNextBillingDateAsc(User user);

    Optional<Subscription> findByIdAndUser(Long id, User user);
}