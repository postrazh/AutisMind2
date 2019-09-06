package com.autismindd.utilities;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * Created by Reaz on 1/31/2017.
 */

public class Documentation {



    /*********************************************PackDownload*****************************************************
     http://198.37.116.213/MindApp/ImagesManager/GetPackByID/?id=1&authenticationUserName=AutisMind&password=dniMsituA

    ******************************************* Pack Information Download *******************************************
     http://198.37.116.213/MindApp/ImagesManager/GetAllTask/?authenticationUserName=AutisMind&password=dniMsituA


   ******************************************* Pack Image Download link *******************************************
     http://198.37.116.213/MindApp/Download/GetFile?file=packImages.zip
     */

     // Purchase Activity
    // Added Date  1/31/2017.
    /**
     *
     *                                  Purchase Activity
     *
     *  1. get user from  staticInstance.getUser(); // here user all information
     *  2. user.getMusic() check 1 or 0 if 1 user has Background Music else music option off.
     *  3.  firstLayer from staticInstance.getFirstLayer(); which layer selected for purchase
     *  4. screenModePurchase(isLamp);  // isLamp if true lamp light is off else isLamp of false that time lamp is on
     *  5. FirstLayer Id wise load all information in setFirstLayerImag(final FirstLayer firstLayer)
     *  6. if isPurchase is true that time taskPack Download will be start
     *  else isPurchase is false that time Purchase process will be execute when product id purchased that time  download start
     *  7. getting product id : google play > product id added via  product manage >  firstlayer id wise i mean 1 -10 product id add>
     *  android Studio > Project> ...>Utilities>UserInfo> getProductID(int i) i = firstLayerID from firstLayer
     *  8. TransparentToastActivity  will show a custom toast Dialog. That appear on this activity.
     *
     *                                      MainActivity
     *
     *  This  is a luncher Activity  for this app. Bydafault background music will play.
     *  if app is on release mode, Autismind appear as letter ways animation then go to "WelcomeActivity"  to download task pack.
     *                                      WelcomeActivity
     *  download dialog will appear to download the task pack from server. After permiting to download the task pack, started to download
     *  using this inner class "DownloadFileFromURL" and store on "/sdcard/Autismind/" . After downloading task pack will unZipp using this inner class "UnzipFromDownloadedFolder" from
     *  storage and set on  "FirstLayerActivity" as a grid.
     * if app is on development mode, need not to download just need to install shared task pack from storage by clicking "AUTISMIND" logo.
     * there are another two "Info" and "Cloud animated Arrow"  buttons. OnClick Info Button, "InfoActivity"  will appear.
     * OnClick Cloud animated Arrow button, "UserListActivity"  will appear.
     *
     *                                     InfoActivity
     *
     * this is information screen included with "wwww.autismind.com", "IDAPP", "limbika" and "BackButton". Click on 1st three buttons, enter
     * into specific weblink and click on back button, return to "WelcomeActivity".
     *                                   UserListActivity
     *  There are 4 buttons, "UserCreate", "UserSettings", "Statistics", "UserDelete"
     *
     *
     *
     *                                      WelcomeActivity
     *
     *  1. show dialog for download taskPack .map file and DownloadFileFromURL asyncTask will be execute when download completed that time sharepreference is setDownloadFlag StaticAccess.TAG_SP_DOWNLOAD_FLAG value update
     *  2. after download complete UnzipFromDownloadedFolder asyncTask execute for TaskImageDownload class call this call only download taskPack image zip file and unzip, delete  and downloaded .map file unzip and insert
     *     local database, delete (.map) file.
     *  3. daily check internet and DownloadService service start for TaskImageDownload class call
     *  4. all time Internet check and download Json data from server and check file(.map) are not same that's
     *     time FirstLayer and FirstLayerImage Table update and changes pack download and install those pack.
     *     Flow for download and update(WelcomeActivity in package(activity)>DailyUpdateCheck in package(download)>SynTask in package(download)>).
     *     Model Class directory name POJO for Json - FLayer and FLayerImage
     *     Step 1. check Internet
     *     step 2. call dailyUpdateCheck.executeDailyCheck() for json Data Download (execute JsonReading AsynTask)
     *     Step 3.  call  dailyUpdateCheck.executeCheckFileNameAsync() for compare (.map) file Name change (execute CheckFileNameAsync AsyncTask),
     *              changes file firstLayerTaskID added into arrFirstUpdateList(local FirstLayer)
     *     Step 4.  call  downloadStart() from SynTaskPack and check lock and unlock ids(FirstLayerTaskID) unlock ids(FirstLayerTaskID) are following steps-
     *                       1) download
     *                       2) delete (firstLayerTaskID related TaskPacks, Tasks, Items, Images and Sounds)
     *                       3) unzip Downloaded TaskPacks
     *                       4) update local database Table (FirstLayer and FirstLayerImages).
     *
     *              Also locked FirsLayers only update local database (FirstLayer and FirstLayerImages)
     *
     *
     *
     *
     *
     *                                      TaskImageDownload
     *
     *   1. download start for taskPackImage
     *   2. zip file unzip to Android/Data/package/Images/
     *   3.delete zip file
     *
     *
     */

