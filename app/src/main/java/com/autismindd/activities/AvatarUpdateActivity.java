package com.autismindd.activities;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.customui.CustomAvatarView;
import com.autismindd.dao.User;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by RAFI on 10/26/2016.
 */

////

/**
 * TAG_UPDATE_ACTIVITY value 1 for any activity
 * TAG_UPDATE_ACTIVITY value 2 only for level update activity
 */


public class AvatarUpdateActivity extends BaseActivity {
    Handler h = new Handler();
    RelativeLayout rlAvaterUpdate, rlBackground;
    StaticInstance staticInstance;
    ImageView ivAvatarOne, ivAvatarTwo, ivAvatarThree, ivAvatarFour, ivAvatarFive;
    ImageView ivAvatarOneStar, ivAvatarTwoStar, ivAvatarThreeStar, ivAvatarFourStar, ivAvatarFiveStar;
    ImageView ivAvatarOneLocker, ivAvatarTwoLocker, ivAvatarThreeLocker, ivAvatarFourLocker, ivAvatarFiveLocker;
    TextView tvAvatarOne, tvAvatarTwo, tvAvatarThree, tvAvatarFour, tvAvatarFive;
    private ImageButton topCenterImageButton;

    CustomAvatarView customAvatarView;
    ImageProcessing imageProcessing;
    AvatarUpdateActivity activity;
    ArrayList<Integer> avatarList;

    UserInfo userInfo;
    User user;
    Long getActivityFlag;
    boolean musicControl = false;
    MediaPlayer mp;

    private String giftTapSound = "git_tap.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        activity = this;
        findViewById();
        getActivityFlag = getIntent().getExtras().getLong(StaticAccess.TAG_UPDATE_ACTIVITY, -1);
        rlBackground.setBackgroundColor(Color.parseColor(userInfo.avatarDarkColor(user.getAvatar())));
        customAvatarView = new CustomAvatarView(activity, imageProcessing.getImage(user.getPic()), user.getStars(), user.getName(), userInfo.avatarDarkColor(user.getAvatar()));
        rlAvaterUpdate.addView(customAvatarView);
        avatarList = UserInfo.getUserAvatarList(user.getAvatar());

