package task_8;

/** Represents a Invoice.
 * @author Kyle van der Merwe
 * @version 3.0
 * @since 1.0
*/
public class Invoice {	
	
	private int invoiceID;
	private int customerID;
	private double feeDue;	
    /** Creates a Invoice with the specified details.
     * @param invoiceID The invoice's ID
     * @param customerID The invoice's linked customer
     * @param feeDue The invoice's fee still payable amount     * 
    */ 
	public Invoice(int invoiceID,
					int customerID,
					double feeDue) {
	 
		 this.invoiceID = invoiceID;
		 this.customerID = customerID;
		 this.feeDue = feeDue;
 
	}
	/**Returns a string representation of this project. 
	   *  Format suitable for saving and viewing data to a text file.
	   * @return A string containing a representation of this object
	   */
	public String toString() {
		String thisInvoice = "Invoice Number : " + invoiceID;
		thisInvoice += "\n\nCustomer Details \n\n"+ customerID;
		thisInvoice += "\n\nAmount to Pay : R"+ feeDue;
		return thisInvoice;
	}
	/** Gets the invoice's ID.
	  * @return An integer representing this invoice's ID
	*/
	public int getInvoiceID() {
	      return invoiceID;
	}
	/** Gets the invoice's linked customer.
	  * @return A integer representing this invoice's linked customer
	*/
	public int getCustomerID() {
	      return customerID;
	}
	/** Gets the invoice's fee due amount.
	  * @return A double representing this invoice's total fee amount still due
	*/
	public double getFeeDue() {
	      return feeDue;
	}
	/** Sets the invoice's ID.
	  * @param invoiceID An integer containing this invoice's ID
	*/
	public void setInvoiceID(int invoiceID) {
		   this.invoiceID = invoiceID;
	}
	/** Sets the invoice's linked customer.
	  * @param customerID An integer representing this invoice's linked customer
	*/
	public void setCustomerID(int customerID) {
		   this.customerID = customerID;
	}
	/** Sets the invoice's fee due amount.
	  * @param feeDue A double representing this invoice's total fee amount still due
	*/
	public void setFeeDue(double feeDue) {
		   this.feeDue = feeDue;
	}
}
