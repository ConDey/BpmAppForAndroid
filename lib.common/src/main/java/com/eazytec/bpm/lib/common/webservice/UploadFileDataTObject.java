package com.eazytec.bpm.lib.common.webservice;

/**
 * @author ConDey
 * @version Id: UploadFileDataTObject, v 0.1 2017/12/1 下午4:04 ConDey Exp $$
 */
public class UploadFileDataTObject extends WebDataTObject {


    private String path;

    private String rename;

    private String name;

    private String id;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRename() {
        return rename;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
