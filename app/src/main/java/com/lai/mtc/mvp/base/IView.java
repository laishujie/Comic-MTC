/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lai.mtc.mvp.base;

import android.support.v7.widget.Toolbar;

import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.dialog.LoadingDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;

public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    void showLoading(@LoadingDialog.MODE int mode);

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

    <T> LifecycleTransformer<T> bindToLifecycle();

    void setToolBar(Toolbar toolBar, String title, boolean needBackButton);

    void handleError(ApiException e);
}
