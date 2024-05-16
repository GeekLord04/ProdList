package com.geekster.prodlist.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.geekster.getinfo.models.ListResponseItem
import com.geekster.prodlist.R
import com.geekster.prodlist.databinding.ListItemBinding

class ItemAdapter(private val ItemList : List<ListResponseItem>) : ListAdapter<ListResponseItem, ItemAdapter.ListViewHolder>(ComparatorDiffUtil())
{

    private var originalList: List<ListResponseItem> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ListViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    fun setOriginalList(list: List<ListResponseItem>) {
        originalList = list
        submitList(list)
    }
    fun filter(query: String?) {
        if (query.isNullOrEmpty()) {
            submitList(originalList)
        } else {
            val filteredList = originalList.filter { it.product_name.contains(query, true) }
            submitList(filteredList)
        }
    }

    inner class ListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListResponseItem) {
            binding.prodName.text = item.product_name
            binding.prodType.text = item.product_type
            binding.prodTax.text = "+${item.tax}"
            binding.prodPrice.text = item.price.toString()
            binding.image.load(item.image) {
                transformations(RoundedCornersTransformation(16f)) //radius of rounded corners
                placeholder(R.drawable.loading) // Placeholder image while loading
                error(R.drawable.product) // Error image if loading fails
            }
        }

    }



    class ComparatorDiffUtil : DiffUtil.ItemCallback<ListResponseItem>() {
        override fun areItemsTheSame(oldItem: ListResponseItem, newItem: ListResponseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ListResponseItem, newItem: ListResponseItem): Boolean {
            return oldItem == newItem
        }
    }

}