        setImageResource(avatarList);
        if (getActivityFlag == StaticAccess.TAG_UPDATE_ACTIVITY_FLAG) {

        } else {
//            h.postDelayed(finishRunnable, 10000);
        }


    }

    private void findViewById() {

        ivAvatarOne = (ImageView) findViewById(R.id.ivAvatarOne);
        ivAvatarTwo = (ImageView) findViewById(R.id.ivAvatarTwo);
        ivAvatarThree = (ImageView) findViewById(R.id.ivAvatarThree);
        ivAvatarFour = (ImageView) findViewById(R.id.ivAvatarFour);
        ivAvatarFive = (ImageView) findViewById(R.id.ivAvatarFive);

        ivAvatarOneStar = (ImageView) findViewById(R.id.ivAvatarOneStar);
        ivAvatarTwoStar = (ImageView) findViewById(R.id.ivAvatarTwoStar);
        ivAvatarThreeStar = (ImageView) findViewById(R.id.ivAvatarThreeStar);
        ivAvatarFourStar = (ImageView) findViewById(R.id.ivAvatarFourStar);
        ivAvatarFiveStar = (ImageView) findViewById(R.id.ivAvatarFiveStar);

        ivAvatarOneLocker = (ImageView) findViewById(R.id.ivAvatarOneLocker);
        ivAvatarTwoLocker = (ImageView) findViewById(R.id.ivAvatarTwoLocker);
        ivAvatarThreeLocker = (ImageView) findViewById(R.id.ivAvatarThreeLocker);
        ivAvatarFourLocker = (ImageView) findViewById(R.id.ivAvatarFourLocker);
        ivAvatarFiveLocker = (ImageView) findViewById(R.id.ivAvatarFiveLocker);

        tvAvatarOne = (TextView) findViewById(R.id.tvAvatarOne);
        tvAvatarTwo = (TextView) findViewById(R.id.tvAvatarTwo);
        tvAvatarThree = (TextView) findViewById(R.id.tvAvatarThree);
        tvAvatarFour = (TextView) findViewById(R.id.tvAvatarFour);
        tvAvatarFive = (TextView) findViewById(R.id.tvAvatarFive);

        rlAvaterUpdate = (RelativeLayout) findViewById(R.id.rlAvaterUpdate);
        rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
        topCenterImageButton = (ImageButton) findViewById(R.id.topCenterImageButton);
        topCenterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //h.postDelayed(finishRunnable, 100);

                if (getActivityFlag == StaticAccess.TAG_UPDATE_ACTIVITY_FLAG) {
                    musicControl = true;
                    setResult(StaticAccess.TAG_AVATAR_UPDATE);
                    finish();
                } else {
//                    h.removeCallbacks(finishRunnable);
                    musicControl = true;
                    setResult(StaticAccess.TAG_AVATAR_UPDATE);
                    staticInstance.setLevelUp(false);
                    finish();
                }
            }
        });

        imageProcessing = new ImageProcessing(activity);
        staticInstance = StaticInstance.getInstance();
        userInfo = new UserInfo();
        user = staticInstance.getUser();
        avatarList = new ArrayList<>();
    }

    Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {

            if (getActivityFlag == StaticAccess.TAG_UPDATE_ACTIVITY_FLAG) {
                musicControl = true;
                finish();
            } else {
                musicControl = true;
                setResult(StaticAccess.TAG_AVATAR_UPDATE);
                staticInstance.setLevelUp(false);
                finish();
            }

        }
    };

    public void setImageResource(final ArrayList<Integer> imageResource) {
        ivAvatarOne.setImageResource(imageResource.get(0));
        ivAvatarTwo.setImageResource(imageResource.get(1));
        ivAvatarThree.setImageResource(imageResource.get(2));
        ivAvatarFour.setImageResource(imageResource.get(3));
        ivAvatarFive.setImageResource(imageResource.get(4));
/*        if(user.getStars()>=StaticAccess.TAG_AVATAR_UPDATE_ONE_VALUE && user.getStars()<StaticAccess.TAG_AVATAR_UPDATE_TWO_VALUE){
            ivAvatarOneLocker.setVisibility(View.INVISIBLE);
            Animanation.zoomIn(ivAvatarOne);
        }else*/
        if (getActivityFlag == StaticAccess.TAG_UPDATE_ACTIVITY_FLAG) {
            if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_ONE_VALUE && user.getStars() < StaticAccess.TAG_AVATAR_UPDATE_TWO_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwo.setVisibility(View.INVISIBLE);
//                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThree.setVisibility(View.INVISIBLE);
                ivAvatarFour.setVisibility(View.INVISIBLE);
                ivAvatarFive.setVisibility(View.INVISIBLE);
//                Animanation.zoomIn(ivAvatarOne);
            } else if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_TWO_VALUE && user.getStars() < StaticAccess.TAG_AVATAR_UPDATE_THREE_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThree.setVisibility(View.INVISIBLE);
//                ivAvatarThreeLocker.setVisibility(View.INVISIBLE);
                ivAvatarFour.setVisibility(View.INVISIBLE);
                ivAvatarFive.setVisibility(View.INVISIBLE);
//                Animanation.zoomIn(ivAvatarTwo);

            } else if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_THREE_VALUE && user.getStars() < StaticAccess.TAG_AVATAR_UPDATE_FOUR_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThreeLocker.setVisibility(View.INVISIBLE);
                ivAvatarFour.setVisibility(View.INVISIBLE);
//                ivAvatarFourLocker.setVisibility(View.INVISIBLE);
                ivAvatarFive.setVisibility(View.INVISIBLE);
//                Animanation.zoomIn(ivAvatarThree);
            } else if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_FOUR_VALUE && user.getStars() < StaticAccess.TAG_AVATAR_UPDATE_FIVE_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThreeLocker.setVisibility(View.INVISIBLE);
                ivAvatarFourLocker.setVisibility(View.INVISIBLE);
                ivAvatarFive.setVisibility(View.INVISIBLE);
//                ivAvatarFiveLocker.setVisibility(View.INVISIBLE);
//                Animanation.zoomIn(ivAvatarFour);
            } else if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_FIVE_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThreeLocker.setVisibility(View.INVISIBLE);
                ivAvatarFourLocker.setVisibility(View.INVISIBLE);
                ivAvatarFiveLocker.setVisibility(View.INVISIBLE);
