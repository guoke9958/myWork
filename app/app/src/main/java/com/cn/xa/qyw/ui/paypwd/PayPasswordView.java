package com.cn.xa.qyw.ui.paypwd;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.utils.StringUtils;

/**
 * @author LanYan
 */
public class PayPasswordView implements OnClickListener {

    private ImageView del;
    private TextView zero;
    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView five;
    private TextView sex;
    private TextView seven;
    private TextView eight;
    private TextView nine;
    private TextView box1;
    private TextView box2;
    private TextView box3;
    private TextView box4;
    private TextView box5;
    private TextView box6;
    private TextView title;
    private TextView content;

    private ArrayList<String> mList = new ArrayList<String>();
    private View mView;
    private OnPayListener listener;
    private Context mContext;
    private TextView payContent;
    private View cancelPay;
    private View dismissKey;
    private View keyboard;

    public PayPasswordView(String content, String title, boolean isPay, String tipContent, Context mContext, OnPayListener listener) {
        getDecorView(content, title, isPay, tipContent, mContext, listener);
    }

    public static PayPasswordView getInstance(String content, String title, boolean isPay, String tipContent, Context mContext, OnPayListener listener) {
        return new PayPasswordView(content, title, isPay, tipContent, mContext, listener);
    }

    public void getDecorView(String content, String title, boolean isPay, String tipContent, Context mContext, OnPayListener listener) {
        this.listener = listener;
        this.mContext = mContext;
        mView = LayoutInflater.from(mContext).inflate(R.layout.item_paypassword, null);
        initView(mView);
        initListener();

        this.title.setText(title);

        this.content.setText(content);
        if (!StringUtils.isEmpty(content)) {
            this.content.setVisibility(View.VISIBLE);
        } else
            this.content.setVisibility(View.GONE);

        payContent.setText(tipContent);
        if (isPay) {
            payContent.setVisibility(View.VISIBLE);
        } else
            payContent.setVisibility(View.GONE);


    }

    private void initView(View view) {
        del = (ImageView) view.findViewById(R.id.pay_keyboard_del);
        zero = (TextView) view.findViewById(R.id.pay_keyboard_zero);
        one = (TextView) view.findViewById(R.id.pay_keyboard_one);
        two = (TextView) view.findViewById(R.id.pay_keyboard_two);
        three = (TextView) view.findViewById(R.id.pay_keyboard_three);
        four = (TextView) view.findViewById(R.id.pay_keyboard_four);
        five = (TextView) view.findViewById(R.id.pay_keyboard_five);
        sex = (TextView) view.findViewById(R.id.pay_keyboard_sex);
        seven = (TextView) view.findViewById(R.id.pay_keyboard_seven);
        eight = (TextView) view.findViewById(R.id.pay_keyboard_eight);
        nine = (TextView) view.findViewById(R.id.pay_keyboard_nine);


        box1 = (TextView) view.findViewById(R.id.pay_box1);
        box2 = (TextView) view.findViewById(R.id.pay_box2);
        box3 = (TextView) view.findViewById(R.id.pay_box3);
        box4 = (TextView) view.findViewById(R.id.pay_box4);
        box5 = (TextView) view.findViewById(R.id.pay_box5);
        box6 = (TextView) view.findViewById(R.id.pay_box6);

        title = (TextView) view.findViewById(R.id.pay_title);
        content = (TextView) view.findViewById(R.id.pay_content);
        payContent = (TextView) view.findViewById(R.id.pay_content_title);
        cancelPay = view.findViewById(R.id.cancel_pay);
        dismissKey = view.findViewById(R.id.dismiss_keybroad);
        keyboard = view.findViewById(R.id.keyboard);

    }

    private void initListener() {
        del.setOnClickListener(this);
        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        sex.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        cancelPay.setOnClickListener(this);
        dismissKey.setOnClickListener(this);
        box1.setOnClickListener(this);
        box2.setOnClickListener(this);
        box3.setOnClickListener(this);
        box4.setOnClickListener(this);
        box5.setOnClickListener(this);
        box6.setOnClickListener(this);

        del.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                parseActionType(KeyboardEnum.longdel);
                return true;
            }
        });
    }

    public void onClick(View v) {
        if (v == zero) {
            parseActionType(KeyboardEnum.zero);
        } else if (v == one) {
            parseActionType(KeyboardEnum.one);
        } else if (v == two) {
            parseActionType(KeyboardEnum.two);
        } else if (v == three) {
            parseActionType(KeyboardEnum.three);
        } else if (v == four) {
            parseActionType(KeyboardEnum.four);
        } else if (v == five) {
            parseActionType(KeyboardEnum.five);
        } else if (v == sex) {
            parseActionType(KeyboardEnum.sex);
        } else if (v == seven) {
            parseActionType(KeyboardEnum.seven);
        } else if (v == eight) {
            parseActionType(KeyboardEnum.eight);
        } else if (v == nine) {
            parseActionType(KeyboardEnum.nine);
        } else if (v == del) {
            parseActionType(KeyboardEnum.del);
        } else if (v == dismissKey) {
            keyboard.setVisibility(View.GONE);
        } else if (v == box1 || v == box2 || v == box3 || v == box4 || v == box5 || v == box6) {
            keyboard.setVisibility(View.VISIBLE);
        } else if (v == cancelPay) {
            parseActionType(KeyboardEnum.cancel);
        }
    }

    private void parseActionType(KeyboardEnum type) {
        // TODO Auto-generated method stub
        if (type.getType() == KeyboardEnum.ActionEnum.add) {
            if (mList.size() < 6) {
                mList.add(type.getValue());
                updateUi();
            }
            if(mList.size() == 6){
                String payValue="";
                for (int i = 0; i < mList.size(); i++) {
                    payValue+=mList.get(i);
                }
                listener.onSurePay(payValue);
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.delete) {
            if (mList.size() > 0) {
                mList.remove(mList.get(mList.size() - 1));
                updateUi();
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.longClick) {
            mList.clear();
            updateUi();
        }else if(type.getType() == KeyboardEnum.ActionEnum.cancel){
            listener.onCancelPay();
        }

    }

    private void updateUi() {
        // TODO Auto-generated method stub
        if (mList.size() == 0) {
            box1.setText("");
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 1) {
            box1.setText(mList.get(0));
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 2) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 3) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 4) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 5) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText("");
        } else if (mList.size() == 6) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText(mList.get(5));
        }
    }

    public interface OnPayListener {
        void onCancelPay();

        void onSurePay(String password);
    }

    public View getView() {
        return mView;
    }
}
