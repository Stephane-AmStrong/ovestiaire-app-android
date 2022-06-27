package com.globodai.ovestiaire.ui.edit_article.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.Colour
import com.globodai.ovestiaire.databinding.RowColourBinding

class ColourAdapter1(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "ColourAdapter1"
    private val diffCallback = object : DiffUtil.ItemCallback<Colour>() {

        override fun areItemsTheSame(oldItem: Colour, newItem: Colour): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Colour, newItem: Colour): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    /*
    private val differ =
        AsyncListDiffer(
            ColourRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(diffCallback).build()
        )

    internal inner class ColourRecyclerChangeCallback(
        private val adapter: ColourAdapter1
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }
*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RowColourBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ColourVH(binding, interaction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ColourVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Colour>) {
        for (colour in list) Log.d(TAG, "submitList: ${colour.name} ${colour.isSelected}")

        differ.submitList(list)
    }

    class ColourVH
    constructor(
        val binding: RowColourBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Colour) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            val unwrappedDrawable = AppCompatResources.getDrawable(
                context,
                com.globodai.ovestiaire.R.drawable.ic_baseline_brightness_1_24
            )

            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(item.codeHexa))

            binding.imgColor.setImageDrawable(wrappedDrawable)
            binding.txtName.text = item.name
            binding.row.isChecked = item.isSelected
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, colour: Colour)
    }
}