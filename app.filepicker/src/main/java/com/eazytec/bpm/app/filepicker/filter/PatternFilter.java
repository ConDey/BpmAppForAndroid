package com.eazytec.bpm.app.filepicker.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @version Id: PatternFilter, v 0.1 2017/7/18 14:50 Administrator Exp $$
 */
public class PatternFilter implements FileFilter, Serializable {

    private Pattern mPattern;
    private boolean mDirectoriesFilter;

    public PatternFilter(Pattern pattern, boolean directoriesFilter) {
        mPattern = pattern;
        mDirectoriesFilter = directoriesFilter;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() && !mDirectoriesFilter || mPattern.matcher(f.getName()).matches();
    }
}

