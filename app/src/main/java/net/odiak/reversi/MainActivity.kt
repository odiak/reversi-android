package net.odiak.reversi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.GridView

class MainActivity : AppCompatActivity() {

    val game = Game()
    var adapter = BoardAdapter(this, game.size)
    val myColor = Color.BLACK

    var inputReady = false

    var gridViewOpt: GridView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridViewOpt = findViewById(R.id.gridView) as GridView

        val gridView = requireNotNull(gridViewOpt)

        gridView.numColumns = game.size

        gridView.adapter = adapter

        startGame()

        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            onClick(i)
        }
    }

    private fun onClick(i: Int) {
        game.move(Point(i % game.size, i / game.size))
        applyBoardToView()
    }

    private fun startGame() {
        game.init()
        applyBoardToView()
    }

    private fun applyBoardToView() {
        val gridView = requireNotNull(this.gridViewOpt)

        game.board.forEach { x, y, color ->
            when (color) {
                Color.WHITE -> adapter.setState(x, y, CellState.WHITE)
                Color.BLACK -> adapter.setState(x, y, CellState.BLACK)
                else -> {
                    if (game.isMovableTo(x, y)) {
                        adapter.setState(x, y, CellState.MOVABLE)
                    } else {
                        adapter.setState(x, y, CellState.EMPTY)
                    }
                }
            }
        }
        gridView.requestLayout()
    }
}