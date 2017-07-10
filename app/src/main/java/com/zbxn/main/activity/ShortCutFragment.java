package com.zbxn.main.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.pub.base.BaseApp;
import com.pub.base.BaseFragment;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.main.activity.bulletin.CreatBulletinActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.workblog.CreatWorkBlogActivity;
import com.zbxn.main.entity.Member;
import com.zbxn.main.entity.WorkBlog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 快捷功能入口
 *
 * @author GISirFive
 * @time 2016/8/15
 */
public class ShortCutFragment extends BaseFragment {

    @BindView(R.id.mCreateAnnouncement)
    FrameLayout mCreateAnnouncement;
    @BindView(R.id.mCreateWorkBlog)
    FrameLayout mCreateWorkBlog;
    @BindView(R.id.mAnnouncementLayout)
    LinearLayout mAnnouncementLayout;
    @BindView(R.id.mBlogLayout)
    LinearLayout mBlogLayout;

    private OnItemSelectListener mOnItemSelectListener;
    private WorkBlog mWorkBlog;

    /**
     * 设置Item选中监听
     *
     * @param listener
     */
    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.mOnItemSelectListener = listener;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_main_shortcut, container, false);
        ButterKnife.bind(this, root);
        getIsBlogToday();
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        show();
    }

    @OnClick({R.id.mRootLayout, R.id.mCreateAnnouncement, R.id.mCreateWorkBlog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRootLayout:
                if (mOnItemSelectListener != null)
                    mOnItemSelectListener.OnItemSelected();
                break;
            case R.id.mCreateAnnouncement:
                startActivity(new Intent(getActivity(), CreatBulletinActivity.class));
                if (mOnItemSelectListener != null)
                    mOnItemSelectListener.OnItemSelected();
                break;
            case R.id.mCreateWorkBlog:
                checkBlogToday();
                break;
        }
    }

    public void show() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mAnnouncementLayout, "translationY", 80, 0).setDuration(300),
                ObjectAnimator.ofFloat(mBlogLayout, "translationY", 80, 0).setDuration(400));
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.start();
    }

    /**
     * 查询今天是否已写日志
     *
     * @return
     */
    public void checkBlogToday() {
        int state = Member.get().getBlogStateToday();
        switch (state) {
            case -1://未检查
                openCreateWorkBlog(null);
                break;
            case 0://未写
                openCreateWorkBlog(null);
                break;
            case 1://已写
                final MessageDialog messageDialog = new MessageDialog(getContext());
                messageDialog.setTitle("提示");
                messageDialog.setMessage(R.string.app_main_blogcenter_message);
                messageDialog.setNegativeButton("取消");
                messageDialog.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mWorkBlog != null) {
                            openCreateWorkBlog(mWorkBlog);
                        }
                    }
                });
                messageDialog.show();
                break;
        }
    }

    public void openCreateWorkBlog(WorkBlog blog) {
        Intent intent = new Intent(getActivity(), CreatWorkBlogActivity.class);
        // 将当天日志传入
        if (blog != null) {
            intent.putExtra(CreatWorkBlogActivity.Flag_Input_Blog, blog);
        }
        intent.putExtra("type", 1);
        startActivity(intent);

        if (mOnItemSelectListener != null)
            mOnItemSelectListener.OnItemSelected();
    }

    /**
     * 检查用户当天是否写了日志
     */
    public void getIsBlogToday() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getIsBlogToday(ssid);
        callRequest(call, new HttpCallBack(WorkBlog.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                mWorkBlog = (WorkBlog) mResult.getData();
                Member.get().setBlogStateToday(mWorkBlog == null ? 0 : 1);
                if (mWorkBlog != null) {
                    PreferencesUtils.putString(getContext(), "blog", mWorkBlog.getWorkblogcontent());
                }
            }

            @Override
            public void onFailure(String string) {
                Member.get().setBlogStateToday(mWorkBlog == null ? 0 : 1);
            }
        });
    }

    public interface OnItemSelectListener {
        /**
         * Item被选中
         */
        void OnItemSelected();
    }
}
