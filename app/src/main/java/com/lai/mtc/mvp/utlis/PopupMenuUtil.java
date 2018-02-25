package com.lai.mtc.mvp.utlis;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.View;

import java.util.List;

/**
 * @author Lai
 * @time 2018/1/23 22:16
 * @describe describe
 */

public class PopupMenuUtil {


    public   static void showPopupMenuList(Context context, List<IPopMenu> data, View tagView, PopupMenu.OnMenuItemClickListener listener) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, tagView);
        for (int i = 0; i < data.size(); i++)
            popupMenu.getMenu().add(Menu.NONE, data.get(i).getId(), i, data.get(i).getName());
        // popupMenu.getMenuInflater().inflate(R.menu.sample_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(listener);

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件

            }
        });
    }
}
