package controller;

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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProductForm implements Initializable {

    public Button addSaveProd;
    public Button cancelProd;
    public TextField idTF;
    public TextField nameTF;
    public TextField invTF;
    public TextField priceTF;
    public TextField maxTF;
    public TextField minTF;

    public Button addPart;
    public Button removePart;

    public TableView<Part> associatedPartTable;
    public TableView<Part> allPartTable;

    public TableColumn<Object, Object> partIDCol;
    public TableColumn<Object, Object> partNameCol;
    public TableColumn<Object, Object> invCol;
    public TableColumn<Object, Object> priceCol;
    public TableColumn<Object, Object> assocPartIdCol;
    public TableColumn<Object, Object> assocPartNameCol;
    public TableColumn<Object, Object> assocInvCol;
    public TableColumn<Object, Object> assocPriceCol;
    public TextField addProdQueryTF;


    private ObservableList<Part> ap = FXCollections.observableArrayList(); // Arraylist for holding associated parts


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillTableData();

    }
    /**
     * Saves product and associated parts list.  Uses a for loop to add parts to the product.
     * */
    public void saveProduct(ActionEvent actionEvent) throws IOException {
       try {
           MainForm.idGen++;
           String name = nameTF.getText().trim();
           int invLevel = Integer.parseInt(invTF.getText().trim());
           double price = Double.parseDouble(priceTF.getText().trim());
           int maxLevel = Integer.parseInt(maxTF.getText().trim());
           int minLevel = Integer.parseInt(minTF.getText().trim());

           // Shows alert dialog if invLevel is outside min and max
            if(invLevel < minLevel || invLevel > maxLevel) throw new Exception();

           Product newProduct = new Product(MainForm.idGen, name, price, invLevel, minLevel, maxLevel);

           for (Part part : ap) {
               newProduct.addAssociatedPart(part);
           }

           Inventory.addProduct(newProduct);


           Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
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
           alert.showAndWait();;
       }
    }

    /**
     * Cancels saving product and returns to previous screen
     * */
    public void cancelProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Adds part to the product
     * */
    public void addPart(ActionEvent actionEvent) {
        Part sp = allPartTable.getSelectionModel().getSelectedItem();
        ap.add(sp);

    }

    /**
     * Removes part from the product
     * */
    public void removePart(ActionEvent actionEvent) {
        Part sp = associatedPartTable.getSelectionModel().getSelectedItem();
        if (sp == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part to remove!");
            alert.showAndWait();

        }
        else
        {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "This will remove the part from the product. Do you want to continue?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                ap.remove(sp);
            }
        }
    }

    /**
     * Searches for parts in the all parts table by name or id
     * */
    public void allPartQuery(ActionEvent actionEvent) {
        try {
            String q = addProdQueryTF.getText();

            ObservableList<Part> parts = Inventory.lookupPart(q);

            if (parts.size() == 0) {
                int id = Integer.parseInt(q);
                Part sp = Inventory.lookupPart(id);
                if (sp != null)
                    parts.add(sp);
            }
            allPartTable.setItems(parts);
            addProdQueryTF.setText("");
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Match Found!");
            alert.showAndWait();
            addProdQueryTF.setText("");
        }
    }

    /**
     * used for keeping initialize clean
     * */
    public void fillTableData(){
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartTable.setItems(Inventory.getAllParts());

        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartTable.setItems(ap);

    }


}
