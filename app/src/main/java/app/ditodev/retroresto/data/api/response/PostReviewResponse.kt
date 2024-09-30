package app.ditodev.retroresto.data.api.response

import com.google.gson.annotations.SerializedName

data class PostReviewResponse(
    @field:SerializedName("customerReviews")
    val customerReviews: List<CustomerReviewsItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)
