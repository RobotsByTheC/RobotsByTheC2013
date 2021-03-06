package org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.client;

import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.thread.DefaultThreadManager;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.thread.NTThreadManager;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.stream.IOStreamFactory;
import java.util.HashMap;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.OutgoingEntryReceiver;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.NetworkTableNode;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.TransactionDirtier;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.WriteManager;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.type.NetworkTableEntryTypeManager;

/**
 * A client node in NetworkTables 2.0
 * 
 * @author Mitchell
 *
 */
public class NetworkTableClient extends NetworkTableNode{
	private final ClientConnectionAdapter adapter;
	private final WriteManager writeManager;

	/**
	 * Create a new NetworkTable Client
	 * @param streamFactory
	 * @param threadManager
	 * @param transactionPool
	 */
	public NetworkTableClient(final IOStreamFactory streamFactory, final NetworkTableEntryTypeManager typeManager, final NTThreadManager threadManager){
		ClientNetworkTableEntryStore entryStore;
		init(entryStore = new ClientNetworkTableEntryStore(this));
		adapter = new ClientConnectionAdapter(entryStore, threadManager, streamFactory, this, typeManager);
		writeManager = new WriteManager(adapter, threadManager, getEntryStore(), 1000);
		
		getEntryStore().setOutgoingReceiver(new TransactionDirtier(writeManager));
		getEntryStore().setIncomingReceiver(OutgoingEntryReceiver.NULL);
		writeManager.start();
	}
	/**
	 * Create a new NetworkTable Client
	 * @param streamFactory
	 */
	public NetworkTableClient(final IOStreamFactory streamFactory){
		this(streamFactory, new NetworkTableEntryTypeManager(), new DefaultThreadManager());
	}

	/**
	 * force the client to disconnect and reconnect to the server again. Will connect if the client is currently disconnected
	 */
	public void reconnect() {
		adapter.reconnect();
	}

	public void close() {
		adapter.close();
	}
	
	public void stop() {
		writeManager.stop();
		close();
	}

	public boolean isConnected() {
		return adapter.isConnected();
	}

	public boolean isServer() {
		return false;
	}

}