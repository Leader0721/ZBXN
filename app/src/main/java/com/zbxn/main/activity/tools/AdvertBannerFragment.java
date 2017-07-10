package com.zbxn.main.activity.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.pub.base.BaseApp;
import com.pub.base.BaseFragment;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.JsonUtil;
import com.pub.utils.KEY;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.ScreenUtils;
import com.zbxn.R;
import com.zbxn.main.entity.AdvertEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * @author GISirFive
 * @time 2016/8/5
 */
public class AdvertBannerFragment extends BaseFragment implements CBViewHolderCreator<AdvertHolder> {

    @BindView(R.id.mBanner)
    ConvenientBanner mBanner;

    private List<String> imgList = new ArrayList<>();

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_toolscenter_advertbanner, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        // 调整该页面布局大小
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBanner
                .getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(getContext());
        layoutParams.height = (int) (layoutParams.width * 9f / 16 * 0.618);//自动适配
        mBanner.setLayoutParams(layoutParams);


        String images = PreferencesUtils.getString(BaseApp.getContext(), KEY.MOBILEIMAGES, "[]");

        List<AdvertEntity> list = JsonUtil.fromJsonList(images, AdvertEntity.class);
        setImages(list);

        getImage(getActivity());
    }

    /**
     * 获取轮播图图片
     *
     * @param context
     */
    public void getImage(Context context) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().getCarouselPicture(ssid);
        callRequest(call, new HttpCallBack(AdvertEntity.class, context, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    List<AdvertEntity> list = (List<AdvertEntity>) mResult.getRows();

                    String json = JsonUtil.toJsonString(list);
                    //轮播图列表
                    PreferencesUtils.putString(BaseApp.getContext(), KEY.MOBILEIMAGES, json);

                    setImages(list);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 显示图片
     *
     * @param list
     */
    private void setImages(List<AdvertEntity> list) {
        imgList.clear();
        for (int i = 0; i < list.size(); i++) {
            imgList.add(list.get(i).getPicturesrc());
        }
        mBanner.setPages(AdvertBannerFragment.this, imgList);
        //修改小点
        mBanner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
        mBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        mBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBanner.stopTurning();
    }

    @Override
    public AdvertHolder createHolder() {
        return new AdvertHolder();
    }

}
