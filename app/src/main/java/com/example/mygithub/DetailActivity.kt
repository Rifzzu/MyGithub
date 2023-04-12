package com.example.mygithub

import android.content.ClipData.Item
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithub.database.User
import com.example.mygithub.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "keyUser"
        const val EXTRA_FAVORITE = "extra_favorite"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )

        @StringRes
        val GIT_TABS = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var favoriteUser: User? = null
    private var ivFavorite: Boolean = false
    private var detailUser = DetailUserResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USER)
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        //viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        viewModel = obtainViewModel(this@DetailActivity)
        val userLogin = intent.getStringExtra(EXTRA_USER)
        binding.tvName.text = userLogin

        val sectionsPagerAdapter = SectionsPageAdapter(this)
        sectionsPagerAdapter.username = userLogin.toString()

        val viewPager: ViewPager2 = findViewById(R.id.detail_view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.detail_tabs)
        TabLayoutMediator(tabs, viewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        if (userLogin != null) {
            showLoading(true)
            viewModel.getUserDetail(userLogin)
            showLoading(false)
        }
        viewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        /*viewModel.listUser.observe(this) { detailList ->
            detailUser = detailList

            if (detailList != null) {
                binding?.let {
                    Glide.with(this)
                        .load(detailList.avatarUrl)
                        .circleCrop()
                        .into(it.ciUserPhoto)
                }
            }
            binding?.apply {
                tvName.text = detailList.name
                tvUsername.text = detailList.login
                tvFollowerNumber.text = detailList.followers.toString()
                tvFollowingNumber.text = detailList.following.toString()
            }

            favoriteUser = User(detailList.id, detailList.login, detailList.avatarUrl)
            viewModel.getFavorite().observe(this) { userFavorite: List<User> ->
                if (userFavorite != null) {
                    for (data in userFavorite) {
                        if (detailList.id == data.id) {
                            ivFavorite = true
                            binding?.fabAdd?.setImageResource(R.drawable.ic_draw_favorite)
                        }
                    }
                }
            }
            binding?.fabAdd?.setOnClickListener{
                if(!ivFavorite){
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
        }*/
    }

    private fun setDetailUser(Name: DetailUserResponse) {
        Glide.with(this@DetailActivity)
            .load(Name.avatarUrl)
            .into(binding.ciUserPhoto)
        binding.tvName.text = Name.login
        binding.tvUsername.text = Name.name
        binding.tvFollowerNumber.text = Name.followers.toString()
        binding.tvFollowingNumber.text = Name.following.toString()

        favoriteUser = User(Name.id, Name.login, Name.avatarUrl)
        viewModel.getFavorite().observe(this) { userFavorite: List<User> ->
            if (userFavorite != null) {
                for (data in userFavorite) {
                    if (Name.id == data.id) {
                        ivFavorite = true
                        binding?.fabAdd?.setImageResource(R.drawable.ic_draw_favorite)
                    }
                }
            }
        }
        binding?.fabAdd?.setOnClickListener{
            if(!ivFavorite){
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingDetail.visibility = View.VISIBLE
        } else {
            binding.loadingDetail.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }
    private fun insertToDatabase(detailList: DetailUserResponse) {
        favoriteUser.let { favoriteUser ->
            favoriteUser?.id = detailList.id
            favoriteUser?.login = detailList.login
            favoriteUser?.imageUrl = detailList.avatarUrl
            viewModel.insert(favoriteUser as User)
            Toast.makeText(this, "Favorited", Toast.LENGTH_SHORT).show()
        }
    }
}