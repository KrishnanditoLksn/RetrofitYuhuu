package app.ditodev.retroresto.util

import androidx.recyclerview.widget.DiffUtil
import app.ditodev.retroresto.data.api.response.CustomerReviewsItem

object Utils {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CustomerReviewsItem>() {
        override fun areItemsTheSame(
            oldItem: CustomerReviewsItem,
            newItem: CustomerReviewsItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CustomerReviewsItem,
            newItem: CustomerReviewsItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    const val TAG = "MainActivity"
    const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
}