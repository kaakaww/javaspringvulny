var HttpRequestHeader = Java.type('org.parosproxy.paros.network.HttpRequestHeader');
var HttpHeader = Java.type("org.parosproxy.paros.network.HttpHeader");
var URI = Java.type('org.apache.commons.httpclient.URI');
var AuthenticationHelper = Java.type('org.zaproxy.zap.authentication.AuthenticationHelper');
var Base64 = Java.type("java.util.Base64");

function authenticate(helper, paramsValues, credentials) {
    print("Azure grant_type=ROPC...");

    // set login path and http
    var requestUri = new URI(`https://login.microsoftonline.com/${paramsValues.get("tenant")}/oauth2/v2.0/token`, false);
    var requestMethod = HttpRequestHeader.POST;
    var requestHeader = new HttpRequestHeader(requestMethod, requestUri, HttpHeader.HTTP11);

    //build static request headers
    requestHeader.setHeader("Content-Type", "application/x-www-form-urlencoded");
    requestHeader.setHeader("Accept", "application/json");
    requestHeader.setHeader("Cache-control", "no-cache");

    //build request body
    var requestBody = "" + "client_id=" + credentials.getParam('client_id') +
        "&scope=" + paramsValues.get('scope') +
        "&username=" + credentials.getParam('username') +
        "&password=" + credentials.getParam('password') +
        "&grant_type=" + paramsValues.get('grant_type');

    // build final post
    var msg = helper.prepareMessage();
    msg.setRequestHeader(requestHeader);
    msg.setRequestBody(requestBody);
    print("MSG request header: " + msg.requestHeader);
    print("MSG request body: " + msg.requestBody);
    requestHeader.contentLength = msg.requestBody.length();

    //send message
    helper.sendAndReceive(msg);

    return msg;
}

function getRequiredParamsNames(){
    return ["tenant", "grant_type"];
}

function getOptionalParamsNames(){
    return ["scope"];
}

function getCredentialsParamsNames(){
    return ["client_id", "username", "password"];