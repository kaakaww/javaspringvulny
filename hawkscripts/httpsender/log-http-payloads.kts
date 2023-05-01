
import org.apache.log4j.LogManager
import org.parosproxy.paros.network.HttpMessage
import org.zaproxy.zap.extension.script.HttpSenderScriptHelper

val logger = LogManager.getLogger("log-http-payloads")

// modify a request before it's sent to the web application
fun sendingRequest(msg: HttpMessage, initiator: Int, helper: HttpSenderScriptHelper) {

}

// modify the response from the web application before sending to the client
fun responseReceived(msg: HttpMessage, initiator: Int, helper: HttpSenderScriptHelper) {
    val httpRequestAndResponse = msg.requestHeader.toString() +
        msg.requestBody.toString() +
        msg.responseHeader.toString() +
        msg.responseBody.toString()

    logger.info("request/response: $httpRequestAndResponse")
}
