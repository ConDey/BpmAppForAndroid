package com.eazytec.bpm.app.filepicker.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Administrator
 * @version Id: CompositeFilter, v 0.1 2017/7/18 14:45 Administrator Exp $$
 */
public class CompositeFilter implements FileFilter, Serializable {

    private ArrayList<FileFilter> mFilters;

    public CompositeFilter(ArrayList<FileFilter> filters) {
        mFilters = filters;
    }

    @Override
    public boolean accept(File f) {
        for (FileFilter filter : mFilters) {
            if (!filter.accept(f)) {
                return false;
            }
        }

        return true;
    }
}

