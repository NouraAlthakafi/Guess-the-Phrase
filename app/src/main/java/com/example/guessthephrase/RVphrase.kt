package com.example.guessthephrase

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_phrase.view.*

class RVphrase(val guesses: ArrayList<String>): RecyclerView.Adapter<RVphrase.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_phrase, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val aGuess = guesses[position]
        holder.itemView.apply{
            tvPhrase.text = aGuess
            if(aGuess.endsWith("found")|| aGuess.endsWith("left")){
                tvPhrase.setTextColor(Color.BLUE)
            }
            else if(aGuess.startsWith("Wrong")|| aGuess.startsWith("No")){
                tvPhrase.setTextColor(Color.RED)
            }
            else{
                tvPhrase.setTextColor(Color.BLACK)
            }
        }
    }

    override fun getItemCount()= guesses.size
}