/*
 * Copyright 2014-2025 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.client.engine.js.compatibility

import io.ktor.client.engine.js.*
import io.ktor.client.engine.js.browser.*
import io.ktor.client.fetch.*
import io.ktor.client.utils.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.js.Promise

@OptIn(InternalCoroutinesApi::class)
internal suspend fun commonFetch(
    input: String,
    init: RequestInit,
    config: JsClientEngineConfig,
    callJob: Job,
): org.w3c.fetch.Response = suspendCancellableCoroutine { continuation ->
    val controller = AbortController()
    init.signal = controller.signal

    val abortOnCallCompletion = callJob.invokeOnCompletion(onCancelling = true) {
        controller.abort()
    }

    val promise: Promise<org.w3c.fetch.Response> = when {
        PlatformUtils.IS_BROWSER -> fetch(input, init)
        else -> {
            val options = makeJsCall<RequestInit>(
                jsObjectAssign(),
                makeJsObject<RequestInit>(),
                init,
                config.nodeOptions,
            )
            fetch(input, options)
        }
    }

    promise.then(
        onFulfilled = { x: JsAny ->
            continuation.resumeWith(Result.success(x.unsafeCast()))
            null
        },
        onRejected = { it: JsAny ->
            continuation.resumeWith(Result.failure(Error("Fail to fetch", JsError(it))))
            null
        }
    ).finally { abortOnCallCompletion.dispose() }
}

private fun AbortController(): AbortController {
    val ctor = abortControllerCtorBrowser()
    return makeJsNew(ctor)
}

private fun abortControllerCtorBrowser(): AbortController = js("AbortController")

internal fun CoroutineScope.readBody(
    response: org.w3c.fetch.Response
): ByteReadChannel =
    readBodyBrowser(response)
