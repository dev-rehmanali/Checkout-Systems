package checkout;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The main class where all GUIs or FXML files will be loaded
 * @author Ali Raza
 *
 */
public class CheckoutDriver extends Application{

	/**
	 * The main stage of the application
	 */
	private Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Start the application. The first main GUI named checkout.fxml 
	 * will be loaded. Get controller and set the checkout driver for 
	 * invoking the methods. 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("checkout.fxml"));
		try {
			VBox root = (VBox)loader.load();
			CheckoutController checkoutController = loader.getController();
			checkoutController.setDriver(this);
		
			Scene scene = new Scene(root,800,600);
			
			this.primaryStage.setTitle("Checkout System");
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
			
		} catch (Exception e) {
			System.out.println("checkout.fxml not loaded: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Load the invoice GUI and set invoice stage, 
	 * cart product list, total price of cart added products 
	 * to invoice GUI's controller
	 * @param cartProductList The list of products that are added in the cart for checkout
	 * @param totalPrice The total price of the added products in the cart
	 */
	public void loadInvoice(ObservableList<Product> cartProductList, String totalPrice) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("invoice.fxml"));
		try {
			VBox root = (VBox)loader.load();
			
			Scene scene = new Scene(root,800,600);
			Stage invoiceStage = new Stage();
			
			InvoiceController invoiceController = loader.getController();
			invoiceController.setInvoiceStage(invoiceStage);
			invoiceController.setInvoiceProductList(cartProductList);
			invoiceController.setTotalPrice(totalPrice);
			
            invoiceStage.initOwner(primaryStage);
            invoiceStage.initModality(Modality.WINDOW_MODAL);
			invoiceStage.setTitle("Checkout System");
			invoiceStage.setScene(scene);
			invoiceStage.show();
			
		} catch (Exception e) {
			System.out.println("invoice.fxml not loaded: "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Load the payment GUI and set payment stage, 
	 * cart product list, total price of cart added products 
	 * to payment GUI's controller
	 * @param paymentProductList List of the products for those payments to be paid
	 * @param totalPrice total price of the products for those payment to be paid
	 */
	public void loadPayment(ObservableList<Product> paymentProductList, String totalPrice) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("payment.fxml"));
		try {
			VBox root = (VBox)loader.load();
			
			Scene scene = new Scene(root,800,600);
			Stage paymentStage = new Stage();
			
			PaymentController paymentController = loader.getController();
			paymentController.setPaymentStage(paymentStage);
			paymentController.setInvoiceProductList(paymentProductList);
			paymentController.setTotalPrice(totalPrice);
			
            paymentStage.initOwner(primaryStage);
            paymentStage.initModality(Modality.WINDOW_MODAL);
			paymentStage.setTitle("Payment");
			paymentStage.setScene(scene);
			paymentStage.show();
			
		} catch (Exception e) {
			System.out.println("payment.fxml not loaded: "+e.getMessage());
			e.printStackTrace();
		}
	}

}
