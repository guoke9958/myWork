package com.cn.xa.qyw.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.DepartmentAdapter;
import com.cn.xa.qyw.adapter.GradeAdapter;
import com.cn.xa.qyw.adapter.ZiXunAdapter;
import com.cn.xa.qyw.base.BaseFragment;
import com.cn.xa.qyw.datasource.HospitalAsyncDataSource;
import com.cn.xa.qyw.dialog.DoctorBaseDialog;
import com.cn.xa.qyw.entiy.AddDepartments;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.entiy.NewsDetail;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.hospital.DepartmentActivity;
import com.cn.xa.qyw.ui.hospital.DepartmentAllActivity;
import com.cn.xa.qyw.ui.mypie.FlakeView;
import com.cn.xa.qyw.ui.mypie.GifView;
import com.cn.xa.qyw.ui.search.YhtSearchActivity;
import com.cn.xa.qyw.ui.web.WebViewActivity;
import com.cn.xa.qyw.view.NetworkImageHolderView;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCNormalHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/5/26.
 */
public class HomeFragment extends BaseFragment implements OnItemClickListener {

    private GridView mGridView;
    private MVCNormalHelper<List<AddDepartments>> mvcHelper;
    private DepartmentAdapter mAdapter;
    private ScrollView mScrollView;
    private View mRelative;
    private ListView mListView;
    private List<AddDepartments> mData;
    private ZiXunAdapter mNewsAdapter;
    private boolean mIsCreate;
    private GridView mGradeGirdView;
    private GradeAdapter mGradeAdapter;
    private View mAllSearch;
    private ConvenientBanner mConvenientBanner;


    private String[] images = {"http://www.qiuyiwang.com:8081/download/img/yd.jpg",
            "http://www.qiuyiwang.com:8081/download/img/timg.jpg"
    };


