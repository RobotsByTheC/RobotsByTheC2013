package org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.connection;

import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.thread.PeriodicRunnable;
import java.io.*;



/**
 * A periodic thread that repeatedly reads from a connection
 * @author Mitchell
 *
 */
public class ConnectionMonitorThread implements PeriodicRunnable{
	private final ConnectionAdapter adapter;
	private final NetworkTableConnection connection;

	/**
	 * create a new monitor thread
	 * @param adapter
	 * @param connection
	 */
	public ConnectionMonitorThread(final ConnectionAdapter adapter, final NetworkTableConnection connection) {
		this.adapter = adapter;
		this.connection = connection;
	}

	public void run() throws InterruptedException {
		try{
			connection.read(adapter);
		} catch(BadMessageException e){
			adapter.badMessage(e);
		} catch(IOException e){
			adapter.ioException(e);
		}
	}
}