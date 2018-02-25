package com.lai.mtc.comm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lai.mtc.R;
import com.lai.mtc.mvp.utlis.DensityUtil;


public class CommDialog extends Dialog {
    private View dialogView;
    private Context context;
    private int height, width;
    private boolean cancelTouchout;

    private Context mContext;

    public CommDialog(@NonNull Builder builder) {
        super(builder.context, R.style.commonDialogNoBackground);
        this.mContext = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        dialogView = builder.view;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(cancelTouchout);

        Window win = getWindow();
        if (win != null) {
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.height = height;
            lp.width = width;
            win.setAttributes(lp);
        }

        setContentView(dialogView);
        setCancelable(false);

    }

    public static final class Builder {

        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int resStyle = -1;

        public CommDialog build() {
            return new CommDialog(this);
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder view(int resView) {
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public Builder heightpx(int val) {
            height = val;
            return this;
        }

        public Builder widthpx(int val) {
            width = val;
            return this;
        }

        public Builder heightdp(int val) {
            height = (int) DensityUtil.px2dp(context,val);
            return this;
        }

        public Builder widthdp(int val) {
            width = DensityUtil.dp2px(context,val);
            return this;
        }

        public Builder heightDimenRes(int dimenRes) {
            height = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder widthDimenRes(int dimenRes) {
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }


    }


    /**
     * 向外提供获取view的方法
     *
     * @param viewId viewId
     * @return view
     */
    public View getView(int viewId) {
        return dialogView.findViewById(viewId);
    }
}
