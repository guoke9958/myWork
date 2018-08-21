package com.cn.xa.qyw.city;

import android.content.Context;

import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import io.rong.imageloader.utils.L;

/**
 * Created by 409160 on 2017/1/6.
 */
public class CityTotal {

    public static LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
    private static List<String> province = new ArrayList<>();

    public static void initData(final Context context) {
        new Thread(){
            @Override
            public void run() {
                data(context);
            }
        }.start();

    }

    private static void data(Context context){
        String encoding = "utf-8";
        List<String> list = new ArrayList<String>();
        try {
            InputStreamReader read = new InputStreamReader(
                    context.getAssets().open("city.txt"), encoding);// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {

                if (!StringUtils.isEmpty(lineTxt)) {
                    list.add(lineTxt);
                }

            }
            read.close();
            map.clear();
            for (int i = 0; i < list.size(); i++) {
                if (i % 2 == 0) {
                    String key = list.get(i);
                    i++;
                    String value = list.get(i);
                    List<String> arr = Arrays.asList(value.split(" "));
                    map.put(key,arr);
                }
            }

            province.clear();
            province.addAll(map.keySet());

        } catch (Exception e) {
            L.e(e);
        }
    }

    public static List<String> getProvince(){
        return province;
    }

    public static List<String> getCity(String province){
        return map.get(province);
    }

}
