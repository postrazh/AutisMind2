package com.autismindd.utilities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

import com.autismindd.dao.User;


public class Animanation {

    public static final int noAnimation = 0;
    public static final int shake1 = 1;
    public static final int shake2 = 2;
    public static final int blink1 = 3;
    public static final int slideTopToBottom = 4;
    public static final int slideBottomToTop = 5;
    public static final int slideLeftToRight = 6;
    public static final int slideRightToLeft = 7;
    public static final int wiggleAnimation = 8;
    public static final String animationName[] = {"No Animation", "Alpha 1", "Alpha 2", "Blink 1", "slideTopToBottom", "slideBottomToTop", "slideLeftToRight", "slideRightToLeft", "wiggleAnimation"};

    public static void clear(View view) {
        view.setAnimation(null);
    }


    // zoomout animation method by sumon
    public static void zoomOut(final View v) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat("scaleX", .80f),
                PropertyValuesHolder.ofFloat("scaleY", .80f));
        scaleDown.setDuration(200);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.setRepeatCount(1);

        scaleDown.start();
    }

    // zoomout animation method by sumon
    public static void zoomIn2(final View v) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat("scaleX", 1.30f),
                PropertyValuesHolder.ofFloat("scaleY", 1.30f));
        scaleDown.setDuration(500);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.setRepeatCount(1);

        scaleDown.start();
    }

    public static void zoomIn(final View v) {

        ScaleAnimation scal = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(2000);
        scal.setFillAfter(true);
        v.setAnimation(scal);

    }


    public static void zoomInTransition(final View v) {

        ScaleAnimation scal = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(300);
        scal.setFillAfter(true);
        v.setAnimation(scal);

    }

    public static Animation getZoomIn() {

        ScaleAnimation scal = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(2000);
        scal.setFillAfter(true);
        return scal;

    }


    public static void zoomInZoomOut(View v) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat("scaleY", 1.2f),
                PropertyValuesHolder.ofFloat("scaleX", 1.2f));
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.setDuration(300);
        scaleDown.setRepeatCount(1);
        scaleDown.start();
    }

    public static ObjectAnimator zoomInZoomOutPositive(View v) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat("scaleY", .95f),
                PropertyValuesHolder.ofFloat("scaleX", .95f));
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setDuration(800);
        scaleDown.setRepeatCount(Animation.INFINITE);
        scaleDown.start();

        return scaleDown;
    }



    public static void wiggleAnimation(View v) {
        RotateAnimation rotate = new RotateAnimation(-30, 60, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatMode(2);
        //rotate.setRepeatCount(Animation.INFINITE);
        v.setAnimation(rotate);

    }

    public static void wiggleAnimationFeedBackAnimation(View v) {
        RotateAnimation rotate = new RotateAnimation(-30, 60, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatMode(2);
        rotate.setRepeatCount(Animation.INFINITE);
        v.setAnimation(rotate);

    }


    // shakeAnimation Animation
    public static void shakeAnimation(View v) {
        RotateAnimation rotate = new RotateAnimation(-30, 60, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(1);
        v.setAnimation(rotate);
    }

    // shakeAnimation2 Animation
    public static void shakeAnimation2(final View v) {
        RotateAnimation rotate = new RotateAnimation(-20, 20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(200);
//        rotate.setRepeatMode(2);
        rotate.setRepeatCount(1);
        v.setAnimation(rotate);
        rotate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                v.clearAnimation();
            }
        });
    }

    // Blink Animation
    public static void blink(final View view) {
        /*final AlphaAnimation toAlpha = new AlphaAnimation(1, 0);
        final AlphaAnimation fromAlpha = new AlphaAnimation(0, 1);
        toAlpha.setDuration(1000);
        toAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.startAnimation(fromAlpha);
            }
        });

        fromAlpha.setDuration(1000);
        fromAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(toAlpha);
            }
        });
*/
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
//        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);
        view.startAnimation(anim);
//        view.startAnimation(toAlpha);
    }

    // Blink Animation2
    public static void blink2(final View view) {
      /*  final AlphaAnimation toAlpha = new AlphaAnimation(1, 0);
        final AlphaAnimation fromAlpha = new AlphaAnimation(0, 1);
        toAlpha.setDuration(400);
        toAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(fromAlpha);
            }
        });

        fromAlpha.setDuration(400);
        fromAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(toAlpha);
            }
        });*/
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000); //You can manage the blinking time with this parameter
//        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);
        view.startAnimation(anim);
//        view.startAnimation(toAlpha);
    }

    /**
     * ANIMATION TO SLIDE up to DOWN
     **/
    public static void topToBottom(View v) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, -400.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }

    public static void bottomToTop(View v) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 400.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }


    public static void bottomToTopTransition(final View  v) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -400.0f);
        animation.setDuration(100); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * ANIMATION TO SLIDE down to DOWN only for eventSeq
     **/
    /*public static void slideDown_to_Down(final View v, int id) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 400.0f);
        animation.setDuration(1000);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.INVISIBLE);
                ImageButton iv = (ImageButton) v;
                iv.setImageResource(R.drawable.img_white_face_pressed);
                topToBottom(v);
            }
        });
        v.startAnimation(animation);
    }*/


    /**********************************************************
     * Feedback Dialog Animation
     *****************************************************************/
    // shakeAnimation Animation
    public static void shakeFeedBackAnimation(View v) {
        RotateAnimation rotate = new RotateAnimation(-30, 60, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(1);
        v.setAnimation(rotate);
    }

    // shakeAnimation2 Animation
    public static void shakeFeedBackAnimation2(final View v) {
        RotateAnimation rotate = new RotateAnimation(-20, 20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(200);
//        rotate.setRepeatMode(2);
        rotate.setRepeatCount(1);
        v.setAnimation(rotate);
        rotate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                v.clearAnimation();
            }
        });
    }

    // Blink Animation
    public static void blinkFeedBackAnimation(final View view) {
        final AlphaAnimation toAlpha = new AlphaAnimation(1, 0);
        final AlphaAnimation fromAlpha = new AlphaAnimation(0, 1);
        toAlpha.setDuration(1000);
        toAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.startAnimation(fromAlpha);
            }
        });

        fromAlpha.setDuration(1000);
        fromAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(toAlpha);
            }
        });

    /*    Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
//        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);
        view.startAnimation(anim);*/
        view.startAnimation(toAlpha);
    }

    // Blink Animation2
    public static void blink2FeedBackAnimation(final View view) {
        final AlphaAnimation toAlpha = new AlphaAnimation(1, 0);
        final AlphaAnimation fromAlpha = new AlphaAnimation(0, 1);
        toAlpha.setDuration(400);
        toAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(fromAlpha);
            }
        });

        fromAlpha.setDuration(400);
        fromAlpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(toAlpha);
            }
        });
      /*  Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
//        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);
        view.startAnimation(anim);*/
