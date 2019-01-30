package com.example.krzysztof.hapis_questionare

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import org.jetbrains.anko.db.*

class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Questionary") {
    companion object {
        private var instance: MySqlHelper? = null
        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }
    val context = ctx

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("questions", true,
            "_id" to INTEGER + PRIMARY_KEY,
            "questionName" to TEXT,
            "questionDescription" to TEXT,
            "firstAnswer" to TEXT,
            "secondAnswer" to TEXT,
            "thirdAnswer" to TEXT)
        db.createTable("answers", true,
            "_id" to INTEGER + PRIMARY_KEY,
              "answer1" to TEXT,
              "answer2" to TEXT,
              "answer3" to TEXT,
              "answer4" to TEXT,
              "answer5" to TEXT,
              "answer6" to TEXT,
              "answer7" to TEXT,
              "answer8" to TEXT,
              "answer9" to TEXT,
              "answer10" to TEXT,
              "answer11" to TEXT,
              "answer12" to TEXT,
              "answer13" to TEXT,
              "answer14" to TEXT,
              "answer15" to TEXT,
              "isCommon" to INTEGER
            )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(question: Question){
        this.use {
            insert("questions", "questionName" to question.QuestionName, "questionDescription" to question.QuestionBody,
        "firstAnswer" to question.Answers[0], "secondAnswer" to question.Answers[1], "thirdAnswer" to question.Answers[2])
        }
    }

    fun insertData(answers: Answer){
        val result = this.use {
            insert("answers", "answer1" to answers.Answers[0],
                "answer2" to answers.Answers[1],
                "answer3" to answers.Answers[2],
                "answer4" to answers.Answers[3],
                "answer5" to answers.Answers[4],
                "answer6" to answers.Answers[5],
                "answer7" to answers.Answers[6],
                "answer8" to answers.Answers[7],
                "answer9" to answers.Answers[8],
                "answer10" to answers.Answers[9],
                "answer11" to answers.Answers[10],
                "answer12" to answers.Answers[11],
                "answer13" to answers.Answers[12],
                "answer14" to answers.Answers[13],
                "answer15" to answers.Answers[14],
                "isCommon" to answers.CommonCount)
        }
    }

    fun isTableEmpty(tableName: String) : Boolean{
            val result = this.use {
                select(tableName).whereArgs("_id = 2").exec {
                    this.count
                }
            }
        return result <= 0
    }

     fun loadQuestionFromDatabase(questionId: Int):Question{
            val parser =
                rowParser { id: Int,name: String, body: String, firstAnswer: String, secondAnswer: String, thirdAnswer: String ->
                    Question(
                        id,
                        name,
                        body,
                        arrayOf(firstAnswer, secondAnswer, thirdAnswer)
                    )
                }

         return use {
                select("questions").whereArgs("_id = {questionId}", "questionId" to questionId).exec {
                     parseList(parser)[0]
                }
            }
    }

    fun loadAnswerFromDatabase(): List<Answer>{
        val parser =
            rowParser{id: Int,answer1: String, answer2: String, answer3: String, answer4: String, answer5: String, answer6: String, answer7: String
            ,answer8: String,answer9: String,answer10: String,answer11: String,answer12: String,answer13: String,answer14: String,answer15: String, commCount:Int ->
                Answer(arrayOf(answer1,answer2,answer3,answer4,answer5,answer6,answer7,answer8,answer9,answer10,answer11,answer12,answer13,answer14,answer15), commCount)
            }
        return use {
            select("answers").exec {
                parseList(parser)
            }
        }
    }

}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)