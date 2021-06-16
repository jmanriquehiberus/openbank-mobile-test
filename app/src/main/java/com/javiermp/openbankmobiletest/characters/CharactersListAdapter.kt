package com.javiermp.openbankmobiletest.characters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.utils.MDUtil.inflate
import com.javiermp.model.characters.Character
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.common.extensions.setOnSingleClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.characters_fragment_row.view.*

class CharactersListAdapter(
    private val context: Context,
    private val listener: (Character) -> Unit
) : RecyclerView.Adapter<CharactersListAdapter.CharactersListItemViewHolder>() {

    private var items: List<Character> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersListItemViewHolder =
        CharactersListItemViewHolder(
            parent.inflate(context, R.layout.characters_fragment_row),
            context
        )

    override fun onBindViewHolder(holder: CharactersListItemViewHolder, position: Int) =
        holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    fun setItems(list: List<Character>) {
        items = list
    }

    class CharactersListItemViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(character: Character, listener: (Character) -> Unit) =
            with(itemView) {
                character.thumbnail?.path?.let {
                    val path = "${it.replace("http", "https")}/standard_medium.${character.thumbnail?.extension}"
                    Picasso.get().load(path).into(siv_thumbnail)
                }
                tv_name.text = character.name
                tv_series_amount.text = character.series?.items?.count().toString()
                tv_comics_amount.text = character.comics?.items?.count().toString()
                tv_stories_amount.text = character.stories?.items?.count().toString()

                rl_main_container.setOnSingleClickListener { listener(character) }
            }
    }
}