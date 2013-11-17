package com.dgsd.android.weatherdemo.util;

import android.view.animation.*;

/**
 *
 */
public class Anim {
    private static LayoutAnimationController mCascadeAnimationController;

    public static LayoutAnimationController getCascadeLayoutAnimation() {
        if (mCascadeAnimationController != null) {
            return mCascadeAnimationController;
        }
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(200);
        animation.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(200);
        animation.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(animation);

        mCascadeAnimationController = new LayoutAnimationController(set, 0.5f);
        return mCascadeAnimationController;
    }
}
