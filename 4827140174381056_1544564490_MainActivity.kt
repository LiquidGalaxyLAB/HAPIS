package com.example.krzysztof.hapis_questionare

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

var activeQuestion : Int = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = MySqlHelper(this)
        if (db.isTableEmpty("questions")){
            db.insertData(Question(1,"Question 1","How old are you?", arrayOf("<20", "21-40", "41+")))
            db.insertData(Question(2,"Question 2","Is your body mass proper to your height?", arrayOf("Yes", "No", "Hard to tell")))
            db.insertData(Question(3,"Question 3","How many close relatives do you have?", arrayOf("0", "1-5", "6+")))
            db.insertData(Question(4,"Question 4","Are you addicted to something or taking something regularly?", arrayOf("Yes", "No", "Hard to tell")))
            db.insertData(Question(5,"Question 5","Are you employed?", arrayOf("Yes", "No", "Hard to tell")))
            db.insertData(Question(6,"Question 6","Do you have any children?", arrayOf("Yes, one", "Yes, more than one", "No")))
            db.insertData(Question(7,"Question 7","Do you have a warm place to stay at night?", arrayOf("Yes", "No", "Hard to tell")))
            db.insertData(Question(8,"Question 8","Have you ever been married?", arrayOf("Yes, once", "Yes, more than once", "No")))
            db.insertData(Question(9,"Question 9","What is your education?", arrayOf("Primary", "College", "University")))
            db.insertData(Question(10,"Question 10","How difficult it is for you to get food on daily basis? ", arrayOf("Not difficult at all", "Quite difficult", "Very difficult")))
            db.insertData(Question(11,"Question 11","Do you have access to running water?", arrayOf("Yes", "No", "Hard to tell")))
            db.insertData(Question(12,"Question 12","Do you have access to electricity?", arrayOf("Yes", "No", "Hard to tell")))
            db.insertData(Question(13,"Question 13","Do you have any disability?", arrayOf("Yes, physical", "Yes, mental", "No")))
            db.insertData(Question(14,"Question 14","What is your gender?", arrayOf("Male", "Female", "Other")))
            db.insertData(Question(15,"Question 15","Are you financially independent?", arrayOf("Yes", "No", "Hard to tell")))
        }
        var answersDone: MutableList<String> = ArrayList()
        var commonAnswer = 0
        setQuestion(db.loadQuestionFromDatabase(1))
        var activeAnswer: Int = -1
        firstAnswerButton.setOnClickListener{
            activeAnswer = 1
        }
        secondAnswerButton.setOnClickListener {
            activeAnswer = 2
        }
        thirdAnswerButton.setOnClickListener {
            activeAnswer = 3
        }

        viewStatisticsButton.setOnClickListener {
            val intent = Intent(this, statistics::class.java)
            startActivity(intent)
        }
        nextButton.setOnClickListener {
            if (activeQuestion < 16) {
                if (activeAnswer != -1 && otherTextView.text.toString() == "") {
                    commonAnswer++
                    answersDone.add(db.loadQuestionFromDatabase(activeQuestion).Answers[activeAnswer - 1])
                } else if (otherTextView.text.toString() != "") {
                    answersDone.add(otherTextView.text.toString())
                } else {
                    return@setOnClickListener
                }
                if(activeQuestion < 15) {
                    activeQuestion++
                    setQuestion(db.loadQuestionFromDatabase(activeQuestion))
                    activeAnswer = -1
                }
                else if(activeQuestion >= 15){
                    db.insertData(Answer(answersDone.toTypedArray(), commonAnswer))
                    val intent = Intent(this, thankyou::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setQuestion(question: Question){
        questionTextView.text = question.QuestionName
        questionDescriptionTextView.text = question.QuestionBody
            firstAnswerButton.text = question.Answers[0]
            secondAnswerButton.text = question.Answers[1]
            thirdAnswerButton.text = question.Answers[2]
        otherTextView.setText("")
    }

}
