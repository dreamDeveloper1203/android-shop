package store.technologycenter.android.configurations;

import store.technologycenter.android.BuildConfig;

import java.text.NumberFormat;
import java.util.Locale;

public class TechCenterConfiguration {

    public static final String REALM_NAME = "TechCenterRealm";
    public static final String URL = "https://technologycenter.store";
    public static final String WEBSITE = "WWW.TECHNOLOGYCENTER.STORE";
    public static final String WEBSITE_TO_GO = "https://technologycenter.store/";
    public static final String API_VERSION = "v1";
    public static final String ABOUT = "v1";
    public static final String FACEBOOK = "https://www.facebook.com/techcenterlb1";
    public static final String INSTAGRAM = "https://www.instagram.com/techcenter52/";
    public static final String TIK_TOK = "https://www.tiktok.com/@";
    public static final String WHATSAPP = "whatsapp://send?phone=+96171401535";
    public static final String EMAIL = "info@technologycenter.store";
    public static final String PHONE = "76 868 840";
    public static final String PHONE_TO_CALL = "tel://76868840";
    public static final String LOCATION = "بعلبك - دورس - باتجاه عين بورضاي مقابل تعاضد الجيش Baalbek, Lebanon 10001";
    public static final String LOCATION_TO_GO = "https://goo.gl/maps/njKKzUXeTqR4SeE28";

    private static final String COUNTRY = "US";
    private static final String LANGUAGE = "en";
    public static final String INSTANCE_ID = "011c1c62-4c36-44be-a41e-00f953686b94";
    public static final String DEVICE_INTEREST = "news";
    public static final int ANDROID_APP_SOURCE_NUMBER = 2;


    public static String API_URL(){
        return URL + "/api/" + API_VERSION +"/";
    }


    public static String BLRFormat(double value){
        Locale locale = new Locale(LANGUAGE, COUNTRY);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(value).replace(".00", "");
    }

    public static String versionName(){
        return BuildConfig.VERSION_NAME;
    }
}
