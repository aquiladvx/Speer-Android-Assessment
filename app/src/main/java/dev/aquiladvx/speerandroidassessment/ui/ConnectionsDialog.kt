package dev.aquiladvx.speerandroidassessment.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dev.aquiladvx.speerandroidassessment.common.observe
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.FragmentDialogConnectionsBinding
import timber.log.Timber

class ConnectionsDialog(private val connectionType: ConnectionType) : DialogFragment() {

    companion object {
        enum class ConnectionType{
            FOLLOWERS,
            FOLLOWING
        }
    }

    private var _binding: FragmentDialogConnectionsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: GithubUserViewModel by activityViewModels()

    private lateinit var adapter: UserConnectionsAdapter

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

    private fun getUsersFollowing() {
        binding.tvConnectionType.text = connectionType.name
        viewModel.getUserFollowing()
    }

    private fun getUsersFollowers() {
        binding.tvConnectionType.text = connectionType.name
        viewModel.getUserFollowers()
    }

    private fun setObservers() {
        observe(viewModel.userConnections, ::userConnectionsObserver)
    }

    private fun userConnectionsObserver(result: UserConnectionsState) {
        when (result) {
            UserConnectionsState.Loading -> {
                //TODO skeleton loading
                Timber.d("loading")
            }
            is UserConnectionsState.Found -> {
                showUserConnections(result.userConnections)
            }
            is UserConnectionsState.Error -> {
                //TODO error dialog
                Timber.tag("USER PROFILE ERROR").e(result.error.message)
            }
        }
    }

    private fun showUserConnections(userConnections: List<GithubUserProfile>) {
        adapter.updateConnections(userConnections)
    }

    private fun onUserConnectionClickListener(user: GithubUserProfile) {
        viewModel.getUserProfile(user.login)
        dismiss()
    }

    private fun setupUI() {
        setDialogSize()
        viewModel.getUserFollowers()

        with(binding) {
            adapter = UserConnectionsAdapter()
            adapter.setOnClickListener(::onUserConnectionClickListener)
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