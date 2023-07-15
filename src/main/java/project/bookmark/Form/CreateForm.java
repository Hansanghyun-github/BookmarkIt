package project.bookmark.Form;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Data
public class CreateForm {
    @URL(protocol = "https") @NotBlank
    private String url;
    @NotBlank
    private String name;

    private Long directoryId;
}
