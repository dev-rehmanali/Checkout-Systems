package checkout;

public class Product {
	private int productCode;
	private String productName;
	private double productPrice;
	public Product(int productCode, String productName, double productPrice) {
		super();
		this.productCode = productCode;
		this.productName = productName;
		this.productPrice = productPrice;
	}
	public int getProductCode() {
		return productCode;
	}
	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	
	public String toString() {
		return "Code: "+this.productCode+"\tName: "+this.productName+"\tPrice: "+this.productPrice;
	}
}
