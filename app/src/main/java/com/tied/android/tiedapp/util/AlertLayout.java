package com.tied.android.tiedapp.util;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

/**
 * Created by femi on 8/17/2016.
 */
public class AlertLayout extends LinearLayout
{
    private Animation inAnimation;
    private Animation outAnimation;

    public AlertLayout(Context context)
    {
        super(context);
      //  Animation showAnimation=this.animate().translationY(0).alpha(0.0f);
    }

    public AlertLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setVisibility(View.INVISIBLE);
    }
    public AlertLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setVisibility(View.INVISIBLE);
    }


    public void setInAnimation(Animation inAnimation)
    {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation)
    {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(final int visibility)
    {
        if (getVisibility() != visibility || visibility== View.VISIBLE)
        {
            if (visibility == VISIBLE)
            {
                this.setTranslationY(-1*this.getHeight());
                //this.animate().translationY(-1*this.getHeight()).setDuration(10).start();
                super.setVisibility(visibility);
                Logger.write("Translating "+this.getHeight());
               this.animate().translationYBy(this.getHeight()).setDuration(200).setListener(new Animator.AnimatorListener() {
                   @Override
                   public void onAnimationStart(Animator animator) {
                       Logger.write("Translating started");
                   }

                   @Override
                   public void onAnimationEnd(Animator animator) {
                       Logger.write("Translating ended");
                       AlertLayout.this.setTranslationY(0);
                   }

                   @Override
                   public void onAnimationCancel(Animator animator) {

                   }

                   @Override
                   public void onAnimationRepeat(Animator animator) {

                   }
               }).start();
                return;
            }
            else if ((visibility == INVISIBLE) || (visibility == GONE))
            {
                this.animate().translationY(-1*this.getHeight()).setDuration(200).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                       // AlertLayout.super.setVisibility(visibility);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();

            }
        }else{
            if ((visibility == INVISIBLE) || (visibility == GONE)) {
                this.setTranslationY(-1*this.getHeight());
            }
        }


    }
}