package com.bridgelabz.bookStore.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

public class Token {
    public static String RANDOMSTRING;

    /**
     * this method is use to create a jwt token.
     */
    public static String getToken(Integer userId) {
        //The JWT signature algorithm we will be using to sign the token
        Algorithm algorithm = Algorithm.HMAC256("asdfg");
        return JWT.create().withClaim("userId", userId).sign(algorithm);
    }

    public static Integer decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Verification verification = JWT.require(Algorithm.HMAC256("asdfg"));
        JWTVerifier jwtverifier = verification.build();
        //to decode token
        DecodedJWT decodedjwt=jwtverifier.verify(jwt);
        //retrive data
        Claim claim=decodedjwt.getClaim("userId");
        return claim.asInt();
    }
}
