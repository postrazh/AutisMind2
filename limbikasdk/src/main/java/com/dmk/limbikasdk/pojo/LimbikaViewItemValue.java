package com.dmk.limbikasdk.pojo;

import com.dmk.limbikasdk.views.LimbikaView;

/**
 * Created by LAPTOP DREAM on 4/12/2016.
 */

public class LimbikaViewItemValue {

    private String result;
    private Long id;
    private float x;
    private float y;
    private Integer rotation;
    private Long key;
    private Integer isCircleView;
    private Integer circleColor;
    private String userText;
    private Integer textColor;
    private Integer textSize;
    private Integer borderColor;
    private Integer backgroundColor;
    private Integer drawable;
    private float width;
    private float height;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private String imagePath;
    private String type;
    private LimbikaView limbikaView;
    private Boolean active;
    private int allowDragDrop;
    private Long dragDropTarget;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
    //private Integer outerBorderSize;
    private Integer cornerRound;
    private Long navigateTo;
    private String openApp;
    private String openURL;
    private Integer showedBy;
   // private Integer allowAlign;
   // private Long alignTarget;
    private Integer closeApp;
    private String itemSound;
    private int fontTypeFace;
    private int fontAlign;

    private int autoPlay;
    private int soundDelay;
   private int borderPixel;


    // added by reaz
    public int getFontAlign() {
        return fontAlign;
    }

    // added by reaz
    public void setFontAlign(int fontAlign) {
        this.fontAlign = fontAlign;
    }


    public Integer getCloseApp() {
        return closeApp;
    }

    public void setCloseApp(Integer closeApp) {
        this.closeApp = closeApp;
    }


    public String getItemSound() {
        return itemSound;
    }

    public void setItemSound(String itemSound) {
        this.itemSound = itemSound;
    }

    public String getOpenURL() {
        return openURL;
    }

    public void setOpenURL(String openURL) {
        this.openURL = openURL;
    }


    /*public Integer getAllowAlign() {
        return allowAlign;
    }

    public void setAllowAlign(Integer allowAlign) {
        this.allowAlign = allowAlign;
    }

    public Long getAlignTarget() {
        return alignTarget;
    }

    public void setAlignTarget(Long alignTarget) {
        this.alignTarget = alignTarget;
    }*/


    public Integer getShowedBy() {
        return showedBy;
    }

    public void setShowedBy(Integer showedBy) {
        this.showedBy = showedBy;
    }


    public String getOpenApp() {
        return openApp;
    }

    public void setOpenApp(String openApp) {
        this.openApp = openApp;
    }


    public Long getNavigateTo() {
        return navigateTo;
    }

    public void setNavigateTo(Long navigateTo) {
        this.navigateTo = navigateTo;
    }


    public Integer getCornerRound() {
        return cornerRound;
    }

    public void setCornerRound(Integer cornerRound) {
        this.cornerRound = cornerRound;
    }


    public void setChildView(boolean childView) {
        isChildView = childView;
    }

    public boolean isChildView() {
        return isChildView;
    }

    private boolean isChildView = false;


    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    private boolean isParent = false;

    public LimbikaView getDropTarget() {
        return dropTarget;
    }

    public void setDropTarget(LimbikaView dropTarget) {
        this.dropTarget = dropTarget;
    }

    private LimbikaView dropTarget = null;


    private Long taskId;


    public LimbikaViewItemValue() {
    }

    public LimbikaViewItemValue(Long id) {
        this.id = id;
    }

    public LimbikaViewItemValue(Long id, float x, float y, Integer rotation, Long key, Integer isCircleView, Integer circleColor, String userText, Integer textColor, Integer textSize, Integer borderColor, Integer backgroundColor, Integer drawable, float width, float height, float left, float right, float top, float bottom, String imagePath, String type, LimbikaView limbikaView, Boolean active, java.util.Date createdAt, java.util.Date updatedAt, int itemFontTypeFace, String itemSound, int itemFontTypeAlign,String result,String openApp,Integer closeApp, Long navigateTo, int autoPlay, int soundDelay,int borderPixel) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.key = key;
        this.isCircleView = isCircleView;
        this.circleColor = circleColor;
        this.userText = userText;
        this.textColor = textColor;
        this.textSize = textSize;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.drawable = drawable;
        this.width = width;
        this.height = height;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.imagePath = imagePath;
        this.type = type;
        this.limbikaView = limbikaView;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fontTypeFace = itemFontTypeFace;
        this.itemSound = itemSound;
        this.fontAlign = itemFontTypeAlign;
         this.result=result;
        this.openApp = openApp;
        this.closeApp = closeApp;
        this.navigateTo = navigateTo;
        this.autoPlay = autoPlay;
        this.soundDelay = soundDelay;
        this.borderPixel = borderPixel;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Integer getIsCircleView() {
        return isCircleView;
    }

    public void setIsCircleView(Integer isCircleView) {
        this.isCircleView = isCircleView;
    }

    public Integer getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(Integer circleColor) {
        this.circleColor = circleColor;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(Integer textColor) {
        this.textColor = textColor;
    }

    public Integer getTextSize() {
        return textSize;
    }

    public void setTextSize(Integer textSize) {
        this.textSize = textSize;
    }

    public Integer getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Integer borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getDrawable() {
        return drawable;
    }

    public void setDrawable(Integer drawable) {
        this.drawable = drawable;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LimbikaView getLimbikaView() {
        return limbikaView;
    }

    public void setLimbikaView(LimbikaView limbikaView) {
        this.limbikaView = limbikaView;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getAllowDragDrop() {
        return allowDragDrop;
    }

    public void setAllowDragDrop(int allowDragDrop) {
        this.allowDragDrop = allowDragDrop;
    }

    public Long getDragDropTarget() {
        return dragDropTarget;
    }

    public void setDragDropTarget(Long dragDropTarget) {
        this.dragDropTarget = dragDropTarget;
    }

    public void setFontTypeFace(int fontTypeFace) {
        this.fontTypeFace = fontTypeFace;
    }

    public int getFontTypeFace() {
        return fontTypeFace;
    }
    public int getAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(int autoPlay) {
        this.autoPlay = autoPlay;
    }

    public int getSoundDelay() {
        return soundDelay;
    }

    public void setSoundDelay(int soundDelay) {
        this.soundDelay = soundDelay;
    }

    public void setBorderPixel(int borderPixel) {
        this.borderPixel = borderPixel;
    }
    public int getBorderPixel() {
        return borderPixel;
    }
}
