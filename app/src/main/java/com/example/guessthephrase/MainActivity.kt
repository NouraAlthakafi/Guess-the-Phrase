package com.example.guessthephrase

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    //declare
    lateinit var clForScore: LinearLayout
    lateinit var tvScore: TextView
    lateinit var thePhrase: TextView
    lateinit var lettersG: TextView
    lateinit var rvPhrase: RecyclerView
    lateinit var guesses: ArrayList<String>
    lateinit var etInput: EditText
    lateinit var buttonGuess: Button

    val mystery = "A PIECE OF CAKE"
    var guessAnswer = CharArray(mystery.length)
    var lettersOut = ArrayList<Char>()
    var tryPhrase = 10
    var dealInput = ""

    private lateinit var sharedPreferences: SharedPreferences

    private var myScore = 0
    private var theHighestScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_score), Context.MODE_PRIVATE)
        theHighestScore = sharedPreferences.getInt("HighestScore", 0)

        //initialize
        clForScore = findViewById(R.id.clForScore)
        tvScore = findViewById(R.id.tvScore)
        thePhrase = findViewById(R.id.thePhrase)
        lettersG = findViewById(R.id.lettersG)
        etInput = findViewById(R.id.etInput)
        buttonGuess = findViewById(R.id.button)

        rvPhrase = findViewById(R.id.rvPhrase)
        guesses = ArrayList()
        rvPhrase.adapter = RVphrase(guesses)
        rvPhrase.layoutManager = LinearLayoutManager(this)

        //utilize
        tvScore.text = "Highest Score: $theHighestScore"

        for (i in mystery.indices) {
            if (mystery[i] == ' ') {
                guessAnswer[i] = ' '
            } else {
                guessAnswer[i] = '*'
            }
        }
        thePhrase.text = "Phrase: ${String(guessAnswer)}"
        buttonGuess.setOnClickListener {
            dealInput = etInput.text.toString().toUpperCase()
            if (dealInput.isNotEmpty() && dealInput.length > 1){
                allPhrase()
            }
            else if (dealInput.isNotEmpty() && dealInput.length == 1){
                allLetters()
            }
            else if (dealInput.isEmpty()){
                Toast.makeText(applicationContext, "Please enter your answer!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun allPhrase() {
        dealInput = etInput.text.toString().toUpperCase()
            if (tryPhrase > 0) {
                if (dealInput == mystery) {
                    thePhrase.text = "Phrase: $mystery"
                    guesses.add("You guessed it right!")
                    newScore()
                    customAlert()
                } else {
                    guesses.add("Wrong! Try guessing a letter!")
                    etInput.hint = "Guess a letter"
                }
            } else if (tryPhrase == 0) {
                guesses.add("Oh no! You lost! The answer is $mystery.")
                customAlert()
            }
            rvPhrase.adapter?.notifyDataSetChanged()
            etInput.text.clear()
            etInput.clearFocus()
    }
    fun allLetters(){
        dealInput = etInput.text.toString().toUpperCase()
        var aLetter = dealInput[0]
        var countLetter = 0
        if(tryPhrase > 0){
            for(i in mystery.indices){
                if (aLetter == mystery[i]){
                    guessAnswer[i] = aLetter
                    countLetter++
                }
            }
            lettersOut.add(aLetter)
            lettersG.text = "Guessed Letters: " + lettersOut.toString()
            if (countLetter != 0){
                guesses.add("$countLetter $aLetter found")
            }
            else{
            tryPhrase--
            guesses.add("No $aLetter found! Try again!")
            guesses.add("You have $tryPhrase chances left!")
        }
        }
        else if (tryPhrase == 0) {
            guesses.add("Oh no! You lost! The answer is $mystery.")
            customAlert()
        }
        thePhrase.text = "Phrase: ${String(guessAnswer)}"
        rvPhrase.adapter?.notifyDataSetChanged()
        etInput.text.clear()
        etInput.clearFocus()
        etInput.hint = "Guess the phrase"
    }
    private fun customAlert() {

        val builder1 = AlertDialog.Builder(this)

        // set message of alert dialog
        builder1.setMessage("Do you want to play again?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val wish = builder1.create()
        wish.setTitle("New Game")
        wish.show()
    }
    private fun newScore(){
        myScore = 0 + tryPhrase
        if(myScore >= theHighestScore){
            theHighestScore = myScore
            with(sharedPreferences.edit()) {
                putInt("HighestScore", theHighestScore)
                apply()
            }
            Snackbar.make(clForScore, "BROKE SCORE RECORDS!", Snackbar.LENGTH_LONG).show()
        }
    }
}