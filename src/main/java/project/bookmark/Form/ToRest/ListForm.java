package project.bookmark.Form.ToRest;

import lombok.Data;

import java.util.List;

@Data
public class ListForm {
    List<TBookmark> bookmarks;
    List<TDirectory> directories;
}
