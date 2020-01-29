package com.example.ratitovec

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.datumi_list_row.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DatumiListAdapter(val ctx: Context) :
    PagedListAdapter<Pohod, DatumiListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Pohod>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldConcert: Pohod,
                newConcert: Pohod
            ) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(
                oldConcert: Pohod,
                newConcert: Pohod
            ) = oldConcert == newConcert
        }
    }

    interface ItemListeners {

        fun onItemLongClick(v: View, pos: Int)
        fun onItemClick(v: View, pos: Int)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener,
        View.OnClickListener {
        override fun onClick(v: View?) {
            listeners.onItemClick(v!!, layoutPosition)
        }

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        fun setClickListener(itemClickListener: ItemListeners) {
            this.listeners = itemClickListener
        }

        override fun onLongClick(v: View): Boolean {
            this.listeners.onItemLongClick(v, layoutPosition)
            return false
        }

        lateinit var listeners: ItemListeners
        val DatumView = view.DatumiListDatum
        val CounterView = view.DatumiListCounter
        val yearDivider = view.DatumiListYearDivider
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DatumiListAdapter.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.datumi_list_row, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val pohod = getItem(position)
        val MainViewModel =
            ViewModelProviders.of(ctx as FragmentActivity).get(MainActivityViewModel::class.java)
        holder.DatumView.text = SimpleDateFormat.getDateTimeInstance().format(Date(pohod!!.Datum))

        val count = position + 1
        holder.CounterView.text = "$count X"

        val tempFormat = SimpleDateFormat("YYYY")

        if (position < itemCount - 1) {
            val prevpohod = getItem(position + 1)
            val cutyear = tempFormat.format(Date(pohod.Datum))
            val datYear = tempFormat.format(Date(prevpohod!!.Datum))
            if (tempFormat.format(Date(pohod.Datum)) != tempFormat.format(Date(prevpohod.Datum))) {
                holder.yearDivider.visibility = View.VISIBLE
                holder.yearDivider.text = tempFormat.format(Date(getItem(position + 1)!!.Datum))
            }
        }
        holder.setClickListener(object : ItemListeners {
            override fun onItemClick(v: View, pos: Int) {

            }

            override fun onItemLongClick(v: View, pos: Int) {
                val temp = getItem(position)
                MaterialAlertDialogBuilder(holder.CounterView.context)
                    .setTitle("Delete")
                    .setMessage("Ali res zelite izbrisati vpis?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        android.R.string.yes
                    ) { dialog, which ->
                        MainViewModel.delete(temp!!)

                        val snack = Snackbar.make(
                            holder.CounterView,
                            "Pohod izbrisan!",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction("Razveljavi") { MainViewModel.insert(temp) }
                            .setBackgroundTint(
                                ContextCompat.getColor(
                                    ctx,
                                    R.color.colorPrimaryDark
                                )
                            )
                        snack.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                        snack.show()
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .show()
            }
        })
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id.toLong()
    }
}