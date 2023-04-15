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
import com.ihsanarslan.mynotes.adapter.firebaseNotesAdapter
import com.ihsanarslan.mynotes.firebaseNotes
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.viewmodel.CloudViewModel
import com.ihsanarslan.mynotes.viewmodel.SwipeViewModel

class SwipeToCloudCallback(val view: View,private var context: Context,private val adapter: firebaseNotesAdapter,val owner: ViewModelStoreOwner) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private lateinit var viewModel: SwipeViewModel

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(owner)[SwipeViewModel::class.java]
        val viewModel2 = ViewModelProvider(owner)[CloudViewModel::class.java]

        val currentitem = firebaseNotes[viewHolder.adapterPosition]
        if (direction == ItemTouchHelper.RIGHT) {
            val noteDB= NoteDB(
                0,
                currentitem.title,
                currentitem.content,
                currentitem.color.toInt(),
                currentitem.liked)
            // notların koleksiyonundan başlık ve içerik özelliklerinin her ikisi de yeni notla eşleşenleri sorgula
            viewModel.downloadNote(currentitem,noteDB)
            viewModel2.getAllUserNote()
            Snackbar.make(
                viewHolder.itemView,
                R.string.downloaded_note,
                Snackbar.LENGTH_LONG
            ).setTextColor(Color.RED).show()

        }
        else if (direction == ItemTouchHelper.LEFT) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.warning)
            builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
            builder.setMessage(R.string.cloud_note_content)
                .setCancelable(false) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                .setPositiveButton(R.string.yes) { dialog, id ->
                    viewModel.deleteNoteByUserCloud(currentitem.title,currentitem.color, currentitem.liked, viewHolder)

                }
                .setNegativeButton(R.string.no) { dialog, id ->
                    // Hayır'a tıklandığında yapılacak işlemler buraya yazılır
                    viewModel2.getAllUserNote()
                    dialog.cancel() // Uyarı mesajını kapatır

                }
            val alert = builder.create()
            alert.show()
            // Evet düğmesinin yazı rengini kırmızı olarak ayarla
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED)

            // Hayır düğmesinin yazı rengini yeşil olarak ayarla
            alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#2C9430"))
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
        val itemView2 = viewHolder.itemView

        //sağa kaydırma
        val icon = ContextCompat.getDrawable(context, R.drawable.baseline_cloud_download_24)
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.cornerRadius = 30f
        background.setColor(Color.parseColor("#2196F3"))

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        //sola kaydırma işlemi
        val icon2 = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_forever_24)
        val background2 = GradientDrawable()
        background2.shape = GradientDrawable.RECTANGLE
        background2.cornerRadius = 30f
        background2.setColor(Color.parseColor("#FF0000"))

        val iconMargin2 = (itemView2.height - icon2!!.intrinsicHeight) / 2
        val iconTop2 = itemView2.top + (itemView2.height - icon2.intrinsicHeight) / 2
        val iconBottom2 = iconTop2 + icon2.intrinsicHeight

        if (dX > 0) { // Swipe right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
        } else { // Swipe left
            val iconLeft = itemView2.right - iconMargin2 - icon2.intrinsicWidth
            val iconRight = itemView2.right - iconMargin2
            icon2.setBounds(iconLeft, iconTop2, iconRight, iconBottom2)
            background2.setBounds(itemView2.right + dX.toInt(), itemView2.top, itemView2.right, itemView2.bottom)
        }

        background.draw(c)
        icon.draw(c)

        background2.draw(c)
        icon2.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}