package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.tictactoe.databinding.ActivityGameScreenBinding

class GameScreen : AppCompatActivity() {
    // Enum to REPRESENT THE CURRENT PLAYER'S TURN
    enum class Turn{
        CROSS,
        CIRCLE
    }

    private var firstTurn = Turn.CROSS
    private var secondTurn = Turn.CROSS

    private var crossesScore = 0
    private var circlesScore = 0

    private var crossesHighestScore = 0
    private var circlesHighestScore = 0

    private var roundsPlayed = 0



    private var boardList = mutableListOf<Button>()

    private lateinit var binding: ActivityGameScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard() // Initialize the game board
        loadHighScores() // Load high scores from shared preferences

        //Back Button on GameScreen
        val back_btn1 = findViewById<Button>(R.id.back_btn1)
        back_btn1.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    // Initialize the buttons on the board
    private fun initBoard() {
        boardList.add(binding.box1)
        boardList.add(binding.box2)
        boardList.add(binding.box3)
        boardList.add(binding.box4)
        boardList.add(binding.box5)
        boardList.add(binding.box6)
        boardList.add(binding.box7)
        boardList.add(binding.box8)
        boardList.add(binding.box9)
    }

    // Load high scores from shared preferences
    private fun loadHighScores() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        crossesHighestScore = sharedPreferences.getInt(HIGHEST_SCORE_CROSS, 0)
        circlesHighestScore = sharedPreferences.getInt(HIGHEST_SCORE_CIRCLE, 0)
    }

    fun boardTapped(view: View){
        if (view !is Button)
            return
        addToBoard(view)

        if (checkForVictory(Turn.CIRCLE)){
            circlesScore++
            result("CIRCLES WIN !")
        }
        else if (checkForVictory(Turn.CROSS)){
            crossesScore++
            result("CROSSES WIN !")
        }

        if (fullBoard()){
            result("DRAW")
        }
    }

    // Check for a victory condition
    private fun checkForVictory(s: Turn): Boolean
    {
        //Horizontal Victory
        if (match(binding.box1,s) && match(binding.box2,s) && match(binding.box3,s)) {
            drawRedLine(binding.box1, binding.box2, binding.box3)
            return true
        }
        if (match(binding.box4,s) && match(binding.box5,s) && match(binding.box6,s)) {
            drawRedLine(binding.box4, binding.box5, binding.box6)
            return true
        }
        if (match(binding.box7,s) && match(binding.box8,s) && match(binding.box9,s)) {
            drawRedLine(binding.box7, binding.box8, binding.box9)
            return true
        }

        //Vertical Victory
        if (match(binding.box1,s) && match(binding.box4,s) && match(binding.box7,s)) {
            drawRedLine(binding.box1, binding.box4, binding.box7)
            return true
        }
        if (match(binding.box2,s) && match(binding.box5,s) && match(binding.box8,s)) {
            drawRedLine(binding.box2, binding.box5, binding.box8)
            return true
        }
        if (match(binding.box3,s) && match(binding.box6,s) && match(binding.box9,s)) {
            drawRedLine(binding.box3, binding.box6, binding.box9)
            return true
        }

        //Diagonal Victory
        if (match(binding.box1,s) && match(binding.box5,s) && match(binding.box9,s)) {
            drawRedLine(binding.box1, binding.box5, binding.box9)
            return true
        }
        else if (match(binding.box3,s) && match(binding.box5,s) && match(binding.box7,s)) {
            drawRedLine(binding.box3, binding.box5, binding.box7)
            return true
        }

        return false
    }

    //Drawing a RED LINE
    private fun drawRedLine(vararg buttons: Button) {
        for (button in buttons){
            val params = button.layoutParams as ViewGroup.MarginLayoutParams
            val drawable = ContextCompat.getDrawable(this, R.drawable.red_line)
            params.setMargins(0,0,0,0,) //Adjust the margin
            button.background = drawable
        }

        //reset the buttons after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            for (button in buttons){
                button.background = null
            }
        }, 1500) //resting time

    }

    // Checking the turn matching to the SYMBOL
    private fun match(button: Button, symbol: Turn): Boolean {
        return when (symbol) {
            Turn.CROSS -> button.text == CROSS
            Turn.CIRCLE -> button.text == CIRCLE
        }
    }

    // Display the result
    private fun result(title: String) {
        roundsPlayed++
        updateHighestScores()

        val message = "Circles $circlesScore\nCrosses $crossesScore"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Play Again")
            {_,_ ->
                resetBoard()
            }
            .setOnDismissListener {  }    //dismiss the red line
            .setCancelable(false)
            .show()

    }

    // Reset the board
    private fun resetBoard() {
        for (button in boardList)
        {
            button.text = ""
        }

        if (firstTurn == Turn.CIRCLE)
            firstTurn = Turn.CROSS
        else if (firstTurn == Turn.CROSS)
            firstTurn = Turn.CIRCLE

        secondTurn = firstTurn
        setTurnLabel()
    }

    // Check if board is full
    private fun fullBoard(): Boolean {
        for (button in boardList)
        {
            if (button.text == "")
                return false
        }
        return true
    }

    //adding moves to the board
    private fun addToBoard(button: Button) {
        if (button.text != "")
            return

        if (secondTurn == Turn.CIRCLE){
            button.text = CIRCLE
            secondTurn = Turn.CROSS
        }

        else if (secondTurn == Turn.CROSS){
            button.text = CROSS
            secondTurn = Turn.CIRCLE
        }

        setTurnLabel()
    }

    // Update the turn
    private fun setTurnLabel() {
        var turnText = ""
        if (secondTurn == Turn.CROSS)
            turnText = "Turn X"
        else if (secondTurn == Turn.CIRCLE)
            turnText = "Turn O"

        binding.turnTV.text = turnText
    }

    // Update highest scores
    private fun updateHighestScores() {

        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Update highest scores
        if (roundsPlayed % 10 == 0) {
            if (crossesScore > crossesHighestScore) {
                crossesHighestScore = crossesScore
                editor.putInt(HIGHEST_SCORE_CROSS, crossesHighestScore)
            }
            if (circlesScore > circlesHighestScore) {
                circlesHighestScore = circlesScore
                editor.putInt(HIGHEST_SCORE_CIRCLE, circlesHighestScore)
            }
            editor.apply()

            AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Rounds Won:\nCrosses: $crossesScore\nCircles: $circlesScore\n\nTotal Rounds:$roundsPlayed\n\nHighest Scores:\nCircles: $circlesHighestScore\nCrosses: $crossesHighestScore")
                .setPositiveButton("Back To Home") {_, _ ->
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setCancelable(false)
                .show()
        }
    }

    companion object{
        // Constants
        const val CROSS = "X"
        const val CIRCLE = "O"

        // Constants for shared preferences
        const val PREFS_NAME = "TicTacToePrefs"
        const val HIGHEST_SCORE_CROSS = "highestScoreCross"
        const val HIGHEST_SCORE_CIRCLE = "highestScoreCircle"
    }
}