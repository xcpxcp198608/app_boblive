package com.wiatec.boblive.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;

/**
 * Created by patrick on 2016/12/29.
 */

public class SysUtil {

    /**
     * 获得当前设备的wifi mac地址
     * @return 当前设备wifi的mac地址
     */
    public static String getWifiMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macSerial;
    }


    public static String getEthernetMac(){
        try {
            return loadFileAsString("/sys/class/net/eth0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    private static String loadFileAsString(String filePath) throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * 获得当前系统语言
     * @param context 上下文
     * @return 当前系统设置的语言+国家地区类型
     */
    public static String getLanguage (Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        //Log.d("----px----" ,language+"_"+country);
        return language+"_"+country;
    }

    public static int getNetSpeedBytes() {
        String line;
        String[] segs;
        int received = 0;
        int i;
        int tmp = 0;
        boolean isNum;
        try {
            FileReader fr = new FileReader("/proc/net/dev");
            BufferedReader in = new BufferedReader(fr, 500);
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("rmnet") || line.startsWith("eth") || line.startsWith("wlan")) {
                    segs = line.split(":")[1].split(" ");
                    for (i = 0; i < segs.length; i++) {
                        isNum = true;
                        try {
                            tmp = Integer.parseInt(segs[i]);
                        } catch (Exception e) {
                            isNum = false;
                        }
                        if (isNum) {
                            received = received + tmp;
                            break;
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            return -1;
        }
        return received;
    }

}
