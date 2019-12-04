package edu.uw.eang16.wakey

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_math_solver.*
import kotlinx.android.synthetic.main.activity_math_solver.snooze
import kotlinx.android.synthetic.main.activity_puzzle_solver.*
import java.util.*

class PuzzleActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_solver)

        val TRUE = " "
        val FALSE = ""

        val answer = arrayOf(TRUE, FALSE, TRUE,
            FALSE, TRUE, FALSE,
            FALSE, FALSE, FALSE)

        val buttons = arrayOf(button, button2, button3,
                              button4, button5, button6,
                              button7, button8, button9)

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
            }
        }



    }
}