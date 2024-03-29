package project.bookmark.Domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Role role; // ROLE_USER, ROLE_ADMIN;
    private String provider;
    private String providerId;
    @CreationTimestamp
    private Timestamp createDate;

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Directory> directories = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        role=Role.ROLE_USER;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", createDate=" + createDate +
                '}';
    }

    public List<Role> getRoleList() {
        return Arrays.asList(Role.values());
    }
}
