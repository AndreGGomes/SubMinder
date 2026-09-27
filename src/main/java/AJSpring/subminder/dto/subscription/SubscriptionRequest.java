package AJSpring.subminder.dto.subscription;

import AJSpring.subminder.entity.BillingCycle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Cycle is required")
        BillingCycle cycle,

        @NotNull(message = "Next billing date is required")
        LocalDateTime nextBillingDate
) {}