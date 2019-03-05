package com.example.dbryuzgin.myapplication;

public class ProductsStatsHandler implements Comparable<ProductsStatsHandler> {

    private int id;
    private String name;
    private int salesCount;
    private double total;

    private ProductsStatsHandler(){
    }

    ProductsStatsHandler(int Id, String Name, int SalesCount, double Total) {
        this.id = Id;
        this.name = Name;
        this.salesCount = SalesCount;
        this.total = Total;
    }

    public int getId() {
        return id;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public double getTotal() {
        return total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(ProductsStatsHandler compareProductSales) {
        int compareSales=((ProductsStatsHandler)compareProductSales).getSalesCount();
        return compareSales-this.salesCount;
    }
}


