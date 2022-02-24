package th.co.techberry.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;

import th.co.techberry.constants.ConfigConstants;

public class ApidataUtil {
	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; // with trailing space to separate token
	private static final int STATUS_CODE_UNAUTHORIZED = 401;

	public void setAccessControlHeaders(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
		resp.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers,Access-Control-Allow-Origin, Access-Control-Allow-Credentials,Origin,Accept, X-Requested-With, "
						+ "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,Authorization,authorization");
	}

	public String getUsernameInToken(HttpServletRequest httpRequest) {
		String token = getBearerToken(httpRequest);
		System.out.println("token"+token);
		Algorithm algorithm = Algorithm.HMAC256(ConfigConstants.SECRET_KEY);
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // Reusable verifier instance
		DecodedJWT jwt = verifier.verify(token);
		Claim claimUsername = jwt.getClaim("username");
		System.out.println("claimUsername"+claimUsername);
		return claimUsername.asString();
	}
	
	public int getIdInToken(HttpServletRequest httpRequest) {
		String token = getBearerToken(httpRequest);
		Algorithm algorithm = Algorithm.HMAC256(ConfigConstants.SECRET_KEY);
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // Reusable verifier instance
		DecodedJWT jwt = verifier.verify(token);
		Claim ClaimId = jwt.getClaim("id");
		return ClaimId.asInt();
	}

	public String getBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTH_HEADER_KEY);
		System.out.println("authHeader : "+ authHeader);
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}

	public Map<String, Object> getRequestBodyToMap(HttpServletRequest request) throws IOException {
		String requestBodyStr = IOUtils.toString(request.getInputStream(),"UTF-8");
		Gson gson = new Gson();
		System.out.println("Request body = " + requestBodyStr);
		return gson.fromJson(requestBodyStr, Map.class);
	}
}
