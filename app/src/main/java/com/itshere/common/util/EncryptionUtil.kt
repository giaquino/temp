package com.itshere.common.util

import android.util.Base64
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class EncryptionUtil {

    fun encrypt(text: String, publicKey: String): String {
        val factory = KeyFactory.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
        val spec = X509EncodedKeySpec(
                Base64.decode(publicKey.toByteArray(Charsets.UTF_8), Base64.DEFAULT))
        val key = factory.generatePublic(spec)

        val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val result = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        return String(Base64.encode(result, Base64.DEFAULT))
    }
}