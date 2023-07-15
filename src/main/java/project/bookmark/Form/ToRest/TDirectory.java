package project.bookmark.Form.ToRest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TDirectory {
    private Long id;
    private String name;
    private Long prevDirectoryId;
}