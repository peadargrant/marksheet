/* 
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package com.peadargrant.marksheet;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

public class MarkSheetController implements Initializable {
    
    
    @FXML private Label statusLabel;
    @FXML private Label inputFilenameLabel;
    @FXML private Label outputFilenameLabel;
    @FXML private MenuBar menuBar; 
    @FXML private Menu fileMenu;
    @FXML private Button chooseInputFileButton, chooseOutputFileButton, generateSpreadsheetButton;
    private File inputFile, outputFile;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            // change menu bar based on whether we're on Mac or not
            PlatformInfo platformInfo = new PlatformInfo();
            if ( platformInfo.isMac() ) {
                menuBar.setUseSystemMenuBar(true);
                menuBar.setVisible(false);
            } else {
                MenuItem quitItem = new MenuItem();
                quitItem.setText("Exit");
                KeyCombination quitCombination = KeyCombination.keyCombination("Shortcut+X");
                quitItem.setAccelerator(quitCombination);
                fileMenu.getItems().add(quitItem);
            }

        }
        catch ( Exception e )
        {
            Action response = Dialogs.create()
                    .style(DialogStyle.NATIVE)
                    .title("Initialisation failed")
                    .masthead("An error occurred during initialisation. Please try again or contact support for further assistance.")
                    .showException(e);
            System.exit(0);
        }
        
    }    
    
    @FXML
    public void chooseInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open marking file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML file", "*.xml")
            );
        this.inputFile = fileChooser.showOpenDialog(null);
        this.inputFilenameLabel.setText(this.inputFile.getName());
    }
    
    @FXML
    public void chooseOutputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose destination file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel", "*.xlsx")
            );
        this.outputFile = fileChooser.showSaveDialog(null);
        this.outputFilenameLabel.setText(this.outputFile.getName());
    }
    
    @FXML 
    public void generateExcelSheet() {
        try {
            statusLabel.setText("Generating sheet");
            MarkSheetGenerator msg = new MarkSheetGenerator(inputFile, outputFile);
            msg.generateMarkSheet();
            statusLabel.setText("Generated sheet. Done.");
        }
        catch ( Exception e ) {
            Action response = Dialogs.create()
                    .style(DialogStyle.NATIVE)
                    .title("Generation failed")
                    .masthead("An error occurred when trying to generate Excel sheet.")
                    .showException(e);
        }
    }
}
