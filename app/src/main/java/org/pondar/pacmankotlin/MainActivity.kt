package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.animation.ObjectAnimator
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnClickListener {

    //timer for the moves
    private var pacTimer: Timer = Timer()
    var countMove = 0

    //timer for the countdown
    private var countDownTimer: Timer = Timer()
    var countDown = 60


    //reference to the game class.
    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        play.setOnClickListener(this)
        pause.setOnClickListener(this)
        reset.setOnClickListener(this)


        //progressbar
        progressBar.max = 1000
        val currentProgress = 600

        ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
                .setDuration(60000)
                .start()



        game?.running = true

        //timer 1
        pacTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethodpacMoves()
            }

        }, 0, 200) //0 indicates we start now, 200
        //is the number of miliseconds between each call


        //timer 2
        countDownTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethodCount()
            }
        }, 0, 1000)


        game = Game(this,pointsView)

        //intialize the game view class and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        moveUp.setOnClickListener {
            game?.direction = 0
        }

        moveRight.setOnClickListener {
            game?.direction = 1
        }

        moveDown.setOnClickListener {
            game?.direction = 2
        }

        moveLeft.setOnClickListener {
            game?.direction = 3
        }

    }

    override fun onStop() {
        super.onStop()
        pacTimer.cancel()
    }


    private fun timerMethodpacMoves() {
        this.runOnUiThread(timerTickMoves)
    }

    private fun timerMethodCount() {
        this.runOnUiThread(timerTickCountDown)
    }

    private val timerTickMoves = Runnable {

        if (game?.running == true) {
            countMove++


            if (game?.direction==0)
            { // move up
                game?.movePacmanUp(20)
                game?.moveEnemyDown(20)

            }
            else if (game?.direction==1)
            { // move right
                game?.movePacmanRight(20)
                game?.moveEnemyLeft(20)

            }
            else if (game?.direction==2)
            { // move down
                game?.movePacmanDown(20)
                game?.moveEnemyUp(20)
            }
            else if (game?.direction==3)
            { // move left
                game?.movePacmanLeft(20)
                game?.moveEnemyRight(20)
            }
        }
    }

    //if anything is pressed - we do the checks here
    override fun onClick(v: View) {
        if (v.id == R.id.play) {
            game?.running = true
        } else if (v.id == R.id.pause) {
            game?.running = false
        } else if (v.id == R.id.reset) {
            countMove = 0
            countDown = 60
            game?.newGame() //you should call the newGame method instead of this
            game?.running = false
            //Here we removed the counter for moves

        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.running = false
            countMove = 0
            countDown = 60
            game?.newGame()
            // //Here we removed the counter for moves
            // But it would look like this: Textview.text = getString(R.string.timerValue,countMove)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val timerTickCountDown = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game?.running == true) {
            countDown--
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            timerForCountDown.text = getString(R.string.timerValue, countDown)


            if (countDown <= 0) {
                Toast.makeText(this, "So sad, game over!!", Toast.LENGTH_SHORT).show()
                game?.running = false
                game?.newGame()
                countDown = 60
            }
        }
    }
}
