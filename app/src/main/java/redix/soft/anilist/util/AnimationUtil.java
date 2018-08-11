package redix.soft.anilist.util;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
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

}
