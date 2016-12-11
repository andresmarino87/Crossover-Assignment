package com.crossover_assignment.config;

/**
 * Created by kate on 12/10/16.
 */

public class AppConstants {
    /** URLs **/
    public final static String URL_BASE_API = "https://crossover-demo-back.herokuapp.com/api/v1/";
    public final static String URL_REGISTER = "register";
    public final static String URL_LOGIN = "auth";
    public final static String URL_PLACES = "places";
    public final static String URL_RENT = "rent";

    /** Labels **/
    public final static String ARG_EMAIL = "email";
    public final static String ARG_PASSWORD = "password";
    public final static String ARG_NUMBER = "number";
    public final static String ARG_ID = "id";
    public final static String ARG_NAME = "name";
    public final static String ARG_EXPIRATION = "expiration";
    public final static String ARG_CODE = "code";
    public final static String ARG_RESULTS = "results";
    public final static String ARG_LOCATION = "location";
    public final static String ARG_LAT = "lat";
    public final static String ARG_LNG = "lng";
    public final static String ARG_ACCESS_TOKEN = "accessToken";

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "isAddingNewAccount";

    public final static String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String PARAM_USER_PASS = "USER_PASS";

    public final static String ARG_OK = "OK";
    public final static String ARG_FAIL = "FAIL";
    public final static String ARG_RESULT = "RESULT";
    public final static String ARG_APP_NAME = "Crossover-assignment";

    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.crossover-assignment";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "crossover";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an crossover account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an crossover account";

}
