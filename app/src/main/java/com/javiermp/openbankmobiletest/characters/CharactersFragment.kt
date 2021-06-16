package com.javiermp.openbankmobiletest.characters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.javiermp.model.characters.Character
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.characters.viewmodel.CharactersState
import com.javiermp.openbankmobiletest.characters.viewmodel.CharactersViewModel
import com.javiermp.openbankmobiletest.common.errorhandling.AppAction
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundle
import com.javiermp.openbankmobiletest.common.model.ResourceState
import com.javiermp.openbankmobiletest.common.widget.error.ErrorListener
import com.javiermp.openbankmobiletest.common.widget.BaseFragment
import kotlinx.android.synthetic.main.characters_fragment.*
import kotlinx.android.synthetic.main.characters_fragment.siv_pound
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CharactersFragment : BaseFragment(R.layout.characters_fragment) {

    private val charactersViewModel: CharactersViewModel by sharedViewModel()
    private lateinit var loadingAnimatorSet: AnimatorSet
    private lateinit var charactersListAdapter: CharactersListAdapter

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)
        initializeViews()
        initializeContents()
    }

    private var errorListener = object : ErrorListener {
        override fun onRetry(errorBundle: ErrorBundle) {
            retry(errorBundle.appAction)
        }
    }

    private fun retry(appAction: AppAction) {
        when (appAction) {
            AppAction.GET_CHARACTERS -> {
                charactersViewModel.getCharacters(true)
            }
            else -> Timber.e("AppAction ${appAction.name} not recognized")
        }
    }

    private fun initializeContents() {
        charactersViewModel.getCharactersLiveData().observe(this, Observer {
            if (it != null) handleCharactersState(it)
        })
        charactersViewModel.getCharacters()
    }

    private fun initializeViews() {
        context?.let {
            charactersListAdapter = CharactersListAdapter(it) { character ->
                charactersViewModel.selectedCharacterId = character.id
                charactersViewModel.onGoToDetail()
            }
        }
        srl_characters_fragment.setOnRefreshListener {
            charactersViewModel.getCharacters(true)
            srl_characters_fragment.isRefreshing = false
        }
        rv_characters_fragment_list.layoutManager = LinearLayoutManager(context)
        rv_characters_fragment_list.adapter = charactersListAdapter
        ev_characters_fragment_container.errorListener = errorListener
    }

    private fun handleCharactersState(charactersState: CharactersState) {
        when (charactersState) {
            is ResourceState.Success -> handleCharacters(charactersState.data)
            is ResourceState.Loading -> setupViewForLoading()
            is ResourceState.Error -> setupScreenForError(charactersState.errorBundle)
        }
    }

    private fun handleCharacters(characters: List<Character>) {
        charactersListAdapter.setItems(characters)
        requireActivity().runOnUiThread { charactersListAdapter.notifyDataSetChanged() }
        loadingAnimatorSet.pause()
        rl_characters_fragment_container.visibility = View.VISIBLE
        siv_pound.visibility = View.GONE
    }

    private fun setupViewForLoading() {
        setRotatingPoundAnimation()
        rl_characters_fragment_container.visibility = View.INVISIBLE
        ev_characters_fragment_container.visibility = View.GONE
        siv_pound.visibility = View.VISIBLE
    }

    private fun setupScreenForError(errorBundle: ErrorBundle) {
        rl_characters_fragment_container.visibility = View.GONE
        ev_characters_fragment_container.visibility = View.VISIBLE
        siv_pound.visibility = View.GONE
        ev_characters_fragment_container.errorBundle = errorBundle
    }

    private fun setRotatingPoundAnimation() {
        val rotationAnimation = ObjectAnimator.ofFloat(siv_pound , "rotation", 0f, 360f)
        val fadeAnimation = ObjectAnimator.ofFloat(siv_pound, "alpha", 0f, 0.3f)

        loadingAnimatorSet = AnimatorSet()
        rotationAnimation.repeatMode = ValueAnimator.RESTART
        rotationAnimation.repeatCount = ValueAnimator.INFINITE
        rotationAnimation.duration = 1500
        fadeAnimation.repeatMode = ValueAnimator.REVERSE
        fadeAnimation.repeatCount = ValueAnimator.INFINITE
        fadeAnimation.duration = 750

        loadingAnimatorSet.play(rotationAnimation).with(fadeAnimation)
        loadingAnimatorSet.start()
    }
}