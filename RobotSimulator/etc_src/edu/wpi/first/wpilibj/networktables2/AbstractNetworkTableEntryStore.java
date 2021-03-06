package org.usfirst.frc2084.robotsimulator.library.networktables2;

import org.usfirst.frc2084.robotsimulator.library.networktables2.type.*;
import org.usfirst.frc2084.robotsimulator.library.networktables2.util.*;
import org.usfirst.frc2084.robotsimulator.library.tables.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * An entry store that handles storing entries and applying transactions
 * 
 * @author mwills
 * @author Fredric
 * 
 */

public abstract class AbstractNetworkTableEntryStore implements IncomingEntryReceiver{
	protected final CharacterArrayMap idEntries = new CharacterArrayMap();
	protected final Hashtable namedEntries = new Hashtable();
	
	protected final TableListenerManager listenerManager;
	
	protected AbstractNetworkTableEntryStore(TableListenerManager listenerManager){
		this.listenerManager = listenerManager;
	}
	
	/**
	 * Get an entry based on it's id
	 * @param entryId the id f the entry to look for
	 * @return the entry or null if the entry does not exist
	 */
	public NetworkTableEntry getEntry(final char entryId){
		synchronized(this){
			return (NetworkTableEntry) idEntries.get(entryId);
		}
	}
	/**
	 * Get an entry based on it's name
	 * @param name the name of the entry to look for
	 * @return the entry or null if the entry does not exist
	 */
	public NetworkTableEntry getEntry(String name){
		synchronized(this){ 
			return (NetworkTableEntry) namedEntries.get(name);
		}
	}
	/**
	 * Get an entry based on it's name
	 * @param name the name of the entry to look for
	 * @return the entry or null if the entry does not exist
	 */
	public List keys(){
            synchronized(this){
                List entryKeys = new List();
                Enumeration e = namedEntries.keys();
                while(e.hasMoreElements())
                        entryKeys.add(e.nextElement());
                return entryKeys;
            }
	}
    
	/**
	 * Remove all entries
         * NOTE: This method should not be used with applications which cache entries which would lead to unknown results
         * This method is for use in testing only
	 */
        public void clearEntries() {
            synchronized (this) {
                idEntries.clear();
                namedEntries.clear();
            }
        }

	/**
	 * clear the id's of all entries
	 */
	public void clearIds() {
		synchronized(this){
			idEntries.clear();
			Enumeration e = namedEntries.elements();
			while(e.hasMoreElements())
				((NetworkTableEntry)e.nextElement()).clearId();
		}
	}


	protected OutgoingEntryReceiver outgoingReceiver;
	protected OutgoingEntryReceiver incomingReceiver;
	public void setOutgoingReceiver(final OutgoingEntryReceiver receiver){
		outgoingReceiver = receiver;
	}
	public void setIncomingReceiver(OutgoingEntryReceiver receiver){
		incomingReceiver = receiver;
	}
	
	protected abstract boolean addEntry(NetworkTableEntry entry);
	protected abstract boolean updateEntry(NetworkTableEntry entry, char sequenceNumber, Object value);
	
	/**
         * Stores the given value under the given name and queues it for 
         * transmission to the server.
         * 
         * @param name The name under which to store the given value.
         * @param type The type of the given value.
         * @param value The value to store.
         * @throws TableKeyExistsWithDifferentTypeException Thrown if an 
         *  entry already exists with the given name and is of a different type.
         */
	public void putOutgoing(String name, NetworkTableEntryType type, Object value) throws TableKeyExistsWithDifferentTypeException{
		synchronized(this){
			NetworkTableEntry tableEntry = (NetworkTableEntry)namedEntries.get(name);
			if(tableEntry==null){
				//TODO validate type
				tableEntry = new NetworkTableEntry(name, type, value);
				if(addEntry(tableEntry)){
                                    tableEntry.fireListener(listenerManager);
                                    outgoingReceiver.offerOutgoingAssignment(tableEntry);
				}
			}
			else{
				if(tableEntry.getType().id != type.id)
                                    throw new org.usfirst.frc2084.robotsimulator.library.networktables2.TableKeyExistsWithDifferentTypeException(name, tableEntry.getType());
                                
				if(updateEntry(tableEntry, (char)(tableEntry.getSequenceNumber()+1), value)){
                                    outgoingReceiver.offerOutgoingUpdate(tableEntry);
				}
                                tableEntry.fireListener(listenerManager);
			}
		}
	}

	public void putOutgoing(NetworkTableEntry tableEntry, Object value){
		synchronized(this){
			//TODO Validate type
			if(updateEntry(tableEntry, (char)(tableEntry.getSequenceNumber()+1), value)){
                            outgoingReceiver.offerOutgoingUpdate(tableEntry);
			}
                        tableEntry.fireListener(listenerManager);
		}
	}
	

	public void offerIncomingAssignment(NetworkTableEntry entry) {
            synchronized(this){
                NetworkTableEntry tableEntry = (NetworkTableEntry)namedEntries.get(entry.name);
                if(addEntry(entry)){
                    if(tableEntry==null)
                        tableEntry = entry;
                    tableEntry.fireListener(listenerManager);
                    incomingReceiver.offerOutgoingAssignment(tableEntry);
                }
            }
	}

 
	public void offerIncomingUpdate(NetworkTableEntry entry, char sequenceNumber, Object value) {
            synchronized(this){
                if(updateEntry(entry, sequenceNumber, value)){
                    entry.fireListener(listenerManager);
                    incomingReceiver.offerOutgoingUpdate(entry);
                }
            }
	}
	

	/**
	 * Called to say that a listener should notify the listener manager of all of the entries
	 * @param listener
	 * @param table 
	 */
	public void notifyEntries(final ITable table, final ITableListener listener) {
		synchronized(this){
			Enumeration entryIterator = namedEntries.elements();
			while(entryIterator.hasMoreElements()){
				NetworkTableEntry entry = (NetworkTableEntry) entryIterator.nextElement();
				listener.valueChanged(table, entry.name, entry.getValue(), true);
			}
		}
	}

	
	
	
	
	
	/**
	 * An object that handles firing Table Listeners
	 * @author Mitchell
	 *
	 */
	public interface TableListenerManager {
		/**
		 * Called when the object should fire it's listeners
		 * @param key
		 * @param value
		 * @param isNew
		 */
		void fireTableListeners(String key, Object value, boolean isNew);
	}
}
