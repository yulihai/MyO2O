package com.lihai.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihai.common.utils.StringUtil;

/**
 * Created by LiHai on 2017/3/18.
 */
public class MyDialog extends DialogFragment {

    static Context myContext;

    Button leftBtn,rightBtn;

    Dialog dialog;

    public static MyDialog getInstance(Context context){
        myContext = context;
        return new MyDialog();
    }

    public MyDialog(){
        dialog = new Dialog(myContext,R.style.myDialogTheme);

        dialog.setContentView(R.layout.dialog_widget_layout);

        //设置对话框的宽度
        //获取屏幕属性
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        //获取屏幕密度(dpi)
        float densitys = myContext.getResources().getDisplayMetrics().density;
        //设置对话框宽度
        layoutParams.width = (int) (densitys*280);   //相当于280dp

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialog();
    }

    public Dialog createDialog(){
        //判断有没有按钮
        if (rightBtn == null && leftBtn == null){
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.btnRoot);
            ViewGroup viewGroup = (ViewGroup) linearLayout.getParent();
            viewGroup.removeView(linearLayout);
        }
        return dialog;
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){

        TextView textView = (TextView) dialog.findViewById(R.id.title);

        if (title == null || "".equals(title)){
            LinearLayout linearLayout = (LinearLayout) textView.getParent();
            linearLayout.removeView(textView);
        }else {
            textView.setText(title);
        }
    }

    /**
     * 设置信息
     * @param msg
     */
    public void setMsg(Object msg){

        TextView msgTextView = (TextView) dialog.findViewById(R.id.msg);
        if (msg instanceof String){
            msgTextView.setText(msg + "");
        }else if (msg instanceof Integer){

            String content = myContext.getResources().getString((Integer) msg);
            msgTextView.setText(content);
        }
    }

    /**
     * 在内容（msg） 内设置添加其他布局等。。。
     * @param content
     */
    public void setContentView(Object content){

        TextView msgTextView = (TextView) dialog.findViewById(R.id.msg);
        LinearLayout linearLayout = (LinearLayout) msgTextView.getParent();
        linearLayout.removeView(msgTextView);//移除

        if (content instanceof View){
           linearLayout.addView((View) content);
        }else if (content instanceof Integer){
            int layoutId = (Integer) content;
            //将布局文件 转为view 对象
            LayoutInflater layoutInflater = LayoutInflater.from(myContext);
            //创建布局文件对象的同时添加父控件
            layoutInflater.inflate(layoutId,linearLayout);
        }
    }

    public void setItems(String[] itemName,final MyItemLongClickerListener myItemLongClickerListener){     //   添加 喜欢  不喜欢 删除 拨打电话 短信。。。。。


            //TODO

    }


    public void setBtn(String leftText,String rightText,final BtnClickerListener btnClickerListener ){

        leftBtn = (Button) dialog.findViewById(R.id.leftBtn);
        if (StringUtil.isEmpty(leftText)){
            //不要左边按钮
            LinearLayout linearLayout = (LinearLayout) leftBtn.getParent();
            linearLayout.removeView(leftBtn);
            leftBtn = null;
        }else {
            leftBtn.setText(leftText);
            leftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //由外部来决定要干什么
                    if (btnClickerListener != null){
                        btnClickerListener.onClicker(dialog,0);
                    }
                }
            });
        }


        rightBtn = (Button) dialog.findViewById(R.id.rightBtn);
        if (StringUtil.isEmpty(rightText)){
            //不要右边按钮
            LinearLayout linearLayout = (LinearLayout) rightBtn.getParent();
            linearLayout.removeView(rightBtn);
            leftBtn = null;
        }else {
            rightBtn.setText(rightText);
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //由外部来决定要干什么
                    if (btnClickerListener != null){
                        btnClickerListener.onClicker(dialog,1);
                    }
                }
            });
        }

    }

    public interface BtnClickerListener{
        void onClicker(Dialog dialog, int which);
    }

    public interface MyItemLongClickerListener{
        void onClicker(int which);
    }

}
