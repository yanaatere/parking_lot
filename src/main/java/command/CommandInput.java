package command;

import java.util.HashMap;
import java.util.Map;

public class CommandInput {
	private static volatile Map<String, Integer> commandsParameterMap = new HashMap<String, Integer>();
	
	static {
		commandsParameterMap.put(CommandList.CREATE_PARKING_LOT, 1);
		commandsParameterMap.put(CommandList.PARK, 2);
		commandsParameterMap.put(CommandList.LEAVE, 1);
		commandsParameterMap.put(CommandList.STATUS, 0);
		commandsParameterMap.put(CommandList.REG_NUMBER_FOR_CARS_WITH_COLOR, 1);
		commandsParameterMap.put(CommandList.SLOTS_NUMBER_FOR_CARS_WITH_COLOR, 1);
		commandsParameterMap.put(CommandList.SLOTS_NUMBER_FOR_REG_NUMBER, 1);
	}
	
	public static Map<String, Integer> getCommandsParameterMap()
	{
		return commandsParameterMap;
	}
	

	public static void addCommand(String command, int parameterCount)
	{
		commandsParameterMap.put(command, parameterCount);
	}
	
	

}
