package com.eazytec.bpm.app.filepicker.cursors.loadercallbacks;

import java.util.List;

/**
 * @author Administrator
 * @version Id: FileResultCallback, v 0.1 2017/7/19 10:08 Administrator Exp $$
 */
public interface FileResultCallback<T> {
    void onResultCallback(List<T> files);
}
