/*
 * Simple Logger Strategy has only one ArraySwapper which all producers share
 * 
 */
package org.shotlogger.strategy;

import java.util.ArrayList;
import java.util.Collection;
import com.shotbygun.collections.ArraySwapper;
import org.shotlogger.LogItem;

/**
 *
 * @author shotbygun
 */
public class SimpleLoggerStrategy implements ShotLoggerStrategy {
    
    private final ArraySwapper arraySwapper;
    private final ArrayList<ArraySwapper<LogItem>> arrayList;

    public SimpleLoggerStrategy(Integer size) {
        if(size == null)
            size = 512;
        this.arraySwapper = new ArraySwapper(LogItem.class, size);
        this.arrayList = new ArrayList<>();
        this.arrayList.add(arraySwapper);
    }
    
    

    @Override
    public ArraySwapper getCurrentLogItemSwapper() {
        return arraySwapper;
    }

    @Override
    public int getSize() {
        return arraySwapper.size();
    }

    @Override
    public Collection<ArraySwapper<LogItem>> getAllSwappers() {
        return arrayList;
    }
    
}
