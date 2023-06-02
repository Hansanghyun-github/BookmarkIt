package project.bookmark.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookmark.Domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // SELECT * FROM user WHERE username = ?
    Optional<User> findByUsername(String username);

    // SELECT * FROM user WHERE provider = ?1 and providerId = ?2
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
