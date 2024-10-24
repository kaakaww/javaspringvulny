import com.github.javafaker.Faker
import com.stackhawk.hste.extension.script.ScriptVars
import com.stackhawk.hste.extension.scripts.scanrules.ScriptsActiveScanner
import org.apache.log4j.LogManager
import org.parosproxy.paros.network.HttpMessage

val logger = LogManager.getLogger("fuzzer")

val faker = Faker()
val scriptVars = ScriptVars.getScriptVars("fuzzer.kts")

fun alert(activeScanner: ScriptsActiveScanner, msg: HttpMessage, evidence: String, param: String, fuzzedParam: String) {
    val risk = 2 // 0: info, 1: low, 2: medium, 3: high
    val confidence = 3 // 0: falsePositive, 1: low, 2: medium, 3: high, 4: confirmed
    val title = "Fuzzer found a 5xx error"
    val description = "Fuzzer was able to find a 5xx error"
    val solution = "Handle bad input and never throw a 5xx error"
    val reference = ""
    val otherInfo = "fuzzed param: $param=$fuzzedParam"
    val pluginId = 10_00_063; //Custom Plugin ID

    activeScanner.newAlert()
        .setPluginId(pluginId)
        .setRisk(risk)
        .setConfidence(confidence)
        .setName(title)
        .setDescription(description)
        .setEvidence(evidence)
        .setOtherInfo(otherInfo)
        .setSolution(solution)
        .setReference(reference)
        .setMessage(msg)
        .raise();
}

fun scanNode(activeScanner: ScriptsActiveScanner, origMessage: HttpMessage) {
    logger.debug("scanNode fuzzer hook: ${origMessage.requestHeader.uri}")
    return
}

fun scan(activeScanner: ScriptsActiveScanner, origMessage: HttpMessage, param: String, value: String) {
    logger.debug("scan fuzzer hook: ${origMessage.requestHeader.uri} | ${param}=${value}")
    val iterations = scriptVars["iterations"]?.toInt() ?: 1
    val stringStartLength = scriptVars["stringStartLength"]?.toInt() ?: 1
    val stringEndLength = scriptVars["stringEndLength"]?.toInt() ?: 100
    (1..iterations).forEach { i ->
        val msg = origMessage.cloneRequest()
        val fuzzedParamValue = if (i % 2 == 0) {
            faker.lorem().characters(stringStartLength, stringEndLength)
        } else {
            faker.harryPotter().spell()
        }

        if (param.isNotBlank()) {
            activeScanner.setParam(msg, param, fuzzedParamValue)
        }
        try {
            activeScanner.sendAndReceive(msg, false, false)
            if (msg.responseHeader.statusCode >= 500) {
                logger.debug("request: ${msg.requestHeader}${msg.requestBody}")
                alert(activeScanner, msg, msg.responseHeader.primeHeader, param, fuzzedParamValue)
                logger.debug("response: ${msg.responseHeader.statusCode} ${msg.responseHeader}${msg.responseBody}")
            }
        } catch (e: Exception) {
            logger.error("Error sending request: ${e.message}")
        }
    }

}