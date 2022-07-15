package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart adds new part to allParts list
     * */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    /**
     * @param newProduct add new product to the allProducts list
     * */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    /**
     * @return lookup and return parts by ID, if none found nothing is returned
     * */
    public static Part lookupPart(int id){

        for (Part sp : allParts) {
            if (sp.getId() == id) {
                return sp;
            }
        }
        return null;
    }
    /**
     * @return lookup and return products by id, if none found nothing is returned
     * */
    public static Product lookupProduct(int id){
        for (Product sp : allProducts) {
            if (sp.getId() == id) {
                return sp;
            }
        }
        return null;
    }
    /**
     * @return lookup and return parts by name
     * */
    public static ObservableList<Part> lookupPart(String name){
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
            for (Part ap : allParts) {
                if (ap.getName().contains(name)) {
                    namedParts.add(ap);
                }
            }
        return namedParts;
    }
    /**
     * @return  look up and return product by name
     * */
    public static ObservableList<Product> lookupProduct(String name){
            ObservableList<Product> namedProducts = FXCollections.observableArrayList();
            for (Product ap : allProducts) {
                if (ap.getName().contains(name)) {
                    namedProducts.add(ap);
                }
            }
        return namedProducts;
    }
    /**
     * @param selectedPart
     * Update Part in the part list
     * */
    public static void updatePart(Part selectedPart){
        allParts.add(selectedPart);
    }
    /**
     * @param selectedProduct
     * update product in the product list
     * */
    public static void updateProduct(Product selectedProduct){
        allProducts.add(selectedProduct);
    }
    /**
     * @param selectedPart delete the selected part from the list
     * */
    public static boolean deletePart(Part selectedPart){
        return selectedPart != null;
    }
    /**
     * @param selectedProduct delete the selected product from the list
     * */
    public static boolean deleteProduct(Product selectedProduct){
        return selectedProduct != null;
    }
    /**
     * @return allParts list
     * */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    /**
     * @return allProducts list
     * */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }


}
