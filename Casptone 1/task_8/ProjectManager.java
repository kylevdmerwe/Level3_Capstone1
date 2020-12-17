package task_8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.*;
/**Main Project Manager Program.
 * @author Kyle van der Merwe
 * @version 3.0
 * @since 1.0
*/
public class ProjectManager {
	
	// Create Arrays of the classes to store. 
	// These arrays to be stored in database.
	static Personel[] personel = new Personel[0];
	static List<Project> projectList = new ArrayList<>();
	static Invoice[] invoices = new Invoice[0];
	/**
	 * This is the main method for the Project Manager Program. 
	 * Loop through a menu until exited by user 
	 * @param args Unused.
	 */
	public static void main(String[] args) {
		
		try {
			// Connect to the ebookstore database, via the jdbc:mysql: channel on localhost (this PC)
			// Use username "otheruser", password "swordfish".
			String mysqlUrl = "jdbc:mysql://localhost:3306/poisepms?useSSL=false";
			Connection connection = DriverManager.getConnection(mysqlUrl, "otheruser", "swordfish");
			System.out.println("Connection Established...");
			// Create a direct line to the database for running our queries
			Statement statement = connection.createStatement();
			ResultSet results;
			int rowsAffected;
			
		
		
			// Main Menu printing, creating scanner and using a switch case. 
			// Store the Menu options in an array
			Scanner scanIn = new Scanner(System.in);
			String[] arrMenuItem = {"Create new Personel",
									"Create new Project",
									"Update existing Project",
									"Load Projects from file",
									"Projects to be completed",
									"Projects that are past due date",
									"Find a project",
									"Finalise a Project",
									"Quit"};
			
	
	        // print menu
			// handle user commands
	        boolean quit = false;
	        
	        int menuItem;
	        retrieveProjects(statement);
	        retrievePersonel(statement);
	        // Do while loop to print Menu, receive users inputs and call relevant methods
	        do {
	        	try {
	        		
	        		for (int i = 0; i < arrMenuItem.length; i++)
	           		 System.out.println((i+1) + ". " + arrMenuItem[i]);           	
	                 System.out.println("Choose menu item: ");
	
	             	 menuItem = scanIn.nextInt();
	             	 int projectID = -1;
	                 switch (menuItem) {
	
	                 case 1:
	                	 createNewPersonel(scanIn);
	                	 savePersonel(statement);
		               	 saveProjects(statement);
		               	 saveInvoices(statement);
	                	 break;
	
	                 case 2:
	                	 createNewProject(scanIn);
	                	 break;
	
	                 case 3:  
	                	projectID = selectProject(scanIn);
		               	updateExistingProject(scanIn, projectID);
		               	savePersonel(statement);
		               	saveProjects(statement);
		               	saveInvoices(statement);
		               	break;
	
	                 case 4:
		               	if(retrieveProjects(statement))
		               		System.out.println("Project Loaded from File");
		               	else
		               		System.out.println("Error: Project Not Loaded");
		                 	if(retrievePersonel(statement))
		               		System.out.println("Personel Loaded from File");
		               	else
		               		System.out.println("Error: Personel Not Loaded");
	                 	break;
	             	  	
	
	                 case 5:
	                 	if(printUnCompletedProjects(scanIn)) {
	                 		
	                 	}
	                 	break;
	                 case 6:
	                 	if(printLateProjects(scanIn)) {
	                 		
	                 	}
	                 	break;
	                 case 7:																																																																																																										scanIn.nextLine();
		               	boolean valid = false;
		                   String strChoice;
		               	do {
		               		System.out.println("Would you like to search Project Id or Project Name? ( I / N)");
		               		strChoice = scanIn.nextLine().toLowerCase();
		               		if(strChoice.equals("i")){
		           		        valid = false;
		           	        	do {
		           	        		try {
		           	        			System.out.println("What is the Project ID?");
		           	        			int intChoice = scanIn.nextInt();
		           	        			scanIn.nextLine();
		           	        			if (findProjectWithID(intChoice))
			                        		System.out.println("Project found");
			                        	else
			                        		System.out.println("Project not found");
			                        	valid = true;
									} catch (Exception e) {
										System.out.println("Input is not a valid Int"); 
									}
		                        	
		           	  	        }while(!valid);
		               		}else if(strChoice.equals("n")) {
		               			valid = false;
		           	        	do {
		           		        	System.out.println("What is the Project Name?");
		           					try {  					
		           		  		        strChoice = scanIn.nextLine();
		           		  		        if (findProjectWithName(strChoice))
		           		  		        	System.out.println("Project found");
		           		  		        else
	           		  		        		System.out.println("Project not found");
		           		  		        valid = true;
		           					} catch (Exception e) {
		           						       		
		           					}
		           	  	        }while(!valid);      			
		               		}else {
		               			valid = false;
		               			System.out.println("Invalid Selection");
		               		}
		               	}while(!valid);
		               	break;
		               		
	                 case 8:
		               	scanIn.nextLine();
		               	finaliseProject(scanIn);
		               	savePersonel(statement);
		               	saveProjects(statement);
		               	saveInvoices(statement);
		                   break;
		                     
	                 case 9:
		               	savePersonel(statement);
		               	saveProjects(statement);
		               	saveInvoices(statement);
		                   quit = true;
		                   break;
	
	                 default:
		               	System.out.println("Invalid choice.");
		               	scanIn.nextLine();
	
	                 }
					
				} catch (InputMismatchException e) {
					System.out.println("\nInvalid election, Choose an Integer.\n");
					scanIn.nextLine();
				}
	    	  
	
	        } while (!quit);
	        
	        scanIn.close();
	
	        System.out.println("Bye-bye!");
		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			e.printStackTrace();
		}

	}
	/**Returns a boolean result for a print personel method
	 * Prints the specific type of personel required
	 * Prints personelID and personel name
	 * @param scanIn A scanner to allow reading input from console
	 * @param personelType A String containing the search parameter
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean printPersonel(Scanner scanIn, String personelType) {
		
		System.out.println("Who is the project " + personelType + "?" );
		if ((personel.length==0)) {
    		System.out.println("No recorded Personel yet.");
    		return false;
    	}
    	
    	
    	boolean present = false;
    	for(int i = 0; i < personel.length; i++) {
			
    		if(personel[i].getPersonelType().equals(personelType)) {
				present = true;
				System.out.println("Personel ID : " + personel[i].getPersonelID());
    			System.out.println("Personel Name : " + personel[i].getFirstName() + "\n");
			
    		}
    		
    	}
    	if(!present) {
			System.out.println("No recorded " + personelType + " yet.");
    		return false;
		}
		return true;
		
	}
	/**Returns a boolean result for a print projects method
	 * Prints all projects
	 * Prints projectID and project name
	 * @param scanIn A scanner to allow reading input from console
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean printProjects(Scanner scanIn) {
		if ((projectList.size()==0)) {
    		System.out.println("No recorded Projects Yet.");
    		return false;
    	}
    	for (Project p: projectList) {
      		System.out.println("Project ID : " + p.getProjectID() + "\n" + "Project Name : " + p.getProjectName() + "\n");
      	}
    	return true;
	}
	/**Returns a boolean result for a print uncompleted projects method
	 * Prints all uncompleted projects
	 * Prints projectID and project name
	 * @param scanIn A scanner to allow reading input from console
	 * @return A boolean indicating success or failure of this method
	 */	
	public static boolean printUnCompletedProjects(Scanner scanIn) {
		boolean present = false;
		if ((projectList.size()==0)) {
    		System.out.println("No recorded Projects Yet.");
    		return present;
    	}
    	for (Project p: projectList) {
    		if (!(p.getProjectStatus().equals("Finalised"))) {
    			present = true;
    			System.out.println("Project ID : " + p.getProjectID() + "\n" + "Project Name : " + p.getProjectName() + "\n");
    		}
      			
      	}
    	if (present)
    		return present;
    	else {
    		System.out.println("No Completed Projects Yet.");
    		return present;
    	}
	}
	/**Returns a boolean result for a print late projects method
	 * Prints all overdue projects
	 * Prints projectID and project name
	 * @param scanIn A scanner to allow reading input from console
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean printLateProjects(Scanner scanIn) {
		boolean present = false;
		if ((projectList.size()==0)) {
    		System.out.println("No recorded Projects Yet.");
    		return present;
    	}
    	for (Project p: projectList) {
    		if (p.getProjectDeadline()<Year.now().getValue()) {
    			present = true;
    			System.out.println("Project ID : " + p.getProjectID() + "\n" + "Project Name : " + p.getProjectName() + "\n");
    		}
      			
      	}
    	if (present)
    		return present;
    	else {
    		System.out.println("No Late Projects Yet.");
    		return present;
    	}
	}
	/**Returns a boolean result for a find projects method
	 * Finds a specific project, prints out that projects details
	 * @param projectID An integer containing the projectID searched for
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean findProjectWithID(int projectID) {
		boolean present = false;
		if ((projectList.size()==0)) {
    		System.out.println("No recorded Projects Yet.");
    		return present;
    	}
    	for (Project p: projectList) {
    		if (p.getProjectID() == projectID) {
    			present = true;
    			System.out.println(p.toStringForDisplay());
    		}
      			
      	}
    	if (present)
    		return present;
    	else {
    		System.out.println("Project not found.");
    		return present;
    	}
	}
	/**Returns a boolean result for a find projects method
	 * Finds a specific project, prints out that projects details
	 * @param projectName A string containing the project name searched for
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean findProjectWithName(String projectName) {
		boolean present = false;
		if ((projectList.size()==0)) {
    		System.out.println("No recorded Projects Yet.");
    		return present;
    	}
    	for (Project p: projectList) {
    		if (p.getProjectName().equals(projectName)) {
    			present = true;
    			System.out.println(p.toStringForDisplay());
    		}
      			
      	}
    	if (present)
    		return present;
    	else {
    		System.out.println("Project not found.");
    		return present;
    	}
	}
	/**Creates a new personel object in the current project manager
	 * Method interacts with user, requesting detail needed for new personel creation
	 * Adds the new personel to the personel array
	 * @param scanIn A scanner to allow reading input from console
	 */
	public static void createNewPersonel(Scanner scanIn) {
		String personelType  = "";
	    int personelID = 0;
	    String firstName = "";
	    String surname = "";
	    String email = "";
	    String personelAddress = "";
	    String personelTelephone = "";
		
	    personel = Arrays.copyOf(personel, personel.length + 1);
        personelID = personel.length;
        boolean valid = false;
        int menuItem2;
        System.out.println("Creating new Personel");
        do {
			System.out.println("Please enter Personel Type.");
			System.out.println("1: Customer");
			System.out.println("2: Contractor");
			System.out.println("3: Architect");
			try {
				menuItem2 = scanIn.nextInt();
				scanIn.nextLine();
				switch (menuItem2) {
		    		case 1:		
		    			personelType = "Customer";
		    			valid = true;
		                break;
		            case 2:
		            	personelType = "Contractor";
		            	valid = true;
		                break;
		            case 3:
		            	personelType = "Architect";
		            	valid = true;
		                break;
		            default:
		                System.out.println("Invalid choice");
				}
				
			} catch (InputMismatchException e) {
				valid = false;
				System.out.println("Input is not a valid Integer"); 
				scanIn.nextLine();
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter First Name of " +personelType+ ".");
			try {
				firstName = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");        		
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Surname of " +personelType+ ".");
			try {
				surname = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");        		
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Email Address of " +personelType+ ".");
			try {
				email = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");        		
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Physical Address of "+personelType+ ".");
			try {
				personelAddress = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");        		
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Telephone Number of "+personelType+ ".");
			try {
				personelTelephone = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");        		
			}
        }while(!valid);
        
        
        personel[personel.length-1] = new Personel( personelID, personelType, firstName, surname, email, personelAddress, personelTelephone);
        
        
        
	}
	/**Creates a new project object in the current project manager
	 * Method interacts with user, requesting detail needed for new project creation
	 * Adds the new project to the project list
	 * @param scanIn A scanner to allow reading input from console
	 */
	public static void createNewProject(Scanner scanIn) {
		int projectID = 0;
		String projectName = "";
		String buildingType = "";
		String projectAddress = "";
		String ERF = "";
		double projectFee = 0;
		int projectDeadline = 0;
		Personel architect = null;
		Personel contractor = null;
		Personel customer = null;
		projectID = projectList.size()+1000;
		boolean valid = false;
        
        System.out.println("Creating new Project");
        do {
			System.out.println("Please enter Project Name.");
			try {
				projectName = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");  
				scanIn.nextLine();
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Building Type.");
			try {
				buildingType = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");
				scanIn.nextLine();
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Project Address.");
			try {
				projectAddress = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String"); 
				scanIn.nextLine();
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Project ERF Number.");
			try {
				ERF = scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid String");
				scanIn.nextLine();
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Project Fee.");
			try {
				projectFee = scanIn.nextDouble();
				scanIn.nextLine();
				valid = true;
			} catch (Exception e) {
				System.out.println("Input is not a valid Double");  
				scanIn.nextLine();
			}
        }while(!valid); 
        valid = false;
        do {
			System.out.println("Please enter Project Due Date (Year).");
			try {
				projectDeadline = scanIn.nextInt();
				scanIn.nextLine();
				if(projectDeadline <= Year.now().getValue())
					valid = true;
				else {
					System.out.println("Year is before current Date");
					
				}
			} catch (Exception e) {
				System.out.println("Input is not a valid Integer");
				scanIn.nextLine();
			}
        }while(!valid); 
        valid = false;
        do {
			
			if(printPersonel(scanIn, "Architect")) {
				
			}
			System.out.println("Please enter the Personel ID of the Architect, or for a new architect enter 0..");
			
			try {
				int intChoice = scanIn.nextInt();
				scanIn.nextLine();
				if(intChoice == 0) {
					createNewPersonel(scanIn);
					architect = personel[personel.length-1];
				} else {
					architect = personel[intChoice -1];
				}
				valid = true;
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid Selection"); 
				scanIn.nextLine();
				valid = false;
			}
			
        }while(!valid);
        valid = false;
        do {
        	if(printPersonel(scanIn, "Contractor")) {
        		
        	}
			
			System.out.println("Please enter the Personel ID of the Contract, or for a new contractor enter 0..");
			
			try {
				int intChoice = scanIn.nextInt();
				scanIn.nextLine();
				if(intChoice == 0) {
					createNewPersonel(scanIn);
					contractor = personel[personel.length-1];
				} else {
					contractor = personel[intChoice -1];
				}
				valid = true;
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid Selection");
				scanIn.nextLine();
				valid = false;
			}
        }while(!valid); 
        valid = false;
        do {
			
			if(printPersonel(scanIn, "Customer")) {
				
			}
			
			System.out.println("Please enter the Personel ID of the Customer, or for a new customer enter 0..");
			
			try {
				int intChoice = scanIn.nextInt();
				scanIn.nextLine();
				if(intChoice == 0) {
					createNewPersonel(scanIn);
					customer = personel[personel.length-1];
				} else {
					customer = personel[intChoice -1];
				}
				valid = true;
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid Selection");  
				scanIn.nextLine();
				valid = false;
			}
        }while(!valid);
        projectList.add(new Project(projectID, projectName, buildingType, projectAddress, ERF, projectFee, 0, projectDeadline, 0, architect.getPersonelID(), contractor.getPersonelID(), customer.getPersonelID(), "Active"));
        
	}
	/**Finalizes a project
	 * Method finalizes the requested project 
	 * Invoicing customer if required, sets completed date
	 * Saves invoice to text file if generated
	 * @param scanIn A scanner to allow reading input from console
	 */
	public static void finaliseProject(Scanner scanIn) {		
		
        int projectChoice;

        Project workingProject;
        if(printProjects(scanIn)) {
	    	System.out.println("Which Project are you Finalising? ");
	
	    	projectChoice = scanIn.nextInt() - 1001;
	    	workingProject = projectList.get(projectChoice);
	        scanIn.nextLine();
	        if(workingProject.getProjectFee() == workingProject.getPaidToDate()) {
	        	workingProject.setProjectStatus("Finalised");
		    	workingProject.setProjectDateCompleted(Year.now().getValue());
	        } else {
	        	int intInvoiceID;
	        	int customerID;
	        	double dblFeeDue;
	        	invoices = Arrays.copyOf(invoices, invoices.length + 1);
	        	intInvoiceID = invoices.length;
	        	customerID = workingProject.getCustomerID();
	        	dblFeeDue = workingProject.getProjectFee() - workingProject.getPaidToDate();
	        	invoices[invoices.length - 1] = new Invoice(intInvoiceID, customerID, dblFeeDue);
	           	workingProject.setProjectStatus("Finalised");
	        	workingProject.setProjectDateCompleted(Year.now().getValue());
	        	projectList.set(projectChoice, workingProject);
	        }
	        
        }else return;
        
	}
	/**Returns a boolean result from a retrieve projects method
	 * Loads all projects from the poisepms database
	 * Saves the retrieved projects in a project list
	 * @param statement A statement allowing queries to database
	 * @return A boolean indicating success or failure of this method
	 * @throws SQLException on SQL database query failure
	 */
	public static boolean retrieveProjects(Statement statement) throws SQLException {
		boolean success = false;
		Project tempProject;
		
		ResultSet results = statement.executeQuery("SELECT * FROM project");
		while (results.next()) {
			tempProject = new Project(
					results.getInt("projectID"), 
					results.getString("projectName"), 
					results.getString("buildingType"),  
					results.getString("projectAddress"),  
					results.getString("ERF"),
					results.getDouble("projectFee"),
					results.getDouble("paidToDate"),
					results.getInt("projectDeadline"),
					results.getInt("projectDateCompleted"),
					results.getInt("architectID"),
					results.getInt("contractorID"),
					results.getInt("customerID"),
					results.getString("projectStatus"));
			projectList.add(tempProject);
		}
		System.out.println("Project File loaded");
		success = true;
		return success;
	}
	/**Returns a boolean result from a retrieve personel method
	 * Loads all personel from database poisepms
	 * Saves the retrieved personel in a personel array
	 * @param statement A statement allowing query of the database
	 * @return A boolean indicating success or failure of this method
	 * @throws SQLException when SQL error
	 */
	public static boolean retrievePersonel(Statement statement) throws SQLException {
		
		personel = new Personel[0]; 
		boolean success = false;
		
		ResultSet results = statement.executeQuery("SELECT * FROM personel");
		while (results.next()) {

			personel = Arrays.copyOf(personel, personel.length + 1);
			personel[results.getInt("personelID")-1] = new Personel(
					results.getInt("personelID"), 
					results.getString("personelType"), 
					results.getString("firstName"),  
					results.getString("surname"),   
					results.getString("email"),  
					results.getString("personelAddress"),
					results.getString("personelTelephone"));
		}
		System.out.println("Personel File loaded");
		success = true;
		return success;
	}
	/**Returns a boolean result from a retrieve invoice method
	 * Loads all invoices from database poisepms
	 * Saves the retrieved invoices in a invoices array
	 * @param statement A statement allowing query of the database
	 * @return A boolean indicating success or failure of this method
	 * @throws SQLException when SQL error
	 */
	public static boolean retrieveInvoices(Statement statement) throws SQLException {
		
		invoices = new Invoice[0]; 
		boolean success = false;
		
		ResultSet results = statement.executeQuery("SELECT * FROM personel");
		while (results.next()) {

			invoices = Arrays.copyOf(invoices, invoices.length + 1);
			invoices[results.getInt("invoiceID")-1] = new Invoice(
					results.getInt("invoiceID"),
					results.getInt("personeID"),
					results.getDouble("feeDue") );
		}
		System.out.println("Personel File loaded");
		success = true;
		return success;
	}
	/**Returns a boolean result from a save invoice method
	 * Saves all invoices to database, Overriding if duplicates
	 * @param statement A statement allowing query of the database
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean saveInvoices(Statement statement) {
		int rowsAffected;
		boolean success = false;
		//Creates a new table. 
		try {
			rowsAffected = statement.executeUpdate("create table if not exists invoice (invoiceID int, customerID int, feeDue double, primary key (invoiceID));");
			for (int i = 0; i < invoices.length; i++) {
				rowsAffected = statement.executeUpdate("REPLACE into invoice values (" 
								+invoices[i].getInvoiceID() + ", "
								+invoices[i].getCustomerID() + ", "
								+invoices[i].getFeeDue()+ ");");
			}
			success = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
		
	}
	
	/**Returns a boolean result from a save all projects method
	 * Saves all projects database, overriding duplicate entries. (Assumes latest data in program)
	 * @param statement A statement allowing query of the database
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean saveProjects(Statement statement) {
		int rowsAffected;
		boolean success = false;
		//Creates a new table. 
		try {
			rowsAffected = statement.executeUpdate("create table if not exists project (projectID int, " + 
					"projectName varchar(50), " + 
					"buildingType varchar(50), " + 
					"projectAddress varchar(250), " + 
					"ERF varchar(20), " + 
					"projectFee double, " + 
					"paidToDate double, " + 
					"projectDeadline int(4), " + 
					"projectDateCompleted int(4), " + 
					"architectID int, " + 
					"contractorID int, " + 
					"customerID int, " + 
					"projectStatus varchar (15), " + 
					"primary key (projectID));");
			for (Project p: projectList) {
				rowsAffected = statement.executeUpdate("REPLACE into project values (" 
								+p.getProjectID() + ", \'"								 
								+p.getProjectName() + "\', \'"								 
								+p.getBuildingType() + "\', \'"							 
								+p.getProjectAddress() + "\', \'"
								+p.getERF() + "\', "
								+p.getProjectFee() + ", "
								+p.getPaidToDate() + ", "
								+p.getProjectDeadline() + ", "
								+p.getProjectDateCompleted() + ", "
								+p.getArchitectID() + ", "
								+p.getContractorID() + ", "
								+p.getCustomerID() + ", \'"
								+p.getProjectStatus() + "\');");
			}
			success = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
		
	}
	/**Returns a boolean result from a save all personel method
	 * Saves all personel to a text file
	 * @param statement A statement allowing query of the database
	 * @return A boolean indicating success or failure of this method
	 */
	public static boolean savePersonel(Statement statement) {
		int rowsAffected;
		boolean success = false;
		//Creates a new table. 
		try {
			rowsAffected = statement.executeUpdate("create table if not exists personel ( " + 
					"personelID int, " + 
					"personelType varchar(15), " + 
					"firstName varchar(50), " + 
					"surname varchar(50), " + 
					"email varchar(50), " + 
					"personelAddress varchar(250), " + 
					"personelTelephone varchar(12), " + 
					"primary key (personelID));");
			for (int i = 0; i < personel.length; i++) {
				rowsAffected = statement.executeUpdate("REPLACE into personel values (" 
						+personel[i].getPersonelID() + ", \'"
						+personel[i].getPersonelType() + "\', \'"
						+personel[i].getFirstName() + "\', \'"
						+personel[i].getSurname() + "\', \'"
						+personel[i].getEmail() + "\', \'"
						+personel[i].getPersonelAddress() + "\', \'"
						+personel[i].getPersonelTelephone() + "\');");
			}
			success = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
		
	}
	/**Method to update an existing projects details
	 * Loops a menu, allowing editing of specific project details
	 * @param scanIn A scanner to allow reading input from console
	 * @param ProjectID An integer containing the project to be updated
	 */
	public static void updateExistingProject(Scanner scanIn, int ProjectID) {
		
		String[] arrMenuItem = {"Update Name",
								"Update Building Type",
								"Update Address",
								"Update ERF",
								"Update Project Fee",
								"Update Fee Paid to Date",
								"Update Due Date",
								"Update Architect",
								"Update Contractor",
								"Update Customer",
								"Change project being Edited",
								"Main Menu"};
		boolean quit = false;
        int menuItem;
        Project tempProject = projectList.get(ProjectID-1001);
        
        // Do while loop to print Menu, receive users inputs and call relevant methods
        do {
        	
          System.out.println("Current Project Selected: ");
	      findProjectWithID(ProjectID);
    	  for (int i = 0; i < arrMenuItem.length; i++)
    		 System.out.println((i+1) + ". " + arrMenuItem[i]);
    	
          System.out.print("Choose menu item: ");
          menuItem = scanIn.nextInt();
          scanIn.nextLine();
          boolean valid = false;
          String strChoice = "";
          double dblChoice = 0;
          int intChoice = 0;
          switch (menuItem) {
          
	          case 1:
	        	
  		        do {
	  	        	System.out.print("What is the new Project Name for Project : " + tempProject.getProjectName() + " ?");
	  				try {  					
	  	  		        strChoice = scanIn.nextLine();         
	  	  		        valid = true;
	  				} catch (Exception e) {
	  					valid = false;
	  					System.out.println("Input is not a valid String");        		
	  				}
	  	        }while(!valid);	  	        
  	        	tempProject.setProjectName(strChoice);
  		        projectList.set(ProjectID-1001, tempProject);
  		        break;
	          	
	          case 2: 	        
		        do {
	  	        	System.out.print("What is the new Building Type for Project : " + tempProject.getProjectName() + " ?");
	  				try {  					
	  	  		        strChoice = scanIn.nextLine();         
	  	  		        valid = true;
	  				} catch (Exception e) {
	  					valid = false;
	  					System.out.println("Input is not a valid String");        		
	  				}
	  	        }while(!valid);
	  	        
	  	        tempProject.setBuildingType(strChoice);
		        projectList.set(ProjectID-1001, tempProject);
		        break;
	          case 3:
  		        do {
	  	        	System.out.println("What is the new Address for Project : " + tempProject.getProjectName() + " ?");
	  				try {  					
	  	  		        strChoice = scanIn.nextLine();         
	  	  		        valid = true;
	  				} catch (Exception e) {
	  					valid = false; 
	  					System.out.println("Input is not a valid String");        		
	  				}
  	        	}while(!valid);
	  	                        
  		        tempProject.setProjectAddress(strChoice);
  		        projectList.set(ProjectID-1001, tempProject);
  		        break;
	          case 4:
  		        do {
	  	        	System.out.println("What is the new ERF for Project : " + tempProject.getProjectName() + " ?");
	  				try {  					
	  	  		        strChoice = scanIn.nextLine();         
	  	  		        valid = true;
	  				} catch (Exception e) {
	  		  	        valid = false; 
	  					System.out.println("Input is not a valid String");        		
	  				}
	  	        }while(!valid);                
  		        tempProject.setERF(strChoice);
  		        projectList.set(ProjectID-1001, tempProject);
  		        break; 	
	          case 5:
	        	tempProject = projectList.get(menuItem);
  		        do {
	  	        	System.out.println("What is the new Project Fee for Project : " + tempProject.getProjectName() + " ?");
	  				try {
	  	  	        	dblChoice = scanIn.nextDouble();          
	  	  		        valid = true;
	  				} catch (Exception e) {
	  		  	        valid = false; 
	  					System.out.println("Input is not a valid Double");   
	  					scanIn.nextLine();
	  				}
	  	        }while(!valid);                  
  		        tempProject.setProjectFee(dblChoice);
  		        projectList.set(ProjectID-1001, tempProject);
  		        break;
	          case 6:
  		        do {
	  	        	System.out.println("What is the new Fee Paid to Date for Project : " + tempProject.getProjectName() + " ?");
	  				try {
	  	  	        	dblChoice = scanIn.nextDouble();          
	  	  		        valid = true;
	  				} catch (Exception e) {
	  		  	        valid = false; 
	  					System.out.println("Input is not a valid Double");   
	  					scanIn.nextLine();
	  				}
	  	        }while(!valid);                    
  		        tempProject.setPaidToDate(dblChoice);
  		        projectList.set(ProjectID-1001, tempProject);
  		        break;              
	          case 7:  
  		        do {
	  	        	System.out.println("What is the new Due Date for Project : " + tempProject.getProjectName() + " ?");
	  				try {  					
	  	  		        intChoice = scanIn.nextInt();
		  	  		    if(intChoice >= Year.now().getValue())
							valid = true;
						else {
						System.out.println("Year is before current Date");
					}
	  	  		        valid = true;
	  				} catch (Exception e) {
	  		  	        valid = false; 
	  					System.out.println("Input is not a valid Int");
	  					scanIn.nextLine();
	  				}
	  	        }while(!valid);                  
  		        tempProject.setProjectDeadline(intChoice);
  		        projectList.set(ProjectID-1001, tempProject);
  		        break;
	          case 8:
	        	replacePersonel(scanIn,tempProject, ProjectID, "Architect");
  		        break;
	          case 9:
	        	replacePersonel(scanIn,tempProject, ProjectID, "Contractor");
		        break;
	          case 10:
	        	replacePersonel(scanIn,tempProject, ProjectID, "Customer");
  		        break;
	          case 11:
	        	int newProjectID = selectProject(scanIn);
	        	tempProject = projectList.get(newProjectID-1001);
	          	break;
	          case 12:
		         quit = true;
		         break;
	
	          default:
	        	System.out.println("Invalid choice.");
	
	          }
          
	
        } while (!quit);
     }
	/**Returns a integer result from a select project method
	 * Requests feedback from user, after displaying all projects
	 * @param scanIn A scanner to allow reading input from console
	 * @return An integer containing the specific project user want to interact with
	 */
	public static int selectProject(Scanner scanIn) {
		int selection = -1;
		boolean present = false;
		if(printProjects(scanIn)) {  
			while(!present) {
				System.out.println("Which Project are you editing? ");
				try {
					selection = scanIn.nextInt();
					scanIn.nextLine();
					
					if (1000 < (selection) && (selection) <= (projectList.get(projectList.size() - 1).getProjectID()) ) {
			        	present = true;
			        	System.out.println("Valid project selection.");
			        }else {
			        	present = false;
			        	System.out.println("Invalid project selection.");
			        }
				} catch (Exception e) {
					scanIn.nextLine();
					System.out.println("Invalid Selection, Not an integer");
					present = false;
				}
			}
		}
		return selection;
	}
	/**Replace a personel ID inside a project
	 * Method interacts with user, requesting detail needed for new project creation
	 * Adds the new project to the project list
	 * @param scanIn A scanner to allow reading input from console
	 * @param tempProject the current project object the user wants to change
	 * @param ProjectID A integer containing the project ID the user wants to make changes to
	 * @param Type A string containing the specific personel type the user wants to change
	 */
	public static void replacePersonel(Scanner scanIn, Project tempProject, int ProjectID, String Type) {
		int tempID = -1;
		boolean valid = false;
		do {
	  			
	  			if(printPersonel(scanIn, Type)) {
	  				
	  			}
	  			System.out.println("Please enter the Personel ID of the " +Type + ", or for a new " +Type + " enter 0..");
	  			int intChoice = 0;	
	  			try {
	  				intChoice = scanIn.nextInt();
	  				scanIn.nextLine();
	  				if(intChoice == 0) {
	  					createNewPersonel(scanIn);
  	  				if (personel[personel.length-1].getPersonelType().equals(Type)){
	  						tempID = personel[personel.length-1].getPersonelID();
	  						valid = true;
	  					}else {
	  						System.out.println("Selected personel is not a " +Type);
	  						valid = false;
	  					}
	  				} else {
	  					if (personel[intChoice -1].getPersonelType().equals(Type)){
	  						tempID = personel[intChoice -1].getPersonelID();
	  						valid = true;
	  					}else {
	  						System.out.println("Selected personel is not a " +Type);
	  						valid = false;
	  					}
	  					
	  				}
	  				
	  			} catch (NumberFormatException e) {
	          		valid = false;  
	  				System.out.println("Invalid Selection"); 
	  				scanIn.nextLine();
	  			}
			
        	}while(!valid);   

			switch (Type) {
			case "Architect": {
				tempProject.setArchitectID(tempID);
				break;
			}
			case "Contractor": {
				tempProject.setContractorID(tempID);
				break;
			}
			case "Customer": {
				tempProject.setCustomerID(tempID);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + Type);
			}
				        
	        projectList.set(ProjectID-1001, tempProject);
		
	}
}