//                Animanation.zoomIn(ivAvatarFive);
            }
        } else {
            if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_TWO_VALUE && user.getStars() < StaticAccess.TAG_AVATAR_UPDATE_THREE_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);

                ivAvatarTwo.setImageResource(userInfo.getGiftBox(user.getAvatar()));
                ivAvatarTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSoundAsset(giftTapSound, StaticAccess.TAG_SOUND_AVATAR_LEVEL_TWO);
                        ivAvatarTwo.setImageResource(imageResource.get(1));
                        v.startAnimation(Animanation.getZoomIn());
                        ivAvatarTwo.setClickable(false);
                    }
                });

                ivAvatarThree.setVisibility(View.INVISIBLE);
                ivAvatarFour.setVisibility(View.INVISIBLE);
                ivAvatarFive.setVisibility(View.INVISIBLE);
                Animanation.zoomIn(ivAvatarTwo);

            } else if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_THREE_VALUE && user.getStars() < StaticAccess.TAG_AVATAR_UPDATE_FOUR_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThreeLocker.setVisibility(View.INVISIBLE);

                ivAvatarThree.setImageResource(userInfo.getGiftBox(user.getAvatar()));
                ivAvatarThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSoundAsset(giftTapSound, StaticAccess.TAG_SOUND_AVATAR_LEVEL_THREE);
                        ivAvatarThree.setImageResource(imageResource.get(2));
                        v.startAnimation(Animanation.getZoomIn());
                        ivAvatarThree.setClickable(false);
                    }
                });

                ivAvatarFour.setVisibility(View.INVISIBLE);
                ivAvatarFive.setVisibility(View.INVISIBLE);
                Animanation.zoomIn(ivAvatarThree);
            } else if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_FOUR_VALUE && user.getStars() < StaticAccess.TAG_AVATAR_UPDATE_FIVE_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThreeLocker.setVisibility(View.INVISIBLE);
                ivAvatarFourLocker.setVisibility(View.INVISIBLE);

                ivAvatarFour.setImageResource(userInfo.getGiftBox(user.getAvatar()));
                ivAvatarFour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSoundAsset(giftTapSound, StaticAccess.TAG_SOUND_AVATAR_LEVEL_FOUR);
                        ivAvatarFour.setImageResource(imageResource.get(3));
                        v.startAnimation(Animanation.getZoomIn());
                        ivAvatarFour.setClickable(false);
                    }
                });

                ivAvatarFive.setVisibility(View.INVISIBLE);
//                ivAvatarFiveLocker.setVisibility(View.INVISIBLE);
                ivAvatarFiveLocker.setVisibility(View.VISIBLE);
                Animanation.zoomIn(ivAvatarFour);
            } else if (user.getStars() >= StaticAccess.TAG_AVATAR_UPDATE_FIVE_VALUE) {
                ivAvatarOneLocker.setVisibility(View.INVISIBLE);
                ivAvatarTwoLocker.setVisibility(View.INVISIBLE);
                ivAvatarThreeLocker.setVisibility(View.INVISIBLE);
                ivAvatarFourLocker.setVisibility(View.INVISIBLE);
                ivAvatarFiveLocker.setVisibility(View.INVISIBLE);

                ivAvatarFive.setImageResource(userInfo.getGiftBox(user.getAvatar()));
                ivAvatarFive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSoundAsset(giftTapSound, StaticAccess.TAG_SOUND_AVATAR_LEVEL_FIVE);
                        ivAvatarFive.setImageResource(imageResource.get(4));
                        v.startAnimation(Animanation.getZoomIn());
                        ivAvatarFive.setClickable(false);
                    }
                });
                Animanation.zoomIn(ivAvatarFive);
            }
        }
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
    }

    // Sound Play for Starting
    private void playSoundAsset(String song, final int flag) {
        mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);

            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mpSoundRelease();
                    // if any query after sound prize click
                    if (flag == StaticAccess.TAG_SOUND_AVATAR_LEVEL_ONE) {

                    } else if (flag == StaticAccess.TAG_SOUND_AVATAR_LEVEL_TWO) {

                    } else if (flag == StaticAccess.TAG_SOUND_AVATAR_LEVEL_THREE) {

                    } else if (flag == StaticAccess.TAG_SOUND_AVATAR_LEVEL_FOUR) {

                    } else if (flag == StaticAccess.TAG_SOUND_AVATAR_LEVEL_FIVE) {

                    }

                }
            });

            mp.prepare();
            mp.start();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sound release created by reaz
    public void mpSoundRelease() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user.getMusic() > 0) {
            BackgroundMusicService.startMusic(activity);
        }

    }
}
