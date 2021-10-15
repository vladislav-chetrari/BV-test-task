package vlad.chetrari.bvtesttask.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import vlad.chetrari.bvtesttask.data.util.Base64Encoder
import vlad.chetrari.bvtesttask.di.Twitter
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterOAuthInterceptor @Inject constructor(
    private val encoder: Base64Encoder,
    @Twitter.OAuth.Key
    private val key: String,
    @Twitter.OAuth.AccessToken
    private val token: String,
    @Twitter.OAuth.KeySecret
    private val apiKeySecret: String,
    @Twitter.OAuth.AccessSecret
    private val apiAccessSecret: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val oauthMap = mutableMapOf(
            "oauth_consumer_key" to key,
            "oauth_nonce" to encoder.encode(UUID.randomUUID().toString().toByteArray()),
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to (System.currentTimeMillis() / 1000L).toString(),
            "oauth_token" to token,
            "oauth_version" to "1.0"
        )
        oauthMap["oauth_signature"] = oauthSignature(request, oauthMap)
        Timber.d("oauth_signature = ${oauthMap["oauth_signature"]}")

        val authHeadVal = authorizationHeaderValue(oauthMap)
        return chain.proceed(
            request.newBuilder()
                .addHeader("Authorization", authHeadVal)
                .build()
        )
    }

    private fun authorizationHeaderValue(oauthMap: MutableMap<String, String>) = "OAuth " + oauthMap.toSortedMap()
        .map { entry -> "${percentEncode(entry.key)}=\"${percentEncode(entry.value)}\"" }
        .joinToString(", ")

    /*
    * For additional info on OAuth signature compile, please visit https://developer.twitter.com/en/docs/authentication/oauth-1-0a/creating-a-signature
    * */
    private fun oauthSignature(
        request: Request,
        oauthMap: MutableMap<String, String>
    ): String {
        Timber.d("oauthSignature: oauthMap=$oauthMap")
        //1.    percent encode
        val percentEncodedMap = oauthMap.map { entry ->
            percentEncode(entry.key) to percentEncode(entry.value)
        }.toMap().toMutableMap()
        Timber.d("oauthSignature: percentEncodedMap=$percentEncodedMap")
        //1.1.  add query params to map
        request.url.encodedQuery
            ?.split("&")
            ?.map { it.split("=") }
            ?.filter { it.size == 2 }
            ?.forEach { percentEncodedMap[it[0]] = it[1] }
        Timber.d("oauthSignature: percentEncodedMap=$percentEncodedMap")
        //2.    sort by keys
        val sortedMap = percentEncodedMap.toSortedMap()
        Timber.d("oauthSignature: sortedMap=$sortedMap")
        //3.    parameter string
        val paramStr = sortedMap.map { entry -> "${entry.key}=${entry.value}" }.joinToString(separator = "&")
        Timber.d("oauthSignature: paramStr=$paramStr")
        //4.    create signature base string
        val cleanUrl = request.url.toString().substringBefore("?")
        val signatureBaseString = request.method.uppercase() + "&" + percentEncode(cleanUrl) + "&" + percentEncode(paramStr)
        Timber.d("oauthSignature: signatureBaseString=$signatureBaseString")
        //5.    signing key
        val signingKey = "${percentEncode(apiKeySecret)}&${percentEncode(apiAccessSecret)}"
        Timber.d("oauthSignature: signingKey=$signingKey")
        //6.    calculating the signature
        return hashHmac(signatureBaseString, signingKey)
    }

    private fun percentEncode(value: String) = URLEncoder.encode(value, "utf-8")

    private fun hashHmac(str: String, secret: String): String {
        val sha1HMAC = Mac.getInstance("HMAC-SHA1")
        val secretKey = SecretKeySpec(secret.toByteArray(), "HMAC-SHA1")
        sha1HMAC.init(secretKey)
        return encoder.encode(sha1HMAC.doFinal(str.toByteArray()))
    }
}