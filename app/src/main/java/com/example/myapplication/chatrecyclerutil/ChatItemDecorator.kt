package com.example.myapplication.chatrecyclerutil

import android.graphics.Rect
import android.view.View
import androidx.core.math.MathUtils.clamp
import androidx.recyclerview.widget.RecyclerView

class ChatItemDecorator(offset: Int): RecyclerView.ItemDecoration() {

    private val itemOffset = offset

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        outRect.top = itemOffset * clamp( parent.getChildAdapterPosition(view)-1,0,1 )

        outRect.right = itemOffset * clamp( parent.getChildAdapterPosition(view),0,1 )
        outRect.left = itemOffset * clamp( parent.getChildAdapterPosition(view),0,1 )

        outRect.bottom = 0

        //Log.d("decorator", "outRect.top : ${ outRect.top } --- parent.getChildAdapterPosition(view) : ${ parent.getChildAdapterPosition(view) }")
    }
}