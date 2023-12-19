import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.Base64
import java.util.TreeSet
import org.apache.commons.httpclient.URI
import org.apache.hc.client5.http.auth.AuthenticationException
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.Method
import org.apache.log4j.LogManager
import org.parosproxy.paros.network.HtmlParameter
import org.parosproxy.paros.network.HttpHeader
import org.parosproxy.paros.network.HttpMessage
import org.parosproxy.paros.network.HttpRequestHeader
import org.zaproxy.zap.authentication.AuthenticationHelper
import org.zaproxy.zap.authentication.GenericAuthenticationCredentials

val logger = LogManager.getLogger("okta-auth")
val mapper = ObjectMapper()

fun authenticate(
    helper: AuthenticationHelper,
    paramsValues: Map<String, String>,
    credentials: GenericAuthenticationCredentials,
): HttpMessage {
    logger.info("auth hook")

    val oktaDomain = paramsValues["okta_domain"]
    val scope = paramsValues["scope"]
    val clientId = credentials.getParam("client_id")
    val clientSecret = credentials.getParam("client_secret")
    val base64Creds = Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())


    val msg = helper.prepareMessage()
    msg.requestHeader = HttpRequestHeader(
        Method.POST.name,
        URI("https://$oktaDomain/oauth2/default/v1/token", true),
        HttpHeader.HTTP11
    )
    msg.requestHeader.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
    msg.requestHeader.addHeader(HttpHeaders.AUTHORIZATION, "Basic $base64Creds")

    val formTree = TreeSet<HtmlParameter>()
    formTree.add(HtmlParameter(HtmlParameter.Type.form, "grant_type", "client_credentials"))
    formTree.add(HtmlParameter(HtmlParameter.Type.form, "scope", scope))
    msg.requestBody.setFormParams(formTree)
    msg.requestHeader.contentLength = msg.requestBody.length()

    logger.info("::::::auth request:::::\n${msg.requestHeader}${msg.requestBody}")

    helper.sendAndReceive(msg)

    logger.info("::::::auth response:::::\n${msg.responseHeader}${msg.responseBody}")

    // Throw an authentication exception if the status code is not 2xx
    if (!(200..299).contains(msg.responseHeader.statusCode)) {
        val jsonObject = mapper.readValue(msg.responseBody.bytes, ObjectNode::class.java)
        val err = jsonObject.get("error").asText()
        val errDesc = jsonObject.get("error_description").asText()
        throw AuthenticationException("$err $errDesc")
    }

    return msg
}

fun getRequiredParamsNames(): Array<String> {
    return arrayOf("okta_domain", "scope")
}

fun getCredentialsParamsNames(): Array<String> {
    return arrayOf("client_id", "client_secret")
}

fun getOptionalParamsNames(): Array<String> {
    return arrayOf()
}

fun getLoggedInIndicator(): String {
    return ""
}

fun getLoggedOutIndicator(): String {
    return ""
}
