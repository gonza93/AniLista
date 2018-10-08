package redix.soft.anilist.util;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

    public static void expand(final View v) {
        TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
        v.setVisibility(View.VISIBLE);

        anim.setDuration(200);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

    public static void collapse(final View v){
        TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
        Animation.AnimationListener collapselistener= new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        };

        anim.setAnimationListener(collapselistener);
        anim.setDuration(200);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

    public static void fadeIn(View view){
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    public static void fadeOut(View view){
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        anim.setFillAfter(false);
        view.startAnimation(anim);
    }

    public static void switchFragments(View fragmentToHide, View fragmentToShow, Context context){
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(150);
        anim.setFillAfter(false);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fragmentToHide.setVisibility(View.INVISIBLE);
                AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(150);
                anim.setFillAfter(true);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        fragmentToShow.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fragmentToShow.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fragmentToHide.startAnimation(anim);
    }

    public static void rotate(View view, int degrees, int duration){
        /*RotateAnimation rotate = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setDuration(duration);
        rotate.setFillAfter(true);
        view.setAnimation(rotate);*/

        view.animate()
                .rotationBy(degrees)
                .setDuration(duration)
                .start();
    }

}
