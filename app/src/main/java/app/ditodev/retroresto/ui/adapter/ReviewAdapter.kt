package app.ditodev.retroresto.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.ditodev.retroresto.data.api.response.CustomerReviewsItem
import app.ditodev.retroresto.databinding.ItemReviewBinding
import app.ditodev.retroresto.util.Utils

class ReviewAdapter :
    ListAdapter<CustomerReviewsItem, ReviewAdapter.ViewHolder>(Utils.DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(review: CustomerReviewsItem) {
            binding.tvItem.text = "${review.review}\n- ${review.name}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
}