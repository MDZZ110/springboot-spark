package com.yunify.springbootspark.util;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingcloud.sdk.config.EnvContext;
import com.qingcloud.sdk.exception.QCException;
import com.qingcloud.sdk.service.TagService;

public class CommonUtil {

    public static void writeListToFile(List<Integer> list, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { // true表示追加模式，如果不需要追加则设为false或省略
            for (Integer number : list) {
                writer.write(number.toString()); // 将Integer转换为String并写入
                writer.newLine(); // 写入新行
            }
            writer.flush();
        }
    }

    public static boolean validateUserIdentity(String accessToken){
        if(null == accessToken){
            return false;
        }
        if(!CommonUtil.valiateStringType(accessToken, 0, 128)){
            return false;
        }


        String[] splitTokenArray = accessToken.split("\\|");
        if(splitTokenArray.length != 2){
            return false;
        }

        TagService service = getTagService(splitTokenArray);
        TagService.DescribeTagsInput describeTagsInput = new TagService.DescribeTagsInput();
        try{
            TagService.DescribeTagsOutput describeTagsOutput = service.describeTags(describeTagsInput);
            if (describeTagsOutput.getRetCode() == 1200) {
                System.out.println("ret code is " + describeTagsOutput.getRetCode() + ", token is expired !");
                return false;
            }
        } catch (QCException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static TagService getTagService(String[] splitTokenArray) {
        String accessKey = splitTokenArray[0];
        String secretKey = splitTokenArray[1];
        EnvContext context = new EnvContext(accessKey, secretKey);
        context.setProtocol("https");
        context.setHost("api.qingcloud.com");
        context.setPort("443");
        context.setZone("pek3d");
        context.setApiLang("zh-cn");

//        context.setProtocol("http");
//        context.setHost("api.gslnga.com");
//        context.setPort("7777");
//        context.setZone("gslnga1");
//        context.setApiLang("zh-cn");
        TagService service = new TagService(context);
        return service;
    }

    public static boolean valiateStringType(Object param, int minLength, int maxLength){
        if(null == param){
            return false;
        }
        if(param instanceof String){
            int length = ((String) param).length();
            if(length >= minLength && length <= maxLength){
                return true;
            }
        }

        return false;
    }

    public static boolean validateFloatType(Object param, int maxLength, int maxLengthAfterPoint){
        if(null == param){
            return false;
        }
        if(param instanceof Float){
            String data = String.valueOf(param);
            if(data.length() > maxLength){
                return false;
            }

            BigDecimal bdData = new BigDecimal(data);
            int scale = bdData.scale();

            if(scale > maxLengthAfterPoint){
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean validateIntType(int param, int maxLength){
        return String.valueOf(param).length() <= maxLength;
    }

    public static HashMap<String, String> parseClassAndMethod(String userDefinedFunctionPath){
        if(userDefinedFunctionPath == null){
            return null;
        }

        if(!CommonUtil.valiateStringType(userDefinedFunctionPath, 0, 1024)){
            return null;
        }

        HashMap<String, String> resultMap = null;
        try{
            String[] classAndFuncPathArray = userDefinedFunctionPath.split("\\.");
            if(classAndFuncPathArray.length<1){
                return null;
            }
            StringBuilder classPath = new StringBuilder();
            for(int i=0;i<classAndFuncPathArray.length-1;i++){
                if(i!=0){
                    classPath.append(".");
                }
                classPath.append(classAndFuncPathArray[i]);
            }

            if(classPath.length()==0||"".equals(classPath.toString())){
                return null;
            }

            resultMap = new HashMap<>();
            resultMap.put("ClassPath", classPath.toString());

            String funcName = classAndFuncPathArray[classAndFuncPathArray.length-1];
            resultMap.put("FuncName",  funcName);
        }catch (Exception e){
            return null;
        }
        return resultMap;
    }

    public static Method getMethodByName(Class clazz, String methodName)
            throws ClassNotFoundException{
        Method[] methods = clazz.getDeclaredMethods();

        Method udfMethod = null;
        for(int i=0;i<methods.length;i++){
            if(methodName.equals(methods[i].getName())){
                udfMethod = methods[i];
                break;
            }
        }

        return udfMethod;
    }

    public static List<String> readFileAsInput(String fileName) {
        List<String> lines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = br.readLine()) != null){
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }
}
