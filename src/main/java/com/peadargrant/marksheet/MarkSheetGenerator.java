/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package com.peadargrant.marksheet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidation.ErrorStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Peadar Grant
 */
public class MarkSheetGenerator {
    
    private final String inputFilename, outputFilename;
    private final MarksDatabase mdb;
    private final XSSFWorkbook wb;
    private final XSSFSheet sheet;
    private final XSSFFont defaultFont, headerFont;
    private final CellStyle defaultStyle, headerStyle, firstStyle;
    int column;
    private final List<Integer> startIndices, endIndices;
    private final XSSFRow headerRow, detailRow;
    private int totalsStart, totalsEnd;
    
    
    public MarkSheetGenerator(String inputFilename, String outputFilename) throws Exception {
        
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
        mdb = new MarksDatabase(inputFilename);
        
        wb = new XSSFWorkbook();
        sheet = wb.createSheet("EXAM_MARKS");
        
        // Font settings
        defaultFont = wb.createFont();
        defaultFont.setFontHeightInPoints((short)12);
        defaultFont.setFontName("Arial Narrow");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBoldweight(defaultFont.getBoldweight());
        defaultFont.setItalic(false);   
        
        headerFont= wb.createFont();
        headerFont.setFontHeightInPoints((short)12);
        headerFont.setFontName("Arial Narrow");
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setBoldweight((short) (2* defaultFont.getBoldweight()));
        headerFont.setItalic(false);
        
        // Styles
        defaultStyle=wb.createCellStyle();
        defaultStyle.setFont(defaultFont);
        
        firstStyle = wb.createCellStyle();
        firstStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        firstStyle.setFillPattern(PatternFormatting.FINE_DOTS);
        firstStyle.setFont(defaultFont);
        
        headerStyle=wb.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.TEAL.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont); 
        
        column=0;
        
        startIndices = new ArrayList<>();
        endIndices = new ArrayList<>(); 
        
                // Row 
        headerRow = sheet.createRow(0);
        detailRow = sheet.createRow(1);
    }
    
    public void generateMarkSheet() throws Exception {
        
        insertLeaders();
     
        insertEntryFields();
        
        insertQuestionTotals();
            
        insertTotal();
        
        // Apply Autosizing
        XSSFRow row = wb.getSheetAt(0).getRow(0);
        for(int colNum = 0; colNum<row.getLastCellNum();colNum++) {
            wb.getSheetAt(0).autoSizeColumn(colNum);
        }
        
        // Format header
        Header header = sheet.getHeader();
        header.setCenter("Detail marks sheet");
        
        FileOutputStream fos =new FileOutputStream(new File(outputFilename));
	wb.write(fos);
	fos.close();
        
    }
    
    private void insertLeaders() {
        // Generate headers
        ArrayList<String> headers = new ArrayList<>(); 
        headers.add("ID"); 
        headers.add("Name");
        
        // Student name
        for ( String s : headers )
        {
            XSSFCell cell = headerRow.createCell(column);
            cell.setCellValue(s);
            cell.setCellStyle(headerStyle);
            
            XSSFCell cell2 = detailRow.createCell(column);
            cell2.setCellValue("-");
            cell2.setCellStyle(defaultStyle);
            
            column++; 
        }
    }
    
    private void insertEntryFields() throws Exception {
        
        List<List<Integer>> marks = mdb.questions();
        
        for ( int k = 0 ; k < marks.size() ; k++ ) {
            
            List<Integer> question = marks.get(k);
            startIndices.add(column);
            int lastColumn = column;
            
            for ( int m = 0 ; m < question.size() ; m++ ) {
                Integer marksForPart = question.get(m);
                String questionText = "Q" + (k + 1) + "(" + (char)(97+m) + ")";
                
                XSSFCell headerCell = headerRow.createCell(column);
                headerCell.setCellValue(questionText);
                headerCell.setCellStyle(headerStyle);

                XSSFCell detailCell = detailRow.createCell(column);
                detailCell.setCellValue(0);
                if ( m==0 ) {
                    detailCell.setCellStyle(firstStyle);
                }
                else {
                    detailCell.setCellStyle(defaultStyle);
                }
                
                XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(sheet);
                DataValidationConstraint constraint = validationHelper.createIntegerConstraint(OperatorType.BETWEEN, "0", marksForPart.toString());
                CellRangeAddressList cral = new CellRangeAddressList(1, 1, column, column);
                DataValidation validation = validationHelper.createValidation(constraint, cral);
                validation.setErrorStyle(ErrorStyle.STOP);
                validation.createErrorBox(questionText, "Only numeric values are allowed up to "+marksForPart.toString());
                validation.setShowErrorBox(true);
                validation.createPromptBox(questionText, "Marked 0 to "+marksForPart.toString());
                validation.setShowPromptBox(true);
                sheet.addValidationData(validation);
                
                lastColumn = column;
                
                column++;
            }
            
            endIndices.add(lastColumn);
        }
      
    }
    
    private void insertQuestionTotals() throws Exception {
        
        totalsStart = column;
        
        for ( int k = 0 ; k < startIndices.size() ; k++ ) {
            
            String startIndex = CellReference.convertNumToColString(startIndices.get(k)) + "2";
            String endIndex = CellReference.convertNumToColString(endIndices.get(k)) + "2";
            
            XSSFCell headerCell = headerRow.createCell(column);
            headerCell.setCellValue("Q"+(k+1));
            headerCell.setCellStyle(headerStyle);
            
            XSSFCell detailCell = detailRow.createCell(column);
            detailCell.setCellFormula("SUM("+startIndex+":"+endIndex+")");
            detailCell.setCellStyle(defaultStyle);
            
            totalsEnd = column;
            
            column++;
        }
        
        String startIndex = CellReference.convertNumToColString(totalsStart) + "2";
        String endIndex = CellReference.convertNumToColString(totalsEnd) + "2";
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.GT, "");
        PatternFormatting fill1 = rule1.createPatternFormatting();
        fill1.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.index);
        fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf(startIndex+":"+endIndex)
        };
        sheetCF.addConditionalFormatting(regions, rule1);
    }
    
    private void insertTotal() throws Exception {
        
        int nQuestions = startIndices.size();
        int bestOf = mdb.bestOf();
        int nDrop = nQuestions - bestOf;
        
        XSSFCell headerCell = headerRow.createCell(column);
        headerCell.setCellValue("TOTAL");
        headerCell.setCellStyle(headerStyle);
        
        String startIndex = CellReference.convertNumToColString(totalsStart) + "2";
        String endIndex = CellReference.convertNumToColString(totalsEnd) + "2";
        
        String range = startIndex + ":" + endIndex;
        
        StringBuilder totalFormula = new StringBuilder();
        totalFormula.append("SUM(").append(range).append(")");
        for ( int k = 1 ; k <= nDrop ; k++ ) {
            totalFormula.append("-SMALL(").append(range).append(",").append(k).append(")");
        }

        XSSFCell detailCell = detailRow.createCell(column);
        detailCell.setCellFormula(totalFormula.toString());
        detailCell.setCellStyle(defaultStyle);
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.GT, "40");
        PatternFormatting fill1 = rule1.createPatternFormatting();
        fill1.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.index);
        fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf(CellReference.convertNumToColString(column)+"2")
        };
        sheetCF.addConditionalFormatting(regions, rule1);
        
        
    }
    
    
    
}
