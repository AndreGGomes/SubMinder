package AJSpring.subminder.service;

import AJSpring.subminder.entity.User;
import AJSpring.subminder.entity.UserRole;
import AJSpring.subminder.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        this.userRepository.save(user);
    }
    
    @Transactional
    public void updateUser(User user) {
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        this.userRepository.save(user);
    }

    @Transactional 
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public boolean validateLogin(String email, String password) {
        Optional<User> users = userRepository.findByEmail(email);
        if (!users.isPresent()) {
            return false;
        }
        User user = users.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    public String logout(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return "Logout successful for user: " + user.getUsername();
        } else {
            return "User not found with ID: " + userId;
        }
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public String hashPassword(String raw) {
        return passwordEncoder.encode(raw);
    }
}
