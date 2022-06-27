package com.globodai.ovestiaire.ui.edit_article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.globodai.ovestiaire.R
import com.globodai.ovestiaire.data.network.ArticleApi
import com.globodai.ovestiaire.data.repository.ArticleRepository
import com.globodai.ovestiaire.databinding.FragmentEditArticleBinding
import com.globodai.ovestiaire.ui.article.ArticleViewModel
import com.globodai.ovestiaire.ui.base.BaseFragment
import com.globodai.ovestiaire.ui.edit_article.adapters.SectionsPagerAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EditArticleFragment :
    BaseFragment<ArticleViewModel, FragmentEditArticleBinding, ArticleRepository>() {

    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var tabTitles: ArrayList<String>
    private lateinit var dots: ArrayList<ImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTabs()

        binding.btnNext.setOnClickListener {
            slideToNext()
        }
    }


    private fun updateTabs() {
        tabTitles = arrayListOf()
        fragments = arrayListOf()
        dots = arrayListOf()

        dots = ArrayList(fragments.size)

        fragments.add(ChooseSportFragment.newInstance())
        tabTitles.add(resources.getString(R.string.on_which_sport_your_article_come_from))

        fragments.add(ChooseCategoryFragment.newInstance())
        tabTitles.add(resources.getString(R.string.title_choose_category))

        fragments.add(ChooseEquipmentFragment.newInstance())
        tabTitles.add(resources.getString(R.string.title_choose_equipment))

        fragments.add(ChooseGenderFragment.newInstance())
        tabTitles.add(resources.getString(R.string.title_choose_gender))

        fragments.add(ChooseSizeFragment.newInstance())
        tabTitles.add(resources.getString(R.string.title_choose_size))

        fragments.add(ChooseColourFragment.newInstance())
        tabTitles.add(resources.getString(R.string.title_choose_colour))

        fragments.add(ArticleFormFragment.newInstance())
        tabTitles.add(resources.getString(R.string.title_choose_sport))

        for (i in 0 until fragments.size) dots.add(ImageView(requireContext()))

        with(binding) {

            val sectionsPagerAdapter =
                SectionsPagerAdapter(requireContext(), childFragmentManager, tabTitles, fragments)
            viewPager.adapter = sectionsPagerAdapter
            //tabLayout.setupWithViewPager(binding.viewPager)
            createDots(0)

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {
                        createDots(position)
                    }
                }
            )
        }
    }

    private fun createDots(currentPosition: Int) {
        binding.pageTitle.text = tabTitles[currentPosition]

        binding.dotsLayout.removeAllViews()

        for (i in 0 until fragments.size) {
            dots[i] = ImageView(requireContext())

            if (i == currentPosition) {
                dots[i].setImageDrawable(requireContext().getDrawable(R.drawable.dot_selected))
            } else {
                dots[i].setImageDrawable(requireContext().getDrawable(R.drawable.dot_default))
            }

            val layoutParams = LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0)
            binding.dotsLayout.addView(dots[i], layoutParams)
        }
    }

    private fun slideToNext() {
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    override fun getViewModel() = ArticleViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditArticleBinding.inflate(inflater, container, false)


    override fun getFragmentRepository(): ArticleRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ArticleApi::class.java, token)
        return ArticleRepository(api)
    }
}