package com.eazytec.bpm.appstub.view.timepick.listener;


import com.eazytec.bpm.appstub.view.timepick.bean.DateBean;
import com.eazytec.bpm.appstub.view.timepick.bean.DateType;

public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
