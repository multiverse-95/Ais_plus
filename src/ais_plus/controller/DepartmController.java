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
import ais_plus.model.DataDepartm_Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DepartmController {

    public String Get_data_departm(String cookie) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();

        Payload_departm payload_departm =new Payload_departm();
        Data_departm data_departm = new Data_departm();
        Sort_departm sort_departm = new Sort_departm();

        ArrayList<String> null_numb = new ArrayList<String>();
        ArrayList<Data_departm> data_departm_arr = new ArrayList<Data_departm>();
        ArrayList<Sort_departm> sort_departm_arr = new ArrayList<Sort_departm>();

        // Add value for sort_depart
        sort_departm.property="leaf";
        sort_departm.direction="ASC";
        sort_departm_arr.add(sort_departm);
        Sort_departm sort_departm2 = new Sort_departm();
        sort_departm2.property="title";
        sort_departm2.direction="ASC";
        sort_departm_arr.add(sort_departm2);


        // Add value for data_depart
        data_departm.node = "root";
        data_departm.sort = sort_departm_arr;
        data_departm.id = "root";
        data_departm_arr.add(data_departm);

        // Add value for payload_depart
        payload_departm.action ="departmentService";
        payload_departm.method ="getDepartments";
        payload_departm.data = data_departm_arr;
        payload_departm.type ="rpc";
        payload_departm.tid =10;

        String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_departm));//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
        //return gson.toJson(payload_departm);
    }

    public result_departm Parsing_json_depart(String json, String search_text){

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray jsonArray =element.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        JsonArray result= jsonObject.get("result").getAsJsonArray();
        ArrayList<DataDepartm_Model> dataDepartm_modelArr = new ArrayList<DataDepartm_Model>();
        String departm_info="";
        //count_DepartmAll_t.setText(String.valueOf(result.size()));
        int count_DepartmAll=result.size();
        for (int i=0; i<result.size(); i++){
            dataDepartm_modelArr.add(new DataDepartm_Model(result.get(i).getAsJsonObject().get("departmentId").getAsString(),
                    result.get(i).getAsJsonObject().get("title").getAsString()));
        }

        //ArrayList<DataDepartm_Model> dataDepartm_modelArr_find= Search_departm(dataDepartm_modelArr, search_text);
        //ArrayList<DataDepartm_Model> dataDepartm_modelArr_find= null;
        //return dataDepartm_modelArr;
        return new result_departm(dataDepartm_modelArr,count_DepartmAll);

    }

    public ArrayList<DataDepartm_Model> Search_departm(ArrayList<DataDepartm_Model> dataDepartm_modelArr, String search_text){


            Pattern pattern = Pattern.compile(".*" + search_text.toLowerCase() + ".*");

            ArrayList<DataDepartm_Model> dataDepartmFind_modelArr = new ArrayList<DataDepartm_Model>();
            for (int i = 0; i < dataDepartm_modelArr.size(); i++) {
                Matcher matcher = pattern.matcher(dataDepartm_modelArr.get(i).getNameDepartm().toLowerCase());
                if (matcher.find()) {
                    //System.out.println("Search Success!");
                    dataDepartmFind_modelArr.add((new DataDepartm_Model(dataDepartm_modelArr.get(i).getIdDepartm(),
                            dataDepartm_modelArr.get(i).getNameDepartm())));
                } else {
                    //System.out.println("Search Failed!");
                }
            }
            return dataDepartmFind_modelArr;
    }


    public class result_departm {

        private ArrayList<DataDepartm_Model> data_departmArr;
        private int count_DepartmAll;

        public result_departm(ArrayList<DataDepartm_Model> data_departmArr, int count_DepartmAll) {

            this.data_departmArr = data_departmArr;
            this.count_DepartmAll = count_DepartmAll;

        }

        // getters and setters
        public ArrayList<DataDepartm_Model> getData_departmArr() {
            return data_departmArr;
        }
        public int getAllDepartm () {
            return count_DepartmAll;
        }
    }

    class  Payload_departm
    {
        public String action;
        public String method;
        public ArrayList<Data_departm> data;
        public String type;
        public int tid;
    }
    class Data_departm
    {
        public String node;
        public ArrayList<Sort_departm> sort;
        public String id;
    }
    class Sort_departm
    {
        public String property;
        public String direction;
    }
}
