import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.time.Instant
import org.apache.log4j.LogManager
import org.zaproxy.zap.session.ScriptBasedSessionManagementMethodType
import org.zaproxy.zap.extension.script.ScriptVars

val logger = LogManager.getLogger("okta-json-to-token")
val mapper = ObjectMapper()

fun extractWebSession(sessionWrapper: ScriptBasedSessionManagementMethodType.SessionWrapper) {

    val tokenField = sessionWrapper.getParam("jwt_token_field")
    val tokenType = sessionWrapper.getParam("token_type_field") ?: "Bearer"

    logger.info("get token from json: ${sessionWrapper.httpMessage.responseBody}")
    val jsonObject = mapper.readValue(sessionWrapper.httpMessage.responseBody.bytes, ObjectNode::class.java)
    val accessToken = jsonObject.get(tokenField).asText()
    ScriptVars.setGlobalVar("auth_header_value", "$tokenType $accessToken")

    sessionWrapper.session.setValue("jwt", accessToken)

    val jwt = SignedJWT.parse(accessToken)
    logger.info("jwt-expires: ${jwt.jwtClaimsSet.expirationTime}")
    sessionWrapper.session.setValue("jwt_claims", jwt.jwtClaimsSet)
}

fun processMessageToMatchSession(sessionWrapper: ScriptBasedSessionManagementMethodType.SessionWrapper) {

    val nowish = Instant.now().minusMillis(15000)
    val jwtClaims = sessionWrapper.session.getValue("jwt_claims") as JWTClaimsSet?
    val isExpired = jwtClaims?.expirationTime?.toInstant()?.isBefore(nowish)

    if (isExpired == true) {
        logger.info("session expires @ ${jwtClaims.expirationTime}")
        synchronized(this) {
            sessionWrapper.httpMessage.requestingUser.authenticate()
        }
    }

    logger.debug("session-jwt: ${sessionWrapper.session.getValue("jwt")}")

    val hdrVal = ScriptVars.getGlobalVar("auth_header_value")
    logger.debug("auth_header_value: $hdrVal")
    if (!hdrVal.isNullOrEmpty()) {
        sessionWrapper.httpMessage.requestHeader.setHeader("Authorization", hdrVal)
    }

}

fun clearWebSessionIdentifiers(sessionWrapper: ScriptBasedSessionManagementMethodType.SessionWrapper) {
}

fun getRequiredParamsNames(): Array<String> {
    return arrayOf("jwt_token_field")
}

fun getOptionalParamsNames(): Array<String> {
    return arrayOf("token_type_field")
}