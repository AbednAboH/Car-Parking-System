package il.cshaifasweng.converters;

import il.cshaifasweng.ParkingLotEntities.Vehicle;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Converter
public class PriorityQueueConverter implements AttributeConverter<PriorityQueue<Vehicle>, List<Vehicle>> {

    @Override
    public List<Vehicle> convertToDatabaseColumn(PriorityQueue<Vehicle> queue) {
        return new ArrayList<>(queue);
    }

    @Override
    public PriorityQueue<Vehicle> convertToEntityAttribute(List<Vehicle> vehicles) {
        return new PriorityQueue<>(vehicles);
    }
}