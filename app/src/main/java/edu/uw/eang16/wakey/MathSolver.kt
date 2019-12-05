package edu.uw.eang16.wakey

import android.content.Intent
import android.os.Bundle
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
        setContentView(R.layout.activity_math_solver)
        problem.text = makeProblem()
        val data = intent!!.getParcelableExtra("data") as AlarmData
        startAlarm(data,this)

        snooze.setOnClickListener {
            snoozeAlarm(data, this)
            finish()
        }

        math_submit.setOnClickListener {
            if (input_answer.text.toString().equals("$answer")) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                stopAlarm(data,this)
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