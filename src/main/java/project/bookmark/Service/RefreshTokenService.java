package project.bookmark.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.RefreshToken;
import project.bookmark.Repository.RefreshTokenRepository;
import project.bookmark.Repository.UserRepository;
import project.bookmark.exception.InValidRefreshException;
import project.bookmark.exception.TokenRefreshException;
import project.bookmark.jwt.JwtProperties;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        // TODO 이부분 쿼리 너무 많이 나가는데
        if(refreshTokenRepository.existsByUserId(userId)){
            refreshTokenRepository.deleteByUserId(userId);
        }

        RefreshToken refreshToken = new RefreshToken(
                userRepository.findById(userId).get(),
                UUID.randomUUID().toString(),
                new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME)
                );

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(String token) {
        log.debug(token);
        Optional<RefreshToken> byToken = refreshTokenRepository.findByToken(token);
        if(byToken.isEmpty())
            throw new InValidRefreshException("invalid refresh token");

        RefreshToken refreshToken = byToken.get();

        if (refreshToken.getExpiryDate().compareTo(new Date(System.currentTimeMillis())) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh token was expired. Please make a new login request");
        }
        return refreshToken;
    }

    public RefreshToken setNewRefreshToken(RefreshToken refreshToken){
        refreshToken.refreshTokenRotation();
        return refreshToken;
    }

    public void deleteRefreshToken(Long user_id){
        refreshTokenRepository.deleteByUserId(user_id);
    }
}