package com.todolist.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object KInstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        //val isoFormatterWithMilliseconds = DateTimeFormatter.ofPattern("YYYY-MM-DD'T'HH:mm:ss.ZZ'Z'")
        //val isoDateTimeString = isoFormatterWithMilliseconds.format(value)
        //encoder.encodeString(isoDateTimeString)
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return Instant.parse(string)
    }
}
