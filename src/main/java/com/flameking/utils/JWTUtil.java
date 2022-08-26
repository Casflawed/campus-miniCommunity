package com.flameking.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * JWT工具类，用来生成token、加密token、解密token
 */
@Component("JWTUtil")
public class JWTUtil {

  @Value("${community.expire}")
  private Long EXPIRE_TIME;

  @Value("${community.secret}")
  private String SECRET;

  /**
   * token携带用户id
   *
   * @param id
   * @return
   */
  public String createToken(Integer id) {
    try {
      return JWT.create()
              .withClaim("id", id)
              .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
              .sign(Algorithm.HMAC256(SECRET));
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  /**
   * 校验 token 是否正确
   */
  public boolean verify(String token, Integer id) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET);
      //在token中附带了id信息
      JWTVerifier verifier = JWT.require(algorithm)
              .withClaim("id", id)
              .build();
      //验证 token
      verifier.verify(token);
      return true;
    } catch (Exception exception) {
      return false;
    }
  }

  /**
   * 获得token中的信息，无需secret解密也能获得
   */
  public Integer getUserId(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      return jwt.getClaim("id").asInt();
    } catch (JWTDecodeException e) {
      return null;
    }
  }

}
