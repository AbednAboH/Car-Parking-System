# CPS system :

All the employees and customers that are set as default are already in the Azure DB , customers can register ,employees are managed only by the staff and not by any user even the general manager of the full project 

Before you try to enter any screen in the project I advise using the first employee with the first parking lot as i will not put all the information in here because there are many things to show , use the first employee , the first global manager for all testing with the first parking lot , you can use the customers that are already in the DB but you can also register and create a new one.


* This project is a Car Parking System that allows costumers to :
* Client's Side
   * subscribe to a monthly subscription
   * onlineOrder a spot in a specific parking lot before hand 
   * this project also manages how the cars are inserted/extracted to the parking lot 
   * supports multiple parking lots and multiple costumers 
   * allows useres that are not registered in the system to look at a cataloge that describes the parking lot fees and available parking lots in the whole country 
   * a robust and new way to manage parking lots with time based system 
 * Employees Side :
  * each employee has his own personal page ,employees are in ranks :
   1. regular employee : manages parking lot spots and marks them as faulty ,saved if needed 
   2. parking lot manager  : Can see all orders , the ones that are active and the ones that aren't and can request a price change from the global manager of all the parking lots this change is done on all parking lots on all subscriptions/orders/Kiosk Entrances 
   3. Customer service employee : can view all complaints and give refunds on those complaints , and can request a price change for all parking lots on all subscriptions/orders/Kiosk Entrances 
  4. Global manager : Can view all parking lots and the current status of each one plus the ability to approve Pricing chart changes and Can request a report on each parking lot from the parking lot managers .

* first steps were to create a data base :
   * used an azure server to implement a MySQL data base 
     * we used UML language to desighn the strucure of the project (skelaton) that described a 3 tier system :
      * Entity : holds all the data of an entity 
      * Control : holds all function that are communication based 
      * Boundary : A Gui for each user 
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/a3b8e905-a4df-4e91-80ac-b1d67fd35b42)

   * there were some changes done on the UML structure above , such as Order and Subscription both now have a commmon base class called transaction , and both now have an element called entryAndExit log That logs the entrance and exit from the parking lot so that the customer and the employees can see in real time when the vehicle entered and exited the parking lot and so that the payment would be done according to the Entry and Exit log times so that each customer can know why he was charged this much and so that the customer service employee can know what actually happened if she/he gets a complaint on a specific order or subscribtion even a kiosk order is registered with this log .
   * this approch also helps with the reports that should be created to the global manager because they contain all the information needed , thus also allowing us to dump the calculations and filtering that should be done to the Data base instead of throwing this burden on the server .
   
* This implemintation needs an internet connection , as it uses a live Azure server      
* server : 
   * the server does all regularitory actions such as :
     * reminding the subscribers that there subscribtion is about to expire using email services.
     * sends emails when an order is placed ,or when a complaint is answered , recipets and much more email services   
  ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/1dc2c4f8-7d24-4420-b561-4938dc4de730)

* Client:

  * there are 3 types of clients :
    1. A personal account that can be accessed for eaither the client or an employee ,or a general page that allows ordering a parking spot without the need for an account 
    2. Kiosk screen is used in each parking lot for general access to the parking lot for customers 
    3. Entrance and exit screen: when a customer wants to take his car out of the parking lot or into the parking lot he enters his specific credintials .
 
![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/878217ca-c5b3-42de-bdda-50806d88cb98)

Customers For testing:

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/341449dd-5983-4f80-ad39-69f3342236da)

Employees for Testing:

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/383a2c97-2260-48a8-9372-495e4099dbfa)
![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/2b013350-8036-4c69-9afc-72f33d9b4b62)


Personal account:

1.
 ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/2bbc4fca-48c6-4231-91b3-2bfc1508f77f)


 1.1(personal account):
    ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/b70774e0-a3e9-4920-8649-91a8105db026)
     
   1.1.1( failed authintication):
   
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/f956890f-98c8-4f88-a552-95df488debed)
   
   1.1.2 (successful):
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/290e7113-463e-4aef-8291-bcfbc744ebbc)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/7eb44c61-5d25-471c-847d-22f461325520)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/cdf4ac82-aece-484d-8617-7977e8d83ee7)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/f330df9c-68e7-4c6b-bf8e-7a90a691b275)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/3583b054-7789-4322-8217-7f9ec5e64707)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/b71a2577-70c1-4a0d-aa90-5181b5a28a47)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/3a8a1f3e-796c-41f5-a33f-c91ccf9f7822)
   
   Subscribtions(Same with orders):
   
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/de097786-6fab-4c6f-8f22-a74cd6460521)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/d4431616-30c3-4f02-8013-04b93335b7e7)
   ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/b14ba568-5295-480a-a0c2-b69e7b5de8ab) 

 1.2 (Visitor): 
    ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/e6a2181d-dbdb-431d-aac8-a7514612ee2a)
    
 1.3 Parking Lot manager : 

  ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/8c4c4ed7-dda5-4a9f-8f50-e7addab056c5)
  
  Request to change price :
  
  ![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/59958b30-e085-48d2-85b2-47d596aae676)

1.4 Parking Lot Regular Employee :

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/49adc874-fd92-4099-b668-20e9c2c3253d)

1.5 Customer Service Employee:

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/b5a86a0b-fb62-4110-ac87-770090f543c7)

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/c86b6257-3c37-4481-b26c-81741a613057)


1.6 Global Manager:

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/e92bb565-7727-49f5-8dcd-361078a8a16c)

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/aedd22b0-9116-4b11-9e85-e52216e054ba)

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/a7abf609-0ef8-44cb-800f-9f68e8a54540)


2. Kiosk Setup and Screen:

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/b86bc1c6-d7fd-492c-a3bb-144070b86ccd)

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/ae275be2-8f5e-46d7-8fbf-856130b5ce91)

3. Entrance and exit screen:

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/f6fc3c98-09f9-4db3-8641-51bd5c953b9b)

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/d1c8a8de-f15c-47d3-a4e5-40ecf6ecd429)

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/93b09f23-2f6b-406c-bbbb-0b57de928239)

![image](https://github.com/AbednAboH/Car-Parking-System/assets/92520508/2024078e-31cd-4263-b6da-bfb650333bb5)


