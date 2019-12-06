package edu.uw.eang16.wakey

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_math_solver.*
import kotlin.random.Random
import android.widget.Toast

enum class Operator {
    PLUS {
        override fun doMath(t: Int, u: Int): Int = t + u
    },
    MINUS {
        override fun doMath(t: Int, u: Int): Int = t - u
    },
    MULTIPLY {
        override fun doMath(t: Int, u: Int): Int = t * u
    },
    DIVIDE {
        override fun doMath(t: Int, u: Int): Int = t / u
    };

    abstract fun doMath(t: Int, u:Int): Int
}

class MathSolver: AppCompatActivity(), WakeyAlarm {


    var answer: Int = 0
    val operator = listOf(Operator.PLUS, Operator.MINUS, Operator.MULTIPLY, Operator.DIVIDE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        setContentView(R.layout.activity_math_solver)
        problem.text = makeProblem()
        val data = intent!!.getParcelableExtra("data") as AlarmData

        snooze.setOnClickListener {
            if (snoozeAlarm(data, applicationContext)) {
                Log.e("msg", "Snooze button pressed")
                finish()
            }
        }

        math_submit.setOnClickListener {
            if (input_answer.text.toString().equals("$answer")) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                stopAlarm(data,applicationContext)
                finish()
            } else {
                Toast.makeText(this, "Wrong answer. Try again!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun makeProblem(): String {
        val todayOperator = mutableListOf<Operator>()
        setTodayOperator(todayOperator)
        answer = (Random.nextInt(10) + 1)
        var result = "$answer"
        for (operator in todayOperator) {
            val temp = Random.nextInt(10) + 1
            if (operator === Operator.PLUS) {
                answer += temp
                result += " + ${temp}"
            } else if (operator === Operator.MINUS) {
                answer -= temp
                result += " - ${temp}"
            } else if (operator === Operator.MULTIPLY) {
                answer *= temp
                result += " x ${temp}"
            } else {
                if (answer % temp === 0) {
                    answer /= temp
                    result += " / ${temp}"
                } else {
                    answer += temp
                    result += " + ${temp}"
                }
            }
        }
        return result
    }

    fun setTodayOperator(list: MutableList<Operator>) {
        list.add(operator[Random.nextInt(operator.size)])
        list.add(operator[Random.nextInt(operator.size)])
        list.add(operator[Random.nextInt(operator.size)])
    }

}