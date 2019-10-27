package model;

import java.util.TreeSet;

public class ParkingStrategyImpl implements ParkingStrategy {

private TreeSet<Integer> freeSlots;
	
	public ParkingStrategyImpl()
	{
		freeSlots = new TreeSet<Integer>();
	}
	
	@Override
	public void add(int i)
	{
		freeSlots.add(i);
	}
	
	@Override
	public int getSlot()
	{
		return freeSlots.first();
	}
	
	@Override
	public void removeSlot(int availableSlot)
	{
		freeSlots.remove(availableSlot);
	}
}
