package jobs;

import play.jobs.*;
import play.Logger;
import play.libs.WS;
import com.google.gson.JsonElement;

public class ImportSfdc extends Job {

  public String code;

  public ImportSfdc(String code) {
    this.code = code;
  }

  public void doJob() {
    String tokenUrl = "https://login.salesforce.com/services/oauth2/token";
    String redirectUrl = "http://localhost:9000/tasks/callback";
    String clientId = "3MVG9yZ.WNe6byQCV4mr8WsWmBGLuWoHBzQouSeZkvuy6OqJ2SvpXG1biHwyrmSDCuVwqcXo_gU9htj8mLCS_";
    String clientSecret = "9207928975343564387"; 

    String url = tokenUrl + "?grant_type=authorization_code&code=" + code + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectUrl;
    JsonElement tokens = WS.url(url).post().getJson(); 
    String accessToken = tokens.getAsJsonObject().get("access_token").getAsString();
    String instanceUrl = tokens.getAsJsonObject().get("instance_url").getAsString();

    url = instanceUrl + "/services/data/v23.0/query/?q=SELECT subject from Case";
    JsonElement cases = WS.url(url).setHeader("Authorization", "OAuth " + accessToken).get().getJson();

    Logger.info(cases.toString());
  }
}
