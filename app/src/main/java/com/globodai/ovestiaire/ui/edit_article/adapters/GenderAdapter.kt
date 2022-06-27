package com.globodai.ovestiaire.ui.edit_article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.Gender
import com.globodai.ovestiaire.databinding.RowGenderBinding

class GenderAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "GenderAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<Gender>() {

        override fun areItemsTheSame(oldItem: Gender, newItem: Gender): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Gender, newItem: Gender): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenderVH(
            RowGenderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GenderVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(genders: List<Gender>?) {
        val newList = genders?.toMutableList()
        differ.submitList(newList)
    }

    class GenderVH
    constructor(
        private val binding: RowGenderBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gender: Gender) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, gender)
            }

            binding.txtName.text = gender.name
            binding.row.isChecked = gender.isSelected
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Gender)

    }
}