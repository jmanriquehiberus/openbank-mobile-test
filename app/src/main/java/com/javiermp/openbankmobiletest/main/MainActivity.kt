package com.javiermp.openbankmobiletest.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.characters.viewmodel.CharactersNavigationCommand
import com.javiermp.openbankmobiletest.common.viewmodel.CommonEvent
import com.javiermp.openbankmobiletest.common.viewmodel.SingleLiveEvent
import com.javiermp.openbankmobiletest.common.widget.BaseActivity
import com.javiermp.openbankmobiletest.characters.viewmodel.CharactersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private lateinit var currentNavController: NavController
    private val commonLiveEvent = SingleLiveEvent<CommonEvent>()
    private val charactersViewModel: CharactersViewModel by viewModel()

    companion object {

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initializeContents()
    }

    private fun initializeContents() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fl_main_activity_host_container) as NavHostFragment
        currentNavController = navHostFragment.navController

        charactersViewModel.charactersNavigationLiveEvent.observe(this, Observer { command ->
            when (command) {
                CharactersNavigationCommand.GoToDetail -> {
                    currentNavController.navigate(R.id.action_charactersFragment_to_characterDetailFragment)
                }
            }
        })
        charactersViewModel.commonLiveEvent = commonLiveEvent
    }
}
