package com.globodai.ovestiaire.ui.edit_article.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.models.Gender

class CustomDropDownAdapter(val context: Context, var dataSource: List<Gender>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_gender_auto_complete, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }

        vh.label.text = dataSource[position].name

        //val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
        //vh.img.setBackgroundResource(id)

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        //val img: ImageView

        init {
            label = row?.findViewById(R.id.txt_name) as TextView
            //img = row?.findViewById(R.id.img) as ImageView
        }
    }

}