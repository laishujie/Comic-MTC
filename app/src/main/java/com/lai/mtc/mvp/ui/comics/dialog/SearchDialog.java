package com.lai.mtc.mvp.ui.comics.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lai.mtc.R;
import com.lai.mtc.comm.CommonAdapter;
import com.lai.mtc.dao.SearchRecordDao;
import com.lai.mtc.dao.bean.SearchRecord;
import com.lai.mtc.mvp.utlis.KeyBoardUtils;
import com.lai.mtc.mvp.utlis.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscription;

/**
 * @author Lai
 * @time 2018/2/10 18:50
 * @describe 搜索对话框
 */

public class SearchDialog extends Dialog {
    @BindView(R.id.ed_title)
    EditText mEdTitle;
    @BindView(R.id.iv_clean)
    ImageView mIvClean;
    @BindView(R.id.ac_toolbar)
    Toolbar mAcToolbar;
    @BindView(R.id.rv_history)
    RecyclerView mRvHistory;
    @BindView(R.id.tv_clean)
    TextView mTvClean;

    private Unbinder bind;
    private SearchRecordDao mSearchRecordDao;
    private CommonAdapter<SearchRecord> commonAdapter;
    private DataSubscription mDataSubscription;

    public void setITextClick(ITextClick ITextClick) {
        mITextClick = ITextClick;
    }

    private ITextClick mITextClick;

    public interface ITextClick {
        void text(String input);
    }

    @OnClick(R.id.iv_clean)
    void onclickClean() {
        mEdTitle.setText("");
    }

    @OnClick(R.id.tv_clean)
    void onclickTvClean() {
        mSearchRecordDao.deleteAll();
    }

    public SearchDialog(@NonNull Context context, SearchRecordDao mSearchRecordDao) {
        super(context);
        this.mSearchRecordDao = mSearchRecordDao;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.comic_dialog_search, null);
        //使Dialog横向全屏
        inflate.setMinimumWidth(10000);
        Window window = getWindow();
        //顶部显示
        if (window != null)
            window.setGravity(Gravity.TOP);
        setContentView(inflate);
        bind = ButterKnife.bind(this, inflate);
        init();
        bindEvent();
        KeyBoardUtils.openKeyboard(getContext(), mEdTitle);
    }

    private void bindEvent() {
        mEdTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != 0) {
                    if (mIvClean.getVisibility() != View.VISIBLE)
                        mIvClean.setVisibility(View.VISIBLE);
                } else {
                    if (mIvClean.getVisibility() != View.GONE)
                        mIvClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEdTitle.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 2. 点击搜索键后，对该搜索字段在数据库是否存在进行检查（查询）->> 关注1
                    String trim = mEdTitle.getText().toString().trim();
                    if (!TextUtils.isEmpty(trim)) {
                        boolean hasData = mSearchRecordDao.hasData(trim);
                        // 3. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
                        if (!hasData) {
                            mSearchRecordDao.addSearch(trim);
                        }
                        if (mITextClick != null) {
                            mITextClick.text(trim);
                            dismiss();
                        }
                    } else {
                        Toast.makeText(getContext(), "不能搜索空内容", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        mAcToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        KeyBoardUtils.closeKeyboard(getContext(), mEdTitle);
        if (mDataSubscription != null)
            mDataSubscription.cancel();
        if (bind != null)
            bind.unbind();
        super.dismiss();
    }


    public void init() {
        List<SearchRecord> all = mSearchRecordDao.getAll();
        mRvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<SearchRecord>(R.layout.comic_item_preview_left, all) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, SearchRecord searchRecordDao, int position) {
                baseViewHolder.setText(R.id.tv_name, StringUtils.isNull(searchRecordDao.getInputName())).addOnClickListener(R.id.tv_name);
            }
        };
        mRvHistory.setAdapter(commonAdapter);
        commonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mITextClick != null) {
                    SearchRecord searchRecord = (SearchRecord) adapter.getData().get(position);
                    mITextClick.text(searchRecord.getInputName());
                }
                dismiss();
            }
        });
        mDataSubscription = mSearchRecordDao.getBox().query().build().subscribe().on(AndroidScheduler.mainThread())
                .observer(new DataObserver<List<SearchRecord>>() {
                    @Override
                    public void onData(@NonNull List<SearchRecord> data) {
                        commonAdapter.setData(true, data);
                    }
                });
    }

}
