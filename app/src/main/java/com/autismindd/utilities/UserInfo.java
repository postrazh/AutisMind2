package com.autismindd.utilities;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.autismindd.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by RAFI on 10/10/2016.
 */

public class UserInfo {

/*
*
* avatar 0 for girl
* avatar 1 for car
* avatar 2 for Dinosaur
* avatar 3 for Unicorn/horse
* avatar 4 for Rocket
*  1-15-45-75-120 prev
*  1-15- 50-100- 175 prev
*  1-15- 50-100- 160
*
* */


    // for count star
    public static int[] returnVal(int count) {
        int arr[] = new int[3];
        if (count == 0) {
            arr[0] = 0;
            arr[1] = 0;
            arr[2] = 0;
        } else if (count == 1) {
            arr[0] = 1;
            arr[1] = 0;
            arr[2] = 0;

        } else if (count == 2) {
            arr[0] = 1;
            arr[1] = 1;
            arr[2] = 0;

        } else if (count == 3) {
            arr[0] = 1;
            arr[1] = 1;
            arr[2] = 1;

        }
        return arr;
    }

    // avatar Wise Dark Color Change
    public String avatarDarkColor(int avatarValue) {
        String avatarColor = "";

        switch (avatarValue) {

            case StaticAccess.AVATAR_GIRL:
                avatarColor = StaticAccess.AVATAR_GIRL_TEXT_COLOR;
                break;
            case StaticAccess.AVATAR_CAR:
                avatarColor = StaticAccess.AVATAR_CAR_TEXT_COLOR;
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                avatarColor = StaticAccess.AVATAR_DINOSAUR_TEXT_COLOR;
                break;

            case StaticAccess.AVATAR_HORSE:
                avatarColor = StaticAccess.AVATAR_HORSE_TEXT_COLOR;
                break;
            case StaticAccess.AVATAR_ROCKET:
                avatarColor = StaticAccess.AVATAR_ROKET_TEXT_COLOR;
                break;
        }
        return avatarColor;
    }


    // avatar Wise lite Color Change
    public String avatarLiteColor(int avatarValue) {
        String avatarColor = "";

        switch (avatarValue) {

            case StaticAccess.AVATAR_GIRL:
                avatarColor = StaticAccess.AVATAR_GIRL_COLOR;
                break;
            case StaticAccess.AVATAR_CAR:
                avatarColor = StaticAccess.AVATAR_CAR_COLOR;
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                avatarColor = StaticAccess.AVATAR_DINOSAUR_COLOR;
                break;
            case StaticAccess.AVATAR_HORSE:
                avatarColor = StaticAccess.AVATAR_HORSE_COLOR;
                break;
            case StaticAccess.AVATAR_ROCKET:
                avatarColor = StaticAccess.AVATAR_ROKET_COLOR;
                break;
        }
        return avatarColor;
    }

    // count Star for each level depend on result
 /*   public int getStar(int result) {
        int star = 0;
        if (result <= 49) {
            star = 0;
        } else if (result >= 1 && result <= 70) {
            star = 1;
        } else if (result > 70 && result <= 99) {
            star = 2;
        } else if (result == 100) {
            star = 3;
        }
        return star;
    }*/
    public int getStar(int result) {
       /* A) 0-49 % correct IS 0 star
        B) 50-74% correct IS 1 star
        C) 75-99% correct IS 2 stars
        D) 100% correct IS 3 stars*/
        int star = 0;
        if (result >= 0 && result <= 49) {
            star = 0;
        } else if (result > 49 && result <= 74) {
            star = 1;
        } else if (result > 74 && result <= 99) {
            star = 2;
        } else if (result == 100) {
            star = 3;
        }
        return star;
    }

