package dev.aquiladvx.speerandroidassessment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.aquiladvx.speerandroidassessment.common.observe
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
                    Timber.tag("result").d(result.userProfile.toString())
                }

                is UserProfileState.NotFound -> {
                    Timber.tag("result").d("not found")
                }

                is UserProfileState.Error -> {
                    Timber.tag("USER PROFILE ERROR").e(result.error.message)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observe(viewModel.userProfile, ::userProfileObserver)
        viewModel.getUserProfile("aquiladvx")
    }
}