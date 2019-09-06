package com.autismindd.listener;


import com.autismindd.dao.User;

/**
 * Created by Macbook on 28/09/2016.
 */

public class MultichoiceAdapterInterface {

    public interface ControlMethods{
        public void setSingleModeKey(User user);
        public void addMultichoiseModeKey(User user);
        public void clearMultichoiceMode();
        public void clearSingleChoiceMode();
    }

}
