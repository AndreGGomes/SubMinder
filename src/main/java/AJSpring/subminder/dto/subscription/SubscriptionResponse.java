package AJSpring.subminder.dto.subscription;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id,
        String name,
        BigDecimal price,
        String cycle,
        LocalDateTime nextBillingDate
) {}