package ais_plus.controller;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ais_plus.model.Login_Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {

    public String Autoriz1(String username, String password) throws IOException {
        Login_Model login_model =new Login_Model();
        CookieStore httpCookieStore = new BasicCookieStore();
        login_model.username =username;
        login_model.password =password;
        String postUrl       = "http://192.168.99.91/cpgu/action/user/login";// put in your url
        Gson gson          = new Gson();
        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(login_model));//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
        List<Cookie> cookies = httpCookieStore.getCookies();
        //System.out.println("Login s incorrect!");
        String error="";
        if (cookies.size()==0){
            error="Login or password is incorrect!";
            return error;
        } else {
            return cookies.get(0).getValue();
        }
        //return cookies.get(0).getValue();

    }

    public String Autoriz2 (String username, String password,String cookie) throws IOException {
        Login_Model login_model =new Login_Model();
        CookieStore httpCookieStore = new BasicCookieStore();
        login_model.username = username;
        login_model.password= password;
        String postUrl       = "http://192.168.99.91/cpgu/action/user/login";// put in your url
        Gson gson          = new Gson();
        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(login_model));//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(post);
        //String payl= gson.toJson(payload);
        return response.getStatusLine().toString();
        //return payl;

    }

    public String Get_admin_role(String cookie) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();

        Login_Model login_model = new Login_Model();
        ArrayList<Login_Model> loginModelArr=new ArrayList<Login_Model>();

        Object[] data1 = new Object[0];
        login_model.action="authController";
        login_model.method="getCurrentUser";
        login_model.data=data1;
        login_model.type="rpc";
        login_model.tid=1;
        loginModelArr.add(login_model);

        Object[] data2 = new Object[2];
        data2[0] = 35149;
        data2[1] = "LOGIN";

        Login_Model login_model2 = new Login_Model();
        login_model2.action="changeWorkingAttributesService";
        login_model2.method="setWorkingMfcId";
        login_model2.data=data2;
        login_model2.type="rpc";
        login_model2.tid=2;
        loginModelArr.add(login_model2);

        Object[] data3 = new Object[2];
        data3[0] = "ROLE_ADMIN";
        data3[1] = false;

        Login_Model login_model3 = new Login_Model();
        login_model3.action="authController";
        login_model3.method="singleAuthorityLogin";
        login_model3.data=data3;
        login_model3.type="rpc";
        login_model3.tid=3;
        loginModelArr.add(login_model3);

        Login_Model login_model4 = new Login_Model();
        login_model4.action="cpguConfigurationService";
        login_model4.method="getConfiguration";
        login_model4.data=null;
        login_model4.type="rpc";
        login_model4.tid=4;
        loginModelArr.add(login_model4);



        String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url
        String result_of_req="";
        for (int i=0; i<4; i++){
            HttpClient httpClient = null;
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
            httpClient = builder.build();
            HttpPost post          = new HttpPost(postUrl);
            StringEntity postingString = new StringEntity(gson.toJson(loginModelArr.get(i)));//gson.tojson() converts your payload to json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            post.addHeader("Cookie","JSESSIONID="+cookie);
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            result_of_req = EntityUtils.toString(entity);
        }


        return result_of_req;
        //return payload_admin_arr;
    }

}


