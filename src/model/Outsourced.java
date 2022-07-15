package model;

/**
 * @author Josh Gautney
 * */

public class Outsourced extends Part{

    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
    }

    /**
     * @param companyName is the company name to set
     * */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     * @return the company name
     * */
    public String getCompanyName(){
        return  companyName;
    }
}
