package com.autismindd.utilities;

import android.os.ParcelUuid;

/**
 * Created by DENNOH on 5/13/2016.
 */
public class StaticAccess {


    public static final String PDF_TEMP_IMAGE_NAME = "temp_statistic_pdf_image"; //for generating pdf {sumon}
    public static final String PDF_LAYER_NAME = "pdf_layer_name";/// for generating pdf {sumon}
    public static int musicOnOff = 1;

    //For Sound Static value created by Reaz
    public static final int TAG_SOUND_AVATAR_LEVEL_ONE = 0x1;
    public static final int TAG_SOUND_AVATAR_LEVEL_TWO = 0x2;
    public static final int TAG_SOUND_AVATAR_LEVEL_THREE = 0x3;
    public static final int TAG_SOUND_AVATAR_LEVEL_FOUR = 0x4;
    public static final int TAG_SOUND_AVATAR_LEVEL_FIVE = 0x5;

    //for File Size Static Value Created bt Reaz
    public static final int TAG_SOUND_FILE_SIZE = 3000;

    //For Anim Static value  created by Reaz
    public static final int TAG_ANIM_NEGATIVE = 0x1;
    public static final int TAG_ANIM_POSITIVE = 0x2;
    public static final int TAG_ANIM_FEEDBACK = 0x3;


    //For Repositioning Item sequence
    public static final int SEND_TO_BACK = 0x1;
    public static final int SEND_TO_FRONT = 0x2;
    public static final int SEND_TO_BACK_MOST = 0x3;
    public static final int SEND_TO_TOP_MOST = 0x4;

    //For Task Mode
    public static String Normal = "N";
    public static String DragDrop = "D";
    public static String Assistive = "A";


    public static String NORMAL_TRUE = "T";
    public static String NORMAL_FALSE = "F";

    //For Font Static value  created by Reaz
    public static final int TAG_TYPE_NORMAL = 0;
    public static final int TAG_TYPE_FACE_DANCING = 1;
    public static final int TAG_TYPE_FACE_ROBOTO_THIN = 2;
    public static final int TAG_TYPE_FACE_ROAD_BRUSH = 3;
    public static final int TAG_TYPE_FACE_ROBOTO_CONDENSED = 4;
    public static final int TAG_TYPE_FACE_RANCHO = 5;
    public static final int TAG_DIALOG = 0x04;

    //For Font Static array value  created by Reaz
    public static final String fontName[] = {"DEFAULT", "DANCING ", "ROBOTO THIN", "ROAD BRUSH", "ROBOTO CONDENSED", "RANCHO"};
    public static final String soundDelay[] = {"None", "1 second ", "3 second", "5 second", "7 second", "10 second"};


    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    //For Feedback Type Vaue by RAFI
    public static final int TAG_TYPE_RECTANGULAR = 0;
    public static final int TAG_TYPE_CIRCULAR = 1;

    //For sound  Static value in play mode created by Reaz

    public static final int TAG_ITEM_SOUND_PLAY = 0;
    public static final int TAG_END_SINGLE_TAP_TASK = 1;
    public static final int TAG_POSITIVE_SOUND_PLAY = 2;
    public static final int TAG_NEGATIVE_SOUND_PLAY = 3;
//  public static final int TAG_FEEDBACK_SOUND                     = 3;


    public static final String TAG_PLAY_ERROR_IMAGE = "errorImage";
    public static final String TAG_PLAY_ERROR_TEXT = "errorText";
    public static final String TAG_PLAY_ERROR_COLOR = "errorColor";
    public static final String TAG_PLAY_ERROR_SOUND = "errorSound";
    public static final String TAG_PLAY_ERROR_TASK_ID = "taskCurrentIndex";
    public static final String TAG_PLAY_ERROR_RESPONSE = "ERROR";

    public static final String TAG_FINAL_TASK_ID = "TotalTaskNum";
    public static final String TAG_lAST_TASK_ID = "TaskID";

    public static final String TAG_DT_MILL_SEC = "DT";
    public static final String TAG_RT_MILL_SEC = "RT";

    public static final String TAG_HASH_MAP_TIME_RT_OBJECT_MILE = "rtTime";
    public static final String TAG_HASH_MAP_TIME_INDEX_OBJECT = "rtIndex";

