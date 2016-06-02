package edu.scu.suragys.assignment3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suragys on 5/20/16.
 */
public class MyObject implements Serializable {

    public static final String TABLE_NAME = "PHOTO_INFO" ;
    public static final String ID = "_id" ;
    public static final String COLUMN_NAME_PATH = "PATH";
    public static final String COLUMN_NAME_CAPTION = "CAPTION";
    public static final String COLUMN_NAME_THUMBNAILPATH = "THUMBNAILPATH" ;
    public static final String COLUMN_NAME_POSITION = "POSITION";
    public static final String COLUMN_NAME_LATTITUDE = "LATTITUDE";
    public static final String COLUMN_NAME_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_NAME_AUDIOPATH = "AUDIOPATH";




    private static List<MyObject> objectList = new ArrayList<MyObject>();

    private int position;
    private String path;
    private int id;
    private String thumbNailPath;
    private String caption;
    private String lattitude;
    private String audioPath;
    private String longitude;



    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(int id){ this.id = id;}

    public int getId() {
        return id;
    }

    public String getThumbNailPath() {
        return thumbNailPath;
    }

    public void setThumbNailPath(String thumbNailPath) {
        this.thumbNailPath = thumbNailPath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public static List<MyObject> getObjectsList(){
        return objectList;
    }

    public static void addObjects(MyObject object){
        objectList.add(object);
    }



}
