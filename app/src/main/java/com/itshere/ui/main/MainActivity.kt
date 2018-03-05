package com.itshere.ui.main

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.itshere.R
import com.itshere.common.application.BaseActivity
import com.itshere.common.permission.PermissionFragment
import com.itshere.common.permission.PermissionUtil
import com.itshere.common.util.SnackbarUtil
import com.itshere.databinding.MainBinding
import com.itshere.ui.login.LoginActivity
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : BaseActivity(), OnMapReadyCallback {

    companion object {
        private const val REQUEST_LOGIN = 10
    }

    private val binding by lazy(NONE) {
        DataBindingUtil.setContentView<MainBinding>(this, R.layout.main)
    }
    private val mapFragment by lazy(NONE) {
        supportFragmentManager.findFragmentById(R.id.main_map) as SupportMapFragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.mainDrawer.openDrawer(binding.mainNavigation)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        binding.activity = this
        setSupportActionBar(binding.mainToolbar!!.toolbar)
        mapFragment.getMapAsync(this)
        PermissionUtil.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { result, _ ->
                    if (result != PermissionFragment.Result.GRANTED) {
                        SnackbarUtil.showSnackbarToSettings(binding.root, R.string.permission_rationale_location_2)
                    }
                }
    }

    override fun onMapReady(map: GoogleMap) {
    }

    fun onLocationClick() {
        login()
    }

    fun login() {
        overridePendingTransition(0, 0)
        startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_LOGIN)
    }
}