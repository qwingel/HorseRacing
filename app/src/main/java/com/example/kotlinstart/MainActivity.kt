package com.example.kotlinstart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlinstart.fragments.GameFragment
import com.example.kotlinstart.fragments.HistoryFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.hide()
        InitViewPager()
    }

    fun InitViewPager() {
        // Список фраментов
        val fragments = listOf(
            GameFragment(),
            HistoryFragment()
        )

        // ViewPager2
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        viewPager.adapter = ViewPagerAdapter(this, fragments)

        // TabLayout
        val tabLayout = findViewById<TabLayout>(R.id.tab_bar)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Скачки"
                1 -> "История"
                else -> null
            }
            tab.icon = when(position) {
                0 -> ContextCompat.getDrawable(this, R.drawable.horses) // Иконка кони
                1 -> ContextCompat.getDrawable(this, R.drawable.history) // Иконка история
                else -> null
            }
        }.attach()
    }
}