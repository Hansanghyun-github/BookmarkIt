package project.bookmark.Form.ToRest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TBookmark {
    private Long id;
    private String url;
    private String name;
    private Long directoryId;
}