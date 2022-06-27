package com.globodai.ovestiaire.ui.edit_article.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.Colour
import com.globodai.ovestiaire.databinding.RowColourBinding

class ColourAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "ColourAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<Colour>() {

        override fun areItemsTheSame(oldItem: Colour, newItem: Colour): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Colour, newItem: Colour): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColourVH(
            RowColourBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
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

    fun submitList(colours: List<Colour>?) {
        val newList = colours?.toMutableList()
        differ.submitList(newList)
    }

    class ColourVH
    constructor(
        private val binding: RowColourBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(colour: Colour) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, colour)
            }

            val unwrappedDrawable = AppCompatResources.getDrawable(
                context,
                com.globodai.ovestiaire.R.drawable.ic_baseline_brightness_1_24
            )

            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(colour.codeHexa))

            binding.imgColor.setImageDrawable(wrappedDrawable)
            binding.txtName.text = colour.name
            binding.row.isChecked = colour.isSelected
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Colour)

    }
}