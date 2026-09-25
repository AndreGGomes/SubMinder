package AJSpring.subminder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;

    // TODO: Descomentar quando a entidade Subscription estiver implementada
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Subscription> subscriptions;
    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
    }

    public User(String username, String email, String password, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role != null ? role : UserRole.USER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role != null ? role : UserRole.USER;
    }

    // TODO: Descomentar quando a entidade Subscription estiver implementada
    // public Subscription getSubscription() {
    //     return subscription;
    // }

    // public void setSubscription(Subscription subscription) {
    //     this.subscription = subscription;
    // }
}
