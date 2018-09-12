package com.cn.xa.qyw.http;

/**
 * Created by Administrator on 2016/7/3.
 */
public class HttpAddress {


    public static final String HOST_1 = "http://192.168.0.104:8080/";

    public static final String HOST_2 = "http://www.qiuyiwang.com:8081/";

//    public static final String HOST_2 = "http://172.16.99.248:8080/";

//    public static final String HOST_3 = "http://www.qiuyiwang.com:8081/download/";
    public static final String HOST_3 =  HOST_2 + "download/";

//    public static final String NET_HOST = HOST_2 + "Doctor/api/";

    public static final String NET_HOST = HOST_2 + "YHT/api/";

    public static final String NET_HOST_LUCKDRAW = HOST_2 + "luckdraw/api/";

    public static final String PHOTO_URL = HOST_3;

    public static final String MAIN_ADDRESS = NET_HOST;

    public static final String GET_ALL_DEPARTMENT = MAIN_ADDRESS + "departments/all";

    public static final String USER_LOGIN = MAIN_ADDRESS + "user/login";

    public static final String GET_AUTH_CODE = MAIN_ADDRESS + "user/get_auth_code";

    public static final String USER_MODIFY_USERID = MAIN_ADDRESS + "user/update_username";

    public static final String DEPARTMENT_DETAIL_INFO = MAIN_ADDRESS + "doctor/all_by_department";

    public static final String USER_ADD = MAIN_ADDRESS + "user/add";

    public static final String USER_MODIFY_USERINFO = MAIN_ADDRESS + "userinfo/update_user_info";

    public static final String UPLOAD_USER_PHOTO = MAIN_ADDRESS + "userinfo/update_photo";

    public static final String GET_SHOP_TYPE = MAIN_ADDRESS + "shoptype/all";

    public static final String CREATE_TRADE_NO = MAIN_ADDRESS + "pay/create_trade_no";

    public static final String GET_RECHARGE_GIFT = MAIN_ADDRESS + "capital/get_recharge_gift";

    public static final String EXPAND_GIFT = MAIN_ADDRESS + "capital/expand_gift";

    public static final String GET_MY_CAPITAL = MAIN_ADDRESS + "capital/userid";

    public static final String GET_CAPITAL_HISTORY = MAIN_ADDRESS + "capital/history";

    public static final String GET_CAPITAL_HISTORY_INCOME = MAIN_ADDRESS + "capital/history_income";

    public static final String SEND_HONGBAO = MAIN_ADDRESS + "capital/payToDoctor";

    public static final String QUERY_USER_NAME = MAIN_ADDRESS + "user/query_user_info";

    public static String GET_HONGBAO_DETAIL = MAIN_ADDRESS + "capital/getHongBaoDetail";

    public static String GET_ORDER = MAIN_ADDRESS + "capital/get_order";

    public static String GET_DOCTOR_DETAIL = MAIN_ADDRESS + "doctor/detail";

    public static String GET_USER_INFO = MAIN_ADDRESS + "userinfo/query_user_info";

    public static String UPDATE_PHOTO_FILE = MAIN_ADDRESS + "file/upload_user_photo";

    public static String GET_LIST_NEWS = MAIN_ADDRESS + "news/query_news";

    public static String GET_NEWS_DETAIL = MAIN_ADDRESS + "news/news_detail";

    public static String GET_NEWS_ALL = MAIN_ADDRESS + "news/get_news";

    public static String GET_VERSIOIN_INFO = MAIN_ADDRESS + "app/version/info";

    public static String GET_ALL_HOSPITAL = MAIN_ADDRESS + "hospital/all";

    public static String UPDATE_USER_INFO = MAIN_ADDRESS + "userinfo/update_user_info";

    public static String ADDUSER_INFO = MAIN_ADDRESS + "userinfo/add";

    public static String ADD_CRASH = MAIN_ADDRESS + "crash/add";

    public static String UPDATE_USER_PWD = MAIN_ADDRESS  + "user/update_password";

    public static String FORGET_USER_PWD = MAIN_ADDRESS  + "user/forget_password";

    public static String ADD_FEED_BACK = MAIN_ADDRESS + "feedback/add";

    public static String GET_USER_PAY_PWD = MAIN_ADDRESS + "paypwd/query";

    public static String CHECK_USER_PAY_PWD = MAIN_ADDRESS + "paypwd/check";

    public static String ADD_AND_UPDATE_USER_PAY_PWD = MAIN_ADDRESS + "paypwd/addAndUpdate";

    public static String SEND_DOCTOR_ORDER = MAIN_ADDRESS + "send_msg/sendDoctor";

    public static String CHECK_AUTH_CODE = MAIN_ADDRESS + "auth/query";

    public static String QUERY_HOSPITAL_NAME = MAIN_ADDRESS + "hospital/querylike";

    public static String QUERY_HOSPITAL_CITY = MAIN_ADDRESS + "hospital/queryHospitalCity";

    public static String ADD_HOSPITAL = MAIN_ADDRESS + "hospital/add";

    public static String GET_HOSPITAL_GRADE = MAIN_ADDRESS + "hospital/getAllHospitalGrade";

    public static String GET_DEPARTMENT_GRADE = MAIN_ADDRESS + "departments/allByGrade";

    public static String ADD_USER_ALI_ACCOUNT = MAIN_ADDRESS + "userAli/add";

    public static String GET_USER_ALI_ACCOUNT = MAIN_ADDRESS + "userAli/get";

    public static String DELETE_USER_ALI_ACCOUNT = MAIN_ADDRESS + "userAli/delete";

    public static String SEARCH = MAIN_ADDRESS + "search/key";

    public static String SEARCH_HOT = MAIN_ADDRESS + "search/hot";

    public static String PUSH_CONTENT = MAIN_ADDRESS + "send_msg/push";

    public static String PUSH_CONTENT_ALL = MAIN_ADDRESS + "send_msg/pushAll";

    public static String PUSH_SINGLE_USER = MAIN_ADDRESS + "send_msg/singleUser";

    public static String UPDATE_USER_INVITE_PHONE = MAIN_ADDRESS + "user/updateInvitePhone";

    public static String GET_USER_VOUCHER = MAIN_ADDRESS + "capital/get_voucher_count";

    public static String GET_ALL_VIDEO_TYPE = MAIN_ADDRESS + "video/allType";

    public static String GET_ALL_VIDEO_BY_TYPE = MAIN_ADDRESS + "video/videosByType";

    public static String GET_VIDEO_DETAIL = MAIN_ADDRESS + "video/videoDetail";

    /**
     * 获取栏目列表
     */
    public static String GET_NEW_COLUMN = NET_HOST_LUCKDRAW + "categorylist";

}
