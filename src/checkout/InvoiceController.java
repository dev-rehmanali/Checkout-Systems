package checkout;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * The InvoiceController Class. This class handle the actions on the
 * Invoice GUI
 * @author Ali Raza
 *
 */
public class InvoiceController implements Initializable{

	/**
	 * All (@FXML) data members are the the references of the nodes
	 * in the invoice.fxml
	 */
	@FXML private VBox invoicePrintNode;
	@FXML private TableView<Product> invoiceProductTableView;
	@FXML private TableColumn<Product, String> invoiceProductNameTableColumn;
	@FXML private TableColumn<Product, Double> invoiceProductPriceTableColumn;
	@FXML private Label totalPriceLabel;
	@FXML private Button okButton;

	private Stage invoiceStage;
	private ObservableList<Product> invoiceProductList;

	/**
	 * The initialize method will run after constructor automatically run.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//Invoice Products TableView
		invoiceProductNameTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
		invoiceProductPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("productPrice"));
		
	}

	/**
	 * Handle the Ok button click
	 * @param event OK button clicked event
	 */
	@FXML
    void okButtonAction(ActionEvent event) {
		printInvoice(invoicePrintNode);
    }
	
	/**
	 * Set the invoice product list
	 * @param invoiceProductList this list of products that will be in the invoice
	 */
	public void setInvoiceProductList(ObservableList<Product> invoiceProductList) {
		this.invoiceProductList = invoiceProductList;
		refereshTables();
	}

	/**
	 * Set the invoice Stage
	 * @param invoiceStage invoice Stage
	 */
	public void setInvoiceStage(Stage invoiceStage) {
		this.invoiceStage = invoiceStage;
	}
	
	/**
	 * Set the total price
	 * @param totalPrice total price of the products that are added in the cart
	 */
	public void setTotalPrice(String totalPrice) {
		this.totalPriceLabel.setText("\u20AC"+totalPrice);
	}
	
	/**
	 * Set the updated invoice product list to the invoice product tableview
	 */
	private void refereshTables() {
		this.invoiceProductTableView.setItems(invoiceProductList);
	}
	
	/**
	 * Print the Node with printer if the printer does not exist then
	 * can be save in PDF format
	 * @param node Node to be print 
	 */
    private void printInvoice(Node node) {
    	
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.getDefaultPageLayout();

        // Printable area
        double pWidth = pageLayout.getPrintableWidth();
        double pHeight = pageLayout.getPrintableHeight();

        // Node's (Image) dimensions
        double nWidth = node.getBoundsInParent().getWidth();
        double nHeight = node.getBoundsInParent().getHeight();

        // How much space is left? Or is the image to big?
        double widthLeft = pWidth - nWidth;
        double heightLeft = pHeight - nHeight;

        // scale the image to fit the page in width, height or both
        double scale;

        if (widthLeft < heightLeft) scale = pWidth / nWidth;
        else scale = pHeight / nHeight;

        // preserve ratio (both values are the same)
        node.getTransforms().add(new Scale(scale, scale));

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                System.out.println("PRINTING FINISHED");
                job.endJob();
                this.invoiceStage.close();
            }
        }
    }
}
