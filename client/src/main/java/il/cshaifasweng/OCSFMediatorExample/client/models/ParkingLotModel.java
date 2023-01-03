package il.cshaifasweng.OCSFMediatorExample.client.models;

import javafx.beans.property.SimpleIntegerProperty;

public class ParkingLotModel {
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty floor;
    private final SimpleIntegerProperty rowsInEachFloor;
    private final SimpleIntegerProperty rowCapacity;

    public ParkingLotModel(Integer id, Integer rowCapacity) {
        this.id = new SimpleIntegerProperty(id);
        this.floor = new SimpleIntegerProperty(3);
        this.rowsInEachFloor = new SimpleIntegerProperty(3);
        this.rowCapacity = new SimpleIntegerProperty(rowCapacity);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getFloor() {
        return floor.get();
    }

    public SimpleIntegerProperty floorProperty() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor.set(floor);
    }

    public int getRowsInEachFloor() {
        return rowsInEachFloor.get();
    }

    public SimpleIntegerProperty rowsInEachFloorProperty() {
        return rowsInEachFloor;
    }

    public void setRowsInEachFloor(int rowsInEachFloor) {
        this.rowsInEachFloor.set(rowsInEachFloor);
    }

    public int getRowCapacity() {
        return rowCapacity.get();
    }

    public SimpleIntegerProperty rowCapacityProperty() {
        return rowCapacity;
    }

    public void setRowCapacity(int rowCapacity) {
        this.rowCapacity.set(rowCapacity);
    }
}
