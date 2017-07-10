package com.pub.dialog;

import android.app.Dialog;

/**
 * Created by wuzy on 2015/8/9.
 */
public interface IDialogAlertListener {

    public void onDialogOk(Dialog dlg, Object param);

    public void onDialogCancel(Dialog dlg, Object param);

}
