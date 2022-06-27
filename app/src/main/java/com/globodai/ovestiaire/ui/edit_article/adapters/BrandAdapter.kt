package com.globodai.ovestiaire.ui.edit_article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.dtos.BrandDto
import com.globodai.ovestiaire.databinding.RowBrandBinding

class BrandAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "BrandAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<BrandDto>() {

        override fun areItemsTheSame(oldItem: BrandDto, newItem: BrandDto): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: BrandDto, newItem: BrandDto): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BrandVH(
            RowBrandBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BrandVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(brands: List<BrandDto>?) {
        val newList = brands?.toMutableList()
        differ.submitList(newList)
    }

    class BrandVH
    constructor(
        private val binding: RowBrandBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: BrandDto) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, brand)
            }

            binding.txtName.text = brand.name
            binding.row.isChecked = brand.isSelected
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, brand: BrandDto)
    }
}