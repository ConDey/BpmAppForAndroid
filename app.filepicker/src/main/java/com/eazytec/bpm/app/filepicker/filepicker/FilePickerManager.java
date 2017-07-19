package com.eazytec.bpm.app.filepicker.filepicker;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.models.BaseFile;
import com.eazytec.bpm.app.filepicker.models.FileType;

import java.util.ArrayList;

/**
 * @author Administrator
 * @version Id: FileFilePickerManager, v 0.1 2017/7/19 9:48 Administrator Exp $$
 */
public class FilePickerManager {

    private static FilePickerManager ourInstance = new FilePickerManager();
    private int maxCount = FilePickerConst.DEFAULT_MAX_COUNT;
    private int currentCount;

    public static FilePickerManager getInstance() {
        return ourInstance;
    }

    private ArrayList<String> docFiles;

    private ArrayList<FileType> fileTypes;

    private int theme = R.style.LibAppTheme;

    private boolean docSupport = true;

    private boolean enableOrientation = false;

    private boolean showFolderView = true;

    private String providerAuthorities;

    private FilePickerManager() {
        docFiles = new ArrayList<>();
        fileTypes = new ArrayList<>();
    }

    public void setMaxCount(int count) {
        clearSelections();
        this.maxCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void add(String path, int type) {
        if (path != null && shouldAdd()) {
            if (type == FilePickerConst.FILE_TYPE_DOCUMENT)
                docFiles.add(path);
            else
                return;

            currentCount++;
        }
    }

    public void add(ArrayList<String> paths, int type) {
        for (int index = 0; index < paths.size(); index++) {
            add(paths.get(index), type);
        }
    }

    public void remove(String path, int type) {

        if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {
            docFiles.remove(path);

            currentCount--;
        }
    }

    public boolean shouldAdd() {
        return currentCount < maxCount;
    }


    public ArrayList<String> getSelectedFiles() {
        return docFiles;
    }

    public ArrayList<String> getSelectedFilePaths(ArrayList<BaseFile> files) {
        ArrayList<String> paths = new ArrayList<>();
        for (int index = 0; index < files.size(); index++) {
            paths.add(files.get(index).getPath());
        }
        return paths;
    }

    public void clearSelections() {
        docFiles.clear();
        fileTypes.clear();
        currentCount = 0;
        maxCount = 0;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }


    public boolean isShowFolderView() {
        return showFolderView;
    }

    public void setShowFolderView(boolean showFolderView) {
        this.showFolderView = showFolderView;
    }

    public void addFileType(FileType fileType)
    {
        fileTypes.add(fileType);
    }

    public void addDocTypes()
    {
        String[] pdfs = {"pdf"};
        fileTypes.add(new FileType(FilePickerConst.PDF,pdfs,R.mipmap.ic_pdf_box));

        String[] docs = {"doc", "docm", "docx", "dot", "mcw", "rtf", "pages", "odt", "ott"};
        fileTypes.add(new FileType(FilePickerConst.DOC,docs,R.mipmap.ic_word_box));

        String[] ppts = {"pptx", "keynote", "ppt", "pps", "pot", "odp", "otp"};
        fileTypes.add(new FileType(FilePickerConst.PPT,ppts,R.mipmap.ic_powerpoint_box));

        String[] xlss = {"xls", "xlk", "xlsb", "xlsm", "xlsx", "xlr", "xltm", "xlw", "numbers", "ods", "ots"};
        fileTypes.add(new FileType(FilePickerConst.XLS,xlss,R.mipmap.ic_excel_box));

        String[] txts = {"txt"};
        fileTypes.add(new FileType(FilePickerConst.TXT,txts,R.mipmap.ic_document_box));

        String[] zips = {"cab", "7z", "alz", "arj", "bzip2", "bz2", "dmg", "gzip", "gz", "jar", "lz", "lzip", "lzma", "zip", "rar", "tar", "tgz"};
        fileTypes.add(new FileType(FilePickerConst.ZIP,zips,R.mipmap.ic_zip_box));

        String[] apks = {"apk"};
        fileTypes.add(new FileType(FilePickerConst.APK,apks,R.mipmap.ic_apk_box));

        String[] certificates = {"cer", "der", "pfx", "p12", "arm", "pem"};
        fileTypes.add(new FileType(FilePickerConst.CERTIFICATE,certificates,R.mipmap.ic_certificate_box));

    }

    public ArrayList<FileType> getFileTypes()
    {
        return fileTypes;
    }

    public boolean isDocSupport() {
        return docSupport;
    }

    public void setDocSupport(boolean docSupport) {
        this.docSupport = docSupport;
    }


    public boolean isEnableOrientation() {
        return enableOrientation;
    }

    public void setEnableOrientation(boolean enableOrientation) {
        this.enableOrientation = enableOrientation;
    }

    public String getProviderAuthorities() {
        return providerAuthorities;
    }

    public void setProviderAuthorities(String providerAuthorities) {
        this.providerAuthorities = providerAuthorities;
    }
}
