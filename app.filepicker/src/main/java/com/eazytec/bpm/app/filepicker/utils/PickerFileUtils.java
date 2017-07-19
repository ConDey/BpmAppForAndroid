package com.eazytec.bpm.app.filepicker.utils;

import android.text.TextUtils;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerConst;

import java.io.File;

/**
 * @author Administrator
 * @version Id: PickerFileUtils, v 0.1 2017/7/19 10:22 Administrator Exp $$
 */
public class PickerFileUtils {

    public static int getTypeDrawable(String path)
    {
        if(getFileType(path)== FilePickerConst.FILE_TYPE.EXCEL)
            return R.mipmap.ic_excel_box;
        if(getFileType(path)== FilePickerConst.FILE_TYPE.WORD)
            return R.mipmap.ic_word_box;
        if(getFileType(path)== FilePickerConst.FILE_TYPE.PPT)
            return R.mipmap.ic_powerpoint_box;
        if(getFileType(path)== FilePickerConst.FILE_TYPE.PDF)
            return R.mipmap.ic_pdf_box;
        if(getFileType(path)== FilePickerConst.FILE_TYPE.TXT)
            return R.mipmap.ic_document_box;
        if(getFileType(path)== FilePickerConst.FILE_TYPE.APK)
            return R.mipmap.ic_apk_box;
        if(getFileType(path)== FilePickerConst.FILE_TYPE.CERTIFICATE)
            return R.mipmap.ic_certificate_box;
        if(getFileType(path)== FilePickerConst.FILE_TYPE.ZIP)
            return R.mipmap.ic_zip_box;
        else
            return R.mipmap.ic_document_box;
    }

    public static FilePickerConst.FILE_TYPE getFileType(String path)
    {
        String fileExtension = Utils.getFileExtension(new File(path));
        if(TextUtils.isEmpty(fileExtension))
            return FilePickerConst.FILE_TYPE.UNKNOWN;
        if(isExcelFile(path))
            return FilePickerConst.FILE_TYPE.EXCEL;
        if(isDocFile(path))
            return FilePickerConst.FILE_TYPE.WORD;
        if(isPPTFile(path))
            return FilePickerConst.FILE_TYPE.PPT;
        if(isPDFFile(path))
            return FilePickerConst.FILE_TYPE.PDF;
        if(isTxtFile(path))
            return FilePickerConst.FILE_TYPE.TXT;
        if(isApkFile(path))
            return FilePickerConst.FILE_TYPE.APK;
        if(isCertificatesFile(path))
            return FilePickerConst.FILE_TYPE.CERTIFICATE;
        if(isZipsFile(path))
            return FilePickerConst.FILE_TYPE.ZIP;
        else
            return FilePickerConst.FILE_TYPE.UNKNOWN;
    }

    public static boolean isExcelFile(String path)
    {
        String[] types = {"xls", "xlk", "xlsb", "xlsm", "xlsx", "xlr", "xltm", "xlw", "numbers", "ods", "ots"};
        return Utils.contains(types, path);
    }

    public static boolean isDocFile(String path)
    {
        String[] types = {"doc", "docm", "docx", "dot", "mcw", "rtf", "pages", "odt", "ott"};
        return Utils.contains(types, path);
    }

    public static boolean isPPTFile(String path)
    {
        String[] types = {"pptx", "keynote", "ppt", "pps", "pot", "odp", "otp"};
        return Utils.contains(types, path);
    }

    public static boolean isPDFFile(String path)
    {
        String[] types = {"pdf"};
        return Utils.contains(types, path);
    }

    public static boolean isTxtFile(String path)
    {
        String[] types = {"txt"};
        return Utils.contains(types, path);
    }

    public static boolean isApkFile(String path)
    {
        String[] types = {"apk"};
        return Utils.contains(types, path);
    }

    public static boolean isZipsFile(String path)
    {
        String[] types = {"cab", "7z", "alz", "arj", "bzip2", "bz2", "dmg", "gzip", "gz", "jar", "lz", "lzip", "lzma", "zip", "rar", "tar", "tgz"};
        return Utils.contains(types, path);
    }

    public static boolean isCertificatesFile(String path)
    {
        String[] types = {"cer", "der", "pfx", "p12", "arm", "pem"};
        return Utils.contains(types, path);
    }

}
