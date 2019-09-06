package com.autismindd.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.autismindd.Dialog.ImageSearchCustomDialog;
import com.autismindd.Dialog.ImageSelectionDialog;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.User;
import com.autismindd.logging.L;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.FileProcessing;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.utilities.InternalStorageContentProvider;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by RAFI on 9/26/2016.
 */

public class UserCreateActivity extends BaseActivity implements View.OnClickListener {
    ImageView imgProPic;
    EditText etUserName;
    MediaPlayer mp;
    ImageButton ibtnSubmit, ibtnCancel, ibtnUserCreateLamp;
    RelativeLayout rlUserCreateLayerLamp;
    UserCreateActivity activity;
    ImageSearchCustomDialog imageSearchDialog;
    ImageProcessing imgProc;
    private String appImagePath = null;
    public boolean isImageSearchDialogShow = false;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public File mFileTemp;
    private static final int SELECT_PICTURE = 0x1;
    private static final int MATERIAL_FILE_PICKER = 0x3;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x8;
    boolean isLamp = false;
    String lampSound = "sound_click_lamp.mp3";
    int selectedMode;
    User currentUser;
    StaticInstance staticInstance;
    int itemPicFlag = 0;
    int intent_source = 0;
    public ImageSelectionDialog imageSelectionDialog;
    DatabaseManager databaseManager;
    FileProcessing fileProcessing;
    String filePath, imgPath = "";
    User user;

    RadioGroup rdGrpVoiceHelp, rdGrpMusic, rdGrpPlayProgress, rdGrpSoundEffects;
    RadioButton rbtnVoiceHelpOff, rbtnVoiceHelpOn, rbtnMusicOff, rbtnMusicOn, rbtnPlayProgressOff, rbtnPlayProgressOn;
    RadioButton rbtnSoundEffectsOff, rbtnSoundEffectsOn;
    RadioGroup rdGrpAvatar;
    RadioButton rdbtnGirl, rdbtnCar, rdbtnDinosaur, rdbtnHorse, rdbtnRoket;

    int help = 1;
    int closeApp = 1;
    int taskPlayProgress = 1;
    int superError = 1;

    int avatar = 4;
    Long selectedUser;

    RelativeLayout wholeLayout_usercreate;
    boolean musicControl = false;

