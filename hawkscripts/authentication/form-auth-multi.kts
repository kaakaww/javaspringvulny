import com.fasterxml.jackson.databind.ObjectMapper
import com.stackhawk.hste.extension.talon.HawkConfExtensions
import com.stackhawk.hste.extension.talon.cleanHost
import com.stackhawk.hste.extension.talon.hawkscan.ExtensionTalonHawkscan
import org.apache.commons.httpclient.URI
import org.apache.log4j.LogManager
import org.parosproxy.paros.control.Control
import org.parosproxy.paros.network.HttpHeader
import org.parosproxy.paros.network.HttpMessage
import org.parosproxy.paros.network.HttpRequestHeader
import com.stackhawk.hste.authentication.AuthenticationHelper
import com.stackhawk.hste.authentication.GenericAuthenticationCredentials

val logger = LogManager.getLogger("form-auth-multi")

val talon = Control
    .getSingleton()
    .extensionLoader
    .getExtension(ExtensionTalonHawkscan::class.java)

fun hostUrl(path: String): String {
    return "${HawkConfExtensions.cleanHost(talon.talonHawkScanConf.hawkscanConf.app)}$path"
}

// This function is called before a scan is started and when the loggedOutIndicator is matched indicating re-authentication is needed.
fun authenticate(
    helper: AuthenticationHelper,
    paramsValues: Map<String, String>,
    credentials: GenericAuthenticationCredentials,
): HttpMessage {
    logger.info("Kotlin auth template")
    logger.info("TalonConf: ${talon.talonHawkScanConf}")
    logger.info("host ${talon.talonHawkScanConf.hawkscanConf.app.cleanHost()}")

    val mapper = ObjectMapper()
    val payload = mapper.writeValueAsString(
        mapOf(
            "username" to credentials.getParam("username"),
            "password" to credentials.getParam("password"),
        ),
    )

    logger.info("payload? $payload")

    val loginPagePathUrl = hostUrl(paramsValues["loginPagePath"]!!)
    logger.info("TARGET_URL: $loginPagePathUrl")
    val msg = helper.prepareMessage()
    msg.requestHeader = HttpRequestHeader(
        HttpRequestHeader.GET,
        URI(loginPagePathUrl, true),
        HttpHeader.HTTP11,
    )
    logger.info("msg: ${msg.requestHeader} ${msg.requestBody} ${msg.requestHeader.headers.size}")
    msg.requestHeader.headers.forEach { println(it) }
    helper.sendAndReceive(msg)
    logger.info("resp: ${msg.responseHeader} ${msg.responseBody} ")

    if (msg.responseBody.length() > 0) {
        val map = mapper.readValue(msg.responseBody.bytes, Map::class.java)
        logger.info("map $map")
    } else {
        logger.info("no body to parse")
    }
    return msg
}

// The required parameter names for your script, your script will throw an error if these are not supplied in the script.parameters configuration.
fun getRequiredParamsNames(): Array<String> {
    return arrayOf("loginPagePath", "loginPage", "remember")
}

// The required credential parameters, your script will throw an error if these are not supplied in the script.credentials configuration.
fun getCredentialsParamsNames(): Array<String> {
    return arrayOf("username", "password")
}

fun getOptionalParamsNames(): Array<String> {
    return arrayOf("logging", "formType", "csrfExtra")
}

fun getLoggedInIndicator(): String {
    return ""
}

fun getLoggedOutIndicator(): String {
    return ""
}
