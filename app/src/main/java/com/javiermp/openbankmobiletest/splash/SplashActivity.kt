package com.javiermp.openbankmobiletest.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.common.navigation.Navigator
import com.javiermp.openbankmobiletest.common.widget.BaseActivity
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        setAnimations()
        navigateToMainActivity()
    }

    // region Animations
    private fun setAnimations() {
        setLaunchAnimation()
        setRotatingPoundAnimation()
    }

    private fun setLaunchAnimation() {
        val animation = AlphaAnimation(0f , 1.0f )
        animation.isFillEnabled = false
        animation.duration = 900
        l_splash_activity.startAnimation(animation)
    }

    private fun setRotatingPoundAnimation() {
        val animatorSet = AnimatorSet()
        val rotationAnimation = ObjectAnimator.ofFloat(siv_pound , "rotation", 0f, 360f)
        val fadeAnimation = ObjectAnimator.ofFloat(siv_pound, "alpha", 0.5f, 0.1f)

        rotationAnimation.repeatMode = ValueAnimator.RESTART
        rotationAnimation.repeatCount = ValueAnimator.INFINITE
        rotationAnimation.duration = 1500
        fadeAnimation.repeatMode = ValueAnimator.REVERSE
        fadeAnimation.repeatCount = ValueAnimator.INFINITE
        fadeAnimation.duration = 750

        animatorSet.play(rotationAnimation).with(fadeAnimation)
        animatorSet.start()
    }
    // endregion

    // region Navigation
    private fun navigateToMainActivity() {
        Handler().postDelayed({
            Navigator.navigateToMainActivity(this)
        }, 4500)
    }
    // endregion
}