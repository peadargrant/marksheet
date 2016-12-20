/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package marksheet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Peadar Grant
 */
public class TxtMarksDatabase implements MarksDatabase {
    
    private final List<List<Integer>> questionMarks;
        
    public TxtMarksDatabase(File inputFile) throws Exception {
        
        questionMarks = new ArrayList<>();
        
        Scanner s = new Scanner(inputFile);
        ArrayList<Integer> currentQuestion = null;

        while ( s.hasNextLine() ) {
            String line = s.nextLine();
            if ( "QUESTION".equals(line) ) {
                currentQuestion = new ArrayList<>();
                questionMarks.add(currentQuestion);
                continue;
            }
            if ( line.startsWith("PART,")) {
                if ( null == currentQuestion ) {
                    throw new Exception("no question defined");
                }
                currentQuestion.add(new Integer(line.split(",")[1]));
            }
        }
        
    }
    
    @Override
    public int marksPerQuestion() throws Exception {
        return 0;
    }
    
    @Override
    public int bestOf() throws Exception {
        return questionMarks.size();
    }
    
    @Override
    public List<List<Integer>> questions() throws Exception {
        
        return questionMarks;
        
    }
    
}
