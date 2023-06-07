# CPS system :

# UPDATE these notes for the future ! this is not the final version , the project should be explained in more detail 

* This project is a Car Parking System that allows costumers to :
  * subscribe to a monthly subscription
  * onlineOrder a spot in a specific parking lot before hand 
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

## Date 11.1.23
# remaining entities:
 *Kiosk
 *onlineOrder
 *robot
# authintication protocols are completed , we use a query class for authintication that resides in EntitiesLogIn
 2 options :
  * get a number between 1 to 7 if an entity exists.
  * get an entity !
## Date 12.1.23
 * authintication and registration (sign in and sign up)between client and server established and the returened object is correct
 * when each client signs in , its id is saved in the server ,so when we switch between scenes we always know which type of client is connected to the server ! 
## Date 13.1.23
# Login
 * login GUI works 
 * Regestration also works , might need to change inputs no more no less 
 * used threads to ensure that the GUI doesn't freeze using Task Class and a String authentication object to determine the state of the request
 *authentication is null when the request to get the account is still waiting on the server and when its updated we get out of the loop , as the server would surely return an answer . 
 
 ![image](https://user-images.githubusercontent.com/92520508/212211370-d96bbfeb-e9d8-45df-a5dc-40d8381f4e49.png)

  the final version would be updated for more fields as this is a proof of concept page !
 
 
 
