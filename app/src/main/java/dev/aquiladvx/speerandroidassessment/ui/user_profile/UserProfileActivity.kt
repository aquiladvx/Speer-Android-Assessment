package dev.aquiladvx.speerandroidassessment.ui.user_profile

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
import dev.aquiladvx.speerandroidassessment.databinding.ActivityUserProfileBinding
import dev.aquiladvx.speerandroidassessment.ui.user_connections.ConnectionsDialog
import timber.log.Timber

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {

    private val binding: ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObservers()
        setupUI()
    }

    private fun userProfileObserver(result: UserProfileUiState) {
        when (result) {
            is UserProfileUiState.Loading -> {
                showLoading()
            }

            is UserProfileUiState.Found -> {
                hideLoading()
                setUserProfileOnUi(result.userProfile)
            }

            is UserProfileUiState.NotFound -> {
                hideLoading()
                binding.profile.clProfile.hide()
                binding.clNotFound.show()
            }

            is UserProfileUiState.Error -> {
                //TODO error dialog
                hideLoading()
                Timber.tag("USER PROFILE ERROR").e(result.error.message)
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            sklProfile.slProfile.startShimmer()
            sklProfile.slProfile.show()
            profile.clProfile.hide()
        }
    }

    private fun hideLoading() {
        with(binding) {
            sklProfile.slProfile.hideShimmer()
            sklProfile.slProfile.hide()
            profile.clProfile.show()
        }
    }

    private fun setUserProfileOnUi(user: GithubUserProfile) {
        binding.clNotFound.hide()
        binding.etSearchUsername.setText(user.login)
        with(binding.profile) {
            clProfile.show()
            Glide
                .with(this@UserProfileActivity)
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

    private fun setupUI() {
        with(binding) {
            btnSearch.setOnClickListener { getUserProfile() }
            profile.tvUserFollowers.setOnClickListener { showUserFollowersDialog() }
            profile.tvUserFollowing.setOnClickListener { showUserFollowingDialog() }
        }
    }

    private fun showConnectionDialog(connectionType: ConnectionsDialog.Companion.ConnectionType) {
        ConnectionsDialog(
            connectionType,
            viewModel.username.value!!
        )
            .setOnUserClickListener(::getUserProfile)
            .show(supportFragmentManager, ConnectionsDialog::class.java.name)
    }

    private fun showUserFollowingDialog() {
        showConnectionDialog(ConnectionsDialog.Companion.ConnectionType.FOLLOWING)
    }

    private fun showUserFollowersDialog() {
        showConnectionDialog(ConnectionsDialog.Companion.ConnectionType.FOLLOWERS)
    }

    private fun getUserProfile(username: String? = null) {
        if (username == null) {
            viewModel.getUserProfile(binding.etSearchUsername.text.toString())
        } else {
            viewModel.getUserProfile(username)
        }
    }

    private fun setObservers() {
        observe(viewModel.userProfile, ::userProfileObserver)
    }
}