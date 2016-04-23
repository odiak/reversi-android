package net.odiak.reversi

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.view.View

class CellView : View {
    var state: CellState = CellState.EMPTY

    private val paint = Paint()
    private val bgPaint = Paint()

    private var viewHeight = 0
    private var viewWidth = 0

    private val whiteColor: Int
    private val blackColor: Int
    private val movableColor: Int

    constructor(context: Context) : super(context) {
        bgPaint.color = ContextCompat.getColor(context, R.color.boardBackground)
        bgPaint.style = Paint.Style.FILL

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        whiteColor = ContextCompat.getColor(context, R.color.discWhite)
        blackColor = ContextCompat.getColor(context, R.color.discBlack)
        movableColor = ContextCompat.getColor(context, R.color.movableMark)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        setMeasuredDimension(width, width)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        val width = viewWidth
        val height = viewHeight

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        if (state == CellState.EMPTY) return

        paint.style = Paint.Style.FILL
        paint.color = android.graphics.Color.BLACK
        val cx = width / 2f
        val cy = height / 2f
        val r: Float

        when (state) {
            CellState.BLACK -> {
                paint.color = blackColor
                r = width * 0.4f
            }
            CellState.WHITE -> {
                paint.color = whiteColor
                r = width * 0.4f
            }
            CellState.MOVABLE -> {
                paint.color = movableColor
                r = width * 0.2f
            }
            else -> return
        }
        canvas.drawCircle(cx, cy, r, paint)
    }
}

enum class CellState {
    BLACK,
    WHITE,
    EMPTY,
    MOVABLE
}