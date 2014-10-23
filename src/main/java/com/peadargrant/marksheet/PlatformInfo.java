/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */

package com.peadargrant.marksheet;

/**
 *
 * @author Peadar Grant
 */
public class PlatformInfo {
    
    public boolean isMac() {
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        return operatingSystem.contains("mac"); 
    }
    
}