    User tempUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_create);
        activity = this;
        findViewById();
        isLamp = SharedPreferenceValue.getLampFlag(activity);

        selectedMode = getIntent().getIntExtra("mode", 0);
        selectedUser = getIntent().getLongExtra("CurrentUser", 0);
        currentUser = databaseManager.getUserById(selectedUser);
        staticInstance = StaticInstance.getInstance();

        if (currentUser != null) {
            getDataForEdit();
        }


        hideKeyboard();
        screenMode(isLamp);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
    }

    private void findViewById() {
        imgProPic = (ImageView) findViewById(R.id.imgProPicID);
        ibtnCancel = (ImageButton) findViewById(R.id.ibtnCancel);
        ibtnSubmit = (ImageButton) findViewById(R.id.ibtnSubmit);
        ibtnUserCreateLamp = (ImageButton) findViewById(R.id.ibtnUserCreateLamp);
        etUserName = (EditText) findViewById(R.id.etUserNameID);

        rlUserCreateLayerLamp = (RelativeLayout) findViewById(R.id.rlUserCreateLayerLamp);
        rdGrpVoiceHelp = (RadioGroup) findViewById(R.id.rdGrpVoiceHelp);
        rdGrpMusic = (RadioGroup) findViewById(R.id.rdGrpMusic);
        rdGrpPlayProgress = (RadioGroup) findViewById(R.id.rdGrpPlayProgress);
        rdGrpSoundEffects = (RadioGroup) findViewById(R.id.rdGrpSoundEffects);
        rdGrpAvatar = (RadioGroup) findViewById(R.id.rdGrpAvatar);

        rbtnVoiceHelpOff = (RadioButton) findViewById(R.id.rbtnVoiceHelpOff);
        rbtnVoiceHelpOn = (RadioButton) findViewById(R.id.rbtnVoiceHelpOn);
        rbtnMusicOff = (RadioButton) findViewById(R.id.rbtnMusicOff);
        rbtnMusicOn = (RadioButton) findViewById(R.id.rbtnMusicOn);
        rbtnPlayProgressOff = (RadioButton) findViewById(R.id.rbtnPlayProgressOff);
        rbtnPlayProgressOn = (RadioButton) findViewById(R.id.rbtnPlayProgressOn);
        rbtnSoundEffectsOff = (RadioButton) findViewById(R.id.rbtnSoundEffectsOff);
        rbtnSoundEffectsOn = (RadioButton) findViewById(R.id.rbtnSoundEffectsOn);

        rdbtnGirl = (RadioButton) findViewById(R.id.rdbtnGirl);
        rdbtnCar = (RadioButton) findViewById(R.id.rdbtnCar);
        rdbtnDinosaur = (RadioButton) findViewById(R.id.rdbtnDinosaur);
        rdbtnHorse = (RadioButton) findViewById(R.id.rdbtnHorse);
        rdbtnRoket = (RadioButton) findViewById(R.id.rdbtnRoket);

        rbtnVoiceHelpOff.setOnClickListener(this);
        rbtnVoiceHelpOn.setOnClickListener(this);
        rbtnMusicOff.setOnClickListener(this);
        rbtnMusicOn.setOnClickListener(this);
        rbtnPlayProgressOff.setOnClickListener(this);
        rbtnPlayProgressOn.setOnClickListener(this);
        rbtnSoundEffectsOff.setOnClickListener(this);
        rbtnSoundEffectsOn.setOnClickListener(this);

        rdbtnGirl.setOnClickListener(this);
        rdbtnCar.setOnClickListener(this);
        rdbtnDinosaur.setOnClickListener(this);
        rdbtnHorse.setOnClickListener(this);
        rdbtnRoket.setOnClickListener(this);

        ibtnSubmit.setOnClickListener(this);
        ibtnCancel.setOnClickListener(this);
        ibtnUserCreateLamp.setOnClickListener(this);

        databaseManager = new DatabaseManager(activity);
        user = new User();
        imgProc = new ImageProcessing(activity);
        appImagePath = imgProc.getImageDir();

        imgProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                imageSelectionDialog = new ImageSelectionDialog(activity, activity, itemPicFlag);
                DialogNavBarHide.navBarHide(activity, imageSelectionDialog);
//                isCircularImage = false;
            }
        });

        wholeLayout_usercreate = (RelativeLayout) findViewById(R.id.wholeLayout_usercreate);
        wholeLayout_usercreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        ImageView ic_man_usr = (ImageView) findViewById(R.id.ic_man_usr);
        ic_man_usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        ic_man_usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
