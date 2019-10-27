package process;

import command.CommandList;
import execption.Error;
import execption.ParkingException;
import model.Car;
import service.ParkingService;

public class RequestProcessor implements AbstractProcessor {

	private ParkingService parkingService;

	public void setParkingService(ParkingService parkingService) throws ParkingException {
		this.parkingService = parkingService;
	}

	@Override
	public void setService(ParkingService service) {
		this.parkingService = (ParkingService) service;

	}

	@Override
	public void execute(String action) throws ParkingException {
		int level = 1;
		String[] inputs = action.split(" ");
		String key = inputs[0];
		switch (key) {
		case CommandList.CREATE_PARKING_LOT:
			try {
				int capacity = Integer.parseInt(inputs[1]);
				parkingService.createParkingLot(level, capacity);
			} catch (NumberFormatException e) {
				throw new ParkingException(Error.INVALID_VALUE.getMessage().replace("{variable}", "capacity"));
			}
			break;
		case CommandList.PARK:
			parkingService.parking(level, new Car(inputs[1], inputs[2]));
			break;
		case CommandList.LEAVE:
			try {
				int slotNumber = Integer.parseInt(inputs[1]);
				parkingService.leaveParking(level, slotNumber);
			} catch (NumberFormatException e) {
				throw new ParkingException(Error.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"));
			}
			break;
		case CommandList.STATUS:
			parkingService.getStatus(level);
			break;
		case CommandList.REG_NUMBER_FOR_CARS_WITH_COLOR:
			parkingService.getRegistrationNumberForColor(level, inputs[1]);
			break;
		case CommandList.SLOTS_NUMBER_FOR_CARS_WITH_COLOR:
			parkingService.getSlotNumbersFromColor(level, inputs[1]);
			break;
		case CommandList.SLOTS_NUMBER_FOR_REG_NUMBER:
			parkingService.getSlotNoFromRegistrationNo(level, inputs[1]);
			break;
		default:
			break;
		}
	}

	public ParkingService getParkingService() {
		return parkingService;
	}

}
