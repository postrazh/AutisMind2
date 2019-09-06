package com.dmk.limbikasdk.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ibrar on 4/6/2016.
 */
public class ImageProcessing {

    String imageNameForSDCard;
    private String appImagePath = null;
    public static final String tempFileName = "temp_photo.jpg";

    //Context ctx;

    public ImageProcessing(String packageName) {
        // ctx = context;
        appImagePath = "/Android/Data/" + packageName + "/Images/";

    }

    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    // image decode method
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    // Save image to SD card
    public String imageSave(Bitmap bmp) {
        try {
            if (bmp != null) {
                File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + appImagePath);
                if (!sdCardDirectory.exists()) {
                    sdCardDirectory.mkdirs();
                }

                imageNameForSDCard = "md_" + System.currentTimeMillis() + ".png";
                File image = new File(sdCardDirectory, imageNameForSDCard);
                FileOutputStream outStream;

                outStream = new FileOutputStream(image);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
                //bmp.recycle();
                return imageNameForSDCard;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public String getAbsolutepath_Of_Image(String imageName) {
        return Environment.getExternalStorageDirectory() + appImagePath + imageName;
    }
    // Getting Image from SD Card

    public Bitmap getImage(String imageName) {
        File image = new File(getAbsolutepath_Of_Image(imageName));
        if (image.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath());
            return bmp;
        } else {
            return null;
        }
    }

    private Bitmap resize(Bitmap originalImage, int width, int height) {
        Bitmap background = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        float originalWidth = originalImage.getWidth(), originalHeight = originalImage.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = width / originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(originalImage, transformation, paint);
        originalImage.recycle();
        return background;
    }

    public boolean deleteImage(String imageName) {
        boolean deleted = false;
        File image = new File(getAbsolutepath_Of_Image(imageName));
        if (image.exists()) {
            deleted = image.delete();
        }
        return deleted;
    }

    public String getImageDir() {
        return appImagePath;
    }

    /**
     * Save image to SD card for crop
     **/
    public File imageSaveToCrop(Bitmap bmp, File imageFile) {
        try {
            File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + appImagePath);
            if (!sdCardDirectory.exists()) {
                sdCardDirectory.mkdirs();
            }

            imageFile = new File(sdCardDirectory, tempFileName);
            FileOutputStream outStream;

            outStream = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            //bmp.recycle();
            return imageFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getCroppedImage() {
        File image = new File(getAbsolutepath_Of_Image(tempFileName));
        if (image.exists()) {
            return image;
        } else {
            return null;
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2, (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }


}
