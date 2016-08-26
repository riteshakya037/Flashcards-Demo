package com.sorcery.flashcards.CustomViews;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Flip animation for cards view. Switching between to view while changing camera.
 * <p>
 * Created by Ritesh Shakya on 8/24/2016.
 */
public class FlipAnimation extends Animation {
    /**
     * Change the camera of view while animating.
     */
    private Camera camera;

    /**
     * Current active view
     */
    private View fromView;
    /**
     * View to rotate to.
     */
    private View toView;

    /**
     * Center X value of view
     */
    private float centerX;
    /**
     * Center Y value of view.
     */
    private float centerY;

    private boolean forward = true;

    public FlipAnimation(View fromView, View toView) {
        this.fromView = fromView;
        this.toView = toView;

        setDuration(700);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    /**
     * Play animation in reverse.
     */
    public void reverse() {
        forward = false;
        View switchView = toView;
        toView = fromView;
        fromView = switchView;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width / 2;
        centerY = height / 2;
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);

        if (interpolatedTime >= 0.5f) { // Alternate the visibility of views at the exact half of animation duration.
            degrees -= 180.f;
            fromView.setVisibility(View.GONE);
            toView.setVisibility(View.VISIBLE);
        }

        if (forward)
            degrees = -degrees;

        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
