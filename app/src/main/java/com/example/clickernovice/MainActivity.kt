package com.example.clickernovice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.widget.Toast
import com.example.clickernovice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var score = 0
    var timeleft = 0
    var isGameStarted = false
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scoreView.text = getString(R.string.score_value, score)
        binding.timeleftView.text = getString(R.string.timeleft_value, durationTotal / 1000)
        binding.clickMeBtn.setOnClickListener {
            incrementScore()
        }
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeleft = savedInstanceState.getInt(TIMELEFT_KEY)
            restoreGame()
        } else {
            restartGame()
        }
    }


    private fun restoreGame() {
        binding.scoreView.text = getString(R.string.score_value, score)
        binding.timeleftView.text = getString(R.string.timeleft_value, timeleft)
        countDownTimer = object : CountDownTimer((timeleft * 1000).toLong(), durationOfStepTimer) {
            override fun onTick(millisUntilFinished: Long) {
                onTickCustom(millisUntilFinished)
            }

            override fun onFinish() {
                endGame()
            }


        }
        isGameStarted = true
        countDownTimer.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIMELEFT_KEY, timeleft)
        countDownTimer.cancel()


    }

    fun onTickCustom(millisUntilFinished: Long) {
        timeleft = (millisUntilFinished / 1000).toInt()
        binding.timeleftView.text =
            getString(R.string.timeleft_value, millisUntilFinished / 1000)
    }

    private fun restartGame() {
        score = 0

        countDownTimer = object :
            CountDownTimer(durationTotal, durationOfStepTimer) {
            override fun onTick(millisUntilFinished: Long) {
                onTickCustom(millisUntilFinished)
            }


            override fun onFinish() {

                endGame()
            }


        }
        countDownTimer.start()
    }

    private fun endGame() {
        Toast.makeText(
            this@MainActivity,
            getString(R.string.finish_text, score), Toast.LENGTH_SHORT
        ).show()
        isGameStarted = false
        binding.scoreView.text = getString(R.string.score_value, 0)
        binding.timeleftView.text = getString(R.string.timeleft_value, durationTotal / 1000)
    }

    private fun incrementScore() {
        score++
        binding.scoreView.text =
            getString(R.string.score_value, score)
        if (!isGameStarted) {
            startGame()
        }
    }

    private fun startGame() {
        isGameStarted = true
        restartGame()
    }

    companion object {
        private const val durationTotal: Long = 15000
        private const val durationOfStepTimer: Long = 1000
        private const val SCORE_KEY = "SCORE_BUNDLE"
        private const val TIMELEFT_KEY = "TIMELEFT_KEY"
    }
}