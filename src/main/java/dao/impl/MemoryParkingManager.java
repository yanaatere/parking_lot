package dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dao.ParkingLevelDataManager;
import dao.ParkingManagerData;
import model.ParkingStrategy;
import model.ParkingStrategyImpl;
import model.Vehicle;

public class MemoryParkingManager<T extends Vehicle> implements ParkingManagerData<T> {

	private Map<Integer, ParkingLevelDataManager<T>> levelParkingMap;

	@SuppressWarnings("rawtypes")
	private static MemoryParkingManager instance = null;

	@SuppressWarnings("unchecked")
	public static <T extends Vehicle> MemoryParkingManager<T> getInstance(List<Integer> parkingLevels,
			List<Integer> capacityList, List<ParkingStrategy> parkingStrategies) {
		// Make sure the each of the lists are of equal size
		if (instance == null) {
			synchronized (MemoryParkingManager.class) {
				if (instance == null) {
					instance = new MemoryParkingManager<T>(parkingLevels, capacityList, parkingStrategies);
				}
			}
		}
		return instance;
	}

	private MemoryParkingManager(List<Integer> parkingLevels, List<Integer> capacityList,
			List<ParkingStrategy> parkingStrategies) {
		if (levelParkingMap == null)
			levelParkingMap = new HashMap<>();
		for (int i = 0; i < parkingLevels.size(); i++) {
			levelParkingMap.put(parkingLevels.get(i), MemoryParkingLevelManager.getInstance(parkingLevels.get(i),
					capacityList.get(i), new ParkingStrategyImpl()));

		}
	}

	@Override
	public int parkCar(int level, T vehicle) {
		// TODO Auto-generated method stub
		return levelParkingMap.get(level).parkCar(vehicle);
	}

	@Override
	public boolean leaveCar(int level, int slotNumber) {
		// TODO Auto-generated method stub
		return levelParkingMap.get(level).leaveCar(slotNumber);
	}

	@Override
	public List<String> getStatus(int level) {

		return levelParkingMap.get(level).getStatus();
	}

	@Override
	public List<String> getRegNumberForColor(int level, String color) {
		// TODO Auto-generated method stub
		return levelParkingMap.get(level).getRegNumberForColor(color);
	}

	

	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) {
		return levelParkingMap.get(level).getSlotNoFromRegistrationNo(registrationNo);
	}

	@Override
	public int getAvailableSlotsCount(int level) {
		return levelParkingMap.get(level).getAvailableSlotsCount();
	}

	@Override
	public void doCleanup() {
		for (ParkingLevelDataManager<T> levelDataManager : levelParkingMap.values()) {
			levelDataManager.doCleanUp();
		}
		levelParkingMap = null;
		instance = null;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	@Override
	public List<Integer> getSlotNumbersFromColor(int level, String color) {
		// TODO Auto-generated method stub
		return levelParkingMap.get(level).getSlotNumbersFromColor(color);
	}

}
