package project.bookmark.jwt;

public interface JwtProperties {
	String SECRET = "FDEWF123DWEFS32R3409FDSKLSG324DSF"; // 우리 서버만 알고 있는 비밀값
	int ACCESS_TOKEN_EXPIRATION_TIME = 1000*60*10; // 10분
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";

	int REFRESH_TOKEN_EXPIRATION_TIME = 1000*60*60*24*14; // 2주
}
