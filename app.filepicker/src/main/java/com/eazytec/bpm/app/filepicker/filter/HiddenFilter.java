package com.eazytec.bpm.app.filepicker.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * @author Administrator
 * @version Id: HiddenFilter, v 0.1 2017/7/18 14:49 Administrator Exp $$
 */
public class HiddenFilter implements FileFilter, Serializable {

    @Override
    public boolean accept(File f) {
        return !f.isHidden();
    }
}
