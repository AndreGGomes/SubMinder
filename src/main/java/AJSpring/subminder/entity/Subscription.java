package AJSpring.subminder.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle cycle;

    @Column(nullable = false)
    private LocalDateTime nextBillingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Subscription() {}

    public Subscription(String name, BigDecimal price, BillingCycle cycle, LocalDateTime nextBillingDate, User user) {
        this.name = name;
        this.price = price;
        this.cycle = cycle;
        this.nextBillingDate = nextBillingDate;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BillingCycle getCycle() { return cycle; }
    public void setCycle(BillingCycle cycle) { this.cycle = cycle; }

    public LocalDateTime getNextBillingDate() { return nextBillingDate; }
    public void setNextBillingDate(LocalDateTime nextBillingDate) { this.nextBillingDate = nextBillingDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}