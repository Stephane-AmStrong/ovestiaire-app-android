package com.globodai.ovestiaire.ui.edit_article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globodai.ovestiaire.data.models.Equipment
import com.globodai.ovestiaire.databinding.RowEquipmentBinding
import com.globodai.ovestiaire.utils.visible

class EquipmentAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "EquipmentAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<Equipment>() {

        override fun areItemsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EquipmentVH(
            RowEquipmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EquipmentVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(equipments: List<Equipment>?) {
        val newList = equipments?.toMutableList()
        differ.submitList(newList)
    }

    class EquipmentVH
    constructor(
        private val binding: RowEquipmentBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(equipment: Equipment) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, equipment)
            }

            if (equipment.name.count()==1){
//                txtName.setTypeface(null, Typeface.BOLD)
                binding.checkCategory.visible(false)
                binding.txtName.text = equipment.name
            }else{
                binding.checkCategory.text = equipment.name
                binding.row.isChecked = equipment.isSelected
                binding.txtName.visible(false)
            }
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Equipment)

    }
}