package com.example.mygithub.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithub.*
import com.example.mygithub.database.User
import com.example.mygithub.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteDetail : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    private var detailUser = ItemsItem()
    private var ivFavorite: Boolean = false
    private var favUser: User? = null

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@FavoriteDetail)
        val user = intent.getStringExtra(DetailActivity.EXTRA_FAVORITE)

        if (user != null) {
            viewModel.getUserDetail(user)
        }

        //Binding Items (Check this if there's an error on favorite
        viewModel.listUser.observe(this) { detailList ->
            detailUser = detailList

            favUser = User(detailUser.id, detailUser.login, detailUser.avatarUrl)
            binding?.let {
                Glide.with(this)
                    .load(detailUser.avatarUrl)
                    .circleCrop()
                    .into(it.ciUserPhoto)
            }
            binding?.apply {
                tvName.text = detailUser.name
                tvUsername.text = detailUser.login
                tvFollowerNumber.text = detailUser.followers.toString()
                tvFollowingNumber.text = detailUser.following.toString()

            }

            viewModel.getFavorite().observe(this) { userFavorite: List<User> ->
                if (userFavorite != null) {
                    for (data in userFavorite) {
                        if (detailUser.id == data.id) {
                            ivFavorite = true
                            binding?.fabAdd?.setImageResource(R.drawable.ic_draw_favorite)
                        }
                    }
                }
            }

            binding?.fabAdd?.setOnClickListener {
                if (!ivFavorite) {
                    ivFavorite = true
                    binding!!.fabAdd.setImageResource(R.drawable.ic_draw_favorite)
                    insertToDatabase(detailUser)
                } else {
                    ivFavorite = false
                    binding!!.fabAdd.setImageResource(R.drawable.ic_draw_favorite_border)
                    viewModel.delete(detailUser.id)
                    Toast.makeText(this, "Bookmark Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Tab untuk favorite
        val sectionPagerAdapter = SectionsPageAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.detail_view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.detail_tabs)
        TabLayoutMediator(tabs, viewPager) { detailTabs, position ->
            detailTabs.text = resources.getString(DetailActivity.GIT_TABS[position])
        }.attach()

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.error.observe(this){
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
    }

    private fun insertToDatabase(gitDetailList: ItemsItem) {
        favUser.let { favoriteUser ->
            favoriteUser?.id = gitDetailList.id
            favoriteUser?.login = gitDetailList.login
            favoriteUser?.imageUrl = gitDetailList.avatarUrl
            viewModel.insert(favoriteUser as User)
            Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.loadingDetail?.visibility = View.VISIBLE
        } else {
            binding?.loadingDetail?.visibility = View.GONE
        }
    }
}