/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package marksheet;

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
