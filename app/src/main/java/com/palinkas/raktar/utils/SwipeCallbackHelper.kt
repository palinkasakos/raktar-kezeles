package com.palinkas.raktar.utils

import androidx.recyclerview.widget.ItemTouchHelper

fun createSimpleSwipeCallback(
    getSwipeDirs: androidx.recyclerview.widget.RecyclerView.ViewHolder.() -> Int?,
    onSwiped: androidx.recyclerview.widget.RecyclerView.ViewHolder.() -> Unit
): ItemTouchHelper.SimpleCallback =
// http://nemanjakovacevic.net/blog/english/2016/01/12/recyclerview-swipe-to-delete-no-3rd-party-lib-necessary/
    object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END or ItemTouchHelper.START) {
        override fun getSwipeDirs(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        ): Int =
            getSwipeDirs.invoke(viewHolder) ?: super.getSwipeDirs(recyclerView, viewHolder)

        override fun onMove(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
            target: androidx.recyclerview.widget.RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(
            viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
            direction: Int
        ) {
            onSwiped.invoke(viewHolder)
        }
    }