package com.example.nikestore.feature.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.nikestore.R
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.common.convertDpToPixel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.example.nikestore.common.setupWithNavController
import com.example.nikestore.data.CartItemCount
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : NikeActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val navGraphIds = listOf(R.navigation.home, R.navigation.cart, R.navigation.profile)

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCartItemsCountChangeEvent(cartItemCount: CartItemCount) {
        val badge = bottomNavigation.getOrCreateBadge(R.id.cart)
        badge.badgeGravity = BadgeDrawable.BOTTOM_END
        badge.backgroundColor = MaterialColors.getColor(bottomNavigation, R.attr.colorPrimary)
        badge.number = cartItemCount.count
        badge.verticalOffset = convertDpToPixel(12f, this).toInt()

        badge.isVisible = cartItemCount.count > 0
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartItemsCount()
    }
}
