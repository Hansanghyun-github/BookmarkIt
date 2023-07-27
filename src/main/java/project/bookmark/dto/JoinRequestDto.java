package project.bookmark.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class JoinRequestDto {
    @NotBlank
    private String username;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$",
            message = "\uBE44\uBC00\uBC88\uD638\uB294 \uC601\uBB38,\uC22B\uC790,\uD2B9\uC218\uBB38\uC790\uB97C \uD3EC\uD568\uD558\uC5EC 8\uC790 \uC774\uC0C1 20\uC790 \uC774\uD558\uC5EC\uC57C \uD569\uB2C8\uB2E4.")
    private String password;
}
