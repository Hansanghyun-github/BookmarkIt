package project.bookmark.Domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role; // ROLE_USER, ROLE_ADMIN;
    private String provider;
    private String providerId;
    @CreationTimestamp
    private Timestamp createDate;

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks=new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", createDate=" + createDate +
                '}';
    }
}
