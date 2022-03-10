package com.testscore.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.testscore.pojo.CveMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class AccessScore {

    public static String getReturnData(String path,String command){
        Process process =  null ;
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(path +  " "  + command);
            System.out.println("收到命令:"+command);
            reader = new BufferedReader( new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line =  null ;
            while ((line = reader.readLine()) !=  null ){
                stringBuffer.append(line +  "\n" );
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process!=null) {
                process.destroy();
            }
        }
        return stringBuffer.toString();
    }

    public static CveMessage handleMessage(String str){
        CveMessage cveMessage = new CveMessage();
        if (str==null){
            return cveMessage;
        }
        JSONObject jsonObject = JSONObject.parseObject(str);
        String cvss = null;
        if (jsonObject!=null){
            cvss = jsonObject.getString("cvss");
        }
//        System.out.println(cvss);
        if (cvss!=null){
            DecimalFormat df =new DecimalFormat("0.0");
            String format = df.format(Float.parseFloat(cvss));
            cveMessage.setCvssScore(Float.parseFloat(format));
        }
        return cveMessage;
    }

//    public static void main(String[] args) {
//        String path = "D:\\curl\\curl-7.78.0-win64-mingw\\bin\\curl.exe";
//        String command = "https://cve.circl.lu/api/cve/CVE-2017-0143";
//        String returnData = getReturnData(path, command);
//        CveMessage cveMessage = handleMessage(returnData);
//        System.out.println(cveMessage);
//    }
}
