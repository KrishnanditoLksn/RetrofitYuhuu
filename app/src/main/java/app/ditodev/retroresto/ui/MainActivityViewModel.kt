package app.ditodev.retroresto.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.ditodev.retroresto.data.api.config.ApiConfig
import app.ditodev.retroresto.data.api.response.CustomerReviewsItem
import app.ditodev.retroresto.data.api.response.PostReviewResponse
import app.ditodev.retroresto.data.api.response.Restaurant
import app.ditodev.retroresto.data.api.response.RestaurantResponse
import app.ditodev.retroresto.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    private var _restaurant = MutableLiveData<Restaurant>()

    var restaurant: LiveData<Restaurant> = _restaurant
    private var _listReview = MutableLiveData<List<CustomerReviewsItem>>()

    var listReview: LiveData<List<CustomerReviewsItem>> = _listReview
    private var _isLoading = MutableLiveData<Boolean>()

    var isLoading: LiveData<Boolean> = _isLoading
    init {
        listItem()
    }

    private fun listItem() {
        //call showLoading
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRestaurant(Utils.RESTAURANT_ID)
        client.enqueue(object : Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                if (response.isSuccessful) {
                    /*
                    set restaurant data
                     */
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _restaurant.value = responseBody.restaurant
                        _listReview.value = responseBody.restaurant.customerReviews
                    }
                } else {
                    /*
                    set on failure msg
                     */
                    _isLoading.value = false
                    Log.e(Utils.TAG, "ON FAILURE : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(Utils.TAG, "ON FAILURE : ${t.message}")
            }

        })
    }

    fun postReview(review: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postReview(Utils.RESTAURANT_ID, "Dicoding", review)
        client.enqueue(
            object : Callback<PostReviewResponse> {
                override fun onResponse(
                    call: Call<PostReviewResponse>,
                    response: Response<PostReviewResponse>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        if (responseBody != null) {
                            _listReview.value = responseBody.customerReviews
                        }
                    } else {
                        _isLoading.value = false
                        Log.e(Utils.TAG, "ON FAILURE : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(Utils.TAG, "ON FAILURE : ${t.message}")
                }
            }
        )
    }
}