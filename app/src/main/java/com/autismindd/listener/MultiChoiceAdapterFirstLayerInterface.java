package com.autismindd.listener;


import com.autismindd.dao.FirstLayer;

/**
 * Created by Macbook on 28/09/2016.
 */

public class MultiChoiceAdapterFirstLayerInterface {

    public interface ControlMethods{
        public void setSingleModeId(FirstLayer firstLayer);
        public void addMultichoiseModeId(FirstLayer firstLayer);
        public void clearMultichoiceMode();
        public void clearSingleChoiceMode();
    }

}
