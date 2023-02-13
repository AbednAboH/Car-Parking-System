package il.cshaifasweng.ParkingLotEntities;

import il.cshaifasweng.LogInEntities.Employees.GlobalManager;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotManager;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "parkinglots")
@Getter
@Setter
public class ParkingLot extends ParkingLotScheduler implements Serializable{

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
    private  List<ParkingSpot> spots=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="executiveManager_id")
    private static GlobalManager executiveManager=new GlobalManager("ElonMusk","CEO",1000000);

    public ParkingLot(ParkingLot pl){

        this.id=pl.getId();
        this.rowCapacity=pl.getRowCapacity();
        this.floor=pl.getFloor();
        this.rowsInEachFloor=pl.getRowsInEachFloor();
        this.employeeList=pl.getEmployeeList();
        this.manager=pl.getManager();
        spots=pl.getSpots();
//        parkingLotScheduler=new ParkingLotScheduler(this);

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
        super();
        this.floor = floor;
        this.rowsInEachFloor = rowsInEachFloor;
        this.rowCapacity = rowCapacity;
        this.spots=new ArrayList<ParkingSpot>();
        this.employeeList=new ArrayList<ParkingLotEmployee>();
        setManager(manager);
        manager.setParkingLot(this);
        this.initiateParkingSpots();
        this.setMaxCapacity();

//        this.parkingLotScheduler=new ParkingLotScheduler(this);
        // TODO: 1/3/2023 add initiation of specific classes

    }
    public EntryAndExitLog entryToPLot(Transactions transaction,String licensePlate){
        EntryAndExitLog entryAndExitLog;
        restoreQueueFromList();
        setMaxCapacity();
        // TODO: 13/02/2023 do not allow the same car to enter twice
        if (this.getQueue().size()<this.getMaxCapacity()){
            try {
                entryAndExitLog=this.EnterAndLog(transaction, licensePlate) ;
                if (entryAndExitLog!=null){
                    reArrangeParkingLot();
                    sendNewPosistionsToRobot(true,licensePlate);
                }
                return entryAndExitLog;

            }
            catch (Exception e){
                System.out.println("ParkingLotIsFull");

            }



        }
        return null;

    }
    public EntryAndExitLog exitParkingLot(Transactions transaction,String licensePlate){
        EntryAndExitLog entryAndExitLog;
        restoreQueueFromList();
        setMaxCapacity();
        if (this.getQueue().size()!=0){
            try {
                entryAndExitLog=this.extractAndLog(transaction,licensePlate);
                if (entryAndExitLog!=null){
                    reArrangeParkingLot();
                    sendNewPosistionsToRobot(false,licensePlate);
                    System.out.println("\nnot null\n");
                }
                return entryAndExitLog;
            }
            catch (Exception e){
                System.out.println(e.getMessage());

            }


        }
        return null;
    }



    public  void sendNewPosistionsToRobot(boolean enterExit,String licensePlate){
        // TODO: 12/02/2023 to be implemented mostly just send the new positions to the robot
        // TODO : expected format : enter/exit,licensePlate, new positions ,can be inferred easily from the parking spots

         if(enterExit)
             System.out.println("To Robot: Enter->licensePlate=" + licensePlate + ",new positions:");
         else
             System.out.println("To Robot: Exit->licensePlate=" + licensePlate + ",new positions:");
         this.positionsToRobot();
    }
    public  void positionsToRobot(){
        for (ParkingSpot spot:spots)
            if (spot.getEntryAndExitLog()!=null)
                System.out.println("formatted as [liscensePLate][floor][row][depth]: ["+spot.getEntryAndExitLog().getActiveCar()+"],["+spot.getFloor()+"],["+spot.getRow()+"],["+spot.getDepth()+"]");
            else
                System.out.println("formatted as [liscensePLate][floor][row][depth]: [null],["+spot.getFloor()+"],["+spot.getRow()+"],["+spot.getDepth()+"]");
    }
    public void reArrangeParkingLot(){
        restoreQueueFromList();
        this.reInitiateParkingSpots();
        spots.sort( Comparator());
        List<ParkingSpot>spotsToAlter=spots.stream().filter(spot -> !spot.isSaved() && !spot.isFaulty()).toList();

        int deficet=spotsToAlter.size()-queue.size();
        for (ParkingSpot spot:spots){
            if (!spot.isSaved() && !spot.isFaulty()){
                if(queue.size()!=0&&deficet==0)
                    spot.setEntryAndExitLog(queue.poll());
                else{
                    deficet--;
                    spot.resetEntryAndExitLog();
                }
            }
        }
    }
    private Comparator<ParkingSpot> Comparator(){

        return  new Comparator<ParkingSpot>() {
            @Override
            public int compare(ParkingSpot o1, ParkingSpot o2) {
                if (o1.getFloor() == o2.getFloor()) {
                    if (o1.getRow() == o2.getRow())
                        return o1.getDepth() - o2.getDepth();
                    return o1.getRow() - o2.getRow();
                }
                return o1.getFloor() - o2.getFloor();
            }
        };
    }
    private void setMaxCapacity(){
        int maxCapacity=0;
        for (ParkingSpot spot:spots) {
            if(!spot.isSaved()&&!spot.isFaulty()){
                maxCapacity++;
            }
        }
        this.setMaxCapacity(maxCapacity);
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
                if (!spot.isSaved() && !spot.isFaulty()){
                     spot.resetEntryAndExitLog();}
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


    public void addEmployee(ParkingLotEmployee employee) {
        this.employeeList.add(employee);

    }












}







