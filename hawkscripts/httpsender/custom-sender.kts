import com.stackhawk.hste.extension.talon.hawkscan.ExtensionTalonHawkscan
import org.apache.log4j.LogManager
import org.parosproxy.paros.control.Control
import org.parosproxy.paros.network.HttpMessage
import com.stackhawk.hste.extension.script.HttpSenderScriptHelper

val logger = LogManager.getLogger("custom-http-sender")

val talon = Control
    .getSingleton()
    .extensionLoader
    .getExtension(ExtensionTalonHawkscan::class.java)

// modify a request before it's sent to the web application
fun sendingRequest(msg: HttpMessage, initiator: Int, helper: HttpSenderScriptHelper) {
    logger.info("req ${msg.requestHeader.uri}")
    msg.requestHeader.setHeader("X-HawkScanId", talon.talonHawkScanConf.scanId)
}

// modify the response from the web application before sending to the client
fun responseReceived(msg: HttpMessage, initiator: Int, helper: HttpSenderScriptHelper) {
}
