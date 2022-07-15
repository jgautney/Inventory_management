package controller;

/**
 * @author Josh Gautney
 * */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProductForm implements Initializable {
    public Button addSaveProd;
    public Button cancelProd;
    public TextField prodID;
    public TextField prodName;
    public TextField prodInv;
    public TextField prodPrice;
    public TextField prodMax;
    public TextField prodMin;

    public TableView allPartsTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;

    public TableView associatedPartsTable;
    public TableColumn assocPartIdCol;
    public TableColumn assocPartNameCol;
    public TableColumn assocPartInvcol;
    public TableColumn assocPartPriceCol;

    public TextField modProdQuery;

    public Button removePart;
    public Button addPart;


    private static Product selectedProduct = null;
    private static Product oldProduct = null;


    private ObservableList<Part> ap = FXCollections.observableArrayList();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ap.addAll(selectedProduct.getAllAssociatedParts());

        fillTableValues();
        fillTextFields();
    }


    /**
     * Saves the updated product and associated parts. Also deletes the old product
     * */
    public void saveProduct(ActionEvent actionEvent) {

      try {
          int id = Integer.parseInt(prodID.getText().trim());
          String name = prodName.getText().trim();
          double price = Double.parseDouble(prodPrice.getText().trim());
          int invLevel = Integer.parseInt(prodInv.getText().trim());
          int minLevel = Integer.parseInt(prodMin.getText().trim());
          int maxLevel = Integer.parseInt(prodMax.getText().trim());

          // Shows alert dialog if invLevel is outside min and max
          if (invLevel < minLevel || invLevel > maxLevel) throw new Exception();

          Product updateProduct = new Product(id, name, price, invLevel, minLevel, maxLevel);

          for (int i = 0; i < ap.size(); ++i) {
              updateProduct.addAssociatedPart(ap.get(i));
          }
          Inventory.updateProduct(updateProduct);
          Inventory.getAllProducts().remove(oldProduct);


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
      catch (Exception e) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error Dialog");
          alert.setContentText("Inventory must be between maximum and minimum!");
          alert.showAndWait();
      }
    }

    /**
     * Cancels modifying the product and returns to previous screen
     * */
    public void cancelProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     *adds parts to the associated parts table
     * */
    public void addPart(ActionEvent actionEvent) {
        try {
            Part sp = (Part) allPartsTable.getSelectionModel().getSelectedItem();
            ap.add(sp);
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part to add!");
            alert.showAndWait();
        }

    }

    /**
     * Removes parts from the associated parts table. Asks for confirmation before deletion
     * */
    public void removePart(ActionEvent actionEvent) {
        Part sp = (Part)associatedPartsTable.getSelectionModel().getSelectedItem();

        if(sp == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part to delete!");
            alert.showAndWait();
        }
        else
        {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "This will remove the part from the product. Do you want to continue?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (selectedProduct.deleteAssociatedPart(sp)) {
                    ap.remove(sp);
                }
            }
        }
    }

    /**
     * carries over the selected product from the main screen. also saves it to old product in order to delete
     * */
    public static void updateProduct(Product sp){
        selectedProduct = sp;
        oldProduct = sp;
    }

    /**
     * Fills texts fields. Used to keep initialize clean
     * */
    public void fillTextFields(){
        prodID.setText(String.valueOf(selectedProduct.getId()));
        prodName.setText(selectedProduct.getName());
        prodInv.setText(String.valueOf(selectedProduct.getStock()));
        prodPrice.setText(String.valueOf(selectedProduct.getPrice()));
        prodMax.setText(String.valueOf(selectedProduct.getMax()));
        prodMin.setText(String.valueOf(selectedProduct.getMin()));
    }

    /**
     * Fills table data. Used to keep initialize clean
     * */
    public void fillTableValues(){
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartsTable.setItems(Inventory.getAllParts());

        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvcol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartsTable.setItems(ap);
    }

    /**
     * Searches for parts in the all parts table by name or id
     * */
    public void allPartsQuery(ActionEvent actionEvent) {

        try {
            String q = modProdQuery.getText();

            ObservableList<Part> parts = Inventory.lookupPart(q);

            if (parts.size() == 0) {
                int id = Integer.parseInt(q);
                Part sp = Inventory.lookupPart(id);
                if (sp != null)
                    parts.add(sp);
            }
            allPartsTable.setItems(parts);
            modProdQuery.setText("");
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Match Found!");
            alert.showAndWait();
            modProdQuery.setText("");
        }
    }
}
