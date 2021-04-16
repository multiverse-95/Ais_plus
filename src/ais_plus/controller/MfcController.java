package ais_plus.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ais_plus.model.DataMfc_Model;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MfcController {

    public String Get_data_mfc(String cookie, String search, boolean isCheckBox_check) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        //String search="";
        //search =search_mfc_t.getText();
        String onlyWork="";
        if (isCheckBox_check){
            onlyWork="false";
        } else {
            onlyWork="true";
        }
        String sort = "[{\"property\":\"code\",\"direction\":\"ASC\"}]";
        String getUrl = "http://192.168.99.91/cpgu/action/getMfcs?_dc=1617272572092&showClosed="+onlyWork+"&searchString="+
                URLEncoder.encode(search, String.valueOf(StandardCharsets.UTF_8))+"&page=1&start=0&limit=500&sort="
                + URLEncoder.encode(sort, String.valueOf(StandardCharsets.UTF_8));
        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpGet get = new HttpGet(getUrl);
        get.setHeader("Content-type", "application/json");
        get.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
        //return gson.toJson(payload_departm);
    }

    public result_mfc Parsing_json_mfc(String json){

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray records= element.getAsJsonObject().get("records").getAsJsonArray();
        ArrayList<DataMfc_Model> dataMfc_modelArr = new ArrayList<DataMfc_Model>();
        int count_MfcAll = element.getAsJsonObject().get("total").getAsInt();
        //count_mfcAll_t.setText(element.getAsJsonObject().get("total").getAsString());

        for (int i=0; i<records.size(); i++){
            dataMfc_modelArr.add(new DataMfc_Model(records.get(i).getAsJsonObject().get("id").getAsString(),
                    records.get(i).getAsJsonObject().get("name").getAsString()));
        }
        //Download_mfc(dataMfc_modelArr);
        //return dataMfc_modelArr;
        return new result_mfc(dataMfc_modelArr,count_MfcAll);
    }

    public class result_mfc {

        private ArrayList<DataMfc_Model> data_mfcArr;
        private int count_MfcAll;

        public result_mfc(ArrayList<DataMfc_Model> data_mfcArr, int count_MfcAll) {

            this.data_mfcArr = data_mfcArr;
            this.count_MfcAll = count_MfcAll;

        }

        // getters and setters
        public ArrayList<DataMfc_Model> getData_mfcArr() {
            return data_mfcArr;
        }
        public int getAllMfc () {
            return count_MfcAll;
        }
    }
}


