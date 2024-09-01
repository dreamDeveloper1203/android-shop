package store.technologycenter.android.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PaymentMethod extends RealmObject {

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";

    private @PrimaryKey String id;
    private String name;

    public PaymentMethod() {}

    public PaymentMethod(String id, String name) {
        this.id = id;
        this.name = name;
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

}
