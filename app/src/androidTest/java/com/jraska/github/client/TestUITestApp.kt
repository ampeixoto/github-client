package com.jraska.github.client

import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.http.ReplayHttpComponent

class TestUITestApp : GitHubClientApp() {
  val coreComponent = FakeCoreComponent()

  override fun retrofit(): HasRetrofit {
    return ReplayHttpComponent.create()
  }

  override fun coreComponent(): CoreComponent {
    return coreComponent
  }

  companion object {
    fun get(): TestUITestApp {
      return InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestUITestApp
    }
  }
}
