package com.globodai.ovestiaire.ui.edit_article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.dtos.SizeDto
import com.globodai.ovestiaire.databinding.RowSizeBinding

class SizeAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SizeAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<SizeDto>() {

        override fun areItemsTheSame(oldItem: SizeDto, newItem: SizeDto): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SizeDto, newItem: SizeDto): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SizeVH(
            RowSizeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SizeVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(sizes: List<SizeDto>?) {
        val newList = sizes?.toMutableList()
        differ.submitList(newList)
    }

    class SizeVH
    constructor(
        private val binding: RowSizeBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(size: SizeDto) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, size)
            }

            binding.txtName.text = size.name
            binding.row.isChecked = size.isSelected
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: SizeDto)

    }
}