package com.project.base.util.converter;

import com.google.gson.Gson;
import com.project.base.response.UserListResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

@Slf4j
public class ParserToken {
    public static long getUserId(String token) {
        String[] value = token.split("\\.");
        String base64EncodedBody = value[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        Gson gson = new Gson();
        UserListResponse userResponse = gson.fromJson(body, UserListResponse.class);
        return userResponse.getId();
    }
}
