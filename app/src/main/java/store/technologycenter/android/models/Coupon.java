package store.technologycenter.android.models;

public class Coupon {

    String code;
    double percentage;
    String description;

    public Coupon(String code, double percentage, String description) {
        this.code = code;
        this.percentage = percentage;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
