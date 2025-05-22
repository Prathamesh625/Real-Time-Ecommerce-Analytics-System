package org.example;

public class ProductSalesData {
    private String productId;
    private int salesCount;

    public ProductSalesData(String productId, int salesCount) {
        this.productId = productId;
        this.salesCount = salesCount;
    }

    public String getProductId() { return productId; }
    public int getSalesCount() { return salesCount; }

    @Override
    public String toString() {
        return "ProductSalesData{productId='" + productId + "', salesCount=" + salesCount + '}';
    }
}