/*
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
*/


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rbtnVoiceHelpOff:
                help = StaticAccess.HELP_OFF;
                break;

            case R.id.rbtnVoiceHelpOn:
                help = StaticAccess.HELP_ON;
                break;
            case R.id.rbtnMusicOff:
                closeApp = StaticAccess.CLOSE_APP_OFF;
                break;

            case R.id.rbtnMusicOn:
                closeApp = StaticAccess.CLOSE_APP_ON;

                if (rbtnMusicOn.isChecked()) {
                    BackgroundMusicService.startMusic(this);
                    StaticAccess.musicOnOff = 1;
                } else {
                    BackgroundMusicService.stopMusic(this);
                    StaticAccess.musicOnOff = 0;
                }

                break;

            case R.id.rbtnPlayProgressOff:
                taskPlayProgress = StaticAccess.PLAYER_PROGRESS_OFF;
                break;

            case R.id.rbtnPlayProgressOn:
                taskPlayProgress = StaticAccess.PLAYER_PROGRESS_ON;
                break;
            case R.id.rbtnSoundEffectsOff:
                superError = StaticAccess.SUPER_ERROR_OFF;
                break;

            case R.id.rbtnSoundEffectsOn:
                superError = StaticAccess.SUPER_ERROR_ON;
                break;


            case R.id.rdbtnGirl:
                avatar = StaticAccess.AVATAR_GIRL;

                break;
            case R.id.rdbtnCar:
                avatar = StaticAccess.AVATAR_CAR;

                break;
            case R.id.rdbtnDinosaur:
                avatar = StaticAccess.AVATAR_DINOSAUR;

                break;
            case R.id.rdbtnHorse:
                avatar = StaticAccess.AVATAR_HORSE;

                break;
            case R.id.rdbtnRoket:
                avatar = StaticAccess.AVATAR_ROCKET;

                break;
            case R.id.ibtnCancel:
                musicControl = true;
                Intent intent = new Intent(activity, UserListActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.ibtnUserCreateLamp:
                screenMode(isLamp);
                playSound(lampSound);
                break;

            case R.id.ibtnSubmit:
                musicControl = true;
                if (selectedMode == StaticAccess.USER_CREATE) {
                    if (etUserName.getText().length() > 0) {
                        //user = new User();
                        user.setName(etUserName.getText().toString());
                        user.setPassword("asdwed");
                        user.setAvatar(avatar);
                        user.setKey(System.currentTimeMillis());
                        user.setPic(checkGettingImage(imgPath, avatar));
                        user.setUserState(false);
                        user.setFirstLayerTaskID(0);
                        user.setLastLevelID(0);
                        user.setPoint(0);
//                        user.setStars(0);
                        user.setHelper(help);
                        user.setMusic(closeApp);
                        user.setTaskPlayProgress(taskPlayProgress);
                        user.setSoundEffect(superError);
                        user.setCreatedAt(new Date());
                        user.setUpdatedAt(new Date());
                        user.setActive(true);
                        tempUser = databaseManager.insertUser(user);
                        if (tempUser != null)
                            staticInstance.setUser(tempUser);
                        Intent intUserList = new Intent(activity, FirstLayerActivity.class);
                        intUserList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intUserList);
                        finish();
                    } else {
                        CustomToast.t(activity, getResources().getString(R.string.AddName));
                    }

                } else if (selectedMode == StaticAccess.USER_EDIT) {

                    if (etUserName.getText().length() > 0) {
                        //user = new User();
                        user.setId((currentUser.getId()));
                        user.setName(etUserName.getText().toString());
                        user.setPassword("asdwed");
                        user.setAvatar(avatar);
                        user.setKey((currentUser.getKey()));
                        user.setPic(checkGettingImage(imgPath, avatar));
                        user.setUserState(false);
                        user.setFirstLayerTaskID(currentUser.getFirstLayerTaskID());
                        user.setLastLevelID(currentUser.getLastLevelID());
                        user.setPoint(currentUser.getPoint());
                        user.setStars(currentUser.getStars());
                        user.setHelper(help);
                        user.setMusic(closeApp);
                        user.setTaskPlayProgress(taskPlayProgress);
                        user.setSoundEffect(superError);
                        user.setCreatedAt(new Date());
                        user.setUpdatedAt(new Date());
                        user.setActive(true);
                        tempUser = databaseManager.updateUsers(user);
                        if (tempUser != null)
                            staticInstance.setUser(tempUser);
                        Intent intUserList = new Intent(activity, FirstLayerActivity.class);
                        intUserList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intUserList);
                        finish();
                    } else {
                        CustomToast.t(activity, getResources().getString(R.string.AddName));
                    }


                }

                break;


        }

    }

    String checkGettingImage(String imgPath, int avatar) {
        String imgFilePath;
        if (imgPath.length() > 0) {
            imgFilePath = imgPath;
        } else {

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), new UserInfo().getAvatarCreateUser(avatar));
//            imgProc.imageSave(bitmap);
            imgFilePath = imgProc.imageSave(bitmap);
        }
        return imgFilePath;
    }

    // load image from Internet by Rokan
    public void loadImageInternet(int internetFlag) {

        if (isImageSearchDialogShow == false) {
            imageSearchDialog = new ImageSearchCustomDialog(activity, activity, internetFlag);
            DialogNavBarHide.navBarHide(this, imageSearchDialog);
            isImageSearchDialogShow = true;
        }

    }

    // Opening Image Cropper (Transparent)
    public void openCropper(Uri uri) {

        com.theartofdev.edmodo.cropper.CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .start(activity);
    }

    // load image from camera by Rokan
    public void loadImageCamera() {
        // flag for using item or task
        File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + appImagePath);
        if (!sdCardDirectory.exists()) {
            sdCardDirectory.mkdirs();
        }
        String state1 = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state1)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory() + appImagePath, TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir() + appImagePath, TEMP_PHOTO_FILE_NAME);
        }
        musicControl = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state2 = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state2)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

        } catch (ActivityNotFoundException e) {
        }

        intent_source = 2;
    }

    // load image from Gallery by Rokan
    public void loadImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);


        intent_source = 1;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        musicControl = false;
        fileProcessing = new FileProcessing(activity);
        Bitmap widgetImage = null;
        if (resultCode == RESULT_OK) {

            // Load Image from Gallery
            if (requestCode == SELECT_PICTURE && intent_source == 1) {
                openCropper(data.getData());
            }

            // load image from Camera
            if (requestCode == REQUEST_CODE_TAKE_PICTURE && intent_source == 2) {
                openCropper(Uri.fromFile(new File(mFileTemp.getAbsolutePath())));

            }


            // Load image after Cropping (Transparent)
            if (requestCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                com.theartofdev.edmodo.cropper.CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                setImagePro(bitmap);


            } else if (resultCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                L.t(activity, "Crop Error");
            }


            // Getting file with Material File picker
            if (requestCode == MATERIAL_FILE_PICKER) {
                filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                int intFileSize = fileProcessing.fileSize(filePath);

                if (intFileSize <= StaticAccess.TAG_SOUND_FILE_SIZE) {

                } else {
                    CustomToast.t(activity, getResources().getString(R.string.notSupported));
                }

            }

        }


        setImagePro(widgetImage);

        intent_source = 0;

    }


    public void setImagePro(Bitmap bitmap) {
        //scale bitmap
        if (bitmap != null) {
            Bitmap b = (bitmap);
            imgPath = imgProc.imageSave(b);
            imgProc.setImageWith_loaderRound(imgProPic, imgPath);
            b.recycle();
        }

    }

    // Scaling bitmap by Mir
    private Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int builtw = width;
        int builth = height;

        int maxWidth = 512;
        int maxHeight = 512;

        if (width > 1500) {
            maxWidth = 650;
            maxHeight = 650;
        } else if (width > 2000) {
            maxWidth = 750;
            maxHeight = 750;
        }
        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        //Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public void getDataForEdit() {

        imgPath = currentUser.getPic();
        imgProc.setImageWith_loaderRound(imgProPic, imgPath);

        // imgProc.setImageWith_loaderRound(imgProPic, currentUser.getPic());

        etUserName.setText(currentUser.getName());

        if (currentUser.getHelper() == StaticAccess.HELP_OFF) {
            rbtnVoiceHelpOff.setChecked(true);
            help = 0;
        } else {
            rbtnVoiceHelpOn.setChecked(true);
            help = 1;
        }

        if (currentUser.getMusic() == StaticAccess.CLOSE_APP_OFF) {
            rbtnMusicOff.setChecked(true);
            closeApp = 0;
        } else {
            closeApp = 1;
            rbtnMusicOn.setChecked(true);
        }

        if (currentUser.getTaskPlayProgress() == StaticAccess.PLAYER_PROGRESS_OFF) {
            rbtnPlayProgressOff.setChecked(true);
            taskPlayProgress = 0;
        } else {
            rbtnPlayProgressOn.setChecked(true);
            taskPlayProgress = 1;
        }

        if (currentUser.getSoundEffect() == StaticAccess.SUPER_ERROR_OFF) {
            rbtnSoundEffectsOff.setChecked(true);
            superError = 0;
        } else {
            rbtnSoundEffectsOn.setChecked(true);
            superError = 1;
        }

        if (currentUser.getAvatar() == StaticAccess.AVATAR_GIRL) {
            rdbtnGirl.setChecked(true);
            avatar = StaticAccess.AVATAR_GIRL;
        } else if (currentUser.getAvatar() == StaticAccess.AVATAR_CAR) {
            rdbtnCar.setChecked(true);
            avatar = StaticAccess.AVATAR_CAR;
        } else if (currentUser.getAvatar() == StaticAccess.AVATAR_DINOSAUR) {
            rdbtnDinosaur.setChecked(true);
            avatar = StaticAccess.AVATAR_DINOSAUR;
        } else if (currentUser.getAvatar() == StaticAccess.AVATAR_HORSE) {
            rdbtnHorse.setChecked(true);
            avatar = StaticAccess.AVATAR_HORSE;
        } else if (currentUser.getAvatar() == StaticAccess.AVATAR_ROCKET) {
            rdbtnRoket.setChecked(true);
            avatar = StaticAccess.AVATAR_ROCKET;
        }

    }


    private void showPictureialog() {
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);
        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.dialog_back_permission);
        dialog.setCancelable(true);

        dialog.show();
    }

    public void screenMode(boolean isLampTem) {
        if (isLampTem) {
            rlUserCreateLayerLamp.setVisibility(View.VISIBLE);
            ibtnUserCreateLamp.setImageResource(R.drawable.ic_lamp_dream);
            isLamp = false;
        } else {
            rlUserCreateLayerLamp.setVisibility(View.GONE);
            ibtnUserCreateLamp.setImageResource(R.drawable.ic_lamp);
            isLamp = true;
        }
        SharedPreferenceValue.setLampFlag(activity, isLampTem);
    }

    // play Sound for lamp click
    private void playSound(String song) {
        mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BackgroundMusicService.startMusic(activity);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(activity);
    }

}
