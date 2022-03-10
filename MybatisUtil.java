package com.cetc.dobms.util;

import com.cetc.dobms.model.NetworkDef;
import com.cetc.dobms.model.NetworkShip;
import com.cetc.dobms.model.SecretPersonInfoPri;
import com.cetc.dobms.model.WorkflowInstModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MybatisUtil {
    public static String getResultMap(Class <?> cls) {
        String str = "";
        // 头部 <resultMap id="BaseResultMap" type="">
        str = "<resultMap id=" + "\"" + cls.getSimpleName() + "ResultMap\" type=" + "\"" + cls.getName() + "\"" + "> \r\n";
        str = str + "<id column=\"ID\"" + " property=\"id\"" +" jdbcType=\"BIGINT\""+ " />" + " \r\n";
        // 每一行字符串
        String lineStr = "";
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            lineStr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName() + "\"" + " jdbcType=\"" + getJdbcType(field.getType().getName()) + "\""
                    + " />";
            lineStr += "\r\n";
            str += lineStr;
        }
        str += "</resultMap>";
        return str;
    }

    public static String getJdbcType(String type) {
        if (type == null || type.length() == 0) {
            return "";
        }
        if (type.contains("String")) {
            return "VARCHAR";
        }
        if (type.contains("Long") || type.contains("Integer")) {
            return "BIGINT";
        }
        if ((type.contains("Date"))) {
            return "DATE";
        }
        return "OTHER";
    }

    // <sql id="Base_Column_List"> </sql>
    public static String getAllField(Class <?> cls) {
        StringJoiner str = new StringJoiner(",", "<sql id=\"Base_Column\"> " + "\r\n", "\r\n"+"</sql>");
        String lineStr = "";
        Field[] declaredFields = cls.getDeclaredFields();
        int i = 0;
        for (Field field : declaredFields) {
            lineStr = getUpCaseReplace(field.getName());
            if (i % 4 == 0 && i != 0) {
                str.add("\r\n"+lineStr);
            }else {
                str.add(lineStr);
            }
            i++;
        }
        return str.toString();
    }

    // select
    public static String getSelectForeach(Class <?> cls) {
        StringBuilder builder = new StringBuilder();
        builder.append("<select id=\"select" + cls.getSimpleName() + "ById" + "\" parameterType=\"java.lang.Long\" resultMap=\"" + cls.getSimpleName() + "ResultMap\">" + "\r\n");
        builder.append("select " + "\r\n");
        builder.append("<include refid=\"Base_Column\"/>" + "\r\n");
        builder.append("from " + getTableName(cls.getSimpleName()) + " \r\n");
        builder.append("where ID in" + " \r\n");
        builder.append("<foreach collection=\"list\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" + "\r\n");
        builder.append("#{id}" + "\r\n");
        builder.append("</foreach>" + "\r\n");
        builder.append("</select>");
        return builder.toString();
    }

    // selectList
    public static String getSelectList(Class <?> cls) {
        StringBuilder builder = new StringBuilder();
        builder.append("<select id=\"select" + cls.getSimpleName() + "List" + "\" parameterType=\""+cls.getName()+"\" resultMap=\"" + cls.getSimpleName() + "ResultMap\">" + "\r\n");
        builder.append("select " + "\r\n");
        builder.append("<include refid=\"Base_Column\"/>" + "\r\n");
        builder.append("from " + getTableName(cls.getSimpleName()) + " \r\n");
        builder.append("where 1=1" + " \r\n");
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            String s = getJdbcType(field.getType().getName()).equals("BIGINT")?"0":"''";
            builder.append("<if test=\""+field.getName()+" != null and "+field.getName()+" != "+s+"\">"+"\r\n");
            builder.append("and "+getUpCaseReplace(field.getName())+" = #{"+field.getName()+"}"+"\r\n");
            builder.append("</if>"+"\r\n");
        }
        builder.append("</select>");
        return builder.toString();
    }

    // checkExist
    public static String getCheckExist(Class <?> cls) {
        StringBuilder builder = new StringBuilder();
        builder.append("<select id=\"check" + cls.getSimpleName() + "Exist" + "\" parameterType=\""+cls.getName()+"\" resultType=\"java.lang.Integer\">" + "\r\n");
        builder.append("select count(*)" + "\r\n");
        builder.append("from " + getTableName(cls.getSimpleName()) + " \r\n");
        builder.append("where ID = #{id}" + " \r\n");
        builder.append("</select>");
        return builder.toString();
    }

    // insert
    public static String getInsert(Class <?> cls) {
        StringBuilder builder = new StringBuilder();
        builder.append("<insert id=\"insert" + cls.getSimpleName().replace("Model", "") + "\" useGeneratedKeys=\"true\" keyProperty=\"id\" parameterType=\"" + cls.getName() + "\">" + "\r\n");
        builder.append("<selectKey keyProperty=\"id\" order=\"BEFORE\" resultType=\"Long\">\n" +
                "select SEQ_"+getTableName(cls.getSimpleName())+".nextval as id from DUAL\n" +
                "</selectKey>" + "\r\n");
        builder.append("INSERT INTO " + getTableName(cls.getSimpleName()) + "\n" +
                "(" + "\r\n");
        builder.append("<include refid=\"Base_Column\"/>" + "\r\n");
        builder.append(")\n" +
                "values (" + "\r\n");
        Field[] declaredFields = cls.getDeclaredFields();
        int i = 0;
        for (Field field : declaredFields) {
            builder.append("#{" + field.getName() + ",jdbcType=" + getJdbcType(field.getType().getName()) + "},");
            if (i % 4 == 0 && i != 0) {
                builder.append("\r\n");
            }
            i++;
        }
        String substring = builder.substring(0, builder.lastIndexOf("}") + 1) + "\n)\n" + "</insert>";
        return substring;
    }

    // update
    public static String getUpdate(Class <?> cls) {
        StringBuilder builder = new StringBuilder();
        builder.append("<update id=\"update" + cls.getSimpleName().replace("Model", "") + "ById" + "\" parameterType=\"" + cls.getName() + "\">" + "\r\n");
        builder.append("UPDATE " + getTableName(cls.getSimpleName()) + "\r\n");
        builder.append("<trim prefix=\"set\" suffixOverrides=\",\">" + "\r\n");
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            builder.append("<if test=\"" + field.getName() + "!=null\">" + getUpCaseReplace(field.getName()) + "=#{" + field.getName() + "},</if>" + "\r\n");
        }
        builder.append("</trim>" + "\r\n");
        builder.append("WHERE ID=#{" + declaredFields[0].getName() + "}" + "\r\n");
        builder.append("</update>");
        return builder.toString();
    }

    //delete
    public static String getDelete(Class <?> cls) {
        StringBuilder builder = new StringBuilder();
        builder.append("<delete id=\"delete" + cls.getSimpleName().replace("Model", "") + "ById" + "\">" + "\r\n");
        builder.append("delete from " + getTableName(cls.getSimpleName()) + " where "+getUpCaseReplace(cls.getDeclaredFields()[0].getName()) + "=#{" + cls.getDeclaredFields()[0].getName() + "}"+"\r\n");
        builder.append("</delete>");
        return builder.toString();
    }

    /**
     * 将字符串中的驼峰写法替换成'_'
     *
     * @param str
     * @return
     */
    private static String getUpCaseReplace(String str) {
        List <String> listChar = getUpCaseList(str);
        //先将字原属性加上"_"和原属性的首字母大写
        for (int i = 0; i < listChar.size(); i++) {
            str = str.replace(listChar.get(i), "_" + listChar.get(i).toLowerCase());
        }
        //将字段全部装换为大写
        String result = getAllUpCaseList(str);
        return result;
    }

    private static String getTableName(String str) {
        List <String> listChar = getUpCaseList(str);
        //先将字原属性加上"_"和原属性的首字母大写
        int size = listChar.size();
        if (str.contains("Model")){
            size = size-1;
        }
        for (int i = 1; i < size; i++) {
            str = str.replace(listChar.get(i), "_" + listChar.get(i).toLowerCase());
        }
        if (str.contains("Model")) {
            str = str.replace("Model", "");
        }
        //将字段全部装换为大写
        String result = getAllUpCaseList(str);
        return result;
    }

    private static String getAllUpCaseList(String str) {
        // 转为char数组
        char[] ch = str.toCharArray();
        // 得到大写字母
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < ch.length; i++) {
            stringBuffer.append(String.valueOf(ch[i]).toUpperCase());
        }
        return stringBuffer.toString();
    }

    /**
     * @param str
     * @Description: 输出字符串中的大写字母
     */
    private static List <String> getUpCaseList(String str) {
        List <String> listChar = new ArrayList <>();
        // 转为char数组
        char[] ch = str.toCharArray();
        // 得到大写字母
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                listChar.add(String.valueOf(ch[i]));
            }
        }
        return listChar;
    }

    public static void main(String[] args) {
        // 实体类
        NetworkShip a = new NetworkShip();
        System.out.println(getResultMap(a.getClass()));
        System.out.println();
        System.out.println(getAllField(a.getClass()));
        System.out.println();
        System.out.println(getSelectForeach(a.getClass()));
        System.out.println();
        System.out.println(getCheckExist(a.getClass()));
        System.out.println();
        System.out.println(getSelectList(a.getClass()));
        System.out.println();
        System.out.println(getInsert(a.getClass()));
        System.out.println();
        System.out.println(getUpdate(a.getClass()));
        System.out.println();
        System.out.println(getDelete(a.getClass()));
    }

}
