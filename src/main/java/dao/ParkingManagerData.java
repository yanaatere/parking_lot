package dao;

import java.util.List;

import model.Vehicle;

public interface ParkingManagerData<T extends Vehicle> {
	
	public int parkCar(int level, T vehicle);

	public boolean leaveCar(int level, int slotNumber);

	public List<String> getStatus(int level);

	public List<String> getRegNumberForColor(int level, String color);

	public List<Integer> getSlotNumbersFromColor(int level, String color);

	public int getSlotNoFromRegistrationNo(int level, String registrationNo);

	public int getAvailableSlotsCount(int level);

	public void doCleanup();
}
