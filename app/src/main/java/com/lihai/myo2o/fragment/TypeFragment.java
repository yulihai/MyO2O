package com.lihai.myo2o.fragment;

import android.view.View;

import com.lihai.common.base.BaseFragment;
import com.lihai.myo2o.R;
import com.lihai.myo2o.damain.ShopType;

import java.util.List;

/**
 * Created by LiHai on 2017/3/2.
 */
public class TypeFragment extends BaseFragment {

    List<ShopType> shopTypes;

    public void setDate(List<ShopType> shopTypes){

        this.shopTypes = shopTypes;

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_type;
    }

    @Override
    protected void init(View view) {

    }
}
