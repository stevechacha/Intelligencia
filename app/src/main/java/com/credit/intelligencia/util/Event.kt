package com.credit.intelligencia.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import java.util.concurrent.atomic.AtomicBoolean

class Event<T>(private val value: T) {

    private val isConsumed = AtomicBoolean(false)

    internal fun getValue(): T? =
        if (isConsumed.compareAndSet(false, true)) value
        else null

    fun consume(block: (T) -> Unit): T? = getValue()?.also(block)

    fun peek() = value
}

inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline handleEvent: (T) -> Unit
) {
    observe(owner) { event ->
        event.consume { handleEvent.invoke(it) }
    }
}

fun <T> T.asEvent() = Event(this)