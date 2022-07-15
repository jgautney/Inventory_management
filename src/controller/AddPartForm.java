package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static model.Inventory.addPart;

public class AddPartForm implements Initializable {
    public ToggleGroup RadButton;
    public RadioButton inHouseRadButton;
    public RadioButton outsourcedRadButton;
    public Label partSourceLabel;
    public Button addPartSave;
    public Button addPartCancel;
    public TextField nameTF;
    public TextField invTF;
    public TextField priceTF;
    public TextField maxTF;
    public TextField minTF;
    public TextField machCompTF;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Changes label to outsourced depending on which radio button is selected
     * */
    public void toOutsource(ActionEvent actionEvent) {
        partSourceLabel.setText("Machine ID");
    }

    /**
     * Changes label to machine id depending on which radio button is selected
     * */
    public void toInHouse(ActionEvent actionEvent) {
        partSourceLabel.setText("Company Name");
    }

/**
 * when the button labeled save is pressed, saves part to allparts list using addPart method
 * from Inventory class
 *
 * uses if statement to check if the radio button is set to in house or outsourced
 *
 * Try - catch block for handling exceptions with data entry by user
 * */
    public void savePart(ActionEvent actionEvent) {
        try{
            String name = nameTF.getText().trim();
            double price = Double.parseDouble(priceTF.getText().trim());
            int invLevel = Integer.parseInt(invTF.getText().trim());
            int minLevel = Integer.parseInt(minTF.getText().trim());
            int maxLevel = Integer.parseInt(maxTF.getText().trim());

            // Shows alert dialog if invLevel is outside min and max
            if(invLevel < minLevel || invLevel > maxLevel) throw new Exception();

            if(inHouseRadButton.isSelected()){
                MainForm.idGen++;
                int machineID = Integer.parseInt(machCompTF.getText().trim());
                InHouse newPart = new InHouse(MainForm.idGen, name, price, invLevel, minLevel, maxLevel, machineID);
                newPart.setMachineId(machineID);
                addPart(newPart);
            }
            else if(outsourcedRadButton.isSelected()){
                MainForm.idGen++;
                String companyName = machCompTF.getText().trim();
                Outsourced newPart = new Outsourced(MainForm.idGen, name, price, invLevel, minLevel, maxLevel, companyName);
                newPart.setCompanyName(companyName);
                addPart(newPart);
            }

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter valid values!");
            alert.showAndWait();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory must be between maximum and minimum!");
            alert.showAndWait();
        }
    }

    /**
     * Cancels adding the part and returns to previous screen
     * */
    public void cancelPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
