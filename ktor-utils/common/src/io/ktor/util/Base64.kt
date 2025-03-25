/*
* Copyright 2014-2025 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.util

import io.ktor.utils.io.core.*
import kotlinx.io.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Encode [String] in base64 format and UTF-8 character encoding.
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.util.encodeBase64)
 */
@OptIn(ExperimentalEncodingApi::class)
public fun String.encodeBase64(): String =
    Base64.Default.encode(this.toByteArray())

/**
 * Encode [ByteArray] in base64 format
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.util.encodeBase64)
 */
@OptIn(ExperimentalEncodingApi::class)
public fun ByteArray.encodeBase64(): String =
    Base64.Default.encode(this)

/**
 * Encode [ByteReadPacket] in base64 format
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.util.encodeBase64)
 */
public fun Source.encodeBase64(): String = readByteArray().encodeBase64()

/**
 * Decode [String] from base64 format encoded in UTF-8.
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.util.decodeBase64String)
 */
public fun String.decodeBase64String(): String {
    val bytes = decodeBase64Bytes()
    return bytes.decodeToString(0, 0 + bytes.size)
}

/**
 * Decode [String] from base64 format
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.util.decodeBase64Bytes)
 */
@OptIn(ExperimentalEncodingApi::class)
public fun String.decodeBase64Bytes(): ByteArray =
    Base64.Default.decode(this)

/**
 * Decode [ByteReadPacket] from base64 format
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.util.decodeBase64Bytes)
 */
@OptIn(ExperimentalEncodingApi::class)
public fun Source.decodeBase64Bytes(): Input = buildPacket {
    writeFully(Base64.Default.decode(this@decodeBase64Bytes.readByteArray()))
}
