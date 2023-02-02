package com.mypack.shell;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommandExecuter {

	public void execute()  throws IOException, InterruptedException, ExecutionException

	{
		boolean isWindows = System.getProperty("os.name")
				  .toLowerCase().startsWith("windows");
		
		String homeDirectory = System.getProperty("user.home");
		Process process;
		if (isWindows) {
		    process = Runtime.getRuntime()
		      .exec(String.format("cmd.exe /c dir %s", homeDirectory));
		} else {
		    process = Runtime.getRuntime()
		      .exec(String.format("/bin/sh -c ls %s", homeDirectory));
		}
		StreamGobbler streamGobbler = 
		  new StreamGobbler(process.getInputStream(), System.out::println);
		Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);

		int exitCode = process.waitFor();
		assert exitCode == 0;

		future.get(); // waits for streamGobbler to finish
	}
}
