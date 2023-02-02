package com.mypack.shell;



public class ExceCMD {
	
	

	public static void main(String... args)	{
		
		
		CommandExecuter commandExecuter=new CommandExecuter();
		try
		{
		
		commandExecuter.execute();
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}
		
	}
}
