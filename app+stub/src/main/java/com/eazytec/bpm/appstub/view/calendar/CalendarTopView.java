package com.eazytec.bpm.appstub.view.calendar;


public interface CalendarTopView {

    int[] getCurrentSelectPositon();

    int getItemHeight();

    void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener);

}
