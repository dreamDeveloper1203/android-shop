package store.technologycenter.android.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Area extends RealmObject {

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_FEE = "fee";
    public static final String COL_VIEW_FEE = "viewFee";
    public static final String COL_TIME = "time";
    public static final String COL_VIEW_TIME = "viewTime";

    private @PrimaryKey String id;
    private String name;
    private double fee;
    private String viewFee;
    private int time;
    private String viewTime;

    public Area(){}

    public Area(String id, String name, double fee, String viewFee, int time, String viewTime) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.viewFee = viewFee;
        this.time = time;
        this.viewTime = viewTime;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getViewFee() {
        return viewFee;
    }

    public void setViewFee(String view_fee) {
        this.viewFee = view_fee;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }
}
