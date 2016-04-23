package net.odiak.reversi

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


class BoardAdapter(val context: Context, private val size: Int) : BaseAdapter() {

    private val count: Int = size * size
    private val views: Array<CellView?> = Array(count) { null }

    override fun getCount() = count

    fun getView(position: Int): CellView {
        val view = views[position]
        if (view != null) return view

        val newView = CellView(context)
        views[position] = newView
        return newView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) = getView(position)

    override fun getItem(p0: Int): Any? = null

    override fun getItemId(p0: Int) = p0.toLong()

    fun setState(x: Int, y: Int, cellState: CellState) {
        getView(x + y * size).state = cellState
    }
}