//        view.startAnimation(toAlpha);
    }

    /**
     * ANIMATION TO SLIDE up to DOWN
     **/
    public static void bottomToTopFeedBackAnimation(View v) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 700.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }


    public static void topToBottomFeedBackAnimation(View v) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, -700.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }


    public static void slideRightToLeft(View v) {
        TranslateAnimation animation = new TranslateAnimation(700.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }

    public static void slideRightToLeftFeedBackAnimation(View v) {
        TranslateAnimation animation = new TranslateAnimation(700.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }

    public static void slideLeftToRight(View v) {
        TranslateAnimation animation = new TranslateAnimation(-700.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }

    public static void slideLeftToRightFeedBackAnimation(View v) {
        TranslateAnimation animation = new TranslateAnimation(-700.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(1000); // animation duration
        //animation.setRepeatCount(1);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }


    public static void moveAnimation(View v) {
        TranslateAnimation animation = new TranslateAnimation(-50.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(1500); // animation duration
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(2);
        v.startAnimation(animation);
    }

    public static void rotationAnfZoomAnimation(final View v, final View container) {
        AnimationSet as = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 1440,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setDuration(3000);
        rotate.setFillAfter(true);
        rotate.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                zoomIn2(container);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //rotate.setRepeatMode(ScaleAnimation.REVERSE);
        //rotate.setRepeatCount(2);


        v.setAnimation(rotate);
    }

    // handling tutorial

    public static void tutorialWithOutReverse(View v,int posX,int posY) {
        float animationPosX=(float) posX;
        float animationPosY=(float) posY;
        TranslateAnimation animation = new TranslateAnimation(0.0f, animationPosX, 0.0f, animationPosY);
        animation.setDuration(1000); // animation duration
        animation.setRepeatCount(Animation.INFINITE);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }
    public static void tutorialWithReverse(View v,int posX,int posY) {
        float animationPosX=(float) posX;
        float animationPosY=(float) posY;
        TranslateAnimation animation = new TranslateAnimation(0.0f, animationPosX, 0.0f, animationPosY);
        animation.setDuration(1000); // animation duration
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(TranslateAnimation.REVERSE);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animation);
    }
    // wobbleAnimation by Rokan
    public static void wobbleAnimation(final View v) {
        RotateAnimation rotate = new RotateAnimation(-10, 10, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(400);
        rotate.setRepeatMode(2);
        rotate.setRepeatCount(1);
        v.setAnimation(rotate);
        rotate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                v.clearAnimation();
            }
        });
    }
    // avatar Animation Top to bottom when click avatar
    public static void avatarStartAnimation(final ImageButton v, final User user) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,0.0f ,-200.0f);
        animation.setDuration(1000);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                v.setImageResource(UserInfo.getAvatarDragLevel(user.getAvatar(), user.getStars()));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
          /*      v.setVisibility(View.INVISIBLE);
                ImageButton iv = (ImageButton) v;
                iv.setImageResource(R.drawable.img_white_face_pressed);
                Animanation.topToBottom(v);*/
//                 v.setImageResource(UserInfo.getAvatar(user.getAvatar(), user.getStars()));
                avatarTopToBottomAnimation(v,user);
            }
        });

        v.startAnimation(animation);
    }
    public static void avatarTopToBottomAnimation(final ImageButton v, final User user) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,-200.0f,0.0f );
        animation.setDuration(1000);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                v.setImageResource(UserInfo.getAvatarDragLevel(user.getAvatar(), user.getStars()));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
          /*      v.setVisibility(View.INVISIBLE);
                ImageButton iv = (ImageButton) v;
                iv.setImageResource(R.drawable.img_white_face_pressed);
                Animanation.topToBottom(v);*/
                v.setImageResource(UserInfo.getAvatar(user.getAvatar(), user.getStars()));
                v.clearAnimation();
            }
        });

        v.startAnimation(animation);
    }

    // avatar wise book click Animation Top to bottom when
    public static void avatarWiseBookStartAnimation(final ImageButton v, final User user) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,0.0f ,-200.0f);
        animation.setDuration(1000);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                v.setImageResource(UserInfo.getAvatarWiseBookImage(user.getAvatar()));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
          /*      v.setVisibility(View.INVISIBLE);
                ImageButton iv = (ImageButton) v;
                iv.setImageResource(R.drawable.img_white_face_pressed);
                Animanation.topToBottom(v);*/
//                 v.setImageResource(UserInfo.getAvatar(user.getAvatar(), user.getStars()));
                avatarWiseBookEndAnimation(v,user);
            }
        });

        v.startAnimation(animation);
    }
    public static void avatarWiseBookEndAnimation(final ImageButton v, final User user) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,-200.0f,0.0f );
        animation.setDuration(1000);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
//                v.setImageResource(UserInfo.getAvatarLevel(user.getAvatar(), user.getStars()));// if  give any image

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
          /*      v.setVisibility(View.INVISIBLE);
                ImageButton iv = (ImageButton) v;
                iv.setImageResource(R.drawable.img_white_face_pressed);
                Animanation.topToBottom(v);*/
                v.setImageResource(UserInfo.getAvatarWiseBookImage(user.getAvatar()));
                v.clearAnimation();
            }
        });

        v.startAnimation(animation);
    }




    public static void lockerRotateAnimation(View v) {
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setDuration(5000);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.RESTART);
        v.startAnimation(rotate);
    }

    public static void blinkAnim(View view) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);

        anim.setDuration(1000);
        anim.setFillAfter(true);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        view.startAnimation(anim);
    }


}
