package org.usfirst.frc2084.robotsimulator.wpilibj.networktables;

import org.usfirst.frc2084.robotsimulator.wpilibj.tables.ITableListener;
import org.usfirst.frc2084.robotsimulator.wpilibj.tables.ITable;
import java.util.ArrayList;
import org.usfirst.frc2084.robotsimulator.wpilibj.networktables2.util.Set;

/**
 * An adapter that is used to filter sub table change notifications and make the path relative to the NetworkTable
 * 
 * @author Mitchell
 *
 */
public class NetworkTableSubListenerAdapter implements ITableListener {
	
	private final ITableListener targetListener;
	private final NetworkTable targetSource;
	private final String prefix;
	
	private final Set notifiedTables = new Set();

	/**
	 * Create a new adapter
	 * @param prefix the prefix of the current table
	 * @param targetSource the source that events passed to the target listener will appear to come from
	 * @param targetListener the listener where events are forwarded to
	 */
	public NetworkTableSubListenerAdapter(String prefix, NetworkTable targetSource, ITableListener targetListener){
		this.prefix = prefix;
		this.targetSource = targetSource;
		this.targetListener = targetListener;
	}

	public void valueChanged(ITable source, String key, Object value, boolean isNew) {//TODO use string cache
            if(key.startsWith(prefix)){
                String relativeKey = key.substring(prefix.length()+1);
                int endSubTable = -1;//TODO implement sub table listening better
                for(int i = 0; i<relativeKey.length(); ++i){
                    if(relativeKey.charAt(i)==NetworkTable.PATH_SEPARATOR){//is sub table
                        endSubTable = i;
                        break;
                    }
                }
                if(endSubTable!=-1){
                    String subTableKey = relativeKey.substring(0, endSubTable);
                    if(!notifiedTables.contains(subTableKey)){
                        notifiedTables.add(subTableKey);
                        targetListener.valueChanged(targetSource, subTableKey, targetSource.getSubTable(subTableKey), true);
                    }
                }
            }
	}

}