    //////////////****************** LevelView custom view class functionality details*****************///////////////

    /*
    * 1. initMyView() //initialize all paints
    * 2. getCircleLocation(int angle) //return a circle position by giving an angle
    * 3. onDraw(Canvas canvas) //draw all view like circle,star,user avatar, lock and unlock bitmaps
    * 4. drawBigCircle(Canvas canvas, Paint p)//method that draw the big circle bottom on the screen
    * 5. drawCircle(Canvas canvas, Paint paint, Bitmap bitmap, float x, float y, int radius) //   /// method that draw 6 circle by given position (used in on draw method)
    * 6. drawDisAppearCircle(Canvas canvas, Paint paint, float cx, float cy) // used to draw a blank circle for a certain time by giving the circle position(cx,cy)
    * 7. drawCircleWithText(Canvas canvas, Paint paint, String text, float x, float y, int radius) /// method that draw text  with a circle called from drawStar method by checking if the level played before
    * 8. drawText(Canvas canvas, float x, float y, String text, float size, int color, float boldWidth) // method that draw text called from drawCircleWithText() Method/// its draw percent text on the given circle position
    * 9. drawStar(Canvas canvas, float x, float y, int starCount, Bitmap starBitmap,int point)    /// method that draw 3 star by checking starcount variable and called from on draw by giving its required value
    *10. drawLeftStar(Canvas canvas, float x, float y, Paint paint, Bitmap bitmap) //method that draw left star on a given circle above
    *11. drawRightStar(Canvas canvas, float x, float y, Paint paint, Bitmap bitmap)    //method that draw right star on a given circle above
    *12. drawMiddleStar(Canvas canvas, float x, float y, Paint paint, Bitmap bitmap) //method that draw middle star on a given circle above
    *13. getWhiteGreyBitmap() //method that return grey bitmap
    *14. getYellowBitmap()/// method that return yellow bitmap
    *15. getResizedBitmap(Bitmap bm, int newWidth, int newHeight)/// method that resized a bitmap by giving new width and height
    *16. getCircleBitmap(Bitmap bitmap)/// method that make a image circular
    *17. isViewOverlapping(int x, int y, float[] circleLocation)/// method that check if two rectangle intersect or not called from overlap method called when we touch a circle
    *18. checkOverLaping(float x, float y, int width, int height)    ///// method called from taskpackActivity for checking if user avatar is overlap to a circle
    *19. enabledCircleClick(boolean enableCircleClick)  ////central method that control circle click event
    *20. getPlayAble()  // helper method that give us the next level that is playable
    *21. lastStar()    //this method will return the last available star which can indicate the unlocker screen
    *22. onDetachedFromWindow()  /// media player release int onDetachedFromWindow  method
    *22. onTouchEvent(MotionEvent event)/// on touch event method that give us x y position
    *23. makeUnlockDisAppear(boolean makeDisAppear)    /// method that used for making unlock bitmap disappear
    *24. public interface WhichViewClicked {
         public void viewPosition(int labelID);// seting circle postion

         public void playsound(int avatar);/// play sound on touch

         public void getAnimationPos(float x, float y);// set animaton positn

         public void getUnlockCircleInfo(float cx, float cy, int radius);///setting unlock level information
         }// whoever implement this// if position=5 must check which circle
    * */


    ///// method that return device size in inch.
//    private void getPhoneInfo() {
//        Display display = getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();  // deprecated
//        int height = display.getHeight();  // deprecated
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        double x = Math.pow(width / dm.xdpi, 2);
//        double y = Math.pow(height / dm.ydpi, 2);
//        double screenInches = Math.sqrt(x + y);
//        Log.d("hai", "Screen inches : " + screenInches + "");
//
//    }


}
