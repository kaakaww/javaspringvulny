package hawk.api.okta;

public class OktaIdInfo {
    private final String token;

    public OktaIdInfo(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
