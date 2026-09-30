package AJSpring.subminder.service;

import AJSpring.subminder.entity.Subscription;
import AJSpring.subminder.entity.User;
import AJSpring.subminder.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public Subscription addSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription updateSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public void deleteSubscription(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    public List<Subscription> getUserSubscriptions(User user) {
        return subscriptionRepository.findByUserOrderByIdAsc(user);
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> findByIdAndUser(Long id, User user) {
        return subscriptionRepository.findByIdAndUser(id, user);
    }
}