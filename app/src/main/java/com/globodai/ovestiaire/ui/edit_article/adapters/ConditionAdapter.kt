package com.globodai.ovestiaire.ui.edit_article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.Condition
import com.globodai.ovestiaire.databinding.RowConditionBinding

class ConditionAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "ConditionAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<Condition>() {

        override fun areItemsTheSame(oldItem: Condition, newItem: Condition): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Condition, newItem: Condition): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConditionVH(
            RowConditionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConditionVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(conditions: List<Condition>?) {
        val newList = conditions?.toMutableList()
        differ.submitList(newList)
    }

    class ConditionVH
    constructor(
        private val binding: RowConditionBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(condition: Condition) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, condition)
            }

            binding.txtTitle.text = condition.title
            binding.txtSubtitle.text = condition.subtitle
            binding.row.isChecked = condition.isSelected
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, condition: Condition)
    }
}