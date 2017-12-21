/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package marksheet;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Peadar Grant
 */
public class TxtMarksDatabase implements MarksDatabase {
    
    private final List<List<Part>> questionMarks;
    private int bestOfQuestions = 0;
    private int compulsoryQuestions = 0;
        
    public TxtMarksDatabase(File inputFile) throws Exception {
        
        questionMarks = new ArrayList<>();
        
        Scanner s = new Scanner(inputFile);
        ArrayList<Part> currentQuestion = null;
        Part currentPart = null;

        while ( s.hasNextLine() ) {
            String line = s.nextLine();
            if ( line.startsWith("QUESTION") ) {
                currentQuestion = new ArrayList<>();
                questionMarks.add(currentQuestion);
                continue;
            }
            if ( line.startsWith("PART,")) {
                if ( null == currentQuestion ) {
                    throw new Exception("no question defined");
                }
                currentPart = new Part(new BigInteger(line.split(",")[1]));
                currentQuestion.add(currentPart);
                continue;
            }
            if ( line.startsWith("RUBRIC,")) {
                if ( null == currentPart ) {
                    throw new Exception("no part defined");
                }
                currentPart.addRubric(line.split(",")[1], line.split(",")[2]);
                continue;
            }
            if ( line.startsWith("BEST_OF,")) {
                if ( bestOfQuestions > 0 ) {
                    throw new Exception("best of questions already set");
                }
                bestOfQuestions = Integer.parseInt(line.split(",")[1]);
                continue;
            }
            if ( line.startsWith("COMPULSORY,")) {
                if ( compulsoryQuestions > 0 ) {
                    throw new Exception("compulsory questions already set");
                }
                compulsoryQuestions = Integer.parseInt(line.split(",")[1]);
                continue;
            }
            System.out.println("ignored: "+line);
        }
        
    }
    
    @Override
    public int marksPerQuestion() throws Exception {
        return 0;
    }
    
    @Override
    public int bestOf() throws Exception {
        if ( this.bestOfQuestions > 0 ) {
            return this.bestOfQuestions;
        }
        return questionMarks.size();
    }
    
    @Override
    public int compulsoryQuestions() throws Exception {
        if ( this.compulsoryQuestions > 0 ) {
            return this.compulsoryQuestions;
        }
        return 0;
    }
    
    @Override
    public List<List<Part>> questions() throws Exception {
        return questionMarks;
    }
    
}
