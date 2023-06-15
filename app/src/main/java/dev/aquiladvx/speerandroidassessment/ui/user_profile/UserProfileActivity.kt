package dev.aquiladvx.speerandroidassessment.ui.user_profile

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.aquiladvx.speerandroidassessment.R
import dev.aquiladvx.speerandroidassessment.common.hide
import dev.aquiladvx.speerandroidassessment.common.hideKeyboard
import dev.aquiladvx.speerandroidassessment.common.loadImage
import dev.aquiladvx.speerandroidassessment.common.observe
import dev.aquiladvx.speerandroidassessment.common.show
import dev.aquiladvx.speerandroidassessment.common.showErrorMessage
import dev.aquiladvx.speerandroidassessment.common.toast
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.ActivityUserProfileBinding
import dev.aquiladvx.speerandroidassessment.ui.user_connections.ConnectionsDialog

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
                showNotFoundView()
            }

            is UserProfileUiState.Error -> {
                hideLoading()
                showErrorMessage(result.error)
            }
        }
    }

    private fun showNotFoundView() {
        binding.profile.clProfile.hide()
        binding.layoutNotFound.clNotFound.show()
    }

    private fun showLoading() {
        with(binding) {
            layoutNotFound.clNotFound.hide()
            sklProfile.slProfile.startShimmer()
            sklProfile.slProfile.show()
            profile.clProfile.hide()
        }
    }

    private fun hideLoading() {
        with(binding) {
            srlUserProfile.isRefreshing = false
            sklProfile.slProfile.hideShimmer()
            sklProfile.slProfile.hide()
            profile.clProfile.show()
        }
    }

    private fun setUserProfileOnUi(user: GithubUserProfile) {
        binding.layoutNotFound.clNotFound.hide()
        binding.etSearchUsername.setText(user.login)
        with(binding.profile) {
            clProfile.show()
            loadImage(user.avatarUrl, ivUserAvatar)
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
            btnSearch.setOnClickListener { onSearchListener() }
            profile.tvUserFollowers.setOnClickListener { showUserFollowersDialog() }
            profile.tvUserFollowing.setOnClickListener { showUserFollowingDialog() }
            srlUserProfile.setOnRefreshListener { viewModel.refreshUserProfile() }
            etSearchUsername.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchListener()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun onSearchListener() {
        binding.etSearchUsername.hideKeyboard()
        val username = binding.etSearchUsername.text.toString()
        if (username.isEmpty()) {
            toast("Enter a username to search")
            return
        }
        getUserProfile(username)
    }

    private fun showConnectionsDialog(connectionsType: ConnectionsDialog.Companion.ConnectionType) {
        ConnectionsDialog(
            connectionsType,
            viewModel.username.value!!
        )
            .setOnUserClickListener(::getUserProfile)
            .show(supportFragmentManager, ConnectionsDialog::class.java.name)
    }

    private fun showUserFollowingDialog() {
        showConnectionsDialog(ConnectionsDialog.Companion.ConnectionType.FOLLOWING)
    }

    private fun showUserFollowersDialog() {
        showConnectionsDialog(ConnectionsDialog.Companion.ConnectionType.FOLLOWERS)
    }

    private fun getUserProfile(username: String) {
        viewModel.getUserProfile(username)
    }

    private fun setObservers() {
        observe(viewModel.userProfile, ::userProfileObserver)
    }
}