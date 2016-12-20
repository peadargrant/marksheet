/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package marksheet;

import marksheet.MarkSheetGenerator;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author Peadar Grant
 */
public class MarksheetTool {

    public static void main(String[] args) throws Exception {

        System.out.println("marksheet generator");

        if (args.length != 2 && !(args.length == 1 && args[0].equals("--"))) {
            System.err.println("incorrect number of arguments received!");
            System.err.println("batch usage: marksheet <input filename> <output filename>");
            System.err.println("interactive usage: marksheet --");
            System.exit(1);
        }

        File inputFile;
        File outputFile;
        if ("--".equals(args[0])) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("enter input filename: ");
            inputFile = new File(scanner.next());
            System.out.print("enter output filename: ");
            outputFile = new File(scanner.next());
        } else {
            inputFile = new File(args[0]);
            outputFile = new File(args[1]);
        }

        MarkSheetGenerator mg = new MarkSheetGenerator(inputFile, outputFile);
        mg.generateMarkSheet();
    }

}
