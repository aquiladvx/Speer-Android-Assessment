package dev.aquiladvx.speerandroidassessment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.aquiladvx.speerandroidassessment.R
import dev.aquiladvx.speerandroidassessment.common.observe
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.ActivityMainBinding
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: GithubUserViewModel by viewModels()

    private fun userProfileObserver(result: UserProfileState) {
            when (result) {
                is UserProfileState.Loading -> {
                    Timber.d("loading")
                }

                is UserProfileState.Found -> {
                    setUserProfile(result.userProfile)
                }

                is UserProfileState.NotFound -> {
                    Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show()
                }

                is UserProfileState.Error -> {
                    Timber.tag("USER PROFILE ERROR").e(result.error.message)
                }
            }
    }

    private fun setUserProfile(user: GithubUserProfile) {
       with(binding.profile) {
           Glide.with(this@MainActivity).load(user.avatarUrl)
               .into(ivUserAvatar)

           tvUserName.text = user.name
           tvUserUsername.text = user.login
           tvUserDescription.text = user.bio
           tvUserFollowers.text = resources.getQuantityString(R.plurals.followers_content, user.followers, user.followers)
           tvUserFollowing.text = getString(R.string.following_content, user.following)
       }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObservers()
        setupUI()
    }

    private fun setupUI() {
        binding.btnSearch.setOnClickListener { searchUser() }
    }

    private fun searchUser() {
        val username = binding.etSearchUsername.text.toString()
        viewModel.getUserProfile(username)
    }

    private fun setObservers() {
        observe(viewModel.userProfile, ::userProfileObserver)
    }
}