    //set avatar For
    public int getAvatar(int avatar) {
        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                res = R.drawable.ic_girl;
                break;
            case StaticAccess.AVATAR_CAR:
                res = R.drawable.ic_cat;
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                res = R.drawable.ic_rocket;
                break;
            case StaticAccess.AVATAR_HORSE:
                res = R.drawable.ic_rocket;
                break;
            case StaticAccess.AVATAR_ROCKET:
                res = R.drawable.ic_rocket;
                break;

        }
        return res;
    }

    //set avatar For
    public int getAvatarCreateUser(int avatar) {
        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                res = R.drawable.avatar_default_princess;
                break;
            case StaticAccess.AVATAR_CAR:
                res = R.drawable.avatar_default_car;
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                res = R.drawable.avatar_default_dino;
                break;
            case StaticAccess.AVATAR_HORSE:
                res = R.drawable.avatar_default_uni;
                break;
            case StaticAccess.AVATAR_ROCKET:
                res = R.drawable.avatar_default_rocket;
                break;

        }
        return res;
    }

    //set avatar wise gift Box
    public int getGiftBox(int avatar) {
        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                res = R.drawable.ic_gift_girl;
                break;
            case StaticAccess.AVATAR_CAR:
                res = R.drawable.ic_gift_car;
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                res = R.drawable.ic_gift_dino;
                break;
            case StaticAccess.AVATAR_HORSE:
                res = R.drawable.ic_gift_unicorn;
                break;
            case StaticAccess.AVATAR_ROCKET:
                res = R.drawable.ic_gift_rocket;
                break;

        }
        return res;
    }

    //get avatar wise lamp For
    public int getLamp(int avatar, boolean isLamp) {
        int res = -1;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                if (!isLamp) {
                    res = R.drawable.ic_bulb_girl;
                } else {
                    res = R.drawable.ic_bulb_girl_dream;
                }
                break;
            case StaticAccess.AVATAR_CAR:
                if (!isLamp) {
                    res = R.drawable.ic_bulb_car;
                } else {
                    res = R.drawable.ic_bulb_car_dream;
                }

                break;
            case StaticAccess.AVATAR_DINOSAUR:

                if (!isLamp) {
                    res = R.drawable.ic_bulb_dino;
                } else {
                    res = R.drawable.ic_bulb_dino_dream;
                }

                break;
            case StaticAccess.AVATAR_HORSE:
                if (!isLamp) {
                    res = R.drawable.ic_bulb_unicorn;
                } else {
                    res = R.drawable.ic_bulb_unicorn_dream;
                }

                break;
            case StaticAccess.AVATAR_ROCKET:
                if (!isLamp) {
                    res = R.drawable.ic_bulb_rocket;
                } else {
                    res = R.drawable.ic_bulb_rocket_dream;
                }
                break;

        }
        return res;
    }

    //set avatar and  total point you will get Avatar
    public static int getAvatar(int avatar, int totalStar) {
        int arrGirl[] = {R.drawable.girl_01, R.drawable.girl_02, R.drawable.girl_03, R.drawable.girl_04, R.drawable.girl_05};
        int arrCar[] = {R.drawable.car_01, R.drawable.car_02, R.drawable.car_03, R.drawable.car_04, R.drawable.car_05};
        int arrDino[] = {R.drawable.dino_01, R.drawable.dino_02, R.drawable.dino_03, R.drawable.dino_04, R.drawable.dino_05};
        int arrUni[] = {R.drawable.unicorn_01, R.drawable.unicorn_02, R.drawable.unicorn_03, R.drawable.unicorn_04, R.drawable.unicorn_05};
        int arrRocket[] = {R.drawable.rocket_01, R.drawable.rocket_02, R.drawable.rocket_03, R.drawable.rocket_04, R.drawable.rocket_05};
        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                if (totalStar <= 14) {
                    res = arrGirl[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrGirl[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrGirl[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrGirl[3];
                } else if (159 < totalStar) {
                    res = arrGirl[4];
                }

                break;
            case StaticAccess.AVATAR_CAR:
                if (totalStar <= 14) {
                    res = arrCar[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrCar[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrCar[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrCar[3];
                } else if (159 < totalStar) {
                    res = arrCar[4];
                }
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                if (totalStar <= 14) {
                    res = arrDino[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrDino[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrDino[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrDino[3];
                } else if (159 < totalStar) {
                    res = arrDino[4];
                }
                break;
            case StaticAccess.AVATAR_HORSE:
                if (totalStar <= 14) {
                    res = arrUni[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrUni[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrUni[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrUni[3];
                } else if (159 < totalStar) {
                    res = arrUni[4];
                }
                break;
            case StaticAccess.AVATAR_ROCKET:
                if (totalStar <= 14) {
                    res = arrRocket[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrRocket[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrRocket[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrRocket[3];
                } else if (159 < totalStar) {
                    res = arrRocket[4];
                }
                break;

        }
        return res;
    }

    public static int rendomColorGeneretor(int i) {
        int color = -1;
       /*   int min = 0;
        int max = 4;

        Random r = new Random();
        int caseColor = r.nextInt(max - min + 1) + min;*/


        switch (i) {
            case 0:
                color = Color.parseColor(StaticAccess.FIRSTLAYER1);
                break;
            case 1:
                color = Color.parseColor(StaticAccess.FIRSTLAYER2);
                break;
            case 2:
                color = Color.parseColor(StaticAccess.FIRSTLAYER3);
                break;
            case 3:
                color = Color.parseColor(StaticAccess.FIRSTLAYER4);
                break;
            case 4:
                color = Color.parseColor(StaticAccess.FIRSTLAYER5);
                break;
            case 5:
                color = Color.parseColor(StaticAccess.FIRSTLAYER6);
                break;
            case 6:
                color = Color.parseColor(StaticAccess.FIRSTLAYER7);
                break;
            case 7:
                color = Color.parseColor(StaticAccess.FIRSTLAYER8);
                break;
            case 8:
                color = Color.parseColor(StaticAccess.FIRSTLAYER9);
                break;
            case 9:
                color = Color.parseColor(StaticAccess.FIRSTLAYER10);
                break;
        }
        return color;
    }


    // for count star
    public static ArrayList<Integer> getUserAvatarList(int avaterId) {
        ArrayList<Integer> arr = null;
        switch (avaterId) {
            case StaticAccess.AVATAR_GIRL:
                arr = new ArrayList<>();
                arr.add(R.drawable.shadeless_girl_01);
                arr.add(R.drawable.shadeless_girl_02);
                arr.add(R.drawable.shadeless_girl_03);
                arr.add(R.drawable.shadeless_girl_04);
                arr.add(R.drawable.shadeless_girl_05);


                break;
            case StaticAccess.AVATAR_CAR:
                arr = new ArrayList<>();
                arr.add(R.drawable.shadeless_car_01);
                arr.add(R.drawable.shadeless_car_02);
                arr.add(R.drawable.shadeless_car_03);
                arr.add(R.drawable.shadeless_car_04);
                arr.add(R.drawable.shadeless_car_05);

                break;
            case StaticAccess.AVATAR_DINOSAUR:
                arr = new ArrayList<>();
                arr.add(R.drawable.shadeless_dino_01);
                arr.add(R.drawable.shadeless_dino_02);
                arr.add(R.drawable.shadeless_dino_03);
                arr.add(R.drawable.shadeless_dino_04);
                arr.add(R.drawable.shadeless_dino_05);
                break;
            case StaticAccess.AVATAR_HORSE:
                arr = new ArrayList<>();
                arr.add(R.drawable.shadeless_unicorn_01);
                arr.add(R.drawable.shadeless_unicorn_02);
                arr.add(R.drawable.shadeless_unicorn_03);
                arr.add(R.drawable.shadeless_unicorn_04);
                arr.add(R.drawable.shadeless_unicorn_05);
                break;
            case StaticAccess.AVATAR_ROCKET:
                arr = new ArrayList<>();
                arr.add(R.drawable.shadeless_rocket_01);
                arr.add(R.drawable.shadeless_rocket_02);
                arr.add(R.drawable.shadeless_rocket_03);
                arr.add(R.drawable.shadeless_rocket_04);
                arr.add(R.drawable.shadeless_rocket_05);
                break;
        }

        return arr;
    }


    public int getLevel(int star) {
        //1-15-45-75-120 prev
        //1-15- 50-100- 175 prev
        //1-15- 50-100- 160

        int level = 0;
        if (star <= 14) {
            level = 0;
        } else if (14 < star && star <= 49) {
            level = 1;
        } else if (49 < star && star <= 99) {
            level = 2;
        } else if (99 < star && star <= 159) {
            level = 3;
        } else if (159 < star) {
            level = 4;
        }
        //six level how many star ?
        // here for testt
     /*   else if (124 < star&& star <= 188) {
            level = 5;
        }*/
        return level;
    }
/*    public int getLevel(int star) {
        //1-15-45-75-120 prev
        //1-15- 50-100- 175
        int level = 0;
        if (star <= 14) {
            level = 0;
        } else if (14 < star && star <= 29) {
            level = 1;
        } else if (29 < star && star <= 74) {
            level = 2;
        } else if (74 < star && star <= 124) {
            level = 3;
        } else if (124 < star) {
            level = 4;
        }
        //six level how many star ?
        // here for testt
     *//*   else if (124 < star&& star <= 188) {
            level = 5;
        }*//*
        return level;
    }*/

    // return point range using giftBox appear count
    public int[] getPointRange(int appearCount) {
        int[] points = new int[2];
        switch (appearCount) {
            case 0:
                points[0] = 14;
                points[1] = 49;
                break;
            case 1:
                points[0] = 49;
                points[1] = 99;
                break;
            case 2:
                points[0] = 99;
                points[1] = 159;
                break;
            case 3:
                points[0] = 159;
                points[1] = 181;
                break;
        }
        return points;
    }
    
    // appear level user star wise
    public int getAppearLevel(int star) {
        int levelId = 0;
        if (star <= 14) {
            levelId = 0;
        } else if (14 < star && star <= 49) {
            levelId = 1;
        } else if (49 < star && star <= 99) {
            levelId = 2;
        } else if (99 < star && star <= 159) {
            levelId = 3;
        } else if (159 < star && star <= 180) {
            levelId = 4;
        }

        return levelId;
    }


    //only use for level circle images shadeLess Image s method
    public static int getAvatarLevel(int avatar, int totalStar) {
        int arrGirl[] = {R.drawable.shadeless_girl_01, R.drawable.shadeless_girl_02, R.drawable.shadeless_girl_03, R.drawable.shadeless_girl_04, R.drawable.shadeless_girl_05};
        int arrCar[] = {R.drawable.shadeless_car_01, R.drawable.shadeless_car_02, R.drawable.shadeless_car_03, R.drawable.shadeless_car_04, R.drawable.shadeless_car_05};
        int arrDino[] = {R.drawable.shadeless_dino_01, R.drawable.shadeless_dino_02, R.drawable.shadeless_dino_03, R.drawable.shadeless_dino_04, R.drawable.shadeless_dino_05};
        int arrUni[] = {R.drawable.shadeless_unicorn_01, R.drawable.shadeless_unicorn_02, R.drawable.shadeless_unicorn_03, R.drawable.shadeless_unicorn_04, R.drawable.shadeless_unicorn_05};
        int arrRocket[] = {R.drawable.shadeless_rocket_01, R.drawable.shadeless_rocket_02, R.drawable.shadeless_rocket_03, R.drawable.shadeless_rocket_04, R.drawable.shadeless_rocket_05};
        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                if (totalStar <= 14) {
                    res = arrGirl[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrGirl[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrGirl[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrGirl[3];
                } else if (159 < totalStar) {
                    res = arrGirl[4];
                }

                break;
            case StaticAccess.AVATAR_CAR:
                if (totalStar <= 14) {
                    res = arrCar[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrCar[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrCar[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrCar[3];
                } else if (159 < totalStar) {
                    res = arrCar[4];
                }
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                if (totalStar <= 14) {
                    res = arrDino[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrDino[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrDino[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrDino[3];
                } else if (159 < totalStar) {
                    res = arrDino[4];
                }
                break;
            case StaticAccess.AVATAR_HORSE:
                if (totalStar <= 14) {
                    res = arrUni[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrUni[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrUni[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrUni[3];
                } else if (159 < totalStar) {
                    res = arrUni[4];
                }
                break;
            case StaticAccess.AVATAR_ROCKET:
                if (totalStar <= 14) {
                    res = arrRocket[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrRocket[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrRocket[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrRocket[3];
                } else if (159 < totalStar) {
                    res = arrRocket[4];
                }
                break;

        }
        return res;
    }


    //only use for level circle images shadeLess Image s method
    public static int getAvatarLevelProgress(int avatar, int totalStar) {
        int arrGirl[] = {R.drawable.shadeless_girl_01p, R.drawable.shadeless_girl_02p, R.drawable.shadeless_girl_03p, R.drawable.shadeless_girl_04p, R.drawable.shadeless_girl_05p};
        int arrCar[] = {R.drawable.shadeless_car_01p, R.drawable.shadeless_car_02p, R.drawable.shadeless_car_03p, R.drawable.shadeless_car_04p, R.drawable.shadeless_car_05p};
        int arrDino[] = {R.drawable.shadeless_dino_01p, R.drawable.shadeless_dino_02p, R.drawable.shadeless_dino_03p, R.drawable.shadeless_dino_04p, R.drawable.shadeless_dino_05p};
        int arrUni[] = {R.drawable.shadeless_unicorn_01p, R.drawable.shadeless_unicorn_02p, R.drawable.shadeless_unicorn_03p, R.drawable.shadeless_unicorn_04p, R.drawable.shadeless_unicorn_05p};
        int arrRocket[] = {R.drawable.shadeless_rocket_01p, R.drawable.shadeless_rocket_02p, R.drawable.shadeless_rocket_03p, R.drawable.shadeless_rocket_04p, R.drawable.shadeless_rocket_05p};
        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                if (totalStar <= 14) {
                    res = arrGirl[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrGirl[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrGirl[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrGirl[3];
                } else if (159 < totalStar) {
                    res = arrGirl[4];
                }

                break;
            case StaticAccess.AVATAR_CAR:
                if (totalStar <= 14) {
                    res = arrCar[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrCar[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrCar[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrCar[3];
                } else if (159 < totalStar) {
                    res = arrCar[4];
                }
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                if (totalStar <= 14) {
                    res = arrDino[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrDino[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrDino[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrDino[3];
                } else if (159 < totalStar) {
                    res = arrDino[4];
                }
                break;
            case StaticAccess.AVATAR_HORSE:
                if (totalStar <= 14) {
                    res = arrUni[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrUni[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrUni[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrUni[3];
                } else if (159 < totalStar) {
                    res = arrUni[4];
                }
                break;
            case StaticAccess.AVATAR_ROCKET:
                if (totalStar <= 14) {
                    res = arrRocket[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrRocket[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrRocket[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrRocket[3];
                } else if (159 < totalStar) {
                    res = arrRocket[4];
                }
                break;

        }
        return res;
    }


    //Product Id for Purchase
    public static String getProductID(int i) {
        String productID[] = {"lesson_001", "lesson_002", "lesson_003", "lesson_004", "lesson_005", "lesson_006", "lesson_007", "lesson_008", "lesson_009", "lesson_010"};
        String pID = "";
        switch (i) {
            case 0:
                pID = productID[0];

                break;
            case 1:
                pID = productID[1];
                break;
            case 2:
                pID = productID[2];
                break;
            case 3:
                pID = productID[3];
                break;
            case 4:
                pID = productID[4];
                break;
            case 5:
                pID = productID[5];
                break;
            case 6:
                pID = productID[6];
                break;
            case 7:
                pID = productID[7];
                break;
            case 8:
                pID = productID[8];
                break;
            case 9:
                pID = productID[9];
                break;

        }
        return pID;
    }

    //firstLayer get color
    public int getFirstLayerColor(int i) {
        int color = -1;
        switch (i) {
            case 0:
                color = Color.parseColor(StaticAccess.FIRSTLAYER1);
                break;
            case 1:
                color = Color.parseColor(StaticAccess.FIRSTLAYER2);
                break;
            case 2:
                color = Color.parseColor(StaticAccess.FIRSTLAYER3);
                break;
            case 3:
                color = Color.parseColor(StaticAccess.FIRSTLAYER4);
                break;
            case 4:
                color = Color.parseColor(StaticAccess.FIRSTLAYER5);
                break;
            case 5:
                color = Color.parseColor(StaticAccess.FIRSTLAYER6);
                break;
            case 6:
                color = Color.parseColor(StaticAccess.FIRSTLAYER7);
                break;
            case 7:
                color = Color.parseColor(StaticAccess.FIRSTLAYER8);
                break;
            case 8:
                color = Color.parseColor(StaticAccess.FIRSTLAYER9);
                break;
            case 9:
                color = Color.parseColor(StaticAccess.FIRSTLAYER10);
                break;
        }
        return color;
    }

    //only use for drag level  images method
    public static int getAvatarDragLevel(int avatar, int totalStar) {
        int arrGirl[] = {R.drawable.drag_girl_01, R.drawable.drag_girl_02, R.drawable.drag_girl_03, R.drawable.drag_girl_04, R.drawable.drag_girl_05};
        int arrCar[] = {R.drawable.drag_car_01, R.drawable.drag_car_02, R.drawable.drag_car_03, R.drawable.drag_car_04, R.drawable.drag_car_05};
        int arrDino[] = {R.drawable.drag_dino_01, R.drawable.drag_dino_02, R.drawable.drag_dino_03, R.drawable.drag_dino_04, R.drawable.drag_dino_05};
        int arrUni[] = {R.drawable.drag_horse_01, R.drawable.drag_horse_02, R.drawable.drag_horse_03, R.drawable.drag_horse_04, R.drawable.drag_horse_05};
        int arrRocket[] = {R.drawable.drag_rocket_01, R.drawable.drag_rocket_02, R.drawable.drag_rocket_03, R.drawable.drag_rocket_04, R.drawable.drag_rocket_05};
        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                if (totalStar <= 14) {
                    res = arrGirl[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrGirl[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrGirl[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrGirl[3];
                } else if (159 < totalStar) {
                    res = arrGirl[4];
                }

                break;
            case StaticAccess.AVATAR_CAR:
                if (totalStar <= 14) {
                    res = arrCar[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrCar[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrCar[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrCar[3];
                } else if (159 < totalStar) {
                    res = arrCar[4];
                }
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                if (totalStar <= 14) {
                    res = arrDino[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrDino[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrDino[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrDino[3];
                } else if (159 < totalStar) {
                    res = arrDino[4];
                }
                break;
            case StaticAccess.AVATAR_HORSE:
                if (totalStar <= 14) {
                    res = arrUni[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrUni[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrUni[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrUni[3];
                } else if (159 < totalStar) {
                    res = arrUni[4];
                }
                break;
            case StaticAccess.AVATAR_ROCKET:
                if (totalStar <= 14) {
                    res = arrRocket[0];
                } else if (14 < totalStar && totalStar <= 49) {
                    res = arrRocket[1];
                } else if (49 < totalStar && totalStar <= 99) {
                    res = arrRocket[2];
                } else if (99 < totalStar && totalStar <= 159) {
                    res = arrRocket[3];
                } else if (159 < totalStar) {
                    res = arrRocket[4];
                }
                break;

        }
        return res;
    }

    // get Book Image Jump Image on First layer
    public static int getAvatarWiseBookImage(int avatar) {

        int res = 0;
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                res = R.drawable.ic_floor_girl;
                break;
            case StaticAccess.AVATAR_CAR:
                res = R.drawable.ic_floor_car;
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                res = R.drawable.ic_floor_unicorn;// jorge change  tell us Rokan
                break;
            case StaticAccess.AVATAR_HORSE:
                res = R.drawable.ic_floor_rocket;
                break;
            case StaticAccess.AVATAR_ROCKET:
                res = R.drawable.ic_floor_dino;
                break;
        }
        return res;
    }

    //// method used for get animation duration for star animation
    public long getAnimDuration(int stars) {
        long duration = 2000;
        switch (stars) {
            case 0:
                duration = 2000;
                break;
            case 1:
                duration = 1500;
                break;
            case 2:
                duration = 3000;
                break;
            case 3:
                duration = 4000;
                break;
        }
        Log.d("making duration: ", String.valueOf(duration));
        return duration;
    }

    public String getAvatarWiseSound(int avatar) {


        String res = "";
        switch (avatar) {
            case StaticAccess.AVATAR_GIRL:
                res = "go_to_play_princess.mp3";
                break;
            case StaticAccess.AVATAR_CAR:
                res = "go_to_play_car.mp3";
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                res = "go_to_play_dino.mp3";// jorge change  tell us Rokan
                break;
            case StaticAccess.AVATAR_HORSE:
                res = "go_to_play_unicorn.mp3";
                break;
            case StaticAccess.AVATAR_ROCKET:
                res = "go_to_play_rocket.mp3";
                break;

        }
        return res;
    }

    /// level wise result activity gif images
    public int[] getlevelWiseResultGif(int levelID) {
        int[] res = {R.drawable.img_happy_face_large, R.drawable.img_happy_face_large};
        switch (levelID) {
            case StaticAccess.LEVEL_ONE:
                res = new int[]{R.drawable.result_gif_1_a, R.drawable.result_gif_1_b};
                break;
            case StaticAccess.LEVEL_TWO:
                res = new int[]{R.drawable.result_gif_2_a, R.drawable.result_gif_2_b};
                break;
            case StaticAccess.LEVEL_THREE:
                res = new int[]{R.drawable.result_gif_3_a, R.drawable.result_gif_3_b};
                break;
            case StaticAccess.LEVEL_FOUR:
                res = new int[]{R.drawable.result_gif_4_a, R.drawable.result_gif_4_b};
                break;
            case StaticAccess.LEVEL_FIVE:
                res = new int[]{R.drawable.result_gif_5_a, R.drawable.result_gif_5_b};
                break;
            case StaticAccess.LEVEL_SIX:
                res = new int[]{R.drawable.result_gif_6_a, R.drawable.result_gif_6_b};
                break;
        }
        return res;
    }

    /// return level wise reslults sound file names
    public String getLevelWiseResultSound(int levelID) {


        String res = "";
        switch (levelID) {
            case StaticAccess.LEVEL_ONE:
                res = "music_level_1.mp3";
                break;
            case StaticAccess.LEVEL_TWO:
                res = "music_level_2.mp3";
                break;
            case StaticAccess.LEVEL_THREE:
                res = "music_level_3.mp3";
                break;
            case StaticAccess.LEVEL_FOUR:
                res = "music_level_4.mp3";
                break;
            case StaticAccess.LEVEL_FIVE:
                res = "music_level_5.mp3";
                break;
            case StaticAccess.LEVEL_SIX:
                res = "music_level_6.mp3";
                break;

        }
        return res;
    }

    /// level wise result activity gif images
 /*   public int[] getlevelWiseSupperErrorGIF(int levelID) {
        int res = {R.drawable.img_happy_face_large, R.drawable.img_happy_face_large};
        switch (levelID) {
            case StaticAccess.LEVEL_ONE:
                res = new int[]{R.drawable.level_one_error_image_1, R.drawable.level_one_error_image_2};
                break;
            case StaticAccess.LEVEL_TWO:
                res = new int[]{R.drawable.level_two_error_image_1, R.drawable.level_two_error_image_2};
                break;
            case StaticAccess.LEVEL_THREE:
                res = new int[]{R.drawable.level_three_error_image_1, R.drawable.level_three_error_image_2};
                break;
            case StaticAccess.LEVEL_FOUR:
                res = new int[]{R.drawable.level_four_error_image_1, R.drawable.level_four_error_image_2};
                break;
            case StaticAccess.LEVEL_FIVE:
                res = new int[]{R.drawable.level_five_error_image_1, R.drawable.level_five_error_image_2};
                break;
            case StaticAccess.LEVEL_SIX:
                res = new int[]{R.drawable.level_six_error_image_1, R.drawable.level_six_error_image_2};
                break;

        }
        return res;
    }*//// level wise result activity gif images
    public int getlevelWiseSupperErrorGIF(int levelID) {

        int res = 0;
        switch (levelID) {
            case StaticAccess.LEVEL_ONE:
                res = R.drawable.level_one_error_image;
                break;
            case StaticAccess.LEVEL_TWO:
                res = R.drawable.level_two_error_image;
                break;
            case StaticAccess.LEVEL_THREE:
                res = R.drawable.level_three_error_image;
                break;
            case StaticAccess.LEVEL_FOUR:
                res = R.drawable.level_four_error_image;
                break;
            case StaticAccess.LEVEL_FIVE:
                res = R.drawable.level_five_error_image;
                break;
            case StaticAccess.LEVEL_SIX:
                res = R.drawable.level_six_error_image;
                break;

        }
        return res;
    }

    /// return level wise reslults sound file names
    public String getLevelWiseSupperErrorSound(int levelID) {
        String res = "";
        switch (levelID) {
            case StaticAccess.LEVEL_ONE:
                res = "level_one_error_sound.mp3";
                break;
            case StaticAccess.LEVEL_TWO:
                res = "level_two_error_sound.mp3";
                break;
            case StaticAccess.LEVEL_THREE:
                res = "level_three_error_sound.mp3";
                break;
            case StaticAccess.LEVEL_FOUR:
                res = "level_four_error_sound.mp3";
                break;
            case StaticAccess.LEVEL_FIVE:
                res = "level_five_error_sound.mp3";
                break;
            case StaticAccess.LEVEL_SIX:
                res = "level_six_error_sound.mp3";
                break;

        }
        return res;
    }

    // return level wise reslults sound file names
    public String getLevelWiseSupperErrorString(int levelID, Context context) {


        String res = "";
        switch (levelID) {
            case StaticAccess.LEVEL_ONE:
                res = context.getResources().getString(R.string.levelOneErrorString);
                break;
            case StaticAccess.LEVEL_TWO:
                res = context.getResources().getString(R.string.levelTwoErrorString);
                break;
            case StaticAccess.LEVEL_THREE:
                res = context.getResources().getString(R.string.levelThreeErrorString);
                break;
            case StaticAccess.LEVEL_FOUR:
                res = context.getResources().getString(R.string.levelFourErrorString);
                break;
            case StaticAccess.LEVEL_FIVE:
                res = context.getResources().getString(R.string.levelFiveErrorString);
                break;
            case StaticAccess.LEVEL_SIX:
                res = context.getResources().getString(R.string.levelSixErrorString);
                break;

        }
        return res;
    }

    // list level Shuffle
    public List<Integer> getShuffleList() {
        List<Integer> resLevelID = new ArrayList<>();
        resLevelID.add(0);
        resLevelID.add(1);
        resLevelID.add(2);
        resLevelID.add(3);
        resLevelID.add(4);
        resLevelID.add(5);
        Collections.shuffle(resLevelID);
        return resLevelID;
    }
}
