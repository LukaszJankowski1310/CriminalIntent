package com.example.criminalintent

import androidx.recyclerview.widget.DiffUtil

class CrimeListDiffUtil(
    private val oldList: List<Crime>,
    private val newList: List<Crime>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return when {
           oldList[oldItemPosition].id != newList[newItemPosition].id -> {false}
           oldList[oldItemPosition].title != newList[newItemPosition].title -> {false}
           oldList[oldItemPosition].date != newList[newItemPosition].date -> {false}
           oldList[oldItemPosition].isSolved != newList[newItemPosition].isSolved -> {false}
           oldList[oldItemPosition].requiresPolice != newList[newItemPosition].requiresPolice -> {false}

           else -> true
       }


    }

}