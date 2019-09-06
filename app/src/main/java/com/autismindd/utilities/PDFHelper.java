package com.autismindd.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Probook 440 on 2/5/2017.
 */
////// PDFHelper>generatePDf>convertBitmapToImage>takeScreenShot>Write doc >view pdf
public class PDFHelper {
    Activity activity;
    File makeFile;
    private ImageProcessing imageProcessing;

    public PDFHelper(Activity activity) {
        this.activity = activity; /// activity that u want to get screenshot pdf
        imageProcessing = new ImageProcessing(activity);
    }

    //// method that generate pdf by getting user name
    public boolean generatePdf(String userName, String firstBitmapPath, String layerTitle) {
        Document doc = new Document();

        if (isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            try {

                makeFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), userName + "_" + System.currentTimeMillis() + ".pdf");

                Log.e("makefile: ", makeFile.getAbsolutePath());
                PdfWriter.getInstance(doc, new FileOutputStream(makeFile));
                doc.open();
                Image firstImage = convertBitmapToImage(doc, imageProcessing.getImage(firstBitmapPath));
                doc.add(firstImage);
                /// adding new line to doc
                doc.add(new Phrase("\n"));
                /// writting layer title
                Paragraph para1 = new Paragraph(layerTitle);
                para1.setAlignment(Paragraph.ALIGN_CENTER);
                para1.setSpacingAfter(20);
                doc.add(para1);

                Image secondImage = takeScreenShot(doc);/// taking screenshot
                doc.add(secondImage);
                doc.close();
                //viewPDF();
                return true;
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /// take screenshot method used in generatePdf method
    @NonNull
    private Image takeScreenShot(Document doc) throws BadElementException, IOException {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin()
                - doc.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler);
        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
        return image;
    }

    // convert bitmap to itext liberay image formate to attach on pdf docs
    private Image convertBitmapToImage(Document doc, Bitmap bitmap) throws IOException, BadElementException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin()
                - doc.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler);
        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
        return image;
    }

    /// return file that created for save
    public File getMakeFile() {
        return makeFile;
    }

    //// view the generated pdf
    public void viewPDF() {
        File file = this.getMakeFile();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    /// method that check the external storage is readonly or not
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    /// method that check the external storage space is available or not
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
