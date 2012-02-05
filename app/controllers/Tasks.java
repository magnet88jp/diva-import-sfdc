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
    String authorizeUrl = Play.configuration.get("local.sfdc.authorizeUrl").toString();
    String redirectUrl = Play.configuration.get("local.sfdc.redirectUrl").toString();
    String clientId = Play.configuration.get("local.sfdc.clientId").toString();

    String url = authorizeUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl;
    redirect(url);
  }

  public static void callback() {
    String code = params.get("code");
    new ImportSfdc(code).now();

    redirect("/tasks/list");
  }
}
