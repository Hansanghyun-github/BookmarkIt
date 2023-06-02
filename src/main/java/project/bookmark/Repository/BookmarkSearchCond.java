package project.bookmark.Repository;

import lombok.Data;

@Data
public class BookmarkSearchCond {
    private Long user_id;
    private String searchWord;
    public BookmarkSearchCond() {
    }

    public BookmarkSearchCond(Long id, String searchWord) {
        this.user_id = id;
        this.searchWord = searchWord;
    }
}
