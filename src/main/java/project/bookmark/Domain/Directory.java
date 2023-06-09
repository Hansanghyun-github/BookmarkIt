package project.bookmark.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
public class Directory {
    @Id @GeneratedValue
    @Column(name = "directory_id")
    private Long id;
    private String directoryName;
    private String prevDirectoryName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "bookmark")
    private List<Bookmark> bookmarks;

    public void setDirectory(User user){
        this.user=user;
        user.getDirectories().add(this);
    }
}