    public static final int TAG_TASK_GENERAL_MODE = 0x01;
    public static final int TAG_TASK_DRAG_AND_DROP_MODE = 0x02;
    public static final int TAG_TASK_ASSISTIVE_MODE = 0x03;
    public static final String TAG_TASK_TUTORIAL = "tutorial";
    public static final String TAG_TASK_MODE_KEY = "MODE";
    public static final String TARGET_KEY = "TARGET";
    public static final String TAG_UPDATE_ACTIVITY = "update";

    public static final String TAG_STATISTICS_VALUE_INDEX = "Value";

    public static final long TAG_UPDATE_ACTIVITY_FLAG = 1;
    public static final long TAG_UPDATE_LEVEL_ACTIVITY_FLAG = 2;
    public static final int PERCENT_HUNDRED = 100;
    //level
    public static final int LEVEL_ONE = 0;
    public static final int LEVEL_TWO = 1;
    public static final int LEVEL_THREE = 2;
    public static final int LEVEL_FOUR = 3;
    public static final int LEVEL_FIVE = 4;
    public static final int LEVEL_SIX = 5;
    public static final int TOTAL_LEVEL = 6;
    // For help by Rokan
    public static final int HELP_OFF = 0;
    public static final int HELP_ON = 1;

    // For Close App by Rokan
    public static final int CLOSE_APP_OFF = 0;
    public static final int CLOSE_APP_ON = 1;

    // For progress value By Rokan
    public static final int PLAYER_PROGRESS_OFF = 0;
    public static final int PLAYER_PROGRESS_ON = 1;

    // For SuperError by Rokan
    public static final int SUPER_ERROR_OFF = 0;
    public static final int SUPER_ERROR_ON = 1;


    // For UserMode/EditMode by Rokan
    public static final int USER_CREATE = 0;
    public static final int USER_EDIT = 1;


    // for avatar static value
    public static final int AVATAR_GIRL = 0;
    public static final int AVATAR_CAR = 1;
    public static final int AVATAR_DINOSAUR = 2;
    public static final int AVATAR_HORSE = 3;
    public static final int AVATAR_ROCKET = 4;

    // for avatar background Color static value

    public static final String AVATAR_GIRL_COLOR = "#ffd5e5";
    public static final String AVATAR_CAR_COLOR = "#ff9999";
    public static final String AVATAR_DINOSAUR_COLOR = "#c6e9af";
    public static final String AVATAR_HORSE_COLOR = "#eeaaff";
    public static final String AVATAR_ROKET_COLOR = "#d7d7f4";

    // for avatar text Color static value
    public static final String AVATAR_GIRL_TEXT_COLOR = "#ff5599";
    public static final String AVATAR_CAR_TEXT_COLOR = "#ff6666";
    public static final String AVATAR_DINOSAUR_TEXT_COLOR = "#8dd35f";
    public static final String AVATAR_HORSE_TEXT_COLOR = "#dd55ff";
    public static final String AVATAR_ROKET_TEXT_COLOR = "#5f5fd3";
    //    http://192.52.243.6/MindApp/ImagesManager/GetAllTask/?authenticationUserName=AutisMind&password=dniMsituA
//    http://192.52.243.6/MindApp/ImagesManager/GetPackByID/?id=3&authenticationUserName=AutisMind&password=dniMsituA
//    public static final String ROOT_URL = "http://192.52.243.6/MindApp/";
    public static final String ROOT_URL = "http://198.37.116.213/AutisMind/";
    public static final String ROOT_URL_IMAGE = "http://192.52.243.6/MindApp/";

    //For Result play Sound Static value created by Reaz
    public static final int TAG_SOUND_OPEN = 0x1;
    public static final int TAG_SOUND_STAR = 0x2;
    public static final int TAG_SOUND_STAR_FINISH = 0x3;
    public static final int TAG_SOUND_PRIZE = 0x4;
    public static final int TAG_SOUND_PRIZE_TAP = 0x5;
    public static final int TAG_SOUND_STAR_END = 0x6;

    public static final String RESULT_FLAG = "resultFlag";
    public static final String RESULT_POINT = "result";
    public static final String RESULT_STAR = "star";
    public static final String PRV_STAR = "prvStar";
    public static final String CUR_LEVEL = "curLevel";
    //1-15-45-75-120 prev
    //1-15- 50-100- 175 prev
    //1-15- 50-100- 160 current

