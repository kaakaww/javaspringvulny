import org.apache.log4j.LogManager
import org.parosproxy.paros.network.HttpMessage
import org.zaproxy.zap.extension.script.HttpSenderScriptHelper
import org.zaproxy.zap.extension.script.ScriptVars

val logger = LogManager.getLogger("sender1")

// modify a request before it's sent to the web application
fun sendingRequest(msg: HttpMessage, initiator: Int, helper: HttpSenderScriptHelper) {
    // logger.info("custom-sender script $initiator")
    msg.requestHeader.setHeader("X-ZAP-Initiator", "$initiator")
}

// modify the response from the web application before sending to the client
fun responseReceived(msg: HttpMessage, initiator: Int, helper: HttpSenderScriptHelper) {
    msg.responseHeader.setHeader("X-ZAP-Initiator", "$initiator")
}
