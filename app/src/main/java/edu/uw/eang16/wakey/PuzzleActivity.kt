package edu.uw.eang16.wakey

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_solver)

        val buttons = arrayOf(button, button2, button3,
                              button4, button5, button6,
                              button7, button8, button9)
        val alarmManager = AlarmHelper.getAlarmManager(this)

        for(btn in buttons) {
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

        val answer = randomAnswer()
        showAnswer(buttons)
        Timer("SetUp", false).schedule(2000) {
            clear(buttons)
        }

        snooze.setOnClickListener {
            var check = true
            for(i in 0..8) {
                if (answer[i] != buttons[i].text) {
                    check = false
                }
            }
            if(check) {
                Toast.makeText(this, "Correct pattern!", Toast.LENGTH_SHORT).show()
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
        }
    }

    fun clear(btns: Array<Button>) {
        for (btn in btns) {
            btn.background = getResources().getDrawable(R.drawable.circle)
        }
    }

    override fun startAlarm(alarmData: AlarmData) {

    }

    // When the alarm wakes up after a snooze, this resumes the ringtone and alarm
    override fun resumeAlarm(alarmData: AlarmData) {

    }

    // Keeps track of how many snoozes are available and disables snooze if none are left
    override fun snoozeAlarm(alarmData: AlarmData) {
        val snoozeTimeInMinutes = SNOOZE_TIMES[alarmData.snooze]
    }

    // Upon successfully completing the task, stops the alarm
    override fun stopAlarm() {

    }
}