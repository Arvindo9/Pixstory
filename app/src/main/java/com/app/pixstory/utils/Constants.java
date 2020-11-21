package com.app.pixstory.utils;

import com.app.pixstory.BuildConfig;

public class Constants {

    public static final String BASE_PATH = "https://pixstory-assets.s3.amazonaws.com/";
    public static final String KEY_DEFAULT_ACTIVITY_BUNDLE = "DEFAULT_ACTIVITY_BUNDLE";
    public static final String KEY_DEFAULT_ACTIVITY_INTENT = "DEFAULT_ACTIVITY_INTENT";
    public static final String KEY_DEFAULT_ACTIVITY_INTENT_INT = "DEFAULT_ACTIVITY_INTENT_INT";
    public static final int REQUEST_PERMISSIONS = 1240;
    public static final int REQUEST_MICROPHONE = 1245;

    public static final String DEFAULT_PROFILE_PIC_URL = "https://pixstory-assets.s3.ap-south-1.amazonaws.com/users/thumbnail/male-avtaar.png?response-content-disposition=inline&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEEMaCmFwLXNvdXRoLTEiSDBGAiEAo0aPT3k%2BB28dV3LhRWH5o7B%2BRdt5o0Had%2Fp%2F4lwilakCIQCRcOVTJ4wzStgE1M3Mru756v34EutJN%2BThMEUp5%2FDRkyrmAQhcEAAaDDg1MTYyNDE3NzE0MyIMbpOXnceJDbjQ%2BOiOKsMBfKBEgrI6M4WZxEtmfbDHnFmQhv5OnQ72EY327iRHQWJ7SB5P7NiiEfXBpYQxcaRPSr9MEidPkv005FJBOoP5KZ0yFv3y6WxUaJT6vLhbBD4%2BeGke08e6iFZhtV49c6OZA9UqtMEHCbgev4xaOHAqpwMvU8nI%2FB0ACsICWN94tEV%2BJBICwc5heFq%2Fo%2FjKmYxsSAoRBK1MmtG0DtwpfVWrSvjivw18ZVZESAy8bPvCzEV9ZR34HXaee1gbWawD06aU17X9MLON0fQFOu8ChiY%2Fpioq6KHEFksBjAsEJlzxnLxqAZ0NEiWBBiJYn5nTJoSOzbkyymYLuTurbcy8vq6yeFq5ooBhjuQm%2FJSftbdO0Wnmlel4HcNAAKdvgHf%2FDea3RXwUy55xwbdT4ZbyfLpcjLkpluo18RW36ybyzW%2FknFM1Yj66V2S%2BOm26W%2FpE3t626cfzag0zLIbpn6cYlTTPeim%2FJfyYZjqvoq6aNHS23drUmQTUXXohVf0LdwgTBo5sSfjAkKpVqarlv4Y0TLiI3CDcck%2BKMYTP5mlulwH65fmYW4YLEtLqfO2vgo1cjJmGgly6SlpDRlt3f8wIzTS%2FZGK12BwJNHEBV64Z%2B9uakeXQSMSFF%2B06646HhIK4jqdzyPtzv36kZ%2B44rKW1T7b8BJ4ZotlZ8BX%2FeimBivZs3BSoomUcRYbSMiSed66PwNuirIQIt8DIsvtWnVtlJhaLJzzBVS5Itf54A2RLd%2Bxja%2FPbBEK8Q7uzGelZkA%3D%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200413T110328Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=ASIA4MSGBVX34AOKJHSF%2F20200413%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Signature=5137a76e1f8628d6596f64730d2451632fd96e864a9886c86573d72b2b3bf602";
    public static final String LOGIN_TYPE_MOBILE_NUMBER = "mobile";
    public static final String LOGIN_TYPE_EMAIL_ID = "email";
    public static final String LOGIN_TYPE_FACEBOOK = "facebook";
    public static final String LOGIN_TYPE_GOOGLE = "google";
    public static final String LOGIN_TYPE_LINKEDIN = "linkedin";
    public static final String LOGIN_TYPE_USER = "username";
    public static final String LOGIN_USERNAME = "username";
    public static final String LOGIN_PASSWORD = "password";
    public static String SIGN_UP_FIRST_NAME = "firstName";
    public static String SIGN_UP_LAST_NAME = "lastName";
    public static String SIGN_UP_PREFERRED_USER_NAME = "preferredUserName";
    public static String SIGN_UP_GENDER = "gender";
    public static String SIGN_UP_DOB = "dob";
    public static String SIGN_HOME_COUNTRY = "homeCountry";
    public static String SIGN_UP_EMAIL = "signUpEmail";
    public static String SIGN_UP_MOBILE = "signUpMobile";
    public static String SIGN_UP_PASSWORD = "signUpPassword";
    public static String SIGN_UP_CONFIRM_PASSWORD = "signUpConfirmPassword";
    public static String CREATE_STORY_FROM = "Your photos";
    public static String PUBLISH_OPTION = "public";

