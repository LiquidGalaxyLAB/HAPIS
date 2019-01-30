package com.example.krzysztof.hapis_questionare

import android.app.slice.Slice
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_statistics.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue

class statistics : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val pieData: MutableList<SliceValue>  = loadDataFromDatabase()
        val pieChartData = PieChartData(pieData)
        pieChartData.setHasLabels(true)
        findViewById<lecho.lib.hellocharts.view.PieChartView>(R.id.chart).pieChartData = pieChartData
    }

    private fun loadDataFromDatabase(): MutableList<SliceValue>{
        val db = MySqlHelper(this)
        val answersSet = db.loadAnswerFromDatabase()
        var commonAnswers: Int = 0
        answersSet.forEach {
            commonAnswers+=it.CommonCount
        }
        println(commonAnswers)
        return mutableListOf(SliceValue(commonAnswers.toFloat(), Color.GREEN).setLabel("Common Answers"), SliceValue((answersSet.count() * 15) - commonAnswers.toFloat(),Color.BLUE).setLabel("Other Answers"))
    }
}
