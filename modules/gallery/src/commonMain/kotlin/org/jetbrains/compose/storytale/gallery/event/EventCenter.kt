package org.jetbrains.compose.storytale.gallery.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

interface Event {
    data object CopyCode : Event
}

inline fun Event.send() = EventCenter.send(this)

object EventCenter {
    private val eventFlow = MutableSharedFlow<Event>(extraBufferCapacity = Int.MAX_VALUE)
    val event = eventFlow.asSharedFlow()

    fun <T : Event> send(event: T) = eventFlow.tryEmit(event)

    suspend inline fun <reified T : Event> observe(noinline block: (Event) -> Unit) {
        event.filterIsInstance<T>().collect(block)
    }
}
