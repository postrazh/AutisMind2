package com.autismindd.parser;

import android.content.Context;
import android.util.Log;

import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.FirstLayerTaskImage;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.pojo.FLayer;
import com.autismindd.pojo.FLayerImages;
import com.autismindd.utilities.StaticAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by RAFI on 3/7/2017.
 */

public class DailyFirstLayerParser {

    Context context;
    JSONArray jsonArray;
    DatabaseManager databaseManager;
    public ArrayList<FLayer> getParserArrayFLayerList;

    // for avatar text Color static value
    public static final String FIRST_LAYER_NAME = "label";
    public static final String FIRST_LAYER_IMG_PATH = "image_path";
    public static final String FIRST_LAYER_ID = "first_layer_id";
    public static final String FIRST_LAYER_FILE_NAME = "task_pack";

    public static final String FIRST_LAYER_TASK_IMAGE_ARRAY = "task_pack_images";
    public static final String FIRST_LAYER_TASK_IMAGE_DESC = "description";
    public static final String FIRST_LAYER_TASK_IMAGE_URL = "task_pack_image";
    public boolean isDownload = false;

    public DailyFirstLayerParser(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
        databaseManager = new DatabaseManager(context);
    }


    public ArrayList<FLayer> getParserArray() {
        Log.d("JsonArray", jsonArray.toString());

        try {
            getParserArrayFLayerList = new ArrayList<>();
            if (jsonArray != null)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jresponse = jsonArray.getJSONObject(i);
                    FLayer fLayer = new FLayer();
                    fLayer.setName(jresponse.getString(FIRST_LAYER_NAME));
                    int firstLayerId = Integer.parseInt(jresponse.getString(FIRST_LAYER_ID));
                    fLayer.setFirstLayerTaskID(firstLayerId);
                    fLayer.setFileName(jresponse.getString(FIRST_LAYER_FILE_NAME));
                    fLayer.setImgUrl(jresponse.getString(FIRST_LAYER_IMG_PATH));
                    fLayer.setState(false);
                    if (i == 0) {
                        fLayer.setLocked(false);
                    } else
                        fLayer.setLocked(true);
                    JSONArray jArray = jresponse.getJSONArray(FIRST_LAYER_TASK_IMAGE_ARRAY);
                    ArrayList<FLayerImages>allFlayerImage=new ArrayList<>();
                    for (int j = 0; j < jArray.length(); j++) {
                        JSONObject ja = jArray.getJSONObject(j);

                        FLayerImages firstLayerTaskImage = new FLayerImages();
                        firstLayerTaskImage.setFirstLayerTaskId(firstLayerId);
                        firstLayerTaskImage.setFirstLayerTaskDes(ja.getString(FIRST_LAYER_TASK_IMAGE_DESC));
                        firstLayerTaskImage.setFirstLayerTaskImages(StaticAccess.ROOT_URL + "Images/taskPackImages/" + ja.getString(FIRST_LAYER_TASK_IMAGE_URL));
                        allFlayerImage.add(firstLayerTaskImage);
//                        databaseManager.insertFLayerTaskImage(firstLayerTaskImage);
                    }
                    fLayer.setfLayerImagesList(allFlayerImage);
                    getParserArrayFLayerList.add(fLayer);
//                    databaseManager.insertFirstLayer(firstLayer);
                    isDownload = true;
                }

        } catch (JSONException e) {
            e.printStackTrace();
            isDownload = false;
        }
        return getParserArrayFLayerList;
    }
}
