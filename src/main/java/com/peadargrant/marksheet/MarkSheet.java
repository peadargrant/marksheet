/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package com.peadargrant.marksheet;

/**
 *
 * @author Peadar Grant
 */
public class MarkSheet {
    
    public static void main(String args[]) throws Exception {

        String inputFilename = "/Users/peadar/Documents/courses/datacomms/exam/dc_exam1_sol_marks.xml";
        String outputFilename = "/Users/peadar/Desktop/markingSheet.xlsx";
        
        MarkSheetGenerator msg = new MarkSheetGenerator(inputFilename, outputFilename);
        msg.generateMarkSheet();
        
    }
    
}
