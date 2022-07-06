package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.R
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.databinding.GameWildBinding

class WildGame : AppCompatActivity() {
    private lateinit var wildBinding: GameWildBinding
    private var gameStarted = false

    private val items = listOf(
        R.drawable.a,
        R.drawable.k,
        R.drawable.mushroom,
        R.drawable.toad,
        R.drawable.wolf
    )

    private var lineItem: LineItem? = null
    private val currentItems = mutableListOf<Int>()
    private var currentPoints = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wildBinding = GameWildBinding.inflate(layoutInflater)
        setContentView(wildBinding.root)

        wildBinding.btnNext.setOnClickListener { btn ->
            if (gameStarted) {
                btn.isEnabled = false
                goAway(wildBinding.iv1, 300)
                goAway(wildBinding.iv2, 600)
                goAway(wildBinding.iv3, 900) {
                    revealItems {
                        lineItem = checkLineItem()
                        if (lineItem != null) {
                            val addPoints = calculateAddPoints()
                            animatePoints(currentPoints, currentPoints + addPoints) {
                                btn.isEnabled = true
                                currentPoints += addPoints
                            }
                        } else {
                            btn.isEnabled = true
                        }
                    }
                    drawItems()
                }
            } else {
                drawItems()
                revealItems()
                gameStarted = true
            }

        }
    }

    private fun animatePoints(from: Int, to: Int, onEnd: () -> Unit) {
        var currentPoints = from
        ValueAnimator.ofInt(from, to).apply {
            addUpdateListener {
                val currentValue = it.animatedValue as Int
                if (currentValue != currentPoints) {
                    wildBinding.tvPoints.text = currentValue.toString()
                    currentPoints = currentValue
                }
            }
            duration = 1000
            doOnEnd { onEnd() }
            start()
        }
    }

    private fun drawItems() {
        currentItems.clear()
        repeat(3) { currentItems.add(items.random()) }
        wildBinding.iv1.setImageResource(currentItems[0])
        wildBinding.iv2.setImageResource(currentItems[1])
        wildBinding.iv3.setImageResource(currentItems[2])
    }

    private fun revealItems(onEnd: () -> Unit = {}) {
        wildBinding.iv1.translationX = 0f
        wildBinding.iv1.alpha = 0f
        wildBinding.iv2.translationX = 0f
        wildBinding.iv2.alpha = 0f
        wildBinding.iv3.translationX = 0f
        wildBinding.iv3.alpha = 0f

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(wildBinding.iv1, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(wildBinding.iv2, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(wildBinding.iv3, "alpha", 0f, 1f)
            )
            doOnEnd { onEnd() }
            duration = 500
            start()
        }
    }

    private fun goAway(view: View, spinDelay: Long = 0L, onEnd: () -> Unit = {}) {
        val spinner = ValueAnimator.ofFloat(0f, 360f).apply {
            addUpdateListener {
                view.rotation = it.animatedValue as Float
            }
            interpolator = AccelerateInterpolator()
            duration = 100
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            start()
        }
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0f, 1500f)
            )
            interpolator = AccelerateInterpolator()
            duration = 500
            startDelay = spinDelay
            doOnEnd {
                spinner.end()
                onEnd()
            }
            start()
        }
    }

    private fun calculateAddPoints(): Int = when (lineItem) {
        LineItem.A, LineItem.K -> 100
        LineItem.MUSHROOM -> 200
        LineItem.TOAD -> 300
        LineItem.WOLF -> 500
        else -> 0
    }

    private fun checkLineItem(): LineItem? {
        if (currentItems[0] == currentItems[1] && currentItems[1] == currentItems[2]) {
            when {
                currentItems[0] == R.drawable.a -> return LineItem.A
                currentItems[0] == R.drawable.k -> return LineItem.K
                currentItems[0] == R.drawable.mushroom -> return LineItem.MUSHROOM
                currentItems[0] == R.drawable.toad -> return LineItem.TOAD
                currentItems[0] == R.drawable.wolf -> return LineItem.WOLF
            }
        }
        return null
    }
}