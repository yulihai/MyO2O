package com.lihai.myo2o.activity;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lihai.common.adapter.MyAdapter;
import com.lihai.common.base.BaseActivity;
import com.lihai.myo2o.R;
import com.lihai.myo2o.utils.MyFlowLayout;
import com.lihai.request.utils.RequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiHai on 2017/3/22.
 */
public class SearchPageActivity extends BaseActivity implements View.OnClickListener {


    ListView search_page_list_view ;
    MyAdapter myAdapter;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText editText;
    TextView search_textView,clean_history;
    MyFlowLayout myFlowLayout;

    List<Map<String, Object>> historyDatas;


    @Override
    protected int setLayout() {
        return R.layout.layout_search_page;
    }


    @Override
    protected void initView() {

        preferences = getSharedPreferences("searchPage", SearchPageActivity.MODE_PRIVATE);  //共享参数，保存搜索历史
        editor = preferences.edit();

        search_textView = (TextView) findViewById(R.id.search_textView);//搜索
        search_textView.setOnClickListener(this);

        clean_history = (TextView) findViewById(R.id.clean_history);  //清除历史记录
        clean_history.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.edit_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {    //监听键盘回车键
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        myFlowLayout = (MyFlowLayout) findViewById(R.id.flow_layout);  //流式布局
        initData();    //加载数据

        historyDatas = new ArrayList<Map<String, Object>>();   //listView 的数据
        Map<String, Object> dataMap;
        String historyNames = preferences.getString("historyName", "");
        if (!TextUtils.isEmpty(historyNames)) {
            for (Object historyName : historyNames.split(",")) {      //TODO
                dataMap = new HashMap<>();
                dataMap.put("historyName", historyName);
                historyDatas.add(dataMap);
            }
        }
        search_page_list_view = (ListView) findViewById(R.id.search_page_list_view);    //listView
        myAdapter = new MyAdapter(this, historyDatas, R.layout.item_search_history, new String[]{"historyName"}, new int[]{R.id.history_record});
        search_page_list_view.setAdapter(myAdapter);
        setListViewHeightBasedOnChildren(search_page_list_view);
        search_page_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(historyDatas.get(position).get("historyName") +"");
            }
        });
    }

    /**
     * 加载 MyFlowLayout 的数据
     */
    public void initData() {

        String URL = "http://testwxys.rhy.com/hotsearch";
        RequestUtil.post(URL, null, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                int status = Integer.parseInt(result.get("status") + "");
                if (status == 1) {
                    Map<String, Object> data = (Map<String, Object>) result.get("data");

                    LayoutInflater layoutInflater = LayoutInflater.from(SearchPageActivity.this);
                    List<Map<String, Object>> searchLists = (List<Map<String, Object>>) data.get("hotSearchList");
                    for (Map<String, Object> searchList : searchLists) {

                        Button button = (Button) layoutInflater.inflate(R.layout.btn_flow_layout, myFlowLayout, false);
                        button.setText(searchList.get("name") + "");
                        myFlowLayout.addView(button);
                    }
                    int childCount = myFlowLayout.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        myFlowLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button button = (Button) v;
                                editText.setText(button.getText());
                                //TODO    搜索此内容
                            }
                        });
                    }
                } else {
                    Toast.makeText(SearchPageActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    /**
     * ScrollView中嵌套ListView空间，无法正确的计算ListView的大小
     * 需要根据当前的ListView的列表项计算列表的尺寸
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int len = listAdapter.getCount();
        for (int i = 0; i< len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_textView:
                String historyName = editText.getText().toString();
                String oldHistoryName = preferences.getString("historyName", "");
                if (!TextUtils.isEmpty(historyName) && !oldHistoryName.contains(historyName)) {
                    editor.putString("historyName", historyName + "," + oldHistoryName);
                    editor.commit();
                }
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.clean_history:
                editor.clear();
                editor.commit();
                historyDatas.clear();
                myAdapter.notifyDataSetChanged();
                break;
        }
    }
}
