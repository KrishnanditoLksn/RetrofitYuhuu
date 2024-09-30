package app.ditodev.retroresto.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.ditodev.retroresto.R
import app.ditodev.retroresto.data.api.config.ApiConfig
import app.ditodev.retroresto.data.api.response.CustomerReviewsItem
import app.ditodev.retroresto.data.api.response.PostReviewResponse
import app.ditodev.retroresto.data.api.response.Restaurant
import app.ditodev.retroresto.data.api.response.RestaurantResponse
import app.ditodev.retroresto.databinding.ActivityMainBinding
import app.ditodev.retroresto.ui.adapter.ReviewAdapter
import app.ditodev.retroresto.util.Utils
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.rvRestaurant.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvRestaurant.addItemDecoration(itemDecoration)

        binding.btnReview.setOnClickListener {
            postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        listItem()
    }

    private fun listItem() {
        //call showLoading
        showLoad(true)
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
                    showLoad(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setRestaurantData(responseBody.restaurant)
                        setReviewData(responseBody.restaurant.customerReviews)
                    }
                } else {
                    /*
                    set on failure msg
                     */
                    showLoad(false)
                    Log.e(Utils.TAG, "ON FAILURE : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                showLoad(false)
                Log.e(Utils.TAG, "ON FAILURE : ${t.message}")
            }

        })
    }

    private fun postReview(review: String) {
        showLoad(true)
        val client = ApiConfig.getApiService().postReview(Utils.RESTAURANT_ID, "Dicoding", review)
        client.enqueue(
            object : Callback<PostReviewResponse> {
                override fun onResponse(
                    call: Call<PostReviewResponse>,
                    response: Response<PostReviewResponse>
                ) {
                    showLoad(false)
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        if (responseBody != null) {
                            setReviewData(responseBody.customerReviews)
                        }
                    } else {
                        showLoad(false)
                        Log.e(Utils.TAG, "ON FAILURE : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                    showLoad(false)
                    Log.e(Utils.TAG, "ON FAILURE : ${t.message}")
                }

            }
        )
    }


    private fun setRestaurantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description
        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivResto)
    }

    private fun setReviewData(consumeRevs: List<CustomerReviewsItem>) {
        val adapter = ReviewAdapter()
        adapter.submitList(consumeRevs)
        binding.rvRestaurant.adapter = adapter
        binding.edReview.setText("")
    }

    private fun showLoad(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}