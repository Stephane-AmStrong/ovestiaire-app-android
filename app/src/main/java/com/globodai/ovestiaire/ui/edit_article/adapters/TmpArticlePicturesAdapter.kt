package com.globodai.ovestiaire.ui.edit_article.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.globodai.ovestiaire.databinding.RowTmpUriBinding

class TmpArticlePicturesAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "UriAdapter"

    private val diffCallback = object : DiffUtil.ItemCallback<Uri>() {

        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UriVH(
            RowTmpUriBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UriVH -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(uris: List<Uri>?) {
        val newList = uris?.toMutableList()
        differ.submitList(newList)
    }

    class UriVH
    constructor(
        private val binding: RowTmpUriBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri) = with(itemView) {
            binding.btnClose.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, uri)
            }

            Glide.with(context)
                .load(uri)
                .into(binding.imgArticle)
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, uri: Uri)

    }
}