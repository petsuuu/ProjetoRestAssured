package br.com.petsu.core;

import io.restassured.http.ContentType;

public interface Constantes {
    String APP_BASE_URl = "https://barrigarest.wcaquino.me";
    Integer APP_PORT = 443;  //htp ->80
    String APP_BASE_PATH = "";

    ContentType APP_CONTENT_TYPE = ContentType.JSON;

    Long MAX_TIMEOUT = 5000L;

    String APP_PROXY= "spobrproxy.serasa.intranet";
    Integer APP_PROXY_PORT = 3128;


}