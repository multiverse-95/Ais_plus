package ais_plus.controller;

import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ais_plus.model.DataUslug_Model;

import java.io.IOException;
import java.util.ArrayList;


public class ServiceController {

    public String Get_data_uslug(String cookie, String search_text, int limit_usl) throws IOException {


        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();

        Payload_uslug payload_uslug =new Payload_uslug();
        Data_uslug data_uslugs = new Data_uslug();
        Sort_uslug sort_uslug = new Sort_uslug();

        ArrayList<String> null_numb = new ArrayList<String>();
        ArrayList<Data_uslug> data_uslug = new ArrayList<Data_uslug>();
        ArrayList<Sort_uslug> sort_usl = new ArrayList<Sort_uslug>();

        // Add value for sort_uslug
        sort_uslug.property="title";
        sort_uslug.direction="ASC";
        sort_usl.add(sort_uslug);

        // Add value for data_uslug
        data_uslugs.serviceType = null;
        data_uslugs.requesterType = null;
        data_uslugs.departmentFilter = null;
        data_uslugs.searchString = search_text;
        data_uslugs.page = 1;
        data_uslugs.start = 0;
        data_uslugs.limit = limit_usl;
        data_uslugs.sort = sort_usl;
        data_uslug.add(data_uslugs);

        // Add value for payload_uslug
        payload_uslug.action ="customServiceInfoService";
        payload_uslug.method ="getCustomServices";
        payload_uslug.data =data_uslug;
        payload_uslug.type ="rpc";
        payload_uslug.tid =11;


        String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_uslug), "UTF-8");//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json;charset=UTF-8");
        post.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        System.out.println(gson.toJson(payload_uslug));
        //System.out.println(EntityUtils.toString(entity));
        return EntityUtils.toString(entity);
        //return gson.toJson(payload_uslug);
    }

    public int Total_usl_from_serv(String json){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray jsonArray =element.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        JsonObject result = jsonObject.get("result").getAsJsonObject();
        JsonArray records= result.get("records").getAsJsonArray();
        String title_usl="";
        int limit_usl_fromServer=jsonObject.get("result").getAsJsonObject().get("total").getAsInt();
        if (limit_usl_fromServer==0) limit_usl_fromServer=25;
        return limit_usl_fromServer;
    }

    public result_service Parsing_json_uslug(String json, int limit_usl, boolean isCheckBox_check) throws IOException {


        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray jsonArray =element.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        JsonObject result = jsonObject.get("result").getAsJsonObject();
        JsonArray records= result.get("records").getAsJsonArray();
        String title_usl="";
        int limit_usl_fromServer=jsonObject.get("result").getAsJsonObject().get("total").getAsInt();
        System.out.println("Total "+limit_usl_fromServer);
        ArrayList<DataUslug_Model> dataUslug_models_arr = new ArrayList<DataUslug_Model>();
        int SumAllUslug =limit_usl_fromServer;
        int SumActualUslug =0;


        if (limit_usl>limit_usl_fromServer){
            limit_usl=limit_usl_fromServer;
            System.out.println("LIMIT_USLUG_OUT "+limit_usl);
            if (isCheckBox_check){
                System.out.println("CheckBox is selected! >");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                        dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                                records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                                records.get(i).getAsJsonObject().get("title").getAsString()));
                    }
                }

            } else {
                System.out.println("CheckBox is NOT selected! <");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                    }
                    dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                            records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                            records.get(i).getAsJsonObject().get("title").getAsString()));

                }
            }

        } else {
            if (isCheckBox_check){
                System.out.println("CheckBox is selected! <");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                        dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                                records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                                records.get(i).getAsJsonObject().get("title").getAsString()));
                    }

                }

            } else {
                System.out.println("CheckBox is NOT selected! <");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                    }
                    dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                            records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                            records.get(i).getAsJsonObject().get("title").getAsString()));
                }
            }
        }

        return new result_service(dataUslug_models_arr, SumAllUslug, SumActualUslug);
    }

    public class result_service {

        private ArrayList<DataUslug_Model> data_uslugArr;
        private int SumAllUslug;
        private int ActualUslug;

        public result_service(ArrayList<DataUslug_Model> data_uslugArr, int SumAllUslug, int ActualUslug) {

            this.data_uslugArr = data_uslugArr;
            this.SumAllUslug = SumAllUslug;
            this.ActualUslug = ActualUslug;
        }

        // getters and setters
        public ArrayList<DataUslug_Model> getData_uslugArr() {
            return data_uslugArr;
        }
        public int getAllUslug () {
            return SumAllUslug;
        }
        public int getActualUslug() {
            return ActualUslug;
        }


    }

    class  Payload_uslug
    {
        public String action;
        public String method;
        public ArrayList<Data_uslug> data;
        public String type;
        public int tid;
    }
    class Data_uslug
    {
        public ArrayList<String> serviceType;
        public ArrayList<String> requesterType;
        public ArrayList<String> departmentFilter;
        public String searchString;
        public int page;
        public int start;
        public int limit;
        public ArrayList<Sort_uslug> sort;
    }

    class Sort_uslug
    {
        public String property;
        public String direction;
    }


}
