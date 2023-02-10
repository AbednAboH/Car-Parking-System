package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.MoneyRelatedServices.Transactions;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class ParkingLotScheduler {
    private Queue<Vehicle> queue = new PriorityQueue<>(new VehicleComparator());

    public void addVehicle(Vehicle vehicle) {
        queue.offer(vehicle);
    }

    public Vehicle getNextVehicle() {
        return queue.poll();
    }


    class VehicleComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            int result = Integer.compare(v1.getPriority(), v2.getPriority());
            if (result == 0) {
                result = v1.getEstimatedExitTime().compareTo(v2.getEstimatedExitTime());
            }
            return result;
        }
    }

    class Vehicle {
        private static final int PRIORITY_SUBSCRIPTION_DEFAULT = 1;
        private static final int PRIORITY_SUBSCRIPTION_UNLIMITED = 2;
        private static final int PRIORITY_NO_SUBSCRIPTION = 3;


        private int priority;
        private LocalDateTime estimatedExitTime;

        private Transactions orderSubKioskEntity;
        public Vehicle(String subscriptionType, LocalDateTime estimatedExitTime) {
            switch (subscriptionType) {
                case SubscriptionType.DEFAULT:
                    this.priority = PRIORITY_SUBSCRIPTION_DEFAULT;
                    break;
                case SubscriptionType.UNLIMITED:
                    this.priority = PRIORITY_SUBSCRIPTION_UNLIMITED;
                    break;
                default:
                    this.priority = PRIORITY_NO_SUBSCRIPTION;
                    break;
            }
            this.estimatedExitTime = estimatedExitTime;
        }

        public int getPriority() {
            return priority;
        }

        public LocalDateTime getEstimatedExitTime() {
            return estimatedExitTime;
        }
    }



}

