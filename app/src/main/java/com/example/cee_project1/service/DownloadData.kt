package com.example.cee_project1.service

import android.os.Bundle
import android.os.Handler
import com.example.cee_project1.data.Quiz
import com.example.cee_project1.data.Term
import io.realm.RealmList
import org.jsoup.Jsoup

class DownloadData {

    private val baseURL = "https://bitda2022.github.io/cee_html/"

    private val termURL = arrayOf("knowledge/economy_basic.html", "knowledge/financial_basic.html", "knowledge/stock_advanced.html")

    private val quizURL = arrayOf("quiz/economy_basic_quiz.html", "quiz/financial_basic_quiz.html", "quiz/stock_advanced_quiz.html")

    fun getVersion(handler : Handler) {
        var version = ""

        Thread {
            kotlin.run {
                try {
                    val doc = Jsoup.connect(baseURL).get()
                    val verElement = doc.select("#version")

                    if(!verElement.isEmpty())
                        version = verElement.text()

                    val bundle = Bundle()
                    bundle.putString("version", version)
                    val msg = handler.obtainMessage()
                    msg.data = bundle
                    handler.sendMessage(msg)
                } catch(e : Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    fun downloadTerms(handler : Handler) {
        val termList = ArrayList<Term>()

        Thread {
            kotlin.run {
                try {
                    var id = 0
                    for(url in termURL) {
                        val doc = Jsoup.connect(baseURL + url).get()
                        val forms = doc.select(".form")

                        for(form in forms) {
                            val name = form.select(".term").text()
                            val description = form.select(".description").text()
                            val metaphor = form.select(".metaphor").text()
                            val example = form.select(".real_example").text()
                            val hasStudied = false

                            val quizs = RealmList<Quiz>()

                            val term = Term(
                                id,
                                url,
                                name,
                                description,
                                metaphor,
                                example,
                                hasStudied,
                                quizs
                            )

                            termList.add(id, term)
                            id++
                        }
                    }

                    val msg = handler.obtainMessage()
                    msg.obj = termList
                    handler.sendMessage(msg)
                } catch(e : Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    fun downloadQuizs(handler: Handler) {
        val quizList = ArrayList<Quiz>()

        Thread {
            kotlin.run {
                try {
                    var id = 0
                    for(url in quizURL) {
                        val doc = Jsoup.connect(baseURL + url).get()
                        val forms = doc.select(".form")

                        for(form in forms) {
                            val term = form.select(".term").text()
                            val content = form.select(".content").text()

                            // crawling answer
                            val answer = form.select(".answer").text()
                            val bool : Boolean = if(answer.equals("o"))
                                true
                            else if(answer.equals("x"))
                                false
                            else
                                throw java.lang.Exception("wrong answer form")


                            val wrong = 0

                            val quiz = Quiz(
                                id,
                                term,
                                content,
                                bool,
                                wrong
                            )

                            quizList.add(id, quiz)
                            id++
                        }
                    }

                    val msg = handler.obtainMessage()
                    msg.obj = quizList
                    handler.sendMessage(msg)
                } catch(e : Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

}