    public static String YOU_PAGE_STORIES = "stories";
    public static String YOU_PAGE_PAGES = "pages";
    public static String YOU_PAGE_PHOTOS = "photos";
    public static String YOU_FOLLOW = "follow";
    public static String YOU_CHAT = "chat";


// you page
    public static String CREATED = "Created";
    public static String FAVOURITES = "Favourites";
    public static String FOLLOWING = "Following";
    public static String MEMBERSHIP = "Membership";

    public static String STORY_CREATED = "story created";
    public static String STORY_FAVOURITES = "story favourites";
    public static String PAGE_CREATED = "page created";
    public static String PAGE_FOLLOWING = "page following";
    public static String PAGE_MEMBERSHIP = "page membership";
    public static String PHOTO_CREATED = "photo created";
    public static String PHOTO_FAVOURITES = "photo membership";


    public static String FAV = "fav";
    public static String UN_FAV = "unfav";
    public static String STORY_FILTER_TYPE = "unfav";
    public static String PHOTO_FILTER_TYPE = "unfav";
    public static String PAGE_REQUEST_CREATED = "created";
    public static String PAGE_REQUEST_FOLLOW = "follow";
    public static String PAGE_REQUEST_MEMBER = "memeber";

    // home page -- filter

    public static String HOME_FILTER_TRENDING = "Trending";
    public static String HOME_FILTER_FEATURE = "Featured";
    public static String HOME_FILTER_LATEST = "Latest";
    public static String HOME_FILTER_FRIEND = "Friends";
    public static String HOME_FILTER_PAGE = "Pages";

    public static String HOME_FILTER_TRENDING_DATA = "trend";
    public static String HOME_FILTER_FEATURE_DATA = "feature";
    public static String HOME_FILTER_LATEST_DATA = "latest";
    public static String HOME_FILTER_FRIEND_DATA = "friend";
    public static String HOME_FILTER_PAGE_DATA = "page";


    public static String CAMERA_GALLERY_LIST = "cgl";
    public static String FROM = "from";







    public static final String GENERAL_DATE_FORMAT = "dd MMMM yyyy";

    public static final String DATE_TIME_NAME_FORMAT = "yyyyMMdd_HHmmss";

    public static final String DATE_SEND_FORMAT = "yyyy-MM-dd";

    public static final String DATE_INT_FORMAT = "yyyyMMdd";

    public static final String DATE_FORMAT = "dd/MM/yyy";

    public static final String EDIT = "edit";
    public static final String REMOVE = "remove";


    public static final String VERIY_TYPE = "register";

    public static final String TYPE_LOGIN = "login";
    public static final String TYPE_SIGNUP = "signup";

    public static final String VERIFY_TYPE_REGISTER = "register";
    public static final String VERIFY_TYPE_LOGIN = "login";
    public static final String VERIFY_TYPE_FORGET = "forget";

    public static final String FROM_LOGIN = "from_login";
    public static final String FROM_SIGNUP = "from_signup";
    public static final String FROM_HELP = "from_help";

    public static final String EMAIL_VERIFIED_TRUE = "yes";
    public static final String EMAIL_VERIFIED_FALSE = "no";

    public static final String MOBILE_VERIFIED_TRUE = "yes";
    public static final String MOBILE_VERIFIED_FALSE = "no";


    public static String STORY_FORMAT = "quick";
    public static String USER_TYPE = "self";
    public static String FROM_YOU = "";
    public static int USER_ID = 0;
    public static int IS_STORY_FAV = 0;
    public static String IS_TYPE = "default";
    public static String CREATION_TYPE = "";
    public static Integer STORY_ID = 0;

    public static String INTEREST_TYPE = "add";

    //Notifications-------------------

    public static final String FIREBASE_NOTIFICATION_TOPIC = "NOTIFICATION_TOPIC_GENERAL";

