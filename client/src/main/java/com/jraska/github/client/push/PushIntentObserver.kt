package com.jraska.github.client.push

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.OnAppCreate
import javax.inject.Inject

class PushIntentObserver(private val pushHandler: PushHandler) : LifecycleObserver {

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate(owner: LifecycleOwner) {
    if (owner !is Activity) {
      return
    }

    val activity = owner as Activity
    val intent = activity.intent
    if (isPush(intent)) {
      val message = RemoteMessage(intent.extras!!)
      val action = RemoteMessageToActionConverter.convert(message)

      pushHandler.handlePush(action)
    }
  }

  private fun isPush(intent: Intent): Boolean {
    if (Intent.ACTION_MAIN != intent.action || intent.extras == null) {
      return false
    }

    for (key in intent.extras!!.keySet()) {
      if (key.startsWith("google.sent_time")) {
        return true
      }

      if (key.startsWith("google.to")) {
        return true
      }

      if (key.startsWith("gcm.")) {
        return true
      }
    }

    return false
  }

  class CallbacksSetup @Inject constructor(
    private val pushHandler: PushHandler
  ) : OnAppCreate {

    override fun onCreate(app: Application) {
      app.registerActivityLifecycleCallbacks(PushCallbacks(PushIntentObserver(pushHandler)))
    }
  }
}
