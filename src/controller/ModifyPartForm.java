package controller;
/**
 * @Author Josh Gautney
 * */

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ModifyPartForm implements Initializable {
    public ToggleGroup RadButton;
    public RadioButton inHouseRadButton;
    public RadioButton outsourcedRadButton;
    public Label partSourceLabel;
    public Button addPartSave;
    public Button addPartCancel;
    public TextField idTF;
    public TextField nameTF;
    public TextField InvTF;
    public TextField PriceTF;
    public TextField maxTF;
    public TextField minTF;
    public TextField machCompTF;

    private static Part selectedPart = null;
    private static Part oldPart = null;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTextFields();

    }


    /**
     * @param actionEvent changes label to Company Name for Outsourced parts
     * */
    public void toOutsource(ActionEvent actionEvent) {
        partSourceLabel.setText("Machine ID");
    }

    /**
     * @param actionEvent changes label to machine ID for In House parts
     * */
    public void toInHouse(ActionEvent actionEvent) {
        partSourceLabel.setText("Company Name");
    }

    /**
     * Saves part to the allParts list
     * */
    public void savePart(ActionEvent actionEvent) throws Exception {
    try {
        int id = Integer.parseInt(idTF.getText().trim());
        String name = nameTF.getText().trim();
        double price = Double.parseDouble(PriceTF.getText().trim());
        int invLevel = Integer.parseInt(InvTF.getText().trim());
        int minLevel = Integer.parseInt(minTF.getText().trim());
        int maxLevel = Integer.parseInt(maxTF.getText().trim());

        // Shows alert dialog if invLevel is outside min and max
        if(invLevel < minLevel || invLevel > maxLevel) throw new Exception();

        if (inHouseRadButton.isSelected()) {
            int machineID = Integer.parseInt(machCompTF.getText().trim());
            Part selectedPart = new InHouse(id, name, price, invLevel, minLevel, maxLevel, machineID);
            Inventory.updatePart(selectedPart);
            Inventory.getAllParts().remove(oldPart);
        } else if (outsourcedRadButton.isSelected()) {
            String companyName = machCompTF.getText().trim();
            Part selectedPart = new Outsourced(id, name, price, invLevel, minLevel, maxLevel, companyName);
            Inventory.updatePart(selectedPart);
            Inventory.getAllParts().remove(oldPart);
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    catch (NumberFormatException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText("Please enter valid values!");
        alert.showAndWait();
    }
    catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText("Inventory must be between maximum and minimum!");
        alert.showAndWait();;
    }
    }

    /**
     * @param actionEvent cancels modifying part and returns to the main screen
     * */
    public void cancelPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * carried over parts from MainForm
     * */
    public static void updatePart(Part sp){
        selectedPart = sp;
        oldPart = sp;
    }

    /**
     * A method for filling the text fields to keep initialize clean
     * */
    private void fillTextFields(){
        idTF.setText(String.valueOf(selectedPart.getId()));
        nameTF.setText(selectedPart.getName());
        InvTF.setText(String.valueOf(selectedPart.getStock()));
        PriceTF.setText(String.valueOf(selectedPart.getPrice()));
        maxTF.setText(String.valueOf(selectedPart.getMax()));
        minTF.setText(String.valueOf(selectedPart.getMin()));

        if (selectedPart instanceof InHouse) {
            InHouse sp = (InHouse) selectedPart;
            inHouseRadButton.setSelected(true);
            partSourceLabel.setText("Machine ID");
            machCompTF.setText(String.valueOf(sp.getMachineId()));
        }
        else if (selectedPart instanceof Outsourced)
        {
            outsourcedRadButton.setSelected(true);
            partSourceLabel.setText("Company Name");
            machCompTF.setText(((Outsourced) selectedPart).getCompanyName());
        }
    }
}
