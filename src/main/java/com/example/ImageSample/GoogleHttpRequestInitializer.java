package com.example.ImageSample;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;//3
//3
//HTTP リクエスト初期化子。
//何らかの機能を拡張するため ?
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;

@Getter @Setter
public class GoogleHttpRequestInitializer implements HttpRequestInitializer {
    GoogleCredentials credentials; //HTTP に適応する認証情報インスタンス
    HttpCredentialsAdapter adapter;

    @Override
    public void initialize(HttpRequest httpRequest) throws IOException {
        credentials = GoogleCredentials
                .getApplicationDefault() //1
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));//2
        //(1)
        //doc : https://cloud.google.com/java/docs/reference/google-auth-library/latest/com.google.auth.oauth2.GoogleCredentials#com_google_auth_oauth2_GoogleCredentials_getApplicationDefault__
        //アプリケーションのデフォルト認証情報を見つける。
        //GOOGLE_APPLICATION_CREDENTIALS環境変数 が指す認証情報ファイルを読み込む。

        //(2)
        //@param String[] scopes
        //doc : https://cloud.google.com/java/docs/reference/google-auth-library/latest/com.google.auth.oauth2.GoogleCredentials#com_google_auth_oauth2_GoogleCredentials_createScoped_java_lang_String____
        //資格情報がスコープをサポートしている場合は、指定されたスコープで ID のコピーを作成します。それ以外の場合は、同じインスタンスを返します。

        adapter = new HttpCredentialsAdapter(credentials);

        adapter.initialize(httpRequest);
        // doc : https://cloud.google.com/java/docs/reference/google-auth-library/latest/com.google.auth.http.HttpCredentialsAdapter#com_google_auth_http_HttpCredentialsAdapter_initialize_com_google_api_client_http_HttpRequest_
        //実行前に HTTP 要求を初期化します。

        httpRequest.setConnectTimeout(60000); // 1 minute connect timeout
        httpRequest.setReadTimeout(60000);
    }
}
