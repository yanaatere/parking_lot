package parkingLot;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import execption.ParkingException;
import model.Car;
import service.ParkingService;
import service.ParkingServiceImpl;
import execption.Error;


/**
 * Unit test for simple App.
 */
public class MainTest
{
	private int							parkingLevel;
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void init()
	{
		parkingLevel = 1;
		System.setOut(new PrintStream(outContent));
	}
	
	@After
	public void cleanUp()
	{
		System.setOut(null);
	}
	
	@Test
	public void createParkingLot() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		instance.createParkingLot(parkingLevel, 65);
		assertTrue("createdparkinglotwith65slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
	}
	
	@Test
	public void alreadyExistParkingLot() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		instance.createParkingLot(parkingLevel, 65);
		assertTrue("createdparkinglotwith65slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_ALREADY_EXIST.getMessage()));
		instance.createParkingLot(parkingLevel, 65);
		instance.doCleanUp();
	}
	
	@Test
	public void testParkingCapacity() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 11);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.parking(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertEquals(3, instance.getAvailableSlotsCount(parkingLevel));
		instance.doCleanUp();
	}
	
	@Test
	public void testEmptyParkingLot() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertTrue("Sorry,CarParkingDoesnotExist".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.createParkingLot(parkingLevel, 6);
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nSlotNo.\tRegistrationNo.\tColor\nSorry,parkinglotisempty."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
	}
	
	@Test
	public void testParkingLotIsFull() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 2);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.parking(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertTrue("createdparkinglotwith2slots\\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSorry,parkinglotisfull"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
	}
	
	@Test
	public void testNearestSlotAllotment() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 5);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-9999");
		assertTrue("createdparkinglotwith5slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
	}
	
	@Test
	public void leave() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.leaveParking(parkingLevel, 2);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 6);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.parking(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		instance.leaveParking(parkingLevel, 4);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nAllocatedslotnumber:3\nSlotnumber4isfree"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
	}
	
	@Test
	public void testWhenVehicleAlreadyPresent() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 3);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith3slots\nAllocatedslotnumber:1\nSorry,vehicleisalreadyparked."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
	}
	
	@Test
	public void testWhenVehicleAlreadyPicked() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 99);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.leaveParking(parkingLevel, 1);
		instance.leaveParking(parkingLevel, 1);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith99slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotnumberisEmptyAlready."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
	}
	
	@Test
	public void testStatus() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 8);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith8slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\tKA-01-HH-1234\tWhite\n2\tKA-01-HH-9999\tWhite"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanUp();
		
	}
	
	@Test
	public void testGetSlotsByRegNo() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 10);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith6slots\n" + "\n"
				+ "Allocatedslotnumber:1\n" + "\n" + "Allocatedslotnumber:2\n1",
				outContent.toString().trim().replace(" ", ""));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1235");
		assertEquals("Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith10slots\n" + "\n"
				+ "Allocatedslotnumber:1\n" + "\n" + "Allocatedslotnumber:2\n1\nNotFound",
				outContent.toString().trim().replace(" ", ""));
		instance.doCleanUp();
	}
	
	@Test
	public void testGetSlotsByColor() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(Error.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getRegistrationNumberForColor(parkingLevel, "white");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 7);
		instance.parking(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parking(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getStatus(parkingLevel);
		instance.getRegistrationNumberForColor(parkingLevel, "Cyan");
		assertEquals(
				"Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith7slots\n" + "\n" + "Allocatedslotnumber:1\n"
						+ "\n" + "Allocatedslotnumber:2\nKA-01-HH-1234,KA-01-HH-9999",
				outContent.toString().trim().replace(" ", ""));
		instance.getRegistrationNumberForColor(parkingLevel, "Red");
		assertEquals(
				"Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith6slots\n" + "\n" + "Allocatedslotnumber:1\n"
						+ "\n" + "Allocatedslotnumber:2\n" + "KA-01-HH-1234,KA-01-HH-9999,Notfound",
				outContent.toString().trim().replace(" ", ""));
		instance.doCleanUp();;
		
	}
}
