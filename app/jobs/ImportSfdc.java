package jobs;

import java.util.*;
import play.*;
import play.jobs.*;
import play.libs.WS;
import com.google.gson.*;
import models.*;

public class ImportSfdc extends Job {

  public String code;

  public ImportSfdc(String code) {
    this.code = code;
  }

  public void doJob() {
    String tokenUrl = Play.configuration.get("local.sfdc.tokenUrl").toString();
    String redirectUrl = Play.configuration.get("local.sfdc.redirectUrl").toString();
    String clientId = Play.configuration.get("local.sfdc.clientId").toString();
    String clientSecret = Play.configuration.get("local.sfdc.clientSecret").toString();

    String url = tokenUrl + "?grant_type=authorization_code&code=" + code + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectUrl;
    JsonElement tokens = WS.url(url).post().getJson(); 
    String accessToken = tokens.getAsJsonObject().get("access_token").getAsString();
    String instanceUrl = tokens.getAsJsonObject().get("instance_url").getAsString();

    url = instanceUrl + "/services/data/v23.0/query/?q=SELECT Id, Subject, Description from Case";
    JsonElement cases = WS.url(url).setHeader("Authorization", "OAuth " + accessToken).get().getJson();
    Logger.info(cases.toString());

    Iterator<JsonElement> records = cases.getAsJsonObject().get("records").getAsJsonArray().iterator();
    while(records.hasNext()) {
      JsonElement attributes = records.next();
      String caseId = attributes.getAsJsonObject().get("Id").getAsString();
      String subject = attributes.getAsJsonObject().get("Subject").getAsString();
      JsonElement elem = attributes.getAsJsonObject().get("Description");
      String description = elem.isJsonNull() ? "" : elem.getAsString();

      Inquiry inquiry = null;
      List<Inquiry> inquirys = Inquiry.find("byCode", caseId ).fetch();
      if(inquirys.size() == 0) {
        inquiry = new Inquiry(caseId, subject, description, "");
      } else {
        inquiry = inquirys.get(0);
        inquiry.subject = subject;
        inquiry.question = description;
      }
      inquiry._save();
      
    }
  }
}
