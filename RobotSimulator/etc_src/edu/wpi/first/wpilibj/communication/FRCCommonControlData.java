/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.usfirst.frc2084.robotsimulator.library.communication;

import org.usfirst.frc2084.robotsimulator.hardware.FRCCommonControlDataConnector;

/**
 * Structure for data exchanged between the robot and the driver station.
 */
public final class FRCCommonControlData {

    public static final short RESET_BIT = 0x80;
    public static final short ESTOP_BIT = 0x40;
    public static final short ENABLED_BIT = 0x20;
    public static final short AUTONOMOUS_BIT = 0x10;
    public static final short FMS_ATTATCHED = 0x08;
    public static final short RESYNCH = 0x04;
    public static final short TEST_MODE_BIT = 0x02;
    public static final short CHECK_VERSIONS_BIT = 0x01;
    /**
     * The index of the packet
     */
    public /*UINT16*/ int packetIndex;
    /**
     * The control mode e.g. Autonomous, E-stop, enabled ...
     */
    public /*UINT8*/ short control;
    // { reset, notEStop, enabled, autonomous, fmsAttached, resync, cRIOChkSum, fpgaChkSum }

    /**
     * Determine if the robot should be enabled
     *
     * @return true if the robot is enabled
     */
    public boolean enabled() {
        return (control & ENABLED_BIT) == ENABLED_BIT;
    }

    /**
     * Determine if the robot is in test mode
     *
     * @return true if the robot is in test mode
     */
    public boolean testMode() {
        return (control & TEST_MODE_BIT) == TEST_MODE_BIT;
    }

    /**
     * Determine if the robot should be in autonomous
     *
     * @return true if the robot is in autonomous
     */
    public boolean autonomous() {
        return (control & AUTONOMOUS_BIT) == AUTONOMOUS_BIT;
    }
    /**
     * The state of the digital inputs on the ds
     */
    public /*UINT8*/ short dsDigitalIn;
    /**
     * The team number from the ds
     */
    public /*UINT16*/ int teamID;
    /**
     * Which alliance the robot is on
     */
    public char dsID_Alliance;
    /**
     * The position of the controls on the alliance station wall.
     */
    public char dsID_Position;
    /**
     * Position of the axes of the first js
     */
    public byte[] stick0Axes = new byte[6];
    /**
     * Button state of the first js
     */
    public short stick0Buttons;		// Left-most 4 bits are unused
    /**
     * Position of the axes of the second js
     */
    public byte[] stick1Axes = new byte[6];
    /**
     * Button state of the second js
     */
    public short stick1Buttons;		// Left-most 4 bits are unused
    /**
     * Position of the axes of the third js
     */
    public byte[] stick2Axes = new byte[6];
    /**
     * Button state of the third js
     */
    public short stick2Buttons;		// Left-most 4 bits are unused
    /**
     * Position of the axes of the fourth js
     */
    public byte[] stick3Axes = new byte[6];
    /**
     * Button state of the fourth js
     */
    public short stick3Buttons;		// Left-most 4 bits are unused
    //Analog inputs are 10 bit right-justified
    /**
     * Driver Station analog input
     */
    public short analog1;
    /**
     * Driver Station analog input
     */
    public short analog2;
    /**
     * Driver Station analog input
     */
    public short analog3;
    /**
     * Driver Station analog input
     */
    public short analog4;

    // Other fields are used by the lower-level comm system and not needed by robot code:
    /**
     * Create a new FRCControlData structure
     */
    public FRCCommonControlData() {
    }

    /**
     * Method to free the memory used by this structure
     */
    protected void free() {
    }

    /**
     * Read new data in the structure
     */
    public void read() {
        packetIndex = FRCCommonControlDataConnector.packetIndex;
        control = FRCCommonControlDataConnector.control;

        dsDigitalIn = FRCCommonControlDataConnector.dsDigitalIn;
        teamID = FRCCommonControlDataConnector.teamID;

        dsID_Alliance = FRCCommonControlDataConnector.dsID_Alliance;
        dsID_Position = FRCCommonControlDataConnector.dsID_Position;

        stick0Axes = FRCCommonControlDataConnector.stick0Axes;
        stick0Buttons = FRCCommonControlDataConnector.stick0Buttons;

        stick1Axes = FRCCommonControlDataConnector.stick1Axes;
        stick1Buttons = FRCCommonControlDataConnector.stick1Buttons;

        stick2Axes = FRCCommonControlDataConnector.stick2Axes;
        stick2Buttons = FRCCommonControlDataConnector.stick2Buttons;

        stick3Axes = FRCCommonControlDataConnector.stick3Axes;
        stick3Buttons = FRCCommonControlDataConnector.stick3Buttons;

        analog1 = FRCCommonControlDataConnector.analog1;
        analog2 = FRCCommonControlDataConnector.analog2;
        analog3 = FRCCommonControlDataConnector.analog3;
        analog4 = FRCCommonControlDataConnector.analog4;

        // Other fields are used by the lower-level comm system and not needed by robot code
    }

    /**
     * Write new data in the structure
     */
    public void write() {
        throw new IllegalStateException("FRCCommonControlData is not writable");
    }

    /**
     * Get the size of the structure
     *
     * @return size of the structure
     */
    public int size() {
        return 80;
    }
}