    public static final int PUSH_NOTIFICATION_ID = 1201;
    public static final String CHANNEL_NAME = BuildConfig.APPLICATION_ID + " Service";
    public static final String CHANNEL_ID = BuildConfig.APPLICATION_ID;

    public static final String FIREBASE_NOTIFICATION_TOPIC_GROUP = "NOTIFICATION_TOPIC_GROUP";
    public static final String NOTIFICATION_TOPIC_GENERAL = "NOTIFICATION_TOPIC_GENERAL";
    public static final String NOTIFICATION_TOPIC_LOGOUT = "NOTIFICATION_TOPIC_LOGOUT";
    //    public static final String NOTIFICATION_TOPIC_MESSAGE = "NOTIFICATION_TOPIC_MESSAGE";
    public static final String NOTIFICATION_TOPIC_MESSAGE = "message";
    public static final String NOTIFICATION_TOPIC_MATCH = "NOTIFICATION_TOPIC_MATCH";
    public static final String NOTIFICATION_TOPIC_ROSE = "NOTIFICATION_TOPIC_ROSE";


    public static final String LOGOUT_CODE = "100";
    public static final String BLOCK_CODE = "200";
    public static final String KEY_FROM_SERVICE = "KEY_FROM_SERVICE";
    public static final String KEY_FROM_SERVICE_MESSAGE = "KEY_NEW_MESSAGE";
    public static final String KEY_FROM_SERVICE_MESSAGE_DATA = "KEY_NEW_MESSAGE_DATA";
    public static final String INTENT_SERVER_RECEIVE = "LogoutSession";
    public static final String INTENT_SERVER_RECEIVE_MESSAGES = "MessageBroadcast";
    public static final String VALUE_FROM_SERVICE_MESSAGE = "NewMessages";

    public static final String PUSH_MESSAGE_TYPE = "message";

    public static final String MESSAGE_TYPE_INBOX = "MESSAGE_TYPE_INBOX";
    public static final String MESSAGE_TYPE_BULLETIN= "MESSAGE_TYPE_BULLETIN";
    public static final String MESSAGE_TYPE_BULLETIN_INTEREST= "interest";
    public static final String MESSAGE_TYPE_BULLETIN_COUNTRY= "country";
    public static final String MESSAGE_TYPE_BULLETIN_DEFAULT= "default";
    public static final String MESSAGE_TEXT = "Text";

    public static final String GENERAL_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";


    public static final String GENERAL_TIME_READABLE_FORMAT = "hh:mm a";

    public static final String GENERAL_DATE_READABLE_FORMAT = "dd MMMM yyyy";

    public static final String DATE_FORMAT_INTEGER = "yyyyMMdd";


    public static final int ACTION_GALLERY_SINGLE_IMAGE = 1020;
    public static int PAGE_TYPE = 0;
    public static String PAGE_STATUS = "publish";

    // simmer loader
    public static String DEFAULT_LOADER = "default";
    public static String HOME_LOADER = "home";
    public static String YOU_STORY_LOADER = "your_story";
    public static String YOU_PAGE_LOADER = "your_page";
    public static String YOU_PHOTO_LOADER = "your_page";
    public static String STORY_VIEW_LOADER = "story_view";
    public static String UPLOAD_LOADER = "upload_loader";


    public static final String FROM_ACCOUNT = "from_account";
    public static final String SKIP = "skip";
    public static final String VERIFY = "verify";


    public static final String BEARER = "Bearer";

    public static final String LOADING_FIRST ="loading_first";
    public static final String LOADED ="loaded";
    public static final String LOADING_SECOND ="loading_second";
    public static String HOME_FILTER ="";

    public static final int MESSAGE_TYPE_CHAT_TEXT= 1;
    public static final int MESSAGE_TYPE_CHAT_AUDIO= 2;
    public static final int DEFAULT_PAGE_LIMIT_MESSAGE = 10;
    private int messageType = Constants.MESSAGE_TYPE_CHAT_TEXT;


    public static final String APP_DIRECTORY = "pixstory";
    public static final String APP_FOLDER_AUDIO = "audio";
    public static final String APP_AUDIO_FILE_FORMAT = ".mpga";

    public static final int REQUEST_WRITE_FILE = 1250;

    public static final String STORIES ="stories";
    public static final String MEMBERS ="users";
    public static final String INTERESTS ="interests";
    public static final String CHAT ="chat";

    public static final String PAGE_CREATE ="create";
    public static final String PAGE_UPDATE ="update";



}
