package info.phj233.onlinechat.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.User;
import lombok.NoArgsConstructor;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * JWT 工具类
 * @author phj233
 * @since  2023/3/10 23:55
 * @version 1.0
 */
@NoArgsConstructor
public class JWTUtil {
    /**
     * 生成token
     * @param user 用户
     * @return token
     */
    public static String generateToken(User user) {
//        KeyPair keyPair = generateKeyPair();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return JWT.create()
                .withIssuer("phj233")
                .withIssuedAt(new Date())
                .withJWTId(String.valueOf(user.getUid()))
                .withAudience(user.getUsername())
                .withClaim("username", user.getUsername())
                .withClaim("authorities", user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTConfig.expiration * 1000 * 60))
                .sign(Algorithm.HMAC256(JWTConfig.secret));
    }


    /**
     * 验证token
     * @param token token
     */
    public static Boolean verifyToken(String token) {
        if (token.startsWith(JWTConfig.tokenPrefix)){
            token = token.replace(JWTConfig.tokenPrefix, "");
        }
        return JWT.decode(token).getExpiresAt().getTime() > System.currentTimeMillis();
    }
    /**
     * @deprecated
     */
    private static KeyPair generateKeyPair()  {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
