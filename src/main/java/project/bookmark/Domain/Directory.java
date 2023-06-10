package project.bookmark.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Directory {
    @Id @GeneratedValue
    @Column(name = "directory_id")
    private Long id;
    private String directoryName;
    private Long prevDirectoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "directory")
    private List<Bookmark> bookmarks = new ArrayList<>();

    public void setUser(User user){
        this.user=user;
        user.getDirectories().add(this);
    }

    @Override
    public String toString() {
        return "Directory{" +
                "id=" + id +
                ", directoryName='" + directoryName + '\'' +
                ", prevDirectoryId=" + prevDirectoryId +
                ", user=" + user +
                '}';
    }
}
