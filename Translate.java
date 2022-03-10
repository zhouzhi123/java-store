package com.example.translate;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import static com.example.util.Unicode2Zh.convert;

public class Translate {
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20210820000922311";
    private static final String SECURITY_KEY = "9KY1IHKYMNUVrkRLphQH";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "The parse_rock_ridge_inode_internal function in fs/isofs/rock.c in the Linux kernel through 3.16.1 allows local users to cause a denial of service (unkillable mount process) via a crafted iso9660 image with a self-referential CL entry.";
        String transResult = api.getTransResult(query, "en", "zh");
        String s = StringEscapeUtils.unescapeJava(transResult);

//        String convert = convert(api.getTransResult(query, "en", "zh")).trim();

        System.out.println(s);
        int i = s.indexOf("dst\":");
        int length = s.length();
        System.out.println(s.substring(i+6,length-4));

//        JSONObject jsonObject = JSONObject.parseObject(s);
//        JSONArray trans_result = jsonObject.getJSONArray("trans_result");
//        String dst = trans_result.getJSONObject(0).getString("dst");
//        System.out.println(dst);
    }

}
