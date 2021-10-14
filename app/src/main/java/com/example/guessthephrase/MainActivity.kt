package com.example.guessthephrase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    //declare
    lateinit var thePhrase: TextView
    lateinit var lettersG: TextView
    lateinit var rvPhrase: RecyclerView
    lateinit var guesses: ArrayList<String>
    lateinit var etInput: EditText
    lateinit var buttonGuess: Button

    val mystery = "a piece of cake"
    var guessAnswer = CharArray(mystery.length)
    var lettersOut = ArrayList<Char>()
    var tryPhrase = 10
    var dealInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize
        thePhrase = findViewById(R.id.thePhrase)
        lettersG = findViewById(R.id.lettersG)
        etInput = findViewById(R.id.etInput)
        buttonGuess = findViewById(R.id.button)

        rvPhrase = findViewById(R.id.rvPhrase)
        guesses = ArrayList()
        rvPhrase.adapter = RVphrase(guesses)
        rvPhrase.layoutManager = LinearLayoutManager(this)

        //utilize
        for (i in mystery.indices) {
            if (mystery[i] == ' ') {
                guessAnswer[i] = ' '
            } else {
                guessAnswer[i] = '*'
            }
        }
        thePhrase.text = "Phrase: ${String(guessAnswer)}"
        buttonGuess.setOnClickListener {
            dealInput = etInput.text.toString()
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
        dealInput = etInput.text.toString()
            if (tryPhrase > 0) {
                if (dealInput == mystery) {
                    thePhrase.text = "Phrase: $mystery"
                    guesses.add("You guessed it right!")
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
        dealInput = etInput.text.toString()
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
}
