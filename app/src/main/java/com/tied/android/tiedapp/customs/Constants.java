package com.tied.android.tiedapp.customs;

import com.loopj.android.http.PersistentCookieStore;

import java.io.File;

/**
 * Created by Emmanuel on 4/23/2016.
 */
public class Constants {
    public static final String TAG = Constants.class
            .getSimpleName();
    public static final String SERVER_URL = "http://tied.goattale.com:8101/api/v1/";

    public static final String HOST = "http://tied.goattale.com:8101/";
//    public static final String HOST = "https://tied-api.herokuapp.com/";

    public static final int PORT = 3000;
    public static final String API_PATH = "api/v1/";

    public static final String AUTH_EMAIL_ENDPOINT = API_PATH + "auth/email";
    public static final String AUTH_REGISTER_ENDPOINT = API_PATH + "auth/register";
    public static final String AUTH_LOGIN_ENDPOINT = API_PATH + "auth/login";
    public static final String AUTH_SEND_PHONE_CODE_ENDPOINT = API_PATH + "auth/send_phone_vc";
    public static final String AUTH_VERIFY_PHONE_CODE_ENDPOINT = API_PATH + "auth/verify_phone";

    public static final String GET_AVATAR_ENDPOINT = HOST + "uploads/avatars/";
    public static final String GET_LOGO_ENDPOINT = HOST + "uploads/logos/";

    public static final String USER_FIND_BY_EMAIL = API_PATH + "users/find_by_email/{email}";
    public static final String USER_FIND_BY_EMAIL_OR_PHONE = API_PATH + "users/find_by_email_or_phone";
    public static final String USER_UPDATE_INFO = API_PATH + "users/me";
    public static final String USER_CHANGE_PASSWORD = API_PATH + "users/changePassword";
    public static final String GET_USER_WITH_ID = API_PATH + "users/{user_id}";

    public static final String CLIENTS = API_PATH + "clients";
    public static final String USER_CLIENTS = API_PATH + "users/{user_id}/clients";
    public static final String USER_CLIENTS_COUNT = API_PATH + "users/{user_id}/clients_count";
    public static final String USER_GE0_CLIENTS = API_PATH + "users/{user_id}/clients/geo/{page_number}";

    public static final String ADD_COWORKER = API_PATH + "users/me/coworkers";
    public static final String GET_COWORKERS = API_PATH + "coworker/{user_id}/{group}/{count}/{filter}/{page_number}";

    public static final String GET_CONFIGURATION = API_PATH + "config/{key}";

    public static final String IS_ADDED_USER_AS_COWORKER = API_PATH + "coworkers/are_coworkers/{user_id}/{coworker_id}";
    public static final String COWORKERS_THAT_CAN_SEE = API_PATH + "coworkers/can_see/{user_id}/{section}";
    public static final String COWORKERS_ACTIVITIES = API_PATH + "activities/{user_id}/{page_number}";

    public static final String LINES = API_PATH + "lines";

    public static final String UPDATE_LINE_WITH_ID = API_PATH + "lines/{line_id}";
    public static final String USER_LINES = API_PATH + "users/{user_id}/lines/{page_number}";
    public static final String USER_CLIENT_LINES = API_PATH + "users/me/client_lines";
    public static final String GET_USER_REVENUES = API_PATH + "users/{user_id}/{group_by}/{object_id}/revenues/{page_number}";
    public static final String GET_OBJECT_REVENUES = API_PATH + "revenues/get_list/{object_type}/{object_id}/{page_number}";
    public static final String GET_USER_All_REVENUES = API_PATH + "users/{user_id}/revenues/{page_number}";

    public static final String GET_LINE_REVENUES = API_PATH + "lines/{line_id}/revenues/{page_number}";
    public static final String GET_CLIENT_REVENUES = API_PATH + "clients/{client_id}/revenues/{page_number}";

    public static final String GET_LINE_WITH_ID =  API_PATH + "line/{line_id}";
    public static final String USER_LINE_COUNT=  API_PATH + "lines/count";
    public static final String USER_GE0_LINES =  API_PATH + "lines/geo";
    //public static final String TOTAL_LINE_REVENUE =  API_PATH + "lines/{line_id}/total_revenue";
    public static final String GET_TOTAL_REVENUE =  API_PATH + "revenues/get_total/{object_type}/{object_id}";
    public static final String CLIENT_COUNT=  API_PATH + "lines/{line_id}/num_clients";
    public static final String TOTAL_REVENUE=  API_PATH + "users/{user_id}/revenues/total";
    public static final String REVENUE_DETAILS=  API_PATH + "revenue/{revenue_id}";

