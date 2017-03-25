package com.lihai.myo2o.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.lihai.common.base.BaseFragment;
import com.lihai.myo2o.R;
import com.lihai.myo2o.activity.AboutUsActivity;
import com.lihai.myo2o.activity.LoginActivity;
import com.lihai.request.utils.RequestUtil;

import java.util.Map;

/**
 * Created by LiHai on 2017/2/28.
 */
public class MineFragment extends BaseFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    RelativeLayout relativeLayout;

    @Override
    protected void init(View view) {

       /* //加载个人信息，判断是否登录
        String URL = "http://testwxys.rhy.com/e/theuser";
        RequestUtil.get(URL, null, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                int status = Integer.parseInt(result.get("status") + "");
                if (status == -91){   //未登录

                    Intent intent = new Intent(activity, LoginActivity.class);
                    //intent.putExtra("MineFragment",activity);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(String error) {

            }
        });*/

        relativeLayout = (RelativeLayout) view.findViewById(R.id.personal_info);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AboutUsActivity.class);
                startActivity(intent);
            }
        });

    }
}
