package il.cshaifasweng.OCSFMediatorExample.client.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SubscriptionChartModel {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty type;
    private final SimpleDoubleProperty hourlyPrice;
    private final SimpleIntegerProperty hoursInMonth;
    private final SimpleDoubleProperty total;

    public SubscriptionChartModel(int id, String type, double price, int hoursInMonth, Double total) {
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.hourlyPrice = new SimpleDoubleProperty(price);
        this.hoursInMonth = new SimpleIntegerProperty(hoursInMonth);
        this.total = new SimpleDoubleProperty(total);
    }

    public int getId() {
        return id.get();
    }


    public void setId(int id) {
        this.id.set(id);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public double getHourlyPrice() {
        return hourlyPrice.get();
    }

    public void setHourlyPrice(double hourlyPrice) {
        this.hourlyPrice.set(hourlyPrice);
    }

    public int getHoursInMonth() {
        return hoursInMonth.get();
    }

    public void setHoursInMonth(int hoursInMonth) {
        this.hoursInMonth.set(hoursInMonth);
    }

    public double getTotal() {
        return total.get();
    }

    public void setTotal(double total) {
        this.total.set(total);
    }
}
