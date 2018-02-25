package com.lai.mtc.mvp.ui.comics.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.lai.mtc.R;
import com.lai.mtc.mvp.utlis.SPUtils;

/**
 * @author Lai
 * @time 2018/1/29 22:44
 * @describe describe
 */

public class ModuleDialog extends Dialog {

    private Context mContext;
    private View mView;
    private RadioButton mCbPull;
    private RadioButton mCbPager;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private View.OnClickListener mOnClickListener;

    public ModuleDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(mContext).inflate(R.layout.pop_module, null);
        setContentView(mView);
        mCbPull = mView.findViewById(R.id.rb_pull);
        mCbPager = mView.findViewById(R.id.rb_pager);
        init();
    }

    private void init() {
        int module = SPUtils.getInstance("config").getInt("module", 0);
        if (module == 0)
            mCbPull.setChecked(true);
        else
            mCbPager.setChecked(true);

        mView.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mView.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int module = mCbPager.isChecked() ? 1 : 0;
                SPUtils.getInstance("config").put("module", module);
                if (mOnClickListener != null)
                    mOnClickListener.onClick(v);
                dismiss();
            }
        });
    }

}
