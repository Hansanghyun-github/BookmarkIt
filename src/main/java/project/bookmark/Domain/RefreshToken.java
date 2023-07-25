package project.bookmark.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookmark.jwt.JwtProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Date expiryDate;

    public RefreshToken(User user, String token, Date expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public void refreshTokenRotation(){
        this.token = UUID.randomUUID().toString();
        this.expiryDate = new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME);
    }
}
