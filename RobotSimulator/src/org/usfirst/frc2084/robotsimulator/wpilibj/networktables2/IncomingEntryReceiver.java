package org.usfirst.frc2084.robotsimulator.wpilibj.networktables2;



public interface IncomingEntryReceiver {
	IncomingEntryReceiver NULL = new IncomingEntryReceiver() {
		public void offerIncomingUpdate(NetworkTableEntry entry, char entrySequenceNumber, Object value) {
		}
		public void offerIncomingAssignment(NetworkTableEntry entry) {
		}
	};
	
	
	public void offerIncomingAssignment(NetworkTableEntry entry);
	public void offerIncomingUpdate(NetworkTableEntry entry, char entrySequenceNumber, Object value);
}