    public static final String LINE_CLIENTS=  API_PATH + "lines/{line_id}/clients/{page_number}";

    public static final String LINE_DELETE=  API_PATH + "lines/{line_id}/";
    public static final String ADD_LINE_CLIENT=  API_PATH + "lines/{line_id}/add_client/";
    public static final String CLIENT_DELETE=  API_PATH + "clients/{client_id}/";
    public static final String CLIENT_LINE=  API_PATH + "clients/{client_id}/lines/{page_number}";

    public static final String TERRITORIES = API_PATH + "territories";
    public static final String API_TERRITORY = API_PATH + "territory/{user_id}";
    public static final String UPDATE_TERRITORY_WITH_ID = API_PATH + "territory/{territory_id}";
    public static final String USER_TERRITORIES = API_PATH + "users/{user_id}/territories/{page_number}";
    public static final String TERRITORY_CLIENTS = API_PATH + "territories/get_clients/{page_number}";
    public static final String TERRITORY_FROM_DATABASE = API_PATH + "search/county/{query}";
    public static final String API_UPLOAD_CLIENTS  = API_PATH + "clients/upload/";
    public static final String TERRITORY_DELETE = API_PATH + "territory/{territory_id}";

    public static final String GET_NOTIFICATIONS = API_PATH + "notifications/{user_id}/{page_number}";

    public static final String REVENUES = API_PATH + "revenue";
    public static final String UPDATE_REVENUE_WITH_ID = API_PATH + "revenue/{line_id}";
    public static final String ADD_REVENUE = API_PATH + "revenue";
    public static final String GET_TOP_FIVE_REVENUE = API_PATH + "revenue/{user_id}/top_five/{group_by}";
    public static final String GET_REVENUE_BY_GROUP = API_PATH + "revenue/{user_id}/by_group/{group_by}";

    public static final String GET_TOTAL_REVENUES_FOR_USER = API_PATH + "/users/me/revenue/total";
    public static final String REVENUE_DELETE = API_PATH + "revenue/{revenue_id}";

    public static final String GET_CLIENT_WITH_ID = API_PATH + "client/{client_id}";
    public static final String UPDATE_CLIENT_WITH_ID = API_PATH + "clients/{client_id}";


    public static final String UPDATE_SCHEDULE_WITH_ID = API_PATH + "schedules/{schedule_id}";
    public static final String DELETE_SCHEDULE_WITH_ID = API_PATH + "schedules/{schedule_id}";
    public static final String GET_SCHEDULE_WITH_ID = API_PATH + "schedule/{schedule_id}";
    public static final String SCHEDULES = API_PATH + "schedules";
    public static final String CLIENT_SCHEDULES = API_PATH + "schedules/{client_id}/upcoming/{page_number}";
    public static final String USER_SCHEDULE = API_PATH + "users/{user_id}/schedules/{page_number}";
    public static final String USER_SCHEDULE_COUNT = API_PATH + "users/me/schedules_count";
    public static final String USER_GE0_SCHEDULE = API_PATH + "users/me/schedules/geo";
    public static final String USER_SCHEDULES_BY_DATE = API_PATH + "users/{user_id}/schedules/date/{page_number}";


    public static final String UPDATE_GOAL_WITH_ID = API_PATH + "goals/{goal_id}";
    public static final String DELETE_GOAL_WITH_ID = API_PATH + "goals/{goal_id}";
    public static final String GOALS = API_PATH + "goals";
    public static final String USER_GOAL = API_PATH + "users/me/goals";
    public static final String LINE_GOALS = API_PATH + "lines/{line_id}/goals/{status}/{page_num}";
    public static final String USER_GOALS_BY_DATE = API_PATH + "users/me/goals/date";
    public static final String NUM_LINE_GOALS = API_PATH + "lines/{line_id}/num_goals";

    public static final String ADD_VISIT = API_PATH + "visit";
    public static final String USER_VISITS = API_PATH + "users/{user_id}/visits/{page_number}";
    public static final String CLIENT_VISITS = API_PATH + "clients/{client_id}/visits/{page_number}";
    public static final String VISIT_DELETE = API_PATH + "visit/{visit_id}";
    public static final String VISIT_UPDATE = API_PATH + "visit/{visit_id}";
    public static final String GET_VISIT = API_PATH + "visit/{visit_id}";

