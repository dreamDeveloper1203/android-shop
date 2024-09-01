package store.technologycenter.android.http;

import android.util.Base64;

import java.util.Arrays;

public class Request {

    public static final int HEADER_FORWARDED = 0b000001; // When using RFC 7239
    public static final int HEADER_X_FORWARDED_FOR = 0b000010;
    public static final int HEADER_X_FORWARDED_HOST = 0b000100;
    public static final int HEADER_X_FORWARDED_PROTO = 0b001000;
    public static final int HEADER_X_FORWARDED_PORT = 0b010000;
    public static final int HEADER_X_FORWARDED_PREFIX = 0b100000;

    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_PURGE = "PURGE";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_TRACE = "TRACE";
    public static final String METHOD_CONNECT = "CONNECT";
    public static final String BASIC = "Basic";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static String basic(String credentials){
        String credentialsEncoding = Arrays.toString(Base64.encode((credentials).getBytes(), Base64.DEFAULT));
        return BASIC + " " + credentialsEncoding;
    }
}
