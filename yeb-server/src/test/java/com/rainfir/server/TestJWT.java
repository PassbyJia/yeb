package com.rainfir.server;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class TestJWT {

    //一天时间（毫秒）
    static private long time = 1000*60*60*24;
    //签名（解密钥匙）
    static private String signature = "admin";

    private long t = 1000;
    public static void main(String[] args) {
        String userName = "rainfir";
        String role = "admin";
        String jwtToken = generateJWT(userName, role);
        System.out.println(jwtToken);
        parse(jwtToken);
    }

    //解密JWT
    static private void parse(String jwtToken){
        //创建jwt的parser对象
        JwtParser jwtParser = Jwts.parser();
        //通过签名对jwt进行解析,解析完成获得claims的集合
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        //从集合对象中获取数据
        System.out.println("username:" + claims.get("username"));
        System.out.println("role:" + claims.get("role"));
        System.out.println("expiration:" + claims.getExpiration());

    }

    //生成JWT令牌，存储信息用户名，角色
    static private String generateJWT(String username,String role){
        //创建jwt的builder对象
        JwtBuilder jwtBuilder = Jwts.builder();
        //配置
        String jwtToken = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("username", username)
                .claim("role", role)
                .setSubject("test-jwt")//设置主题
                .setExpiration(new Date(System.currentTimeMillis() + time))//设置有效时间：time:一天
                .setId(UUID.randomUUID().toString())//设置id
                //signature
                .signWith(SignatureAlgorithm.HS256, signature)
                //链接三个部分
                .compact();
        return jwtToken;
    }
}
