package com.zbxn.crm.activity.custom;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.entity.ProvinceEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */
public class ProvinceDBHelper {
    private String DB_NAME = "region.db";
    private Context mContext;
    private DbUtils mDbUtils;
    private String copyToPath;

    public ProvinceDBHelper(Context context) {
        this.mContext = context;
        copyToPath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + DB_NAME;
        File file = new File(copyToPath);
        if (!file.exists()) {
            try {
                InputStream is = context.getResources().openRawResource(R.raw.region);
                FileOutputStream out = new FileOutputStream(file);
                byte[] byteArray = new byte[1024];
                int readlength = is.read(byteArray);
                while (readlength != -1) {
                    out.write(byteArray, 0, readlength);
                    readlength = is.read(byteArray);
                }
                is.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mDbUtils = DbUtils.create(context, copyToPath);
    }

    public List<ProvinceEntity> getArea(String level, String pcode) {
        List<ProvinceEntity> list = new ArrayList<ProvinceEntity>();
        try {
            if (!StringUtils.isEmpty(pcode)) {
                list = mDbUtils.findAll(Selector.from(ProvinceEntity.class).where("RegionLevel", "=", level).and("ParentCode", "=", pcode));//
            } else {
                list = mDbUtils.findAll(Selector.from(ProvinceEntity.class).where("RegionLevel", "=", level));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
    }

}

