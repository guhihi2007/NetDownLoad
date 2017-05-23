package org.network.test;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/3.
 */

public class Jason_Data {
    public static String getData(String jason_data,String data_name) throws Exception {
        JSONObject jsonObject = new JSONObject(jason_data);
        if (data_name.equals("type")){
            return jsonObject.getString("type");
        }else {
            return jsonObject.getJSONObject("data").getString(data_name);
        }
    }
}
