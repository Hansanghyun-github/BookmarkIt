package project.bookmark.Form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DirectoryForm {
    @NotBlank
    private String name;
    private Long prevDirectoryId;
}
