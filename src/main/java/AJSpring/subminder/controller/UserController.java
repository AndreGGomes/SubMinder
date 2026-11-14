package AJSpring.subminder.controller;

import AJSpring.subminder.dto.user.AuthResponse;
import AJSpring.subminder.dto.user.LoginRequest;
import AJSpring.subminder.dto.user.UserRequest;
import AJSpring.subminder.dto.user.UserResponse;
import AJSpring.subminder.entity.User;
import AJSpring.subminder.entity.UserRole;
import AJSpring.subminder.service.JwtService;
import AJSpring.subminder.service.UserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest registerRequest) {
        if (userService.findUserByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse("E-mail já está em uso"));
        }

        User user =
                new User(
                        registerRequest.getUsername(),
                        registerRequest.getEmail(),
                        registerRequest.getPassword(),
                        UserRole.USER);
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Usuário cadastrado com sucesso"));
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserRequest updateRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        Optional<User> existingUser = userService.findUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();
        boolean isAdmin =
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !user.getEmail().equals(currentUserEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Você só pode atualizar a sua própria conta"));
        }

        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        user.setPassword(userService.hashPassword(updateRequest.getPassword()));

        userService.updateUser(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário atualizado com sucesso");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        Optional<User> existingUser = userService.findUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();
        boolean isAdmin =
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !user.getEmail().equals(currentUserEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Você só pode excluir a sua própria conta"));
        }

        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário excluído com sucesso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        boolean valid = userService.validateLogin(request.getEmail(), request.getPassword());
        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Credenciais inválidas"));
        }

        User user =
                userService
                        .findUserByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse("Login realizado com sucesso", token));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token de acesso não informado"));
        }

        String token = authorization.substring(7);
        jwtService.revokeToken(token);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Logout realizado com sucesso. Remova o token e encerre a sessão no cliente."));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllUsersForAdmin() {
        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Acesso de administrador autorizado",
                        "users",
                        userService.findAllUsers().stream()
                                .map(UserResponse::fromEntity)
                                .toList()));
    }
}
