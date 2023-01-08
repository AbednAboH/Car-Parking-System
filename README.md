# CPS system :
* This project is a Car Parking System that allows costumers to :
  * subscribe to a monthly subscription
  * order a spot in a specific parking lot before hand 
  * this project also manages how the cars are inserted/extracted to the parking lot 
  * supports multiple parking lots and multiple costumers 
  * allows useres that are not registered in the system to look at a cataloge that describes the parking lot fees and available parking lots in the whole country 
  * a robust and new way to manage parking lots with time based system 


* first steps were to create a data base :
   * used an azure server to implement a MySQL data base 
     * we used UML language to desighn the strucure of the project (skelaton) that described a 3 tier system :
      * Entity : holds all the data of an entity 
      * Control : holds all function that are communication based 
      * Boundary : A Gui for each user 
     
# progress up until  4.1.23:
* Parking lot data base is done with tests  
* Parking lot Employees data base is done with tests 
* Parking lot managers data bas is done with tests
* parking lot Global manager data base needs to be modified in some way .. don't know how but there might be a future change
## progress update  7.1.23
# Interface :
  * Created an interface that manipulats a Table entity , this was done using templates
  * this interface was based on the DAO design pattern .
  * this interface can accomodate any type of class and thus enables data manipulation dynamically 
  * each class/Table that we wish to update will be accessed and altered using DataBaseManipulation<T> class:
      
## progress update 8.1.23:
  * initial prototype works perfectly with connection to Data Base.
  * need to be altered such that it asks for a permition from Excutive manager for altering the table.cc 
 
# remaining data bases to be configured :
 
Date 8.1.2023
   * complaint
   * Kiosk
   * costumer service
   * Order
   * Report
   * costumer service employee
   * robot


  
