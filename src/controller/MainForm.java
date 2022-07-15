package controller;
/**
 * @author Josh Gautney
 * */

import javafx.application.Platform;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainForm implements Initializable {
    public TableView partTable;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partLevelCol;
    public TableColumn partCostCol;
    public TextField partQueryTF;
    public Button partAdd;
    public Button partDelete;
    public Button partModify;

    public TableView productTable;
    public TableColumn prodIDCol;
    public TableColumn prodNameCol;
    public TableColumn prodLevelCol;
    public TableColumn prodCostCol;
    public TextField prodQueryTF;
    public Button prodDelete;
    public Button prodModify;
    public Button prodAdd;

    public Button fullExit;

    public static int idGen = 0; // ID generator for parts and products. Assigns in sequential order.


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(Inventory.getAllParts());

        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTable.setItems(Inventory.getAllProducts());


    }

    /**
     * @param actionEvent Exit button exits the program
     */
    public void exitProgram(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * @param actionEvent Transitions to the add part screen
     */
    public void toAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addPartForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param actionEvent Transitions to modify part screen
     */
    public void toModifyPart(ActionEvent actionEvent) throws IOException {

        try {
            Part selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();
            Part oldPart = (Part) partTable.getSelectionModel().getSelectedItem();

            ModifyPartForm.updatePart(selectedPart);
            ModifyPartForm.updatePart(oldPart);

            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyPartForm.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part to modify!");
            alert.showAndWait();
        }
    }

    /**
     * @param actionEvent Transitions to modify product screen
     */
    public void toModifyProduct(ActionEvent actionEvent) throws IOException {
        try {
            Product selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();
            Product oldProduct = (Product) productTable.getSelectionModel().getSelectedItem();

            ModifyProductForm.updateProduct(selectedProduct);
            ModifyProductForm.updateProduct(oldProduct);

            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyProductForm.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a product to modify!");
            alert.showAndWait();
        }
    }

    /**
     * @param actionEvent Transitions the add product screen
     */
    public void toAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addProductForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param actionEvent removes selected part from allParts list. Asks for confirmation before deletion
     */
    public void deletePart(ActionEvent actionEvent) throws IOException {
        Part np = (Part) partTable.getSelectionModel().getSelectedItem();

            if(np == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please select a part to delete!");
                alert.showAndWait();
            }
            else
            {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the part. Do you want to continue?");
                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.getAllParts().remove(np);
                }
            }
    }

    /**
     * @param actionEvent removes selected product from the allProducts list.  Prevents deleting a product that contains parts amd asks for confirmation before deletion
     */
    public void deleteProduct(ActionEvent actionEvent) {

        Product sp = (Product) productTable.getSelectionModel().getSelectedItem();

        try {
            if (sp.getAllAssociatedParts().size() == 0) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the product. Do you want to continue?");
                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (Inventory.deleteProduct(sp)) {
                        Inventory.getAllProducts().remove(sp);
                    }
                }
            } else {
                Alert deny = new Alert(Alert.AlertType.ERROR);
                deny.setTitle("Error Dialog");
                deny.setContentText("Can not delete product that contains parts!");
                deny.showAndWait();
            }
        }
        catch (Exception e)
        {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please select a product to delete!");
                alert.showAndWait();
        }
    }

    /**
     * Returns parts based on whether a string for name or int for id is entered
     * */
    public void returnParts(ActionEvent actionEvent) {
        try {
            String q = partQueryTF.getText();

            ObservableList<Part> parts = Inventory.lookupPart(q);

            if (parts.size() == 0) {
                int id = Integer.parseInt(q);
                Part sp = Inventory.lookupPart(id);
                if (sp != null)
                    parts.add(sp);
            }

            partTable.setItems(parts);
            partQueryTF.setText("");
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Match Found!");
            alert.showAndWait();
            partQueryTF.setText("");
        }
    }

    /**
     * Returns products based on whether a string for name or int for id is entered
     * */
    public void returnProducts(ActionEvent actionEvent) {
        try {
            String q = prodQueryTF.getText();

            ObservableList<Product> products = Inventory.lookupProduct(q);

            if (products.size() == 0) {
                int id = Integer.parseInt(q);
                Product sp = Inventory.lookupProduct(id);
                if (sp != null) {
                    products.add(sp);
                }
            }

            productTable.setItems(products);
            prodQueryTF.setText("");
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Match Found!");
            alert.showAndWait();
            prodQueryTF.setText("");
        }
    }
}
