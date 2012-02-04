package controllers;

import play.*;
import play.mvc.*;
import jobs.*;

public class Tasks extends CRUD {

  @Before(only={"blank"})
  static void initialize(String type) {
    params.put("object.type", type);
  }

  @After(only={"create", "save"})
  static void execute(String type) {
    String authorizeUrl = "https://login.salesforce.com/services/oauth2/authorize";
    String redirectUrl = "http://localhost:9000/tasks/callback";
    String clientId = "3MVG9yZ.WNe6byQCV4mr8WsWmBGLuWoHBzQouSeZkvuy6OqJ2SvpXG1biHwyrmSDCuVwqcXo_gU9htj8mLCS_";

    String url = authorizeUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl;
    redirect(url);
  }

  public static void callback() {
    String code = params.get("code");
    new ImportSfdc(code).now();

    redirect("/tasks/list");
  }
}
