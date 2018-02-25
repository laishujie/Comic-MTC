package com.lai.mtc.mvp.ui.comics.pop;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.lai.mtc.R;
import com.lai.mtc.mvp.utlis.SPUtils;

/**
 * @author Lai
 * @time 2018/1/27 18:01
 * @describe  亮度pop弹窗
 */

public class WindowLightPop extends PopupWindow {

    private Context mContext;
    private View view;
    private SeekBar mSeekBar;
    private int light;

    private CheckBox mCheckBox;


    public WindowLightPop(Context context) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mContext = context;

        view = LayoutInflater.from(mContext).inflate(R.layout.pop_windowlight, null);
        this.setContentView(view);
        initView();
        bindEvent();
        light = getLight();
        mSeekBar.setProgress(light);
        mCheckBox.setChecked(SPUtils.getInstance("config").getBoolean("isFlowSystem"));
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
    }

    private void initView() {
        mSeekBar = view.findViewById(R.id.hpb_light);
        mCheckBox = view.findViewById(R.id.scb_follow_sys);
    }

    private void bindEvent() {
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSeekBar.setEnabled(false);
                    //跟随系统
                    setScreenBrightness();
                } else {
                    mSeekBar.setEnabled(true);
                    //不跟随系统
                    mSeekBar.setProgress(light);
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                light = progress;
                setScreenBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        SPUtils config = SPUtils.getInstance("config");

        config.put("light", mSeekBar.getProgress());
        config.put("isFlowSystem", mCheckBox.isChecked());
    }

    private int getLight() {
        int anInt = SPUtils.getInstance("config").getInt("light", -1);
        return anInt == -1 ? getScreenBrightness() : anInt;
    }

    private int getScreenBrightness() {
        int value = 0;
        ContentResolver cr = mContext.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void setScreenBrightness(int value) {
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.screenBrightness = value * 1.0f / 255f;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

    private void setScreenBrightness() {
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

}
