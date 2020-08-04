package redix.soft.anilist.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import redix.soft.anilist.activity.MainActivity;

public class AnimationUtil {

    public static void translate(final View v, int duration){
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
        anim.setDuration(duration);
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

    public static void switchFragments(View fragmentToHide, View fragmentToShow){
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

    public static void smoothSwitch(View fragmentToHide, View fragmentToShow, boolean isRight){
        AnimationSet animationSet = new AnimationSet(true);
        int fromXDelta = !isRight? 30 : -30;

        Animation trAnimation = new TranslateAnimation(fromXDelta, 0, 0, 0);
        trAnimation.setDuration(180);
        trAnimation.setRepeatMode(Animation.REVERSE);
        animationSet.addAnimation(trAnimation);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(180);
        anim.setFillAfter(true);
        animationSet.addAnimation(anim);

        fragmentToHide.setVisibility(View.GONE);
        fragmentToShow.setVisibility(View.VISIBLE);
        fragmentToShow.startAnimation(animationSet);
    }

    public static void rotate(View view, int degrees, int duration){
        view.animate()
                .rotationBy(degrees)
                .setDuration(duration)
                .start();
    }

    public static void expand(final View v, int duration) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(duration == 0? (int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density) : duration);
        v.startAnimation(a);
    }

    public static void collapse(final View v, int duration, int delay) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(duration == 0? (int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density) : duration);
        a.setFillAfter(false);
        a.setStartOffset(delay);
        v.startAnimation(a);
    }

    public static void changeToolbarColor(Activity activity, int colorFrom, int colorTo){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        ValueAnimator colorStatusAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);

        colorAnimation.addUpdateListener(animator -> ((MainActivity) activity).getToolbar().setBackgroundColor((Integer) animator.getAnimatedValue()));
        colorStatusAnimation.addUpdateListener(animator -> activity.getWindow().setStatusBarColor((Integer) animator.getAnimatedValue()));

        colorAnimation.setDuration(400);
        colorAnimation.setStartDelay(0);
        colorAnimation.start();
        colorStatusAnimation.setDuration(400);
        colorStatusAnimation.setStartDelay(0);
        colorStatusAnimation.start();
    }

    public static void changeImageColor(Context context, final ImageView v, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(400); // milliseconds
        colorAnimation.addUpdateListener(animator -> v.setColorFilter((int) animator.getAnimatedValue()));
        colorAnimation.start();
    }

}
