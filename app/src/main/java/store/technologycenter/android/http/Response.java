package store.technologycenter.android.http;

import java.util.HashMap;
import java.util.Map;

public class Response {
    public static final String DATA = "data";
    public static final int HTTP_CONTINUE = 100;
    public static final int HTTP_SWITCHING_PROTOCOLS = 101;
    public static final int HTTP_PROCESSING = 102;            // RFC2518
    public static final int HTTP_EARLY_HINTS = 103;           // RFC8297
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_ACCEPTED = 202;
    public static final int HTTP_NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_RESET_CONTENT = 205;
    public static final int HTTP_PARTIAL_CONTENT = 206;
    public static final int HTTP_MULTI_STATUS = 207;          // RFC4918
    public static final int HTTP_ALREADY_REPORTED = 208;      // RFC5842
    public static final int HTTP_IM_USED = 226;               // RFC3229
    public static final int HTTP_MULTIPLE_CHOICES = 300;
    public static final int HTTP_MOVED_PERMANENTLY = 301;
    public static final int HTTP_FOUND = 302;
    public static final int HTTP_SEE_OTHER = 303;
    public static final int HTTP_NOT_MODIFIED = 304;
    public static final int HTTP_USE_PROXY = 305;
    public static final int HTTP_RESERVED = 306;
    public static final int HTTP_TEMPORARY_REDIRECT = 307;
    public static final int HTTP_PERMANENTLY_REDIRECT = 308;  // RFC7238
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_PAYMENT_REQUIRED = 402;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_METHOD_NOT_ALLOWED = 405;
    public static final int HTTP_NOT_ACCEPTABLE = 406;
    public static final int HTTP_PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int HTTP_REQUEST_TIMEOUT = 408;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_GONE = 410;
    public static final int HTTP_LENGTH_REQUIRED = 411;
    public static final int HTTP_PRECONDITION_FAILED = 412;
    public static final int HTTP_REQUEST_ENTITY_TOO_LARGE = 413;
    public static final int HTTP_REQUEST_URI_TOO_LONG = 414;
    public static final int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    public static final int HTTP_EXPECTATION_FAILED = 417;
    public static final int HTTP_I_AM_A_TEAPOT = 418;                                               // RFC2324
    public static final int HTTP_MISDIRECTED_REQUEST = 421;                                         // RFC7540
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;                                        // RFC4918
    public static final int HTTP_LOCKED = 423;                                                      // RFC4918
    public static final int HTTP_FAILED_DEPENDENCY = 424;                                           // RFC4918
    public static final int HTTP_TOO_EARLY = 425;                                                   // RFC-ietf-httpbis-replay-04
    public static final int HTTP_UPGRADE_REQUIRED = 426;                                            // RFC2817
    public static final int HTTP_PRECONDITION_REQUIRED = 428;                                       // RFC6585
    public static final int HTTP_TOO_MANY_REQUESTS = 429;                                           // RFC6585
    public static final int HTTP_REQUEST_HEADER_FIELDS_TOO_LARGE = 431;                             // RFC6585
    public static final int HTTP_UNAVAILABLE_FOR_LEGAL_REASONS = 451;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_NOT_IMPLEMENTED = 501;
    public static final int HTTP_BAD_GATEWAY = 502;
    public static final int HTTP_SERVICE_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
    public static final int HTTP_VARIANT_ALSO_NEGOTIATES_EXPERIMENTAL = 506;                        // RFC2295
    public static final int HTTP_INSUFFICIENT_STORAGE = 507;                                        // RFC4918
    public static final int HTTP_LOOP_DETECTED = 508;                                               // RFC5842
    public static final int HTTP_NOT_EXTENDED = 510;                                                // RFC2774
    public static final int HTTP_NETWORK_AUTHENTICATION_REQUIRED = 511;                             // RFC6585

    public static boolean isNull(String response){
        return response == null;
    }
    public static Map<Integer, String> statusTexts;

    static {
        statusTexts = new HashMap<>();
        statusTexts.put(100, "Continue");
        statusTexts.put(101, "Switching Protocols");
        statusTexts.put(102, "Processing");            // RFC2518
        statusTexts.put(103, "Early Hints");
        statusTexts.put(200, "OK");
        statusTexts.put(201, "Created");
        statusTexts.put(202, "Accepted");
        statusTexts.put(203, "Non-Authoritative Information");
        statusTexts.put(204, "No Content");
        statusTexts.put(205, "Reset Content");
        statusTexts.put(206, "Partial Content");
        statusTexts.put(207, "Multi-Status");          // RFC4918
        statusTexts.put(208, "Already Reported");      // RFC5842
        statusTexts.put(226, "IM Used");               // RFC3229
        statusTexts.put(300, "Multiple Choices");
        statusTexts.put(301, "Moved Permanently");
        statusTexts.put(302, "Found");
        statusTexts.put(303, "See Other");
        statusTexts.put(304, "Not Modified");
        statusTexts.put(305, "Use Proxy");
        statusTexts.put(307, "Temporary Redirect");
        statusTexts.put(308, "Permanent Redirect");    // RFC7238
        statusTexts.put(400, "Bad Request");
        statusTexts.put(401, "Unauthorized");
        statusTexts.put(402, "Payment Required");
        statusTexts.put(403, "Forbidden");
        statusTexts.put(404, "Not Found");
        statusTexts.put(405, "Method Not Allowed");
        statusTexts.put(406, "Not Acceptable");
        statusTexts.put(407, "Proxy Authentication Required");
        statusTexts.put(408, "Request Timeout");
        statusTexts.put(409, "Conflict");
        statusTexts.put(410, "Gone");
        statusTexts.put(411, "Length Required");
        statusTexts.put(412, "Precondition Failed");
        statusTexts.put(413, "Payload Too Large");
        statusTexts.put(414, "URI Too Long");
        statusTexts.put(415, "Unsupported Media Type");
        statusTexts.put(416, "Range Not Satisfiable");
        statusTexts.put(417, "Expectation Failed");
        statusTexts.put(418, "Im a teapot");                                                 // RFC2324
        statusTexts.put(421, "Misdirected Request");                                         // RFC7540
        statusTexts.put(422, "Unprocessable Entity");                                        // RFC4918
        statusTexts.put(423, "Locked");                                                      // RFC4918
        statusTexts.put(424, "Failed Dependency");                                           // RFC4918
        statusTexts.put(425, "Too Early");                                                   // RFC-ietf-httpbis-replay-04
        statusTexts.put(426, "Upgrade Required");                                            // RFC2817
        statusTexts.put(428, "Precondition Required");                                       // RFC6585
        statusTexts.put(429, "Too Many Requests");                                           // RFC6585
        statusTexts.put(431, "Request Header Fields Too Large");                             // RFC6585
        statusTexts.put(451, "Unavailable For Legal Reasons");                               // RFC7725
        statusTexts.put(500, "Internal Server Error");
        statusTexts.put(501, "Not Implemented");
        statusTexts.put(502, "Bad Gateway");
        statusTexts.put(503, "Service Unavailable");
        statusTexts.put(504, "Gateway Timeout");
        statusTexts.put(505, "HTTP Version Not Supported");
        statusTexts.put(506, "Variant Also Negotiates");                                     // RFC2295
        statusTexts.put(507, "Insufficient Storage");                                        // RFC4918
        statusTexts.put(508, "Loop Detected");                                               // RFC5842
        statusTexts.put(510, "Not Extended");                                                // RFC2774
        statusTexts.put(511, "Network Authentication Required");                             // RFC6585
    }

}
