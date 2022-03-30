package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.gb.androidone.donspb.cinematron.databinding.TabsFragmentBinding
import ru.gb.androidone.donspb.cinematron.viewmodel.MovieListsEnum

class TabsFragment : Fragment() {

    private var _binding: TabsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabsQuantity = MovieListsEnum.values().size
        val tabLayout = binding.tabLayout
        val viewPager = binding.tabsViewpager
        val vpAdapter = TabsPagerAdapter(parentFragmentManager, lifecycle, tabsQuantity)
        tabLayout.layoutMode = TabLayout.MODE_SCROLLABLE
        viewPager.adapter = vpAdapter
        viewPager.isUserInputEnabled = true

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = activity?.getText(MovieListsEnum.values()[position].listNameId)
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = TabsFragment()
    }
}