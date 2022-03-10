package com.example.component;

import com.example.mapper.TranslateMapper;
import com.example.pojo.CveMessage;
import com.example.pojo.CveName;
import com.example.translate.TransApi;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class Translation {
    @Resource
    public TranslateMapper mapper;

//    private static final String APP_ID = "";
//    private static final String SECURITY_KEY = "";
    private static final String APP_ID = "";
    private static final String SECURITY_KEY = "";

//    @PostConstruct
//    public void tanslation(){
//        System.out.println("正在翻译...");
//        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
//        List <CveMessage> cveMessages = mapper.selectKnowledge();
//        for (CveMessage cveMessage : cveMessages) {
//            String knowledgeDesc = cveMessage.getKnowledgeDesc();
////            String convert = convert(api.getTransResult(knowledgeDesc, "en", "zh")).trim();
//            try {
//                String transResult = api.getTransResult(knowledgeDesc, "en", "zh");
//                String s = StringEscapeUtils.unescapeJava(transResult).trim();
//                int i = s.indexOf("dst\":");
//                int length = s.length();
//                String dst = s.substring(i + 6, length - 4);
//                cveMessage.setChineseDesc(dst);
//                mapper.updateChineseDesc(cveMessage);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//        }
//    }

    @PostConstruct
    public void tanslation(){
        System.out.println("正在翻译名称...");
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        List <CveName> cveMessages = mapper.selectWeakName();
        int size = cveMessages.size();
        System.out.println(size);
        for (CveName cveMessage : cveMessages) {
            String weakName = cveMessage.getWeakName();
//            String convert = convert(api.getTransResult(knowledgeDesc, "en", "zh")).trim();
            try {
                String transResult = api.getTransResult(weakName, "en", "zh");
                String s = StringEscapeUtils.unescapeJava(transResult).trim();
                int i = s.indexOf("dst\":");
                int length = s.length();
                String dst = s.substring(i + 6, length - 4);
                cveMessage.setWeakChinese(dst);
                mapper.updateWeakChinese(cveMessage);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}
