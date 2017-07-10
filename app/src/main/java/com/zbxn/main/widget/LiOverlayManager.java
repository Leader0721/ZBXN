package com.zbxn.main.widget;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.pub.utils.OverlayManager;
import com.zbxn.main.listener.ICustomListener;

import java.util.ArrayList;
import java.util.List;

public class LiOverlayManager extends OverlayManager {
    private List<OverlayOptions> optionsList = new ArrayList<OverlayOptions>();
    private ICustomListener listener;

    public LiOverlayManager(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        return optionsList;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        /*NetworkEntity data = (NetworkEntity) marker.getExtraInfo().getSerializable("data");
        listener.onCustomListener(0, data, 0);*/
        listener.onCustomListener(0, marker, 0);
        return false;
    }

    public void setData(List<OverlayOptions> optionsList) {
        this.optionsList = optionsList;
    }

    public void setCustomListener(ICustomListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        return false;
    }
}