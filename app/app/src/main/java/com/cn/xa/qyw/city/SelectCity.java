package com.cn.xa.qyw.city;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by 409160 on 2016/12/6.
 */
public class SelectCity {

    /**
     * 所有省
     */
    protected List<String> mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, List<String>> mCitisDatasMap = new HashMap<String, List<String>>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, List<String>> mDistrictDatasMap = new HashMap<String, List<String>>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";
    private static SelectCity instance;

    /**
     * 解析省市区的XML数据
     */

    public static SelectCity getInstance() {
        if (instance == null) {
            instance = new SelectCity();
        }
        return instance;
    }


    public void initProvinceDatas(Context context) {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new ArrayList<>();
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas.add(provinceList.get(i).getName());
                List<CityModel> cityList = provinceList.get(i).getCityList();
                List<String> cityNames = new ArrayList<>();
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames.add(cityList.get(j).getName());
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    List<String> distrinctNameArray = new ArrayList<>();
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray.add(districtModel.getName());
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames.get(j), distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public List<String> getmProvinceDatas() {
        return mProvinceDatas;
    }

    public List<String> getDistrictDatas(String key) {
        return mDistrictDatasMap.get(key);
    }

    public List<String> getmCitisDatas(String key) {
        return mCitisDatasMap.get(key);
    }


    public List<String> getAllmProvinceDatas() {
        List<String> list = new ArrayList<>();
        list.add("全国");
        list.addAll(mProvinceDatas);
        return list;
    }

    public List<String> getAllDistrictDatas(String key) {
        List<String> list = new ArrayList<>();
        list.add("所有地区");
        list.addAll(mDistrictDatasMap.get(key));
        return list;
    }

    public List<String> getAllmCitisDatas(String key) {
        List<String> list = new ArrayList<>();
        list.add("所有市区");
        list.addAll(mCitisDatasMap.get(key));
        return list;
    }
}
