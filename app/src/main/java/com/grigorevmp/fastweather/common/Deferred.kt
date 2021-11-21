package com.grigorevmp.fastweather.common

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred

suspend fun <T> Task<T>.asDeferred(): T {
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener { result->
        deferred.complete(result)
    }

    this.addOnFailureListener{ exception ->
        deferred.completeExceptionally(exception)
    }

    return deferred.await()
}