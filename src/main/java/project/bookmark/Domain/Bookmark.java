package project.bookmark.Domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "bookmarks")
@Getter @Setter
@ToString
public class Bookmark {

    @Id @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;
    private String siteUrl;
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id")
    private Directory directory;

    public void setBookmark(User user){
        this.user=user;
        user.getBookmarks().add(this);
    }

    public void setBookmark(User user, Directory directory){
        this.user=user;
        user.getBookmarks().add(this);

        this.directory=directory;
        directory.getBookmarks().add(this);
    }
}
