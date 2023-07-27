package project.bookmark.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Domain.RefreshToken;
import project.bookmark.Domain.User;
import project.bookmark.Service.DirectoryService;
import project.bookmark.Service.RefreshTokenService;
import project.bookmark.Service.UserService;
import project.bookmark.advice.ValidationError;
import project.bookmark.dto.DuplicateDto;
import project.bookmark.dto.JoinRequestDto;
import project.bookmark.dto.RefreshTokenDto;
import project.bookmark.jwt.JwtProperties;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final DirectoryService directoryService;

    @PostMapping("join")
    public ResponseEntity<Object> joinUser(@Validated @RequestBody JoinRequestDto joinRequestDto, BindingResult bindingResult){
        log.debug("join start");

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(new ValidationError(bindingResult));
        }

        Long savedId = userService.save(joinRequestDto);

        directoryService.createTwoDirectoryForNewUser(savedId);

        return ResponseEntity.ok().body("join success");
    }

    @PostMapping("refreshtoken")
    public JwtResponse accessTokenExpired(@RequestBody RefreshTokenDto refreshTokenDto){
        //TODO 현재 로그인중인 유저 받아서 토큰이랑 유저 일치하는지 체크 -> 할 필요가 있나? refreshToken rotation 인데
        log.debug("access token expired, and request new access token");
        log.debug("refreshtoken: " + refreshTokenDto.refreshToken);
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenDto.refreshToken);

        // TODO 이전 리프레시토큰 시간 가져와서 다음 리프레시토큰에 넣어주기

        User user = refreshToken.getUser();

        String jwtToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        RefreshToken newRefreshToken = refreshTokenService.setNewRefreshToken(refreshToken);

        log.debug("both token created");

        return new JwtResponse(JwtProperties.TOKEN_PREFIX + jwtToken, newRefreshToken.getToken());
    }

    @PostMapping("duplicate")
    public boolean isDuplicated(@RequestBody DuplicateDto duplicateDto){
        return userService.isDuplicate(duplicateDto.getUsername());
    }

    @PostMapping("logout")
    public String logout(Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
        User user = principal.getUser();
        refreshTokenService.deleteRefreshToken(user.getId());
        return "logout success";
    }
}
