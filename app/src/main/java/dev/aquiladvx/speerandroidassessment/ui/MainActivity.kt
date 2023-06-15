package dev.aquiladvx.speerandroidassessment.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.aquiladvx.speerandroidassessment.R
import dev.aquiladvx.speerandroidassessment.common.hide
import dev.aquiladvx.speerandroidassessment.common.observe
import dev.aquiladvx.speerandroidassessment.common.show
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
                //TODO skeleton loading
                Timber.d("loading")
            }

            is UserProfileState.Found -> {
                setUserProfile(result.userProfile)
            }

            is UserProfileState.NotFound -> {
                binding.profile.clProfile.hide()
                binding.clNotFound.show()
            }

            is UserProfileState.Error -> {
                //TODO error dialog
                Timber.tag("USER PROFILE ERROR").e(result.error.message)
            }
        }
    }

    private fun setUserProfile(user: GithubUserProfile) {
        binding.clNotFound.hide()
        binding.etSearchUsername.setText(user.login)
        with(binding.profile) {
            clProfile.show()
            Glide
                .with(this@MainActivity)
                .load(user.avatarUrl)
                .into(ivUserAvatar)

            tvUserName.text = user.name
            tvUserUsername.text = user.login
            tvUserDescription.text = user.bio
            tvUserFollowers.text = resources.getQuantityString(
                R.plurals.followers_content,
                user.followers,
                user.followers
            )
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
        with(binding) {
            btnSearch.setOnClickListener { getUserProfile() }
            profile.tvUserFollowers.setOnClickListener { getUserFollowers() }
            profile.tvUserFollowing.setOnClickListener { getUserFollowing() }
        }
    }

    private fun getUserFollowing() {
        ConnectionsDialog(
            ConnectionsDialog.Companion.ConnectionType.FOLLOWING
        ).show(supportFragmentManager, ConnectionsDialog::class.java.name)
    }

    private fun getUserFollowers() {
        ConnectionsDialog(
            ConnectionsDialog.Companion.ConnectionType.FOLLOWERS
        ).show(supportFragmentManager, ConnectionsDialog::class.java.name)
    }

    private fun getUserProfile() {
        val username = binding.etSearchUsername.text.toString()
        viewModel.getUserProfile(username)
    }

    private fun setObservers() {
        observe(viewModel.userProfile, ::userProfileObserver)
    }
}