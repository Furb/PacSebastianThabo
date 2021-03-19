package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import android.widget.Toast
import java.util.*


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView) {

        private var pointsView: TextView = view
        private var points = 0

        //bitmaps
        var pacBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        var coinBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.coin)
        var enemyBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)

        var pacx  = 0
        var pacy = 0




        //did we initialize the coins and enemies?
        var coinsInitialized = false
        var enemyInitialized = false

        //the list of goldcoins and enemies- initially empty
        var coins = ArrayList<GoldCoin>()
        var enemy = ArrayList<Enemy>()

        //a reference to the gameview
        private var gameView: GameView? = null
        private var h = 0
        private var w = 0 //height and width of screen

        var countMove = 0
        var countDown = 60
        var running = false
        var direction = 0


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.


    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //Coins
    fun initializeGoldcoins()
    {
        var minX = 0
        var maxX = w - coinBitmap.width

        var minY = 0
        var maxY = h - coinBitmap.height

        var random = Random()

        for (i in 0..1) {
            val randomX = random.nextInt( maxX - minX + 1) + minX
            val randomY = random.nextInt( maxY - minY + 1) + minY
            coins.add(GoldCoin(randomX, randomY, false))
        }



        coinsInitialized = true
    }

    //enemies
    fun initializeEnemy (){
        var minX: Int = 0
        var maxX: Int = w - enemyBitmap.width
        var minY: Int = 0
        var maxY: Int = h - enemyBitmap.height

        val random = Random()

        for (i in 0..2) {
            var randomX: Int = random.nextInt(maxX- minX +1) + minX
            var randomY: Int = random.nextInt(maxY - minY +1) + minY
            enemy.add(Enemy(randomX, randomY, false))
        }

        enemyInitialized = true
    }

    fun newGame() {
        pacx = 75
        pacy = 300 //just some starting coordinates - you can change this.

        coinsInitialized = false
        coins.clear()

        enemyInitialized = false
        enemy.clear()


        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"

        gameView?.invalidate() //redraw screen

        countMove = 0
        countDown = 60

        running= false

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }


    fun movePacmanRight(pixels: Int) {

        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanLeft(pixels: Int) {
        //still within our boundaries?
        if (pacx > 0) {
            pacx = pacx - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanUp(pixels: Int) {
        //still within our boundaries?
        if (pacy > 0) {
            pacy = pacy - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanDown(pixels: Int) {
        if (pacy + pixels + pacBitmap.height < h) {
            pacy = pacy + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun moveEnemyRight(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.x + pixels + enemyBitmap.width < w) {
                enemy.x = enemy.x + pixels
            }
        }
        doCollisionCheckEnemy()
        gameView!!.invalidate()
    }

    fun moveEnemyLeft(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.x > 0) {
                enemy.x = enemy.x - pixels
            }
        }
        doCollisionCheckEnemy()
        gameView!!.invalidate()
    }


    fun moveEnemyUp(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.y > 0) {
                enemy.y = enemy.y - pixels
            }
        }
        doCollisionCheckEnemy()
        gameView!!.invalidate()
    }


    fun moveEnemyDown(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.y + pixels + enemyBitmap.height < h) {
                enemy.y = enemy.y + pixels
            }
        }
        doCollisionCheckEnemy()
        gameView!!.invalidate()
    }


    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {

        for (coin in coins) {
            if (pacx + pacBitmap.width >= coin.coinX && pacx <= coin.coinX + coinBitmap.width && pacy + pacBitmap.height >= coin.coinY && pacy <= coin.coinY + coinBitmap.height && !coin.taken) {

                Toast.makeText(this.context, "Kachiing", Toast.LENGTH_SHORT).show()
                coin.taken = true
                points += 1
                pointsView.text = "${context.resources.getString(R.string.points)} $points"
            }

            if (points == coins.size || coin.taken == null) {
                return newGame()
            }
        }
    }
    fun doCollisionCheckEnemy() {

        var pacDefeated = false

        for (enemy in enemy) {
            if (pacx + pacBitmap.width >= enemy.x && pacx <= enemy.x + enemyBitmap.width && pacy + pacBitmap.height >= enemy.y && pacy <= enemy.y + enemyBitmap.height && !enemy.alive) {
                Toast.makeText(this.context, "Game over, You Lost!", Toast.LENGTH_SHORT).show()
                enemy.alive = true
                pacDefeated = true

            }
        }

        if (pacDefeated == true) {
            return newGame()
        }
    }
}