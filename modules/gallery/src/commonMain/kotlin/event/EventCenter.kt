package org.jetbrains.compose.storytale.gallery.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

interface AppEvent {
  data object CopyCode : AppEvent
}

inline fun AppEvent.send() = EventCenter.send(this)

object EventCenter {
  private val eventFlow = MutableSharedFlow<AppEvent>(extraBufferCapacity = Int.MAX_VALUE)
  val event = eventFlow.asSharedFlow()

  fun <T : AppEvent> send(event: T) = eventFlow.tryEmit(event)

  suspend inline fun <reified T : AppEvent> observe(noinline block: (AppEvent) -> Unit) {
    event.filterIsInstance<T>().collect(block)
  }
}
