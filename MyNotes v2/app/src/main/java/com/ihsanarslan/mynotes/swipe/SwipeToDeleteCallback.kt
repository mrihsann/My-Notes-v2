package com.ihsanarslan.mynotes.swipe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.adapter.NoteAdapter
import com.ihsanarslan.mynotes.noteList
import com.ihsanarslan.mynotes.viewmodel.SwipeViewModel

class SwipeToDeleteCallback(val view: View, private val adapter: NoteAdapter, private var context: Context,val owner: ViewModelStoreOwner) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private lateinit var viewModel: SwipeViewModel

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val currentitem = noteList[viewHolder.adapterPosition]
        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(owner)[SwipeViewModel::class.java]
        viewModel.delNote(currentitem)
        noteList.remove(currentitem)
        adapter.notifyItemRemoved(viewHolder.adapterPosition)
        // Geri alma işlemi için kullanıcıya bir mesaj gösterme
        Snackbar.make(
            viewHolder.itemView,
            R.string.note_deleted,
            Snackbar.LENGTH_LONG
        ).setTextColor(Color.RED)
            .setActionTextColor(Color.WHITE).show()
    }
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        val icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.cornerRadius = 30f
        background.setColor(Color.parseColor("#FF0000"))

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX > 0) { // Swipe right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
        } else { // Swipe left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        }

        background.draw(c)
        icon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}