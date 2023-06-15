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
import dagger.hilt.android.AndroidEntryPoint
import dev.aquiladvx.speerandroidassessment.common.observe
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.FragmentDialogConnectionsBinding
import timber.log.Timber

@AndroidEntryPoint
class ConnectionsDialog(private val connectionType: ConnectionType, private val username: String) : DialogFragment() {

    companion object {
        enum class ConnectionType{
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
        when(connectionType) {
            ConnectionType.FOLLOWERS -> getUsersFollowers()
            ConnectionType.FOLLOWING -> getUsersFollowing()
        }
    }

    fun setOnUserClickListener(listener: (user: String) -> Unit): DialogFragment {
        userClickListener = listener
        return this
    }

    private fun getUsersFollowing() {
        binding.tvConnectionType.text = connectionType.name
        viewModel.getUserFollowing(username)
    }

    private fun getUsersFollowers() {
        binding.tvConnectionType.text = connectionType.name
        viewModel.getUserFollowers(username)
    }

    private fun setObservers() {
        observe(viewModel.userConnections, ::userConnectionsObserver)
    }

    private fun userConnectionsObserver(result: UserConnectionsUiState) {
        when (result) {
            UserConnectionsUiState.Loading -> {
                //TODO skeleton loading
                Timber.d("loading")
            }
            is UserConnectionsUiState.Found -> {
                showUserConnections(result.userConnections)
            }
            is UserConnectionsUiState.Error -> {
                //TODO error dialog
                Timber.tag("USER PROFILE ERROR").e(result.error.message)
            }
        }
    }

    private fun showUserConnections(userConnections: List<GithubUserProfile>) {
        adapter.updateConnections(userConnections)
    }

    private fun onUserConnectionClickListener(username: String) {
        userClickListener?.invoke(username)
        dismiss()
    }

    private fun setupUI() {
        setDialogSize()

        with(binding) {
            adapter = UserConnectionsAdapter()
            adapter.setOnUserClickListener(::onUserConnectionClickListener)
            rvConnections.adapter = adapter

            btnClose.setOnClickListener { dismiss() }
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