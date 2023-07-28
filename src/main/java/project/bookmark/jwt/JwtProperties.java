package project.bookmark.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtProperties {
	public static String SECRET;
	public static int ACCESS_TOKEN_EXPIRATION_TIME;
	public static String TOKEN_PREFIX;
	public static String HEADER_STRING;
	public static int REFRESH_TOKEN_EXPIRATION_TIME;

	@Value("${jwt.token.secret}")
	private String secret;

	@Value("${jwt.access.token.expiration.time}")
	private String accessTokenExpirationTime;

	@Value("${jwt.token.prefix}")
	private String tokenPrefix;

	@Value("${jwt.header.string}")
	private String headerString;

	@Value("${jwt.refresh.token.expiration.time}")
	private String refreshTokenExpirationTime;

	@PostConstruct
	private void initialize(){
		SECRET=secret;
		TOKEN_PREFIX=tokenPrefix + " ";
		HEADER_STRING=headerString;

		String[] strings1 = accessTokenExpirationTime.split("[*]");
		Integer result1 = Arrays.stream(strings1).map(Integer::parseInt).reduce(1, (a, b) -> a * b);
		ACCESS_TOKEN_EXPIRATION_TIME=result1;

		String[] strings2 = refreshTokenExpirationTime.split("[*]");
		Integer result2 = Arrays.stream(strings2).map(Integer::parseInt).reduce(1, (a, b) -> a * b);
		REFRESH_TOKEN_EXPIRATION_TIME=result2;

	}
}
