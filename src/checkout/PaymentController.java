package checkout;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * PaymentController class. This class handle GUI of the payment.
 * @author Ali Raza
 *
 */
public class PaymentController implements Initializable {

	/**
	 * All (@FXML) data members are the the references of the nodes
	 * in the payment.fxml
	 */
	@FXML private TableView<Product> paymentProductTableView;
	@FXML private TableColumn<Product, String> paymentProductNameTableColumn;
	@FXML private TableColumn<Product, Double> paymentProductPriceTableColumn;
	@FXML private Label totalPriceLabel;
	@FXML private Button proceedButton;
	
	private ObservableList<Product> paymentProductList;
	private Stage paymentStage;
	
	/**
	 * The initialize method will run after constructor automatically run.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//Invoice Products TableView
		paymentProductNameTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
		paymentProductPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("productPrice"));
		
	}

	/**
	 * Handle the payment button clicked action. If button
	 * clicked then it will close the payment GUI stage.
	 * @param event payment button click event
	 */
    @FXML
    void paymentButtonAction(ActionEvent event) {
    	this.paymentStage.close();
    }
    
    /**
     * Set product list and assign to tableview
     * @param paymentProductList Cart added product list
     */
	public void setInvoiceProductList(ObservableList<Product> paymentProductList) {
		this.paymentProductList = paymentProductList;
		refereshTables();
	}
	
	/**
	 * Set the updated payment product list to the payment tableview
	 */
	private void refereshTables() {
		this.paymentProductTableView.setItems(paymentProductList);
	}
	
	/**
	 * Set the payment stage
	 * @param paymentStage Payment stage 
	 */
	public void setPaymentStage(Stage paymentStage) {
		this.paymentStage = paymentStage;
	}
	
	/**
	 * Set total price 
	 * @param totalPrice The total price of the cart added products.
	 */
	public void setTotalPrice(String totalPrice) {
		this.totalPriceLabel.setText("\u20AC"+totalPrice);
	}
	
}