    public static final String GET_INDUSTRIES = API_PATH + "config/industries";

    public static final String REPORT = API_PATH + "users/{user_id}/report";

    public static final String BRAINTREE= API_PATH + "braintree";

    public static final String APP_DATA = "app_data";
    public static final String COWORKER = "coworker";
    public static final String USER = "user";
    public static final String USER_DATA = "user_data";
    public static final String CLIENT_DATA = "client_data";
    public static final String PROXIMITY_CLIENT_DATA = "prox_client_data";
    public static final String VISIT_DATA = "visit_data";
    public static final String SCHEDULE_DATA = "schedule_data";
    public static final String SCHEDULE_LIST = "schedules";
    public static final String SCHEDULE_DATE_FILTER = "schedule_date_filter";
    public static final String CODE = "code";
    public static final String SERVER_INFO = "server_info";
    public static final String CURRENT_USER = "current_user";
    public static final String NEW_USER = "new_user";
    public static final String SPLASH_SCREEN_DONE = "sp_lash_screen_done";
    public static final String RETURNING_USER = "returning_user";
    public static final String IS_LOGGED_IN_USER = "login";
    public static final String AVATAR_STATE_SAVED = "avatar_state";
    public static final String EditingProfile = "editing_profile";
    public static final String DISTANCE_UNIT = "distance_unit";
    public static final String LAST_TIME_APP_RAN = "last_ran";

    public static final String TOKEN = "token";
    public static final String TOKEN_HEADER = "x-access-token";

    public static final String TWITTER_API_KEY = "6b2QdtehythOUVjBwEyHKeFGl";
    public static final String TWITTER_API_SECRET = "hyjXQqyRekDAcpeXb1nrb1zVwjmiovXaoTVJNuZy80D0MuNBiR";

    public static final String SELECTED_DATE = "selected_date";
    public static final String SHOW_SELECTED_DATE = "show_selected_date";
    public static final String SCHEDULE_DATA_FILTER_INDEX = "filter";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String FAX = "fax";
    public static final String PHONE = "phone";
    public static final String COMPANY_NAME = "company_name";

    public static final String PREVIOUS = "previous";

    public static final String TerritoryData="territories";
    public static final String PREFS_NEW_NOTIFICATION_COUNT ="num_alerts";
    public static final String PREFS_NEW_MESSAGE_COUNT="num_messages";
    public static final String PREFS_CLICKED_NOTIFICATION ="notification_alerts";

    public static final int Help = 0;
    public static final int SignInUser = 1;
    public static final int Reset = 2;
    public static final int DoneReset = 3;
    public static final int EmailSignUp = 4;
    public static final int Password = 5;
    public static final int Picture = 6;
    public static final int Name = 7;
    public static final int PhoneAndFax = 8;
    public static final int EnterCode = 9;
    public static final int OfficeAddress = 10;
    public static final int HomeAddress = 11;
    public static final int Territory = 12;
    public static final int SalesRep = 13;
    public static final int GroupDesc = 14;
    public static final int Industry = 15;
    public static final int AddBoss = 16;
    public static final int AddBossNow = 17;
    public static final int CoWorkerCount = 18;
    public static final int AddOptions = 19;
    public static final int CoWorker = 20;
    public static final int GOAL_REQUEST = 2300;

    public static final int Completed = 25;

    public static final int CreateSchedule = 30;
    public static final int ActivitySchedule = 31;
    public static final int HomeSchedule = 32;
    public static final int ClientAdd = 33;
    public static final int AddScheduleActivity = 34;
    public static final int ScheduleSuggestions = 35;
    public static final int ActivityFragment = 36;
    public static final int CreateAppointment = 37;
    public static final int ViewSchedule = 38;
    public static final int AppointmentList = 39;
    public static final int ADD_TERRITORY =89;
    public static final int AppointmentCalendar = 41;

    public static final int HomeSale = 42;
    public static final int SaleViewAll = 43;

    public static final int Profile = 40;

    public static final int MapFragment = 45;
    public static final int ProfileFragment = 46;

    public static final int Lines = 50;

    public static final int AddClient = 60;
    public static final int ViewClient = 61;

    public static final int ClientFilter = 80;
    public static final int ClientDelete = 81;
    public static final int LineDelete = 82;
    public static final int Visits = 83;
    public static final int VISIT_FILTER = 84;

