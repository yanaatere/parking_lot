package service;

import java.util.Optional;

import execption.ParkingException;
import model.Vehicle;

public interface ParkingService {
	public void createParkingLot(int level, int capacity) throws ParkingException;

	public Optional<Integer> parking(int level, Vehicle vehicle) throws ParkingException;

	public void leaveParking(int level, int slotNumber) throws ParkingException;

	public void getStatus(int level) throws ParkingException;

	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingException;
	
	public void getRegistrationNumberForColor(int level, String color) throws ParkingException;
	
	public void getSlotNumbersFromColor(int level, String color) throws ParkingException;
	
	public int getSlotNoFromRegistrationNo(int level,String registrationNo) throws ParkingException;
	
	public void doCleanUp();
}
