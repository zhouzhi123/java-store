package com.testscore.service;

import com.testscore.mapper.ScoreInsertMapper;
import com.testscore.pojo.CveMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;

import static com.testscore.service.AccessScore.getReturnData;
import static com.testscore.service.AccessScore.handleMessage;

@Component
public class InsertScore {
    @Resource
    public ScoreInsertMapper mapper;

    @PostConstruct
    public void testScoreUpdate(){
        String path = "D:\\curl\\curl-7.78.0-win64-mingw\\bin\\curl.exe";
        List <String> cveCode = mapper.selectCveCode();
        int size = cveCode.size();
        System.out.println(size);
        for (String s : cveCode) {
//            String s = "CVE-2015-1635";
            String command = "https://cve.circl.lu/api/cve/" + s;
            String returnData = getReturnData(path, command);
            if (returnData.equals("null")){
                continue;
            }
            CveMessage cveMessage = handleMessage(returnData);
            Float cvssScore = cveMessage.getCvssScore();
            if (cvssScore==null){
                continue;
            }
            Integer dangerousLevel = cvssScore>=9.0?1:(cvssScore>=7.0&&cvssScore<=8.9)?2:(cvssScore>=4.0&&cvssScore<=6.9)?3:(cvssScore>=0.1&&cvssScore<=3.9)?4:5;
            cveMessage.setDangerousLevel(dangerousLevel);
            cveMessage.setKnowledgeCode(s);
            System.out.println(cveMessage);
            mapper.updateScoreAndLevel(cveMessage);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void handleScore(){
        List <CveMessage> cveMessages = mapper.selectCveScore();
        int size = cveMessages.size();
        System.out.println(size);
        for (CveMessage cveMessage : cveMessages) {
            Float cvssScoreOrigin = cveMessage.getCvssScore();
            String knowledgeCode = cveMessage.getKnowledgeCode();
            DecimalFormat df =new DecimalFormat("0.0");
            String format = df.format(cvssScoreOrigin);
            System.out.println(cvssScoreOrigin+" "+format);
            float cvssScore = Float.parseFloat(format);
            Integer dangerousLevel = cvssScore>=9.0?1:(cvssScore>=7.0&&cvssScore<=8.9)?2:(cvssScore>=4.0&&cvssScore<=6.9)?3:(cvssScore>=0.1&&cvssScore<=3.9)?4:5;
            cveMessage.setCvssScore(cvssScore);
            cveMessage.setDangerousLevel(dangerousLevel);
            System.out.println(cveMessage);
        }
    }
}
