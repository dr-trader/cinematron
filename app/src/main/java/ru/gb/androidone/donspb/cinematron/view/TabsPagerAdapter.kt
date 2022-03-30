package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.viewmodel.MovieListsEnum

class TabsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var tabsQuantity: Int,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = tabsQuantity

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString(
            Consts.BUNDLE_LISTNAME_TAG,
            MovieListsEnum.values()[position].pathPart)
        val mainFragment = MainFragment()
        mainFragment.arguments = bundle
        return mainFragment
    }
}