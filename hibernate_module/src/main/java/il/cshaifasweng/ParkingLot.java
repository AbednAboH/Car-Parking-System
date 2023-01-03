package il.cshaifasweng;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "parkinglots")
public class ParkingLot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="floor")
    private int floor;
    @Column(name="RowsInFloor")
    private int rowsInEachFloor;
    @Column(name="RowCapacity")
    private int rowCapacity;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "parkingLot")
    private List<ParkingLotEmployee> employeeList;
    @OneToOne(fetch=FetchType.LAZY,mappedBy = "parkingLot")
    private ParkingLotManager manager;
    @ManyToOne(fetch = FetchType.LAZY)
    private GlobalManager executiveManager;
    @OneToMany(fetch=FetchType.LAZY,mappedBy = "parkingLot")
    private List<ParkingSpot> spots=new ArrayList<>();


    public ParkingLot(int floor, int rowsInEachFloor, int rowCapacity) {
        this.floor = floor;
        this.rowsInEachFloor = rowsInEachFloor;
        this.rowCapacity = rowCapacity;
        this.spots=new ArrayList<ParkingSpot>();
        this.employeeList=new ArrayList<ParkingLotEmployee>();
        // TODO: 1/3/2023 add initiation of specific classes

    }

    public void initiateParkingSpots(){
        for(int flor=0;flor<this.floor;flor++){
            for(int row=0;row<this.rowsInEachFloor;row++){
                for(int depth=0;depth<rowCapacity;depth++){
                    this.spots.add(new ParkingSpot(row,flor,depth,this));
                }
            }
        }
    }



    public ParkingLot() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setRowsInEachFloor(int rowsInEachFloor) {
        this.rowsInEachFloor = rowsInEachFloor;
    }

    public void setRowCapacity(int rowCapacity) {
        this.rowCapacity = rowCapacity;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public List<ParkingLotEmployee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<ParkingLotEmployee> employeeList) {
        this.employeeList = employeeList;
    }

    public ParkingLotManager getManager() {
        return manager;
    }

    public void setManager(ParkingLotManager manager) {
        this.manager = manager;
    }

    public GlobalManager getExecutiveManager() {
        return executiveManager;
    }

    public void setExecutiveManager(GlobalManager executiveManager) {
        this.executiveManager = executiveManager;
    }

    public void setSpots(List<ParkingSpot> spots) {
        this.spots = spots;
    }






    public int getFloor() {
        return floor;
    }

    public int getRowsInEachFloor() {
        return rowsInEachFloor;
    }

    public int getRowCapacity() {
        return rowCapacity;
    }}







