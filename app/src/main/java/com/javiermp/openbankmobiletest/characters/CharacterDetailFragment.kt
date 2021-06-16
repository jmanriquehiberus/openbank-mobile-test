package com.javiermp.openbankmobiletest.characters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.javiermp.model.characters.Character
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.characters.viewmodel.CharacterDetailState
import com.javiermp.openbankmobiletest.characters.viewmodel.CharactersViewModel
import com.javiermp.openbankmobiletest.common.errorhandling.AppAction
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundle
import com.javiermp.openbankmobiletest.common.extensions.setOnSingleClickListener
import com.javiermp.openbankmobiletest.common.model.ResourceState
import com.javiermp.openbankmobiletest.common.navigation.Navigator
import com.javiermp.openbankmobiletest.common.widget.error.ErrorListener
import com.javiermp.openbankmobiletest.common.widget.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_detail_fragment.*
import kotlinx.android.synthetic.main.character_detail_fragment.siv_pound
import kotlinx.android.synthetic.main.characters_fragment.*
import kotlinx.android.synthetic.main.view_error.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CharacterDetailFragment : BaseFragment(R.layout.character_detail_fragment) {

    private val charactersViewModel: CharactersViewModel by sharedViewModel()
    private lateinit var loadingAnimatorSet: AnimatorSet

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
            AppAction.GET_CHARACTER_DETAIL -> {
                charactersViewModel.getCharacterDetail(true)
            }
            else -> Timber.e("AppAction ${appAction.name} not recognized")
        }
    }

    private fun initializeViews() {
        ib_character_detail_fragment_back_button.setOnSingleClickListener {
            activity?.onBackPressed()
        }
        ev_character_detail_fragment_container.errorListener = errorListener
    }

    private fun initializeContents() {
        charactersViewModel.getCharacterDetailLiveData().observe(this, Observer {
            if (it != null) handleCharacterDetailState(it)
        })
        charactersViewModel.getCharacterDetail()
    }

    private fun handleCharacterDetailState(characterDetailState: CharacterDetailState) {
        when (characterDetailState) {
            is ResourceState.Success -> handleCharacterDetail(characterDetailState.data)
            is ResourceState.Loading -> setupViewForLoading()
            is ResourceState.Error -> setupScreenForError(characterDetailState.errorBundle)
        }
    }

    private fun handleCharacterDetail(character: Character) {
        tv_character_detail_fragment_name.text = character.name
        character.thumbnail?.path?.let {
            val path = "${it.replace("http", "https")}/standard_fantastic.${character.thumbnail?.extension}"
            Picasso.get().load(path).into(siv_character_detail_fragment_avatar)
        }
        tv_character_detail_fragment_description.text = if (!character.description.isNullOrEmpty()) character.description else getString(R.string.character_detail_description_placeholder)
        tv_character_detail_fragment_series_amount.text = character.series?.items?.count().toString()
        tv_character_detail_fragment_comics_amount.text = character.comics?.items?.count().toString()
        tv_character_detail_fragment_stories_amount.text = character.stories?.items?.count().toString()
        character.urls?.let {urls ->
            val wiki = urls.find { x -> x.type.equals("wiki") }?.url
            val appearances = urls.find { x -> x.type.equals("comiclink") }?.url
            if (wiki.isNullOrEmpty()) rl_character_detail_fragment_extensive_detail.visibility = View.GONE
            if (appearances.isNullOrEmpty()) rl_character_detail_fragment_appearances.visibility = View.GONE
            if (wiki.isNullOrEmpty() && appearances.isNullOrEmpty()) {
                rl_character_detail_fragment_extensive_detail.visibility = View.GONE
                rl_character_detail_fragment_appearances.visibility = View.GONE
                tv_character_detail_fragment_find_out_more.visibility = View.GONE
            }
        }
        rl_character_detail_fragment_extensive_detail.setOnSingleClickListener {
            activity?.let { activity ->
                if (isNetworkAvailable()) {
                    character.urls?.let { urls ->
                        val url = urls.find { x -> x.type.equals("wiki") }?.url
                        url?.let { Navigator.navigateToWebviewActivity(activity, it) }
                    }
                } else {
                    Snackbar.make(rl_character_detail_fragment_extensive_detail, R.string.error_no_connection, Snackbar.LENGTH_LONG).show()
                }
            }
        }
        rl_character_detail_fragment_appearances.setOnSingleClickListener {
            activity?.let { activity ->
                if (isNetworkAvailable()) {
                    character.urls?.let { urls ->
                        val url = urls.find { x -> x.type.equals("comiclink") }?.url
                        url?.let { Navigator.navigateToWebviewActivity(activity, it) }
                    }
                } else {
                    Snackbar.make(rl_character_detail_fragment_appearances, R.string.error_no_connection, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        loadingAnimatorSet.pause()
        rl_character_detail_fragment_container.visibility = View.VISIBLE
        siv_pound.visibility = View.GONE
    }

    private fun setupViewForLoading() {
        setRotatingPoundAnimation()
        rl_character_detail_fragment_container.visibility = View.INVISIBLE
        ev_character_detail_fragment_container.visibility = View.GONE
        siv_pound.visibility = View.VISIBLE
    }

    private fun setupScreenForError(errorBundle: ErrorBundle) {
        rl_character_detail_fragment_container.visibility = View.GONE
        ev_character_detail_fragment_container.visibility = View.VISIBLE
        siv_pound.visibility = View.GONE
        ev_character_detail_fragment_container.errorBundle = errorBundle
    }

    private fun setRotatingPoundAnimation() {
        val rotationAnimation = ObjectAnimator.ofFloat(siv_pound, "rotation", 0f, 360f)
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

    private fun isNetworkAvailable(): Boolean {
        context?.let { context ->
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) return true
            }

            return false
        } ?: return false
    }

   /* fun  isNetworkAvailable():Boolean{
        context?.let { context ->
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val internetInfo = connectivityManager.activeNetworkInfo
            return internetInfo != null && internetInfo.isConnected
        } ?: return false
    }*/
}