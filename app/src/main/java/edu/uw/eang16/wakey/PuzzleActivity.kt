package edu.uw.eang16.wakey

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_math_solver.*
import kotlinx.android.synthetic.main.activity_math_solver.snooze
import kotlinx.android.synthetic.main.activity_puzzle_solver.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class PuzzleActivity: AppCompatActivity(), WakeyAlarm {

    var key = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_solver)
        val data = this.intent!!.getParcelableExtra("data") as AlarmData
        startAlarm(data, this)
        val buttons = arrayOf(button, button2, button3,
                              button4, button5, button6,
                              button7, button8, button9)

        val answer = randomAnswer()

        start.setOnClickListener {
            for(btn in buttons) {
                btn.visibility = View.VISIBLE
                btn.setOnClickListener {
                    if (btn.text === " ") {
                        btn.background = getResources().getDrawable(R.drawable.circle)
                        btn.text = ""
                    } else {
                        btn.background = getResources().getDrawable(R.drawable.circle_selected)
                        btn.text = " "
                    }
                }
            }
            snooze.visibility = View.VISIBLE
            puzzle_submit.visibility = View.VISIBLE
            start.visibility = View.INVISIBLE
            showAnswer(buttons)
            Timer("SetUp", false).schedule(2000) {
                clear(buttons)
            }
        }



        snooze.setOnClickListener {
            if (snoozeAlarm(data, it.context)) {
                Log.e("msg", "Snooze button pressed")
                finish()
            }
        }

        puzzle_submit.setOnClickListener {
            var check = true
            for(i in 0..8) {
                if (answer[i] != buttons[i].text) {
                    check = false
                }
            }
            if(check) {
                Toast.makeText(this, "Correct pattern!", Toast.LENGTH_SHORT).show()
                stopAlarm(data, this)
                finish()
            } else {
                Toast.makeText(this, "Incorrect pattern. Try again!", Toast.LENGTH_SHORT).show()
                clear(buttons)
            }
        }
    }

    fun randomAnswer(): Array<String> {

        val TRUE = " "
        val FALSE = ""

        val pattern = arrayOf(FALSE, FALSE, FALSE,
            FALSE, FALSE, FALSE,
            FALSE, FALSE, FALSE)

        for(i in 0..2) {
            val index = Random.nextInt(8)
            pattern[index] = TRUE
            key.add(index)
        }

        return pattern
    }

    fun showAnswer(btns: Array<Button>) {
        for (key in key) {
            btns[key].background = getResources().getDrawable(R.drawable.circle_selected)
            btns[key].isClickable = false
        }
    }

    fun clear(btns: Array<Button>) {
        for (btn in btns) {
            btn.background = getResources().getDrawable(R.drawable.circle)
            btn.isClickable = true
        }
    }

}