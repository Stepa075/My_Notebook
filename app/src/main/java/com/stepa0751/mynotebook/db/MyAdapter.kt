package com.stepa0751.mynotebook.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stepa0751.mynotebook.EditActivity
import com.stepa0751.mynotebook.R

class MyAdapter(listMain:ArrayList<ListItem>, contextM: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>(){
        var listArray = listMain
        var context = contextM
    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate : TextView = itemView.findViewById(R.id.tvDate)
        val context = contextV

        fun setData(item: ListItem){

            tvTitle.text = item.title
            tvDate.text = item.time

            itemView.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(MyIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.I_DESCRIPTION_KEY, item.description)
                    putExtra(MyIntentConstants.I_ID_KEY, item.id)

                }
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item, parent, false), context)
    }

    override fun getItemCount(): Int {
        return  listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

       holder.setData(listArray[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItems:List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int, dbManager: MyDbManager){
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }

}