package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import command.CommandList;
import dao.ParkingManagerData;
import dao.impl.MemoryParkingManager;
import execption.Error;
import execption.ParkingException;
import model.ParkingStrategy;
import model.ParkingStrategyImpl;
import model.Vehicle;

public class ParkingServiceImpl implements ParkingService {

	private ParkingManagerData<Vehicle> dataManager = null;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public void createParkingLot(int level, int capacity) throws ParkingException {
		if (dataManager != null) {
			throw new ParkingException(Error.PARKING_ALREADY_EXIST.getMessage());
		}
		List<Integer> parkingLevels = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		List<ParkingStrategy> parkingStrategies = new ArrayList<>();
		parkingLevels.add(level);
		capacityList.add(capacity);
		parkingStrategies.add(new ParkingStrategyImpl());
		this.dataManager = MemoryParkingManager.getInstance(parkingLevels, capacityList, parkingStrategies);
		System.out.println("Created parking lot with " + capacity + " slots");

	}

	@Override
	public Optional<Integer> parking(int level, Vehicle vehicle) throws ParkingException {
		Optional<Integer> value = Optional.empty();
		lock.writeLock().lock();
		validateParkingLot();
		try {
			value = Optional.of(dataManager.parkCar(level, vehicle));
			if (value.get() == CommandList.NOT_AVAILABLE) {
				System.out.println("Sorry, Parking lot is full");
			} else if (value.get() == CommandList.VEHICLE_ALREADY_EXIST) {
				System.out.println("Sorry, Vehicle is Already Parked");
			} else {
				System.out.println("Allocated slot number: " + value.get());
			}
		} catch (Exception e) {
			throw new ParkingException(Error.PROCESSING_ERROR.getMessage(), e);
		} finally {
			lock.writeLock().unlock();
		}
		return value;
	}

	@Override
	public void leaveParking(int level, int slotNumber) throws ParkingException {
		lock.writeLock().lock();
		validateParkingLot();
		try {
			if (dataManager.leaveCar(level, slotNumber)) {
				System.out.println("Slot number " + slotNumber);
			} else {
				System.out.println("Slot number is empty Already.");
			}
		} catch (Exception e) {
			throw new ParkingException(Error.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"), e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void getStatus(int level) throws ParkingException {
		lock.readLock().lock();
		validateParkingLot();
		try {
			System.out.println("Slot No.\tRegistration No.\tColor");
			List<String> statusList = dataManager.getStatus(level);
			if (statusList.size() == 0) {
				System.out.println("Sorry, parking lot is empty.");
			} else {
				for (String statusString : statusList) {
					System.out.println(statusString);
				}
			}

		} catch (Exception e) {
			throw new ParkingException(Error.PROCESSING_ERROR.getMessage(), e);
		} finally {
			lock.readLock().unlock();
		}

	}

	@Override
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingException {
		lock.readLock().lock();
		Optional<Integer> value = Optional.empty();
		validateParkingLot();
		try {
			value = Optional.of(dataManager.getAvailableSlotsCount(level));
		} catch (Exception e) {
			throw new ParkingException(Error.PROCESSING_ERROR.getMessage(), e);
		} finally {
			lock.readLock().unlock();
		}
		return value;
	}

	@Override
	public void getRegistrationNumberForColor(int level, String color) throws ParkingException {
		lock.readLock().lock();
		validateParkingLot();
		try {
			List<String> registrationList = dataManager.getRegNumberForColor(level, color);
			if (registrationList.size() == 0) {
				System.out.println("Not Found");
			} else {
				System.out.println(String.join(",", registrationList));
			}
		} catch (Exception e) {
			throw new ParkingException(Error.PROCESSING_ERROR.getMessage(), e);
		} finally {
			lock.readLock().unlock();
		}

	}

	@Override
	public void getSlotNumbersFromColor(int level, String color) throws ParkingException {
		lock.readLock().lock();
		validateParkingLot();
		try {
			List<Integer> slotList = dataManager.getSlotNumbersFromColor(level, color);
			if (slotList.size() == 0) {
				System.out.println("Not Found");
				StringJoiner joiner = new StringJoiner(",");
				for (Integer slot : slotList) {
					joiner.add(slot + "");
				}
				System.out.println(joiner.toString());
			}
		} catch (Exception e) {
			throw new ParkingException(Error.PROCESSING_ERROR.getMessage(), e);

		} finally {
			lock.readLock().unlock();
		}

	}

	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingException {
		int value = -1;
		lock.readLock().lock();
		validateParkingLot();
		try {
			value = dataManager.getSlotNoFromRegistrationNo(level, registrationNo);
			System.out.println(value != -1 ? value : "Not Found");
		} catch (Exception e) {
			throw new ParkingException(Error.PROCESSING_ERROR.getMessage(), e);
		} finally {
			lock.readLock().unlock();
		}
		return value;
	}

	@Override
	public void doCleanUp() {
		{
			if (dataManager != null)
				dataManager.doCleanup();
		}
	}

	private void validateParkingLot() throws ParkingException {
		if (dataManager == null) {
			throw new ParkingException(Error.PARKING_NOT_EXIST_ERROR.getMessage());
		}
	}

}
