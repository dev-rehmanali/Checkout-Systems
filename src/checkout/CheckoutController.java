package checkout;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * CheckoutController class
 * This class handle the all actions of Checkout GUI
 * @author Ali Raza
 *
 */
public class CheckoutController implements Initializable {

	/**
	 * All (@FXML) data members are the the references of the nodes
	 * in the checkout.fxml
	 */
	@FXML private TextField searchTextField;
	@FXML private TableView<Product> productTableView;
	@FXML private TableColumn<Product, Integer> productCodeTableColumn;
	@FXML private TableColumn<Product, String> productNameTableColumn;
	@FXML private TableColumn<Product, Double> productPriceTableColumn;
	@FXML private Button addToCartButton;

	@FXML private Label totalPriceLabel;
	@FXML private TableView<Product> cartProductTableView;
	@FXML private TableColumn<Product, Integer> cartProductCodeTableColumn;
	@FXML private TableColumn<Product, String> cartProductNameTableColumn;
	@FXML private TableColumn<Product, Double> cartProductPriceTableColumn;
	@FXML private Button removeButton;
	@FXML private Button printButton;
	@FXML private Button paymentButton;

	/**
	 * The product list is the all products and the cart product list is the list of all
	 * items that are added in the cart
	 */
	private ObservableList<Product> productList = FXCollections.observableArrayList();
	private ObservableList<Product> cartProductList = FXCollections.observableArrayList();

	private CheckoutDriver driver;

