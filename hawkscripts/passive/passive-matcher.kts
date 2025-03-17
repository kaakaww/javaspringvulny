import com.stackhawk.hste.extension.pscan.PluginPassiveScanner
import com.stackhawk.hste.extension.script.ScriptVars
import com.stackhawk.hste.extension.scripts.scanrules.ScriptsPassiveScanner
import net.htmlparser.jericho.Source
import org.apache.log4j.Logger
import org.parosproxy.paros.network.HttpMessage
import javax.script.ScriptException

val scriptName = "passive-matcher.kts"
val scriptVars = ScriptVars.getScriptVars(scriptName)
val LOG = Logger.getLogger(scriptVars["hawk_script_name"]?.replace(".", "_"))

LOG.info("Script loading with vars $scriptVars")
var badMatcher = false
var matchRe: Regex? = null
fun loadMatcher() = if (scriptVars == null || scriptVars["MATCH"].isNullOrBlank()) {
    LOG.error("Script variable 'MATCH' not found")
} else {
    try {
        matchRe = scriptVars["MATCH"]?.toRegex()
    }catch (e: Exception) {
        LOG.error(e.message, e)
        badMatcher = true
        null
    }
}

val matchFor = scriptVars["MATCH"]

@Throws(ScriptException::class)
fun scan(scriptsPassiveScanner: ScriptsPassiveScanner, msg: HttpMessage, source: Source)
{

    if (!badMatcher && matchRe == null) {
        LOG.info("Loading matcher: $matchFor")
        loadMatcher()
    }

    if (matchRe != null) {
        LOG.info("Checking request: ${msg.requestHeader.uri}")
        val match = matchRe?.find(msg.responseBody.toString())
        match?.let {
            val groupValues = it.groupValues
            LOG.info("Match found: ${groupValues.first()}")
            alert(scriptsPassiveScanner, msg, groupValues.first())
        }
    }

}



fun alert(passiveScanner: ScriptsPassiveScanner, msg: HttpMessage, evidence: String) {
    val risk = 2 // 0: info, 1: low, 2: medium, 3: high
    val confidence = 3 // 0: falsePositive, 1: low, 2: medium, 3: high, 4: confirmed
    val title = "Found match ${matchFor ?: "''"}"
    val description = "The regex ${matchFor ?: "''"} defined in the script matched the response body."
    val solution = "This matching response body violates corpo policy chumba"
    val reference = "https://securityexecutivecouncil.com/insight/program-best-practices/corporate-security-policy-template-31043-1185"
    val otherInfo = "I took a nap!"
    val pluginId = scriptVars["hawk_script_id"]?.toInt() ?: 0

    if (pluginId == 0) {
        LOG.error("StackHawk script id not set, no alert will be raised")
        return
    }
    passiveScanner.newAlert()
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
        .setSourceHistoryId(msg.historyRef.historyId)
        .raise()

}

fun appliesToHistoryType(historyType: Int): Boolean {
    return PluginPassiveScanner.getDefaultHistoryTypes().contains(historyType)
    // return true
}
