package project.bookmark.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookmark.Domain.RefreshToken;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