    //    Profile Activity fragments indexes
    public static final int EditProfile = 101;
    public static final int ProfileAddress = 102;
    public static final int Notification = 103;
    public static final int ChangePassword = 104;
    public static final int PRIVACY = 105;
    public static final int PRIVACY_SALES = 106;
    public static final int LineAndTerritory = 920390;
    public static final int REVENUE_LIST = 23450;
    public static final int SALES_SOURCE = 90000;
    public static final int LINE_SOURCE = 90001;
    public static final int COWORKER_SOURCE = 90002;
    public static final int PICK_CONTACT = 123;
    public static final int VISIT_LIST = 23451;


    public static final String RECEIVER = "geo_address_receiver";
    public static final String FETCH_TYPE_EXTRA = "geo_address_fetch_extra";
    public static final int USE_ADDRESS_NAME = 1;
    public static final String LOCATION_NAME_DATA_EXTRA = "location_name_data_extra";
    public static final int FAILURE_RESULT = -1;
    public static final int SUCCESS_RESULT = 0;
    public static final String RESULT_ADDRESS = "address";
    public static final String RESULT_DATA_KEY = "data_key";
    public static final int SELECT_CLIENT = 1000;
    public static final int SELECT_USER = 1005;
    public static final int SELECT_LINE = 1003;
    public static final int SELECT_TERRITORY = 1004;
    public static final int ADD_SALES = 1001;
    public static final int ADD_CLIENT = 2001;
    public static final int EDIT_CLIENT = 2002;
    public static final int ADD_LINE = 3001;
    public static final int FILTER_CODE = 302;
    public static final String FILTER = "filter";
    public static final String SOURCE = "source";

    public static final String TOTAL_SALES = "Total sales";

    public static final String RAIN = "rain";
    public static final String CLEAR_DAY = "clear-day";
    public static final String CLEAR_NIGHT = "clear-night";
    public static final String SNOW = "sn`ow";
    public static final String SLEET = "sleet";
    public static final String FOG = "fog";
    public static final String WIND = "wind";
    public static final String PARTLY_CLOUDY_DAY = "partly-cloudy-day";
    public static final String PARTLY_CLOUDY_NIGHT = "partly-cloudy-night";

    public static final String TERRITORY_DATA = "territory_data";
    public static final String CLIENT_EDITED = "client_updated";
    public static final String SCHEDULE_EDITED = "schedule_updated";
    public static final String SCHEDULE_CREATED = "a_schedule_exist";
    public static final String SCHEDULE_DELETED = "schedule_deleted";
    public static final String CLIENT_CREATED = "a_client_exist";
    public static final String NO_CLIENT_FOUND = "no_client_found";
    public static final String NO_SCHEDULE_FOUND = "no_schedule_found";
    public static final String LINE_CREATED = "a_line_exist";
    public static final String GOOGLE_REVERSE_GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&key=AIzaSyDdWKslIGOEWJMa5RJFwH2dowBc7rEQ14g&latlng=";
    public static final String LINE_DATA = "line";
    public static final String REVENUE_DATA = "revenue";
    public static final String LINES_lIST = "lines";
    public static final String VISITS_lIST = "visits";
    public static final String CLIENTS_lIST = "clients";
    public static final String SHOW_TERRITORY = "show_territory";
    public static PersistentCookieStore MY_COOKIE_STORE = null;
    public static final String INDUSTRIES = "industries";
    public static final String SHOW_SALE = "showsale";
    public static final String SHOW_FILTER = "showfilter";
    public static final String SHOW_CLIENT = "showClient";
    public static final String SHOW_LINE = "showLine";
    public static final String SHOW_MORE_CLIENT = "showMoreClients";
    public static final String CLIENT_ID = "client_id";
    public static final String LINE_ID = "line_id";
    public static final String SELECTED_IDS ="selected_ids";
    public static final String PROXIMITY_REMINDER_DISTANCE="PROXIMITY_REMINDER_DISTANCE";
    public static final String NEARBY_CLIENTS="NEARBY_CLIENTS";


    public static File DIR_ROOT = new File("StreamLive"),
            DIR_CACHE = new File("cache"), DIR_HTML_CACHE = new File("html"), DIR_DOWNLOADS = new File("downloads"), DIR_MEDIA = new File("media");

    public static final String CLIENT_LIST = "client_list";

    public static String GOAL_DATA = "goal";
    public static final String GOAL_lIST = "goals";

}