    public static final int TAG_AVATAR_UPDATE_ONE_VALUE = 0;
    public static final int TAG_AVATAR_UPDATE_TWO_VALUE = 15;
    public static final int TAG_AVATAR_UPDATE_THREE_VALUE = 50;
    public static final int TAG_AVATAR_UPDATE_FOUR_VALUE = 100;
    public static final int TAG_AVATAR_UPDATE_FIVE_VALUE = 160;

    public static int TAG_AVATAR_UPDATE = 2101;

    // for taskpack Color static value
    public static final String FIRSTLAYER1 = "#5599FF";
    public static final String FIRSTLAYER2 = "#FF5555";
    public static final String FIRSTLAYER3 = "#81C784";
    public static final String FIRSTLAYER4 = "#BA68C8";
    public static final String FIRSTLAYER5 = "#FFB74D";
    public static final String FIRSTLAYER6 = "#90A4AE";
    public static final String FIRSTLAYER7 = "#FF80E5";
    public static final String FIRSTLAYER8 = "#A1887F";
    public static final String FIRSTLAYER9 = "#DCE775";
    public static final String FIRSTLAYER10 = "#4DB6AC";

    /***********************************
     * ANDROID_DATA COMMON Static Value
     **************************************/
    public static final String ANDROID_DATA = "/Android/Data/";
    public static final String ANDROID_DATA_PACKAGE_IMAGE = "/Images/";
    public static final String ANDROID_DATA_PACKAGE_SOUND = "/Sound/";


    //////////////Internet////////////////////////////////////
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    // For touch animation static Value
    public static final int ANIM_TOUCH_NONE = 0;
    public static final int ANIM_TOUCH_COLOR_CIRCLE = 1;
    public static final int ANIM_TOUCH_COLOR_CIRCLE_STROKE = 2;
    public static final int ANIM_TOUCH_CIRCLE_STROKE = 3;
    public static final int ANIM_TOUCH_EVEN = 4;
/*    public static final int ANIM_TOUCH_NONE                       = 4;
    public static final int ANIM_TOUCH_NONE                        = 5;*/


    //    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyb+4yWo6gBviEIcOyi8yVCPB7M0j61htffgEvys/5pfMPtWeEHS6nMwBiK0RU8Umx4Z0EWJSzG0kUkg1y6rMvV/3luATk/HDjfVMM/SCvz/nbr7RCttNaZN8QKYChaDZ4gqLXUXJncflPuiz/aRNgcxFUX+ssKwCS5rY9sQH/rWo9G+LP7MbcKbTrZEI7rth8XN6h0Wb9JeL21gYI77/8yU8NX/jyFJcz9qIf/cFgT7MTxVO/XXcRMuuyyvsga9gj7Chv5iI9xgeJGItleiW/2O96YJiq/53Zmavk7lFdu/x+WttTh/v6zW3sUf1baQgiDTkdUbtxwTbFFytCKXo7wIDAQAB"; // PUT YOUR MERCHANT KEY HERE;
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg5n56/gX/6GBL7AGdrN3jRXqir3RSdCpWhQiZFeHxqWFF2HRmHJTdxCfYWtswk4H80Fh7unAro0EHNv1xHJOlFyyXU3zT3HO4PRmW0PeIRa2pkSddv3lKs4QvvJgofeSOhoF+xfOOB0vb+MgJ7g0uMtpkRX67SXT1E1+r+2PnPQVX+ic0YPME2v+NemrPJXyX/qoxODoBHih5K2z/wsmYjzDIUn59ruzyjSVFq/XAARCENIIIJHB/Kfo2+KolL731FSBmE5x5qSpZCW6Pz8Olx5770h4TCWr41MpLFn1clz6rmvNX/yotD7NWyz4LS+kSblIAiQTKr2UsB1mYxRQ+QIDAQAB";
    public static final String MERCHANT_ID = "10406650668768583378";

    ///// BRAODCAST RECEIVER TAG FOR STARS
    public static final String TAG_BROADCAST_RECEIVER_FOR_STAR = "custom-event-name";
    public static final String TAG_BROADCAST_RECEIVER_KEY = "message";

    public static final String TAG_DETECT_APP_CLOSED_BUTTON_PRESSED = "isAppClosePressed";

    public static final String TAG_SP_DOWNLOAD_FLAG = "downloaded";

    public static final int TAG_DAILY_JSON_DATA_FLAG = 1;
    public static final int TAG_INSTALL_JSON_DATA_FLAG = 0;
    public static final int TAG_UNLOCKED_FLAG = 1;
    public static final int TAG_LOCKED_FLAG = 0;


}
