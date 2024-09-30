package app.ditodev.retroresto.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.ditodev.retroresto.R
import app.ditodev.retroresto.data.api.response.CustomerReviewsItem
import app.ditodev.retroresto.data.api.response.Restaurant
import app.ditodev.retroresto.databinding.ActivityMainBinding
import app.ditodev.retroresto.ui.adapter.ReviewAdapter
import com.bumptech.glide.Glide

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

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainActivityViewModel::class.java]

        viewModel.restaurant.observe(this) {
            setRestaurantData(it)
        }

        viewModel.listReview.observe(this) {
            setReviewData(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnReview.setOnClickListener {
            viewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}