package AJSpring.subminder.repository;

import AJSpring.subminder.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query ("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query ("SELECT u FROM User u WHERE u.username = :username")
    List<User> findByUsername(@Param("username") String username);

}
