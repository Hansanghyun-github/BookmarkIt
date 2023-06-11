package project.bookmark.Form;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private String username;
    private String password;
    private String email;
}
