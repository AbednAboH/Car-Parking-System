package il.cshaifasweng.ParkingLotEntities;

import il.cshaifasweng.LogInEntities.Employees.GlobalManager;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "parkinglots")
public class ParkingLot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="floor")
    private int floor;
    @Column(name="RowsInFloor")
    private int rowsInEachFloor;
    @Column(name="RowCapacity")
    private int rowCapacity;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "parkingLot" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<ParkingLotEmployee> employeeList;
    @OneToOne(fetch=FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="parkingLotManager_id")
    private ParkingLotManager manager;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "parkingLot",cascade =CascadeType.ALL,orphanRemoval = true)
    private List<ParkingSpot> spots=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="executiveManager_id")
    private static GlobalManager executiveManager=new GlobalManager("ElonMusk","CEO",1000000);
    @OneToOne(fetch=FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="parkingLotScheduler_id")
    private ParkingLotScheduler parkingLotScheduler;
    public ParkingLot(ParkingLot pl){
        this.id=pl.getId();
        this.rowCapacity=pl.getRowCapacity();
        this.floor=pl.getFloor();
        this.rowsInEachFloor=pl.getRowsInEachFloor();
        this.employeeList=pl.getEmployeeList();
        this.manager=pl.getManager();
        this.spots=pl.getSpots();
        parkingLotScheduler=new ParkingLotScheduler(this);

    }
    public ParkingLot(int floor, int rowsInEachFloor, int rowCapacity) {
        this.floor = floor;
        this.rowsInEachFloor = rowsInEachFloor;
        this.rowCapacity = rowCapacity;
        this.spots=new ArrayList<ParkingSpot>();
        this.employeeList=new ArrayList<ParkingLotEmployee>();
        this.initiateParkingSpots();
        // TODO: 1/3/2023 add initiation of specific classes

    }
    public ParkingLot(int floor, int rowsInEachFloor, int rowCapacity,ParkingLotManager manager) {
        this.floor = floor;
        this.rowsInEachFloor = rowsInEachFloor;
        this.rowCapacity = rowCapacity;
        this.spots=new ArrayList<ParkingSpot>();
        this.employeeList=new ArrayList<ParkingLotEmployee>();
        setManager(manager);
        manager.setParkingLot(this);
        this.initiateParkingSpots();
        this.parkingLotScheduler=new ParkingLotScheduler(this);
        // TODO: 1/3/2023 add initiation of specific classes

    }
    public ParkingLotScheduler getParkingLotScheduler() {
        return parkingLotScheduler;
    }

    public void initiateParkingSpots(){
        this.spots=new ArrayList<>();
        for(int flor=0;flor<this.floor;flor++){
            for(int row=0;row<this.rowsInEachFloor;row++){
                for(int depth=0;depth<rowCapacity;depth++){
                    this.spots.add(new ParkingSpot(row,flor,depth,this));
                }
            }
        }
    }
    public void reInitiateParkingSpots(){
            spots.forEach(spot -> {
                spot.setSaved(false);
                spot.setOccupied(false);
            });
        }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "id='" + id + '\'' +
                ", Floor=" + floor +
                ", Rows in floor=" + rowsInEachFloor +
                ", Row Depth="+rowCapacity+
                ",Manager id"+manager.getId()+'}';
    }



    public ParkingLot() {

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
    public int getRow(){return this.floor;}
    public int getCol(){return this.rowsInEachFloor;}
    public int getDepth(){return this.rowCapacity;}


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
    public void addEmployee(ParkingLotEmployee employee) {
        this.employeeList.add(employee);

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
    }

}







