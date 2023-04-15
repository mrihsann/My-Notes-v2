package com.ihsanarslan.mynotes.swipe

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import com.ihsanarslan.mynotes.adapter.TrashAdapter
import com.ihsanarslan.mynotes.noteTrashList
import com.ihsanarslan.mynotes.viewmodel.SwipeViewModel


class SwipeToUndoCallback(val view: View, private val adapter: TrashAdapter, private var context: Context,val owner: ViewModelStoreOwner) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private lateinit var viewModel: SwipeViewModel

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(owner)[SwipeViewModel::class.java]
        val currentitem = noteTrashList[viewHolder.adapterPosition]
        when (direction){
            ItemTouchHelper.RIGHT -> { // sağa kaydırma kurtar
                viewModel.saveNote(currentitem)
                noteTrashList.remove(currentitem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Geri alma işlemi için kullanıcıya bir mesaj gösterme
                Snackbar.make(
                    viewHolder.itemView,
                    R.string.note_saved,
                    Snackbar.LENGTH_LONG
                )
                    .setTextColor(Color.GREEN)
                    .setActionTextColor(Color.WHITE)
                    .show()
            }

            ItemTouchHelper.LEFT -> {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.warning)
                builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
                builder.setMessage(R.string.delete_note)
                    .setCancelable(false) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                    .setPositiveButton(R.string.yes) { dialog, id ->
                        viewModel.delTrashNote(currentitem)
                        noteTrashList.remove(currentitem)
                        adapter.notifyItemRemoved(viewHolder.adapterPosition)
                        // Geri alma işlemi için kullanıcıya bir mesaj gösterme
                        Snackbar.make(
                            viewHolder.itemView,
                            R.string.deleted_note,
                            Snackbar.LENGTH_LONG
                        )
                            .setTextColor(Color.RED)
                            .show()
                    }
                    .setNegativeButton(R.string.no) { dialog, id ->
                        // Hayır'a tıklandığında yapılacak işlemler buraya yazılır
                        dialog.cancel() // Uyarı mesajını kapatır
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                val alert = builder.create()
                alert.show()
                // Evet düğmesinin yazı rengini kırmızı olarak ayarla
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED)

                // Hayır düğmesinin yazı rengini yeşil olarak ayarla
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#2C9430"))

                }
            }
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
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        if (dX > 0) { // sağa kaydırıldı
            val background = GradientDrawable()
            background.setBounds(
                itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom
            )
            background.shape = GradientDrawable.RECTANGLE
            background.cornerRadius = 30f
            background.setColor(Color.GREEN)
            background.draw(c)

            val icon = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.ic_baseline_recycling_24
            )!!
            val iconMargin = (itemHeight - icon.intrinsicHeight) / 2
            val iconTop = itemView.top + iconMargin
            val iconBottom = iconTop + icon.intrinsicHeight
            val iconLeft = itemView.left + iconMargin
            val iconRight = iconLeft + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(c)

        } else { // sola kaydırıldı
            val background = GradientDrawable()
            background.setBounds(
                itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom
            )
            background.shape = GradientDrawable.RECTANGLE
            background.cornerRadius = 30f
            background.setColor(Color.RED)
            background.draw(c)
            background.draw(c)

            val icon = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.ic_baseline_delete_forever_24
            )!!
            val iconMargin = (itemHeight - icon.intrinsicHeight) / 2
            val iconTop = itemView.top + iconMargin
            val iconBottom = iconTop + icon.intrinsicHeight
            val iconRight = itemView.right - iconMargin
            val iconLeft = iconRight - icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}
