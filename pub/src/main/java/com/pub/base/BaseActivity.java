package com.pub.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.pub.R;
import com.pub.common.ToolbarHelper;
import com.pub.common.ToolbarParams;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpCallBackDownload;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 对Activity的封装，集成Toolbar等
 * Created by GISirFive on 2016/7/28.
 */
public abstract class BaseActivity extends AppCompatActivity implements ToolbarHelper.IToolBar {

    //Toolbar操作类
    private ToolbarHelper mToolbarHelper;

    public ToolbarHelper getToolbarHelper() {
        return mToolbarHelper;
    }

    //右按钮1
    private MenuItem mItemRight1;
    //右按钮2
    private MenuItem mItemRight2;

    private List<Call> m_listRequest;

    private List<Call> getListRequest() {
        if (m_listRequest == null) {
            m_listRequest = new ArrayList<>();
        }
        return m_listRequest;
    }

    /**
     * 调用网络请求
     *
     * @param call
     * @param httpCallBack
     */
    public void callRequest(Call call, HttpCallBack httpCallBack) {
        call.enqueue(httpCallBack);
        addRequest(call);
    }

    /**
     * 调用网络请求
     *
     * @param call
     * @param httpCallBack
     */
    public void callRequest(Call call, HttpCallBackDownload httpCallBack) {
        call.enqueue(httpCallBack);
        addRequest(call);
    }

    /**
     * 添加到请求队列，以便销毁
     *
     * @param call
     */
    public void addRequest(Call call) {
        getListRequest().add(call);
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setContentInsetsRelative(0, 0);
        //返回按钮关闭当前activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mToolbarHelper.setTitle(title);
    }

    /**
     * 隐藏返回按钮
     */
    public void setBackGone() {
        mToolbarHelper.getToolBar().setNavigationIcon(null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setStatusBarColor(R.color.statusBar_color);
    }

    /**
     * 自定义状态栏颜色 适配4.4及以上
     */
    public void setStatusBarColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //set child View not fill the system window
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(resId);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mToolbarHelper = new ToolbarHelper(this, layoutResID, this);
        setContentView(mToolbarHelper.getRootView());
        Toolbar mToolbar = mToolbarHelper.getToolBar();
        // 把toolbar设置到Activity中
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //取消网络请求
        if (m_listRequest != null && m_listRequest.size() > 0) {
            for (Call call : m_listRequest) {
                call.cancel();
            }
        }
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_activity, menu);
        //返回按钮关闭当前activity
        mToolbarHelper.getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mItemRight1 = menu.findItem(R.id.item_right1);
        mItemRight2 = menu.findItem(R.id.item_right2);
        initRight();
        mItemRight1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                actionRight1(item);
                return true;
            }
        });
        mItemRight2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                actionRight2(item);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听  每次点击menu键都会重新调用
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    /**
     * 当客户点击菜单当中的某一个选项时，会调用该方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 加载右按钮
     */
    public void initRight() {

    }

    /**
     * 设置右按钮1名称
     *
     * @param text
     */
    public void setRight1(String text) {
        mItemRight1.setTitle(text);
    }

    /**
     * 设置右按钮2名称
     *
     * @param text
     */
    public void setRight2(String text) {
        mItemRight2.setTitle(text);
    }

    /**
     * 设置右按钮1图标
     *
     * @param res
     */
    public void setRight1Icon(int res) {
        mItemRight1.setIcon(res);
    }

    /**
     * 设置右按钮2图标
     *
     * @param res
     */
    public void setRight2Icon(int res) {
        mItemRight2.setIcon(res);
    }

    /**
     * 设置右按钮1是否显示
     *
     * @param isShow
     */
    public void setRight1Show(boolean isShow) {
        mItemRight1.setVisible(isShow);
    }

    /**
     * 设置右按钮2是否显示
     *
     * @param isShow
     */
    public void setRight2Show(boolean isShow) {
        mItemRight2.setVisible(isShow);
    }

    /**
     * 设置右按钮1点击事件
     *
     * @param menuItem
     */
    public void actionRight1(MenuItem menuItem) {
    }

    /**
     * 设置右按钮2点击事件
     *
     * @param menuItem
     */
    public void actionRight2(MenuItem menuItem) {
    }
}