    private PopupWindow pop;
    private FlakeView flakeView;
    private View view;
    private TextView snatchPie;
    private RelativeLayout gifView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_view, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mIsCreate = true;
        this.view = view;
        initView(view);
        initData();
        initListener();
    }

    private void initListener() {
        mAllSearch.setOnClickListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddDepartments department = mAdapter.getItem(position);

                if (department.getId() == -1) {
                    Intent intent = new Intent(getActivity(), DepartmentAllActivity.class);
                    intent.putExtra("department_all", JSONObject.toJSONString(mData));
                    intent.putExtra("grade","");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), DepartmentActivity.class);
                    intent.putExtra("department_id", department.getId());
                    intent.putExtra("department_name", department.getDepartmentsName());
                    intent.putExtra("grade",department.getDepartmentsName());
                    getActivity().startActivity(intent);
                }

            }
        });

        mGradeGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DepartmentAllActivity.class);
                String item = mGradeAdapter.getItem(position);
                intent.putExtra("grade",item);
                intent.putExtra("grade_id",mGradeAdapter.getItemId(position)+"");
                startActivity(intent);
            }
        });

    }

    private void initData() {
        mvcHelper = new MVCNormalHelper<List<AddDepartments>>(mScrollView);
        mvcHelper.setDataSource(new HospitalAsyncDataSource());
        mvcHelper.setAdapter(new DepartAdapter());
        mvcHelper.refresh();

        mNewsAdapter = new ZiXunAdapter(getActivity());
        mListView.setAdapter(mNewsAdapter);

        mGradeAdapter = new GradeAdapter(getActivity());
        mGradeGirdView.setAdapter(mGradeAdapter);

        List<String> networkImages = Arrays.asList(images);
        mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
//        .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setOnItemClickListener(this);
        //设置指示器的方向
//        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

    }

    private void initView(View view) {
        mScrollView = (ScrollView) view.findViewById(R.id.home_scrollview);
        mRelative = view.findViewById(R.id.home_gridview_parent);
        mAllSearch = view.findViewById(R.id.all_search);
        mGridView = (GridView) view.findViewById(R.id.department_gridview);
        mListView = (ListView) view.findViewById(R.id.zixun_listview);
        mGradeGirdView = (GridView) view.findViewById(R.id.hospital_grade_gridview);
        mConvenientBanner = (ConvenientBanner)view.findViewById(R.id.convenientBanner);

//        view.findViewById(R.id .lianxi_our).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!DoctorBaseDialog.isShowLoginDialog(getActivity())) {
//
//                    if (DoctorApplication.mUser.getUserId().equals("4d37984066e54024b790d81a5bf293dc")) {
//                        showCofiDialog();
//                    } else {
//                        RongIM.getInstance().startPrivateChat(getActivity(), "4d37984066e54024b790d81a5bf293dc", "系统管理员");
//                    }
//
//                }
//            }
//        });
        snatchPie = (TextView) view.findViewById(R.id.snatch_a_pie);
        snatchPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .theme(Theme.LIGHT)
                        .title("温馨提示")
                        .content("亲，活动还没开始哟！")
                        .positiveText("知道了")
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                pop.dismiss();
                                container.removeAllViews();
                            }
                        })
                        .show();
            }
        });
        setGifView(view);

        gifView = (RelativeLayout) view.findViewById(R.id.gif_view);
        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("温馨提示")
                        .content("恭喜您抢到一个馅饼，是否开启！")
                        .positiveText("亲启")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                gifView.setVisibility(View.GONE);
                                pop.dismiss();
                                container.removeAllViews();
                                new MaterialDialog.Builder(getActivity())
                                        .theme(Theme.LIGHT)
                                        .title("温馨提示")
                                        .content("恭喜您否开成功！")
                                        .positiveText("好的").show();
                            }
                        })
                        .negativeText("狠心放弃")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                gifView.setVisibility(View.GONE);
                            }
                        })
                        .show();
            }
        });
    }

    private int time = 5000;
    private GifView imageView;
    private LinearLayout container;
    private void setGifView(View view){
        imageView=(GifView) view.findViewById(R.id.imageView);
        imageView.setMovieTime(time);

        flakeView = new FlakeView(getActivity());
        container = (LinearLayout) view.findViewById(R.id.container);
        container.addView(flakeView); //将flakeView 添加到布局中
        flakeView.addFlakes(15);//设置同时出现在屏幕上的金币数量  建议64以内 过多会引起卡顿

        /**
         * 绘制的类型
         * @see View.LAYER_TYPE_HARDWARE
         * @see View.LAYER_TYPE_SOFTWARE
         * @see View.LAYER_TYPE_NONE
         */
        flakeView.setLayerType(View.LAYER_TYPE_NONE, null);
        pop = new PopupWindow(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.showAtLocation(container, Gravity.CENTER,0,0);
        MediaPlayer player = MediaPlayer.create(getActivity(), R.raw.shake);
        player.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pop.dismiss();
                container.removeAllViews();
                gifView.setVisibility(View.GONE);
            }
        }, 4000);
    }

    private void showCofiDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title("温馨提示")
                .content("您不能可自己进行聊天")
                .positiveText("取消")
                .show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mvcHelper.destory();
    }

    @Override
    public void onResume() {
        super.onResume();

        mConvenientBanner.startTurning(2000);

        if (!mIsCreate) {
            getNewsData();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        mConvenientBanner.stopTurning();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.all_search){
            getActivity().startActivity(new Intent(getActivity(), YhtSearchActivity.class));
        }
    }

    private void getNewsData() {
        HttpUtils.postDataFromServer(HttpAddress.GET_LIST_NEWS, new NetworkResponseHandler() {
            @Override
            public void onFail(String messsage) {
                Log.e("首页联网 messsage = ",messsage);
            }

            @Override
            public void onSuccess(String data) {
                List<NewsDetail> list = JSONObject.parseArray(data, NewsDetail.class);
                mNewsAdapter.setListNews("0", list);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        if(position == 0){
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url","http://www.dyyy.xjtu.edu.cn/");
            intent.putExtra("title", "西安交大一附院");
            startActivity(intent);
        }else if(position == 1){
            RongIM.getInstance().startPrivateChat(getActivity(), "075bebe27c1c471abe5c7717967ac825", "张龙");
        }


    }

    class DepartAdapter implements IDataAdapter<List<AddDepartments>> {

        @Override
        public void notifyDataChanged(List<AddDepartments> addDepartmentses, boolean isRefresh) {
            mData = addDepartmentses;
            mAdapter = new DepartmentAdapter(getActivity());
            mAdapter.setData(getListData(addDepartmentses));
            mGridView.setAdapter(mAdapter);
            mIsCreate = false;
            getHospitalGrade();
        }

        @Override
        public List<AddDepartments> getData() {
            return mData;
        }

        @Override
        public boolean isEmpty() {
            return mData == null;
        }
    }

    /**
     * 获取首页栏目
     */
    private void getHospitalGrade() {
        HttpUtils.postDataFromServer(HttpAddress.GET_HOSPITAL_GRADE, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                getNewsData();
            }

            @Override
            public void onSuccess(String data) {
                List<HospitalGrade> list = JSONObject.parseArray(data,HospitalGrade.class);
                mGradeAdapter.setData(list);
                getNewsData();
            }
        });
    }

    private List<AddDepartments> getListData(List<AddDepartments> addDepartmentses) {
        List<AddDepartments> list = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            list.add(addDepartmentses.get(i));
        }

        AddDepartments departments = new AddDepartments();
        departments.setDepartmentsName("更多");
        departments.setId(-1);
        list.add(departments);

        return list;
    }

}
