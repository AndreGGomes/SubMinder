package AJSpring.subminder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import AJSpring.subminder.dto.subscription.SubscriptionRequest;
import AJSpring.subminder.dto.subscription.SubscriptionResponse;
import AJSpring.subminder.entity.Subscription;
import AJSpring.subminder.entity.User;
import AJSpring.subminder.service.SubscriptionService;
import AJSpring.subminder.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/subscriptions", produces = "application/json")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public SubscriptionController(SubscriptionService subscriptionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSubscription(@Valid @RequestBody SubscriptionRequest request) {
        User user = getAuthenticatedUser();

        // Note como pegamos os dados do Record: request.name() ao invés de request.getName()
        Subscription subscription = new Subscription(
                request.name(),
                request.price(),
                request.cycle(),
                request.nextBillingDate(),
                user
        );

        Subscription saved = subscriptionService.addSubscription(subscription);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Subscription added successfully");
        response.put("subscriptionId", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions() {
        User user = getAuthenticatedUser();
        List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(user);

        List<SubscriptionResponse> response = subscriptions.stream()
                .map(sub -> new SubscriptionResponse(
                        sub.getId(),
                        sub.getName(),
                        sub.getPrice(),
                        sub.getCycle().name(),
                        sub.getNextBillingDate()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateSubscription(@PathVariable Long id, @Valid @RequestBody SubscriptionRequest request) {
        User user = getAuthenticatedUser();

        Optional<Subscription> optionalSub = subscriptionService.findByIdAndUser(id, user);
        if (optionalSub.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Subscription not found or belongs to another user"));
        }

        Subscription subscription = optionalSub.get();

        subscription.setName(request.name());
        subscription.setPrice(request.price());
        subscription.setCycle(request.cycle());
        subscription.setNextBillingDate(request.nextBillingDate());

        subscriptionService.updateSubscription(subscription);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Subscription updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubscription(@PathVariable Long id) {
        User user = getAuthenticatedUser();

        Optional<Subscription> optionalSub = subscriptionService.findByIdAndUser(id, user);
        if (optionalSub.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Subscription not found or belongs to another user"));
        }

        subscriptionService.deleteSubscription(optionalSub.get());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Subscription deleted successfully");
        return ResponseEntity.ok(response);
    }
}