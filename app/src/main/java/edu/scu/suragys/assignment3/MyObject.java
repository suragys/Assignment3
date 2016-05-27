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


    private static List<MyObject> objectList = new ArrayList<MyObject>();

    private int position;
    private String path;
    private int id;
    private String thumbNailPath;
    private String caption;

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

    public static List<MyObject> getObjectsList(){
        return objectList;
    }

    public static void addObjects(MyObject object){
        objectList.add(object);
    }


}
