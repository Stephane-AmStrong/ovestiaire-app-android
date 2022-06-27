package com.globodai.ovestiaire.ui.edit_article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.dtos.SportDto
import com.globodai.ovestiaire.databinding.RowSportBinding
import com.globodai.ovestiaire.utils.visible

class SportAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SportAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<SportDto>() {

        override fun areItemsTheSame(oldItem: SportDto, newItem: SportDto): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SportDto, newItem: SportDto): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SportVH(
            RowSportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SportVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(sports: List<SportDto>?) {
        val newList = sports?.toMutableList()
        differ.submitList(newList)
    }

    class SportVH
    constructor(
        private val binding: RowSportBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sport: SportDto) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, sport)
            }

            if (sport.name.count() == 1) {
//                txtName.setTypeface(null, Typeface.BOLD)
                binding.checkSport.visible(false)
                binding.txtName.text = sport.name
            } else {
                binding.checkSport.text = sport.name
                binding.row.isChecked = sport.isSelected!!
                binding.txtName.visible(false)
            }
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, sport: SportDto)

    }
}