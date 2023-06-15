package dev.aquiladvx.speerandroidassessment.ui.user_connections

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.aquiladvx.speerandroidassessment.common.hide
import dev.aquiladvx.speerandroidassessment.common.observe
import dev.aquiladvx.speerandroidassessment.common.show
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.FragmentDialogConnectionsBinding
import timber.log.Timber

@AndroidEntryPoint
class ConnectionsDialog(private val connectionType: ConnectionType, private val username: String) :
    DialogFragment() {

    companion object {
        enum class ConnectionType {
            FOLLOWERS,
            FOLLOWING
        }
    }

    private var _binding: FragmentDialogConnectionsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: UserConnectionsViewModel by viewModels()

    private lateinit var adapter: UserConnectionsAdapter
    private var userClickListener: ((String) -> Unit)? = null
    private lateinit var getUserConnections: ((reset: Boolean) -> Unit)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogConnectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setupUI()
        getUserConnections = when (connectionType) {
            ConnectionType.FOLLOWERS -> {
                ::getUsersFollowers
            }

            ConnectionType.FOLLOWING -> {
                ::getUsersFollowing
            }
        }
        getUserConnections(false)
    }

    fun setOnUserClickListener(listener: (user: String) -> Unit): DialogFragment {
        userClickListener = listener
        return this
    }

    private fun getUsersFollowing(reset: Boolean = false) {
        binding.tvConnectionType.text = connectionType.name
        viewModel.getUserFollowing(username, reset)
    }

    private fun getUsersFollowers(reset: Boolean = false) {
        binding.tvConnectionType.text = connectionType.name
        viewModel.getUserFollowers(username, reset)
    }

    private fun setObservers() {
        observe(viewModel.userConnections, ::userConnectionsObserver)
    }

    private fun userConnectionsObserver(result: UserConnectionsUiState) {
        when (result) {
            is UserConnectionsUiState.Loading.FromData -> showPageLoading()

            is UserConnectionsUiState.Loading.FromEmpty -> showLoading()

            is UserConnectionsUiState.NotFound -> showNotFoundView()

            is UserConnectionsUiState.Found -> {
                hideLoading()
                showUserConnections(result.userConnections)
            }

            is UserConnectionsUiState.Error -> {
                //TODO error dialog
                Timber.tag("USER PROFILE ERROR").e(result.error.message)
            }

        }
    }

    private fun showNotFoundView() {
        hideLoading()
        binding.layoutNotFound.clNotFound.show()
    }

    private fun showLoading() {
        binding.srUserConnections.isRefreshing = true
    }

    private fun showPageLoading() {
        with(binding) {
            sklUserConnections.startShimmer()
            sklUserConnections.show()
        }
    }

    private fun hideLoading() {
        with(binding) {
            srUserConnections.isRefreshing = false
            sklUserConnections.hideShimmer()
            sklUserConnections.hide()
        }
    }

    private fun showUserConnections(userConnections: List<GithubUserProfile>) {
        binding.layoutNotFound.clNotFound.hide()
        adapter.updateConnections(userConnections)
    }

    private fun onUserConnectionClickListener(username: String) {
        userClickListener?.invoke(username)
        dismiss()
    }

    private fun setupUI() {
        setDialogSize()
        setupAdapter()
        with(binding) {
            btnClose.setOnClickListener { dismiss() }
            srUserConnections.setOnRefreshListener { getUserConnections(true) }
        }
        binding.btnClose.setOnClickListener { dismiss() }
    }

    private fun setupAdapter() {
        with(binding) {
            adapter = UserConnectionsAdapter()
            adapter.setOnUserClickListener(::onUserConnectionClickListener)
            rvConnections.adapter = adapter

            rvConnections.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        getUserConnections(false)
                    }
                }
            })
        }
    }

    private fun setDialogSize() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val height = (screenHeight * 80) / 100

        dialog?.apply {
            window?.apply {
                setGravity(Gravity.BOTTOM)
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    height
                )
            }
        }
    }

}