	/**
	 * The initialize method will run after constructor automatically run.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeProducts();

		/**
		 * Settings for product table to accept and view the products list
		 */
		//Product TableView
		productTableView.setPlaceholder(new Label("No item in the product table"));
		productCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productCode"));
		productNameTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
		productPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("productPrice"));

		/**
		 * Settings for cart product table to accept and view the products list
		 * that are added in the cart
		 */
		//Cart TableView
		cartProductTableView.setPlaceholder(new Label("No product in the cart table"));
		cartProductCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productCode"));
		cartProductNameTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
		cartProductPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("productPrice"));
		cartProductTableView.setItems(cartProductList);

		//setValuesToTables
		referesh();

		/**
		 * Hanlde the search textfield whenever anything typed in it.
		 */
		//Search
		searchTextField.textProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable o) {
				productTableView.setItems(lookupProduct(searchTextField.getText()));
			}
		});
	}

	/**
	 * Handle AddToCartButton click. If the clicked without selecting
	 * an item in the product table then it show error alert. If the item 
	 * is selected then it will added in the cart product list. if the item
	 * is already in the cart then it will show alert.
	 * @param event AddToCart button clicked event
	 */
	@FXML
	void addToCartButtonAction(ActionEvent event) {
		Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
		if (selectedProduct!=null) {
			if (!cartProductList.contains(selectedProduct)) {
				cartProductList.add(selectedProduct);
				referesh();
			}else {
				ShowAlert("Already Exist", "Product cannot be added because its already exist int he cart.");
			}
		}else {
			ShowAlert("Nothing to Add","Please select the product");
		}
	}

	@FXML
	void paymentButtonAction(ActionEvent event) {
		if (cartProductList.size()>4) {
			this.driver.loadPayment(cartProductList,String.valueOf(totalPriceOfCartProductList()));
		}else {
			ShowAlert("Items to checkout", "The payment should be of atleast 5 products");
		}
	}
	
	/**
	 * Handle the Print button action. If the button clicked and no
	 * item in the cart product list then it will show empty alert otherwise
	 * show the item in the invoice stage. The cart product list and the 
	 * total price of the items in the cart are sent to the invoice GUI to display 
	 * the items and total price. 
	 * @param event Print button event
	 */
	@FXML
	void printButtonAction(ActionEvent event) {
		if (cartProductList.size()>1) {
			this.driver.loadInvoice(cartProductList,String.valueOf(totalPriceOfCartProductList()));
		}else {
			ShowAlert("Items to checkout", "Cart should have atleast 2 product in the cart. Please add the items in the cart");
		}
	}

	/**
	 * Remove button Handle. If the item is not selected from the cart
	 * table then it will show an error alert otherwise remove the selected item
	 * the cart product list.
	 * @param event Remove button clicked event
	 */
	@FXML
	void removeButtonAction(ActionEvent event) {
		Product selectedProduct = cartProductTableView.getSelectionModel().getSelectedItem();
		if (selectedProduct!=null) {
			cartProductList.remove(selectedProduct);
			referesh();
		}else {
			ShowAlert("Nothing to remove","Please select the product");
		}
	}

	/**
	 * This is the template of the error alert. This template is used 
	 * to show the alert with the passed parameters.
	 * @param header The header text of the alert
	 * @param content The content text of the alert
	 */
	private void ShowAlert(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

	/**
	 * This method is used set the updated value. If any changes happened
	 * then this method is used to set the updated values. For example if any item
	 * is removed or add in the cart product list then this method again set the 
	 * updated values the tableview.
	 */
	private void referesh() {
		productTableView.setItems(productList);
		cartProductTableView.setItems(cartProductList);
		totalPriceLabel.setText(String.valueOf("\u20AC"+totalPriceOfCartProductList()));
	}
	
	/**
	 * This method is used to add the products in the product list.
	 */
	private void initializeProducts() {
		//10 products to add in list
		this.productList.add(new Product(123, "Bike", 103.90));
		this.productList.add(new Product(124, "Car", 1039.90));
		this.productList.add(new Product(125, "Truck", 123.90));
		this.productList.add(new Product(126, "Van", 23.90));
		this.productList.add(new Product(127, "Tructor", 54.90));
		this.productList.add(new Product(128, "Bus", 53.90));
		this.productList.add(new Product(129, "Train", 71.90));
		this.productList.add(new Product(130, "Bicycle", 43.90));
		this.productList.add(new Product(131, "Jeep", 41.90));
		this.productList.add(new Product(132, "Riksha", 21.90));
	}

	/**
	 * This method is used to search the items in the product list by
	 * product code. if the passed code is exist in the product's code then
	 * it will return that products list. Its just like a live search of the 
	 * product codes.
	 * @param productCode The code search in the product list
	 * @return
	 */
	public ObservableList<Product> lookupProduct(String productCode){
		FilteredList<Product> filteredProductList = new FilteredList(productList);
		filteredProductList.filtered(product-> product instanceof Product);
		filteredProductList.setPredicate(product->{

			// If filter text is empty, display all products.
			if (productCode.isEmpty()) {
				return true;
			}
			if (String.valueOf(product.getProductCode()).contains(productCode)) {
				return true; // Filter matches name.
			}
			return false; // Does not match.

		});

		return FXCollections.observableArrayList(filteredProductList);
	}//lookupProduct

	/**
	 * This method calculate and return the total price of the products that are
	 * added in the cart.
	 * @return Total price of the cart added products.
	 */
	private double totalPriceOfCartProductList() {
		double price = 0.0;
		if (!cartProductList.isEmpty()) {
			for (Product product : cartProductList) {
				price+=product.getProductPrice();
			}
		}
		/**
		 * Format the decimal upto 2 decimal point
		 */
		DecimalFormat dFormat = new DecimalFormat("#.##");
		dFormat.setRoundingMode(RoundingMode.DOWN);
		System.out.println("total price: "+Double.valueOf(dFormat.format(price)));
		return Double.valueOf(dFormat.format(price));
	}
	
	/**
	 * This method is used to set the reference of the CheckoutDriver object.
	 * After setting the driver method, we can easily call the methods of the 
	 * checkout deriver for example load the GUIs.
	 * @param driver
	 */
	public void setDriver(CheckoutDriver driver) {
		this.driver = driver;
	}

}//CheckoutController
