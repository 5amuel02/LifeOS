package com.example.lifeos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point for Hilt. `@HiltAndroidApp` triggers code generation of
 * the app-level dependency container that the rest of the graph hangs off of.
 */
@HiltAndroidApp
class LifeOSApplication : Application()
