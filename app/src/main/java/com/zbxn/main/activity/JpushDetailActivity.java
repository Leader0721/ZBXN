package com.zbxn.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.zbxn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动页
 */
public class JpushDetailActivity extends Activity  {

    @BindView(R.id.mVersion)
    TextView mVersion;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);

        mVersion.setText(getIntent().getStringExtra("jpush"));
    }
}
