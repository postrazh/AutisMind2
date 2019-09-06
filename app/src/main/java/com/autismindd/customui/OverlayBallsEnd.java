package com.autismindd.customui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.MotionEvent;


import com.autismindd.activities.TransparentActivity;
import com.autismindd.R;

import java.util.ArrayList;
import java.util.Random;


public class OverlayBallsEnd extends OverlayEnd {

    MediaPlayer mediaPlayer;

    TransparentActivity activity;
    private static final int NUM_BALLS = 16;


    //// smilies int res ids array
    private static int[] SMILIES = new int[]{
            R.drawable.imag_smily_blue,
            R.drawable.imag_smily_green,
            R.drawable.imag_smily_orange,
            R.drawable.imag_smily_pink,
            R.drawable.imag_smily_purpple,
            R.drawable.imag_smily_red,
            R.drawable.imag_smily_turquesa,
            R.drawable.imag_smily_yellow
    };

    //// animal int res ids array
    private static int[] ANIMALS = new int[]{
            R.drawable.imag_animal_blue,
            R.drawable.imag_animal_green,
            R.drawable.imag_animal_orange,
            R.drawable.imag_animal_pink,
            R.drawable.imag_animal_purpple,
            R.drawable.imag_animal_red,
            R.drawable.imag_animal_turquesa,
            R.drawable.imag_animal_yellow
    };


    //// bubbles int res ids array
    private static int[] BUBBLES = new int[]{
            R.drawable.img_bubbler_blue,
            R.drawable.img_bubbler_green,
            R.drawable.img_bubbler_orange,
            R.drawable.img_bubbler_pink,
            R.drawable.img_bubbler_purpple,
            R.drawable.img_bubbler_red,
            R.drawable.img_bubbler_turquesa,
            R.drawable.img_bubbler_yellow
    };
    //// numbers int res ids array
    private static int[] NUMBERS = new int[]{
            R.drawable.img_rubber_blue,
            R.drawable.img_rubber_green,
            R.drawable.img_rubber_orange,
            R.drawable.img_rubber_pink,
            R.drawable.img_rubber_purpple,
            R.drawable.img_rubber_red,
            R.drawable.img_rubber_turquesa,
            R.drawable.img_rubber_yellow
    };

    //// clouds int res ids array
    private static int[] BALLS = new int[]{
            R.drawable.img_token_blue_face,
            R.drawable.img_token_green_face,
            R.drawable.img_token_grey_face,
            R.drawable.img_token_orange_face,
            R.drawable.img_token_pink_face,
            R.drawable.img_token_purple_face,
            R.drawable.img_token_red_face,
            R.drawable.img_token_yello_face
    };

    ////2 clouds int res ids array
    private static int[] CLOUD = new int[]{
            R.drawable.img_token_blue_face,
            R.drawable.img_token_green_face,
            R.drawable.img_token_grey_face,
            R.drawable.img_token_orange_face,
            R.drawable.img_token_pink_face,
            R.drawable.img_token_purple_face,
            R.drawable.img_token_red_face,
            R.drawable.img_token_yello_face
    };


    //// shape int res ids array
    private static int[] SHAPE = new int[]{
            R.drawable.img_shape_blue,
            R.drawable.img_shape_green,
            R.drawable.img_shape_orange,
            R.drawable.img_shape_pink,
            R.drawable.img_shape_purpple,
            R.drawable.img_shape_red,
            R.drawable.img_shape_turquesa,
            R.drawable.img_shape_yellow
    };


    private Random mRandom = new Random();
    private ArrayList<Ball> mBalls = new ArrayList<Ball>();

    public OverlayBallsEnd(Context context, int level) {
        super(context);
        activity = (TransparentActivity) context;
        switch (level) {
            case 0:
                BALLS = SMILIES;
                break;
            case 1:
                BALLS = ANIMALS;
                break;
            case 2:
                BALLS = BUBBLES;
                break;
            case 3:
                BALLS = NUMBERS;
                break;
            case 4:
                BALLS = CLOUD;
                break;
            case 5:
                BALLS = SHAPE;
                break;

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Update
        update();

        // Draw
        for (Ball ball : mBalls) ball.draw(canvas);

        // Invalidate
        invalidate();
    }

    protected void update() {
        // Remove finished
        for (int i = 0; i < mBalls.size(); i++) {
            Ball ball = mBalls.get(i);
            if (ball.isFinished(getWidth())) {
                ball.recycle();
                mBalls.remove(i);
            }
        }

        // Update
        for (Ball ball : mBalls) ball.update();

        // Add new
        if (!isFinish() && mBalls.size() < NUM_BALLS) {
            Ball ball = new Ball();
            ball.init(mRandom);
            ball.bitmap = BitmapFactory.decodeResource(getResources(), BALLS[mRandom.nextInt(BALLS.length)]);
            ball.offsetX = getWidth() / 2;
            ball.offsetY = getHeight();

            mBalls.add(ball);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (Ball ball : mBalls) ball.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            int index = -1;
            int count = mBalls.size();
            for (int i = count - 1; i >= 0; i--) {
                Ball ball = mBalls.get(i);
                RectF rect = ball.getRectF();
                if (rect.contains(x, y)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.sd_faces_pop);
                mediaPlayer.start();
                mBalls.remove(index);
                return true;
            } else {
                activity.finishActivity();
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Gumball.
     */
    static class Ball {

        static final int MAX_K = 128;
        static final int MIN_K = 32;

        static final int MAX_SPEED = 8;
        static final int MIN_SPEED = 1;

        Bitmap bitmap;
        int speed;
        int direction;
        int x;
        int y;
        int k;
        int offsetX;
        int offsetY;

        boolean isFinished(int width) {
            return x + offsetX < -1 * bitmap.getWidth() || x + offsetX > width;
        }

        void init(Random random) {
            x = 0;
            y = 0;
            k = random.nextInt(MAX_K - MIN_K) + MIN_K;
            direction = random.nextBoolean() ? 1 : -1;
            speed = random.nextInt(MAX_SPEED - MIN_SPEED) + MIN_SPEED;
        }

        void draw(Canvas canvas) {
            int x = this.offsetX + this.x;
            int y = this.offsetY - this.y;

            canvas.drawBitmap(bitmap, x, y, null);
        }

        void update() {
            x = x + direction * speed;
            y = (int) (k * Math.log((double) Math.abs(x) + 1));
        }

        void recycle() {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }

        RectF getRectF() {
            int x = this.offsetX + this.x;
            int y = this.offsetY - this.y;
            return new RectF(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
        }
    }

}
