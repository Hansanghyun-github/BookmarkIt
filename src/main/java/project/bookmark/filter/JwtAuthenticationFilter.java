package project.bookmark.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Controller.JwtResponse;
import project.bookmark.Domain.RefreshToken;
import project.bookmark.Service.RefreshTokenService;
import project.bookmark.advice.ErrorMessage;
import project.bookmark.dto.LoginRequestDto;
import project.bookmark.jwt.JwtProperties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	private final RefreshTokenService refreshTokenService;
	private ObjectMapper mapper = new ObjectMapper();
	
	// Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
	// 인증 요청시에 실행되는 함수 => /login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		log.debug("JwtAuthenticationFilter : 진입");
		
		// request에 있는 username과 password를 파싱해서 자바 Object로 받기
		ObjectMapper om = new ObjectMapper();
		LoginRequestDto loginRequestDto = null;
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try{
			if(StringUtils.hasText(loginRequestDto.getUsername()) == false || StringUtils.hasText(loginRequestDto.getPassword()) == false){
				unsuccessfulAuthentication(request, response, new InternalAuthenticationServiceException("name or password is null"));
				return null;
			}
		} catch (IOException e){
			log.warn(e.getMessage());
		} catch (ServletException e){
			log.warn(e.getMessage());
		}
		
		log.debug("JwtAuthenticationFilter : "+loginRequestDto);
		
		// 유저네임패스워드 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getUsername(), 
						loginRequestDto.getPassword());
		
		log.debug("JwtAuthenticationFilter : UsernamePassword토큰생성완료");

		super.setDetails(request,authenticationToken);

		log.debug("authenticationToken: "+authenticationToken.toString());
		log.debug(authenticationManager.toString());

		// authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
		// loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
		// UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
		// UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
		// Authentication 객체를 만들어서 필터체인으로 리턴해준다.
		
		// Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
		// Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
		// 결론은 인증 프로바이더에게 알려줄 필요가 없음.
		Authentication authentication =
				authenticationManager.authenticate(authenticationToken);

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		log.debug("Authentication : "+principalDetails.getUser().getUsername());
		return authentication;
	}

	// JWT Token 생성해서 response에 담아주기
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.debug("success handler: jwt token create start!");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(principalDetails.getUser().getId());

		String result = mapper.writeValueAsString(new JwtResponse(JwtProperties.TOKEN_PREFIX + jwtToken, refreshToken.getToken()));
		response.getWriter().write(result);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		log.debug("unsuccess handler: login failed");
		log.debug(failed.getMessage());

		SecurityContextHolder.clearContext();

		String result = mapper.writeValueAsString(
				new ErrorMessage(
						HttpStatus.FORBIDDEN.value(),
						new Date(),
						failed.getMessage(),
						"Authentication failed"));
		response.getWriter().write(result);
	}
}
