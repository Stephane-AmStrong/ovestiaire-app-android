package com.globodai.ovestiaire.ui.edit_article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.dtos.MaterialDto
import com.globodai.ovestiaire.databinding.RowSizeBinding

class MaterialAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MaterialAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<MaterialDto>() {

        override fun areItemsTheSame(oldItem: MaterialDto, newItem: MaterialDto): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MaterialDto, newItem: MaterialDto): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MaterialVH(
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
            is MaterialVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(materials: List<MaterialDto>?) {
        val newList = materials?.toMutableList()
        differ.submitList(newList)
    }

    class MaterialVH
    constructor(
        private val binding: RowSizeBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(material: MaterialDto) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, material)
            }

            binding.txtName.text = material.name
            binding.row.isChecked = material.isSelected
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, material: MaterialDto)

    }
}