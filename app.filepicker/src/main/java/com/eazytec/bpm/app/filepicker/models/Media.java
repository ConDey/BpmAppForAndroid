package com.eazytec.bpm.app.filepicker.models;

/**
 * @author Administrator
 * @version Id: Media, v 0.1 2017/7/19 9:34 Administrator Exp $$
 */
public class Media extends BaseFile {

    public int mediaType;

    public Media(int id, String name, String path, int mediaType) {
        super(id, name, path);
        this.mediaType = mediaType;
    }

    public Media() {
        super(0,null,null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Media)) return false;

        Media media = (Media) o;

        return id == media.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

}

