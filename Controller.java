package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.*;


public class Controller {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button buttonChooseFile;

    @FXML
    private Label chosenFile;

    @FXML
    private TextField textFieldInput;

    @FXML
    private TextField textFieldOutput;

    @FXML
    private Button saveButton;

    @FXML
    private Button changeButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label doneLabel;




    private String loadLocation = null;
    private String saveLocation = null;



    String loadString = null;
    String stringToBeSaved = null;


    public void initialize() {
        if(loadLocation == null){
            saveButton.setDisable(true);
        }

    }


    //ok
    public void clickButton() throws IOException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Please choose password list");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"));

        File file = chooser.showOpenDialog(gridPane.getScene().getWindow());


        if (file != null) {
            //get the load location
            this.loadLocation = file.getPath();
            saveButton.setDisable(false);
        } else {
            System.out.println("Cancelled");
        }


    }




    public void saveList(ActionEvent event) throws IOException {


        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save application file");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text,", "*.txt"));
        File file = chooser.showSaveDialog(gridPane.getScene().getWindow());
        saveLocation = file.getPath();
        System.out.println("Save path = " + saveLocation);
        saveButton.setDisable(true);
        buttonChooseFile.setDisable(true);


        //==========
        //background

        Background write = new Background();
        new Thread(write).start();
        doneLabel.setFont(Font.font(20));
        write.setOnRunning(event1 -> {
            doneLabel.setText("Working...");
        });
        write.setOnSucceeded(event1 -> {
            doneLabel.setText("DONE!");
        });






    }


    private class Background<String> extends Task<Void> {

        @Override
        protected Void call() throws Exception {

            BufferedReader reader = new BufferedReader(new FileReader(loadLocation));
            BufferedWriter br = new BufferedWriter(new FileWriter(saveLocation));
            while ((loadString = reader.readLine()) != null) {
                if (loadString.contains(textFieldInput.getText())) {
                    stringToBeSaved = loadString.replace(textFieldInput.getText(), textFieldOutput.getText());

                    try {

                        br.write(stringToBeSaved);
                        br.newLine();


                        System.out.println("Already saved " + stringToBeSaved);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        br.flush();

                    }
                    System.out.println("Tobe Changed = " + loadString);
                    System.out.println("stringToBeSaved = " + stringToBeSaved);
                } else {
                    stringToBeSaved = loadString;
                    try {
                        br.write(stringToBeSaved);
                        br.newLine();
                        System.out.println("Already saved " + stringToBeSaved);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        br.flush();
                    }
                }

            }

            Platform.exit();
            return null;
        }
    }




}
