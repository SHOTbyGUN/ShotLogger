/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shotlogger.strategy;

import java.util.Collection;
import com.shotbygun.collections.ArraySwapper;
import org.shotlogger.LogItem;

/**
 *
 * @author shotbygun
 */
public interface ShotLoggerStrategy {
    public abstract ArraySwapper<LogItem> getCurrentLogItemSwapper();
    public abstract Collection<ArraySwapper<LogItem>> getAllSwappers();
    public abstract int getSize();
}
