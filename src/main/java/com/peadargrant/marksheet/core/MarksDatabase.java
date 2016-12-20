/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package com.peadargrant.marksheet.core;

import java.util.List;

/**
 *
 * @author Peadar Grant
 */
public interface MarksDatabase {
        
    public int marksPerQuestion() throws Exception;
    public int bestOf() throws Exception;
    public List<List<Integer>> questions() throws Exception;
    
}
