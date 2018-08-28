package com.cn.xa.qyw.datasource;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.entiy.AddDepartments;
import com.cn.xa.qyw.entiy.SearchDoctorByDepartmentAndCity;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.http.AsyncRequestHandle;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

import java.util.List;

/**
 * Created by Administrator on 2016/7/3.
 */
public class DepartmentAsyncDataSource implements IAsyncDataSource<List<SimpleDoctor>> {

    private String grade;
    private String gradeId;
    private int departId;
    private String city;

    public DepartmentAsyncDataSource(int departId, String city, String grade,String gradeId) {
        this.departId = departId;
        this.city = city;
        this.grade = grade;
        this.gradeId = gradeId;
    }

    @Override
    public RequestHandle refresh(ResponseSender<List<SimpleDoctor>> sender) throws Exception {
        return loadDoctor(sender);
    }

    private RequestHandle loadDoctor(final ResponseSender<List<SimpleDoctor>> sender) {

        String city = PreferenceUtils.getPrefString(PreferenceKeys.DEFAULT_CITY, "西安");

        SearchDoctorByDepartmentAndCity search = new
                SearchDoctorByDepartmentAndCity();
        search.setCity("%" + city + "%");
        search.setDepartmentId(departId);

        if(StringUtils.isEmpty(grade)){
            grade = "";
        }
        search.setGrade("%" + grade + "%");

        search.setId(Integer.valueOf(gradeId));


        return new AsyncRequestHandle(HttpUtils.postDataFromServer(HttpAddress.DEPARTMENT_DETAIL_INFO,
                JSONObject.toJSONString(search), new NetworkResponseHandler() {
                    @Override
                    public void onFail(String error) {
                        sender.sendError(new Exception("请求错误"));
                    }

                    @Override
                    public void onSuccess(String data) {
                        List<SimpleDoctor> doctors = JSONObject.parseArray(data, SimpleDoctor.class);
                        sender.sendData(doctors);
                    }
                }));
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<SimpleDoctor>> sender) throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
