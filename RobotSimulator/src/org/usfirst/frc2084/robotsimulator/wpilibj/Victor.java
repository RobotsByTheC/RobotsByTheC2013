/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2084.robotsimulator.wpilibj;

import org.usfirst.frc2084.robotsimulator.wpilibj.communication.UsageReporting;
import org.usfirst.frc2084.robotsimulator.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc2084.robotsimulator.wpilibj.livewindow.LiveWindowSendable;
import org.usfirst.frc2084.robotsimulator.wpilibj.parsing.IDeviceController;
import org.usfirst.frc2084.robotsimulator.wpilibj.tables.ITable;

/**
 * VEX Robotics Victor Speed Controller
 */
public class Victor extends SafePWM implements SpeedController, IDeviceController {

    /**
     * Common initialization code called by all constructors.
     *
     * Note that the Victor uses the following bounds for PWM values.  These values were determined
     * empirically and optimized for the Victor 888. These values should work reasonably well for
     * Victor 884 controllers also but if users experience issues such as asymmetric behavior around
     * the deadband or inability to saturate the controller in either direction, calibration is recommended.
     * The calibration procedure can be found in the Victor 884 User Manual available from VEX Robotics:
     * http://content.vexrobotics.com/docs/ifi-v884-users-manual-9-25-06.pdf
     * 
     *   - 208 = full "forward"
     *   - 131 = the "high end" of the deadband range
     *   - 128 = center of the deadband range (off)
     *   - 125 = the "low end" of the deadband range
     *   - 54 = full "reverse"
     */
    private void initVictor() {
        setBounds(208, 131, 128, 125, 54);
        setPeriodMultiplier(PeriodMultiplier.k2X);
        setRaw(m_centerPwm);

        LiveWindow.addActuator("Victor", getModuleNumber(), getChannel(), (LiveWindowSendable)this);
        UsageReporting.report(UsageReporting.kResourceType_Victor, getChannel(), getModuleNumber()-1);
    }

    /**
     * Constructor that assumes the default digital module.
     *
     * @param channel The PWM channel on the digital module that the Victor is attached to.
     */
    public Victor(final int channel) {
        super(channel);
        initVictor();
    }

    /**
     * Constructor that specifies the digital module.
     *
     * @param slot The slot in the chassis that the digital module is plugged into.
     * @param channel The PWM channel on the digital module that the Victor is attached to.
     */
    public Victor(final int slot, final int channel) {
        super(slot, channel);
        initVictor();
    }

    /**
     * Set the PWM value.
     *
     * @deprecated For compatibility with CANJaguar
     *
     * The PWM value is set using a range of -1.0 to 1.0, appropriately
     * scaling the value for the FPGA.
     *
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     * @param syncGroup The update group to add this Set() to, pending UpdateSyncGroup().  If 0, update immediately.
     */
    public void set(double speed, byte syncGroup) {
        setSpeed(speed);
		Feed();
    }

    /**
     * Set the PWM value.
     *
     * The PWM value is set using a range of -1.0 to 1.0, appropriately
     * scaling the value for the FPGA.
     *
     * @param speed The speed value between -1.0 and 1.0 to set.
     */
    public void set(double speed) {
        setSpeed(speed);
		Feed();
    }

    /**
     * Get the recently set value of the PWM.
     *
     * @return The most recently set value for the PWM between -1.0 and 1.0.
     */
    public double get() {
        return getSpeed();
    }

    /**
     * Write out the PID value as seen in the PIDOutput base object.
     *
     * @param output Write out the PWM value as was found in the PIDController
     */
    public void pidWrite(double output) {
        set(output);
    }

    @Override
    public void initTable(ITable subtable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
