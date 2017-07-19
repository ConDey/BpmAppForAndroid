package com.eazytec.bpm.app.filepicker.adpters;

/**
 * 选择接口
 * @author Administrator
 * @version Id: Selectable, v 0.1 2017/7/19 9:36 Administrator Exp $$
 */
public interface Selectable<T> {


    boolean isSelected(T item);

    void toggleSelection(T item);

    void clearSelection();

    int getSelectedItemCount();

}

