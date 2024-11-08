/*
 * Copyright 2014-2024 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.network.selector

import kotlinx.coroutines.*

/**
 * A selectable entity with selectable NIO [channel], [interestedOps] subscriptions.
 */
public expect interface Selectable

public class ClosedChannelCancellationException : CancellationException("Closed channel.")