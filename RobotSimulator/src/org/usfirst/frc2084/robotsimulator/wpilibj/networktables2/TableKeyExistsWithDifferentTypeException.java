package org.usfirst.frc2084.robotsimulator.wpilibj.networktables2;

import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.type.NetworkTableEntryType;

/**
 * Throw to indicate that an attempt to put data to a table is illegal because
 * the specified key exists with a different data type than the put data type.
 * 
 * @author Paul Malmsten <pmalmsten@gmail.com>
 */
public class TableKeyExistsWithDifferentTypeException extends RuntimeException {

    /**
     * Creates a new TableKeyExistsWithDifferentTypeException
     * 
     * @param existingKey The name of the key which exists.
     * @param existingType The type of the key which exists.
     */
    public TableKeyExistsWithDifferentTypeException(String existingKey, NetworkTableEntryType existingType) {
        this(existingKey, existingType, "");
    }
    
    public TableKeyExistsWithDifferentTypeException(String existingKey, NetworkTableEntryType existingType, String message) {
        super("Illegal put - key '" + existingKey + "' exists with type '" + existingType + "'. "+message);
    }
}
