/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package com.peadargrant.marksheet.tool;

import com.peadargrant.marksheet.core.MarkSheetGenerator;
import java.io.File;

/**
 *
 * @author Peadar Grant
 */
public class MarksheetTool {
    
    public static void main(String[] args) throws Exception {
        
        System.out.println("marksheet generator");
        
        if ( args.length != 2 ) {
            System.err.println("incorrect number of arguments received!");
            System.err.println("usage: marksheet <input filename> <output filename>");
            System.exit(1);
        }
        
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        
        MarkSheetGenerator mg = new MarkSheetGenerator(inputFile, outputFile);
        mg.generateMarkSheet();
    }
    
}
