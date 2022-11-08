import org.apache.commons.httpclient.URI
import org.apache.log4j.LogManager
import org.parosproxy.paros.network.HttpHeader
import org.parosproxy.paros.network.HttpMessage
import org.parosproxy.paros.network.HttpRequestHeader
import org.zaproxy.zap.authentication.AuthenticationHelper
import org.zaproxy.zap.authentication.GenericAuthenticationCredentials

val logger = LogManager.getLogger("authentication-template")

val PARAM_TARGET_URL = "targetUrl"

// This function is called before a scan is started and when the loggedOutIndicator is matched indicating re-authentication is needed.
fun authenticate(
    helper: AuthenticationHelper,
    paramsValues: Map<String, String>,
    credentials: GenericAuthenticationCredentials
): HttpMessage {
    logger.info("Kotlin auth template")

    logger.info("TARGET_URL: ${paramsValues[PARAM_TARGET_URL]}")
    val msg = helper.prepareMessage()
    msg.requestHeader = HttpRequestHeader(
        HttpRequestHeader.GET,
        URI(paramsValues[PARAM_TARGET_URL], true),
        HttpHeader.HTTP11
    )
    logger.info("msg: $msg ${msg.requestHeader.headers.size}")
    msg.requestHeader.headers.forEach { println(it) }
    helper.sendAndReceive(msg)
    return msg
}

// The required parameter names for your script, your script will throw an error if these are not supplied in the script.parameters configuration.
fun getRequiredParamsNames(): Array<String> {
    return arrayOf(PARAM_TARGET_URL)
}

// The required credential parameters, your script will throw an error if these are not supplied in the script.credentials configuration.
fun getCredentialsParamsNames(): Array<String> {
    return arrayOf()
}

fun getOptionalParamsNames(): Array<String> {
    return arrayOf()
}
