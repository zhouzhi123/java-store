package com.example.util;

public class Unicode2Zh {
    public static String convert(String utfString){
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        int iint=0;
        while((i=utfString.indexOf("\\u", pos)) != -1){
            String sd = utfString.substring(pos, i);
            sb.append(sd);
            iint = i+5;

            if(iint < utfString.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
            }
        }
        String endStr = utfString.substring(iint+1, utfString.length());
        return sb.toString();
    }
}
