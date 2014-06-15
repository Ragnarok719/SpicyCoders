package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JButton;

import dbhelper.*;
import data.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPaneMain;
	private JLabel lblWelcomeToSpicy;
	private JPanel panelBooks;
	private JTextField textFieldSearch;
	private JButton btnLibrarianEntrance;
	private JButton buttonAdvanced;

	private Librarian currentLibrarian=null;
	private JScrollPane scrollPaneBook;
	private JTable tableBook;
	private JLabel labelLibrarian1;
	private JLabel lblIsbn;
	private JTextField textFieldIsbn;
	private JLabel lblTitle;
	private JTextField textFieldTitle;
	private JLabel lblAuthor;
	private JTextField textFieldAuthor;
	private JLabel lblType;
	private JTextField textFieldType;
	private JLabel lblCategory;
	private JTextField textFieldCategory;
	private JLabel lblCurrentquantity;
	private JTextField textFieldCurrentQ;
	private JLabel lblTotalquantity;
	private JTextField textFieldTotalQ;
	private JLabel lblPublisher;
	private JTextField textFieldPublisher;
	private JLabel lblPublishyear;
	private JTextField textFieldPulishYear;
	private JLabel lblGenre;
	private JTextField textFieldGenre;
	private JTextField textFieldIsbn2;
	private JTextField textFieldPatron;
	private JTextField textFieldCardNumber;
	private JTextField textFieldPatronName;
	private JTextField textFieldPatronPhone;
	private JTextField textFieldPatronAddress;
	private JTextField textFieldPatronUnpaid;
	private JTextField textFieldLibId;
	private JTextField textFieldLibName;
	private JLabel labelLibrarian2;
	private JTextField textFieldLibAddress;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setResizable(false);
		setTitle("Spicy Coders' Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPaneMain = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPaneMain.setBounds(0, 0, 1274, 691);
		contentPane.add(tabbedPaneMain);
		
		JPanel panelMain = new JPanel();
		tabbedPaneMain.addTab("Search", null, panelMain, null);
		tabbedPaneMain.setEnabledAt(0, true);
		panelMain.setLayout(null);
		
		lblWelcomeToSpicy = new JLabel("Welcome to Spicy Coders' Library System");
		lblWelcomeToSpicy.setFont(new Font("Times New Roman", Font.BOLD, 36));
		lblWelcomeToSpicy.setBounds(287, 10, 684, 67);
		panelMain.add(lblWelcomeToSpicy);
		
		textFieldSearch = new JTextField();
		textFieldSearch.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldSearch.setBounds(325, 76, 480, 40);
		panelMain.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		//Search books with keywords
		//Display results after searching
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BookHelper bookHelper = new BookHelper();
				String searchKeyword=textFieldSearch.getText();
				ArrayList<Book> result=bookHelper.keywordSearch(searchKeyword);
				if ((result!=null) && (!result.isEmpty())){
					for (Book book : result){
						String isbn=book.getIsbn()+"";
						String title=book.getTitle();
						String author="";
						ArrayList<Author> authors=book.getAuthor();
						for (Author a : authors)
							author=author + a.getName()+";";
						String description=book.getDescription();
						String type=book.getTypeName();
						String category=book.getIdNumber()+"";
						String currentQ=book.getCurrentQuantity()+"";
						String totalQ=book.getTotalQuantity()+"";
						String publisher="";
						ArrayList<Publisher> publishers=book.getPublisher();
						for (Publisher p : publishers)
							publisher=publisher+ p.getName()+";";
						String year=book.getPublishYear()+"";
						String genre="";
						ArrayList<SearchGenre> genres=book.getSearchGenre();
						for (SearchGenre g : genres)
							genre=genre+ g.getName()+";";
						
						DefaultTableModel tableModel = (DefaultTableModel)tableBook.getModel();
						tableModel.addRow(new Object[]{isbn,title,author,description,type,category,currentQ,totalQ,publisher,year,genre});
					}
				}		
			}
		});
		btnSearch.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnSearch.setBounds(844, 76, 90, 40);
		panelMain.add(btnSearch);
		
		//Librarians login with id to get access to more functions
		
		btnLibrarianEntrance = new JButton("Librarian Entrance");
		btnLibrarianEntrance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String librarianID=JOptionPane.showInputDialog("Enter your ID");
				LibrarianHelper lHelper=new LibrarianHelper();
				ArrayList<Librarian> result=lHelper.searchLibrarian("idNumber", librarianID);
				if ((result!=null) && (!result.isEmpty())){
					currentLibrarian=result.get(0);
					JOptionPane.showMessageDialog(null, "Hello "+currentLibrarian.getName(), "Welcome!", JOptionPane.PLAIN_MESSAGE);
					tabbedPaneMain.setEnabledAt(1, true);
					tabbedPaneMain.setEnabledAt(2, true);
					tabbedPaneMain.setEnabledAt(3, true);
					labelLibrarian1.setText("Hello librarian "+currentLibrarian.getName()+", your librarian ID is "+currentLibrarian.getIdNumber()+".");
					labelLibrarian2.setText("Hello librarian "+currentLibrarian.getName()+", your librarian ID is "+currentLibrarian.getIdNumber()+".");
				}				
				else{
					JOptionPane.showMessageDialog(null, "Wrong ID!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnLibrarianEntrance.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnLibrarianEntrance.setBounds(1099, 76, 160, 40);
		panelMain.add(btnLibrarianEntrance);
		
		buttonAdvanced = new JButton("Advanced Search");
		buttonAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonAdvanced.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAdvanced.setBounds(1099, 29, 160, 40);
		panelMain.add(buttonAdvanced);
		
		scrollPaneBook = new JScrollPane();
		scrollPaneBook.setBounds(10, 124, 1249, 528);
		panelMain.add(scrollPaneBook);
		
		tableBook = new JTable();
		tableBook.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ISBN", "Title", "Author", "Description", "Type", "Category", "CurrentQuantity", "Totaluantity", "Publisher", "PublishYear", "Genre"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPaneBook.setViewportView(tableBook);
		
		panelBooks = new JPanel();
		tabbedPaneMain.addTab("Books", null, panelBooks, null);
		tabbedPaneMain.setEnabledAt(1, false);
		panelBooks.setLayout(null);
		
		labelLibrarian1 = new JLabel("Hello Librarian Ben, your ID is xxxxxx");
		labelLibrarian1.setFont(new Font("Times New Roman", Font.BOLD, 36));
		labelLibrarian1.setBounds(310, 10, 700, 60);
		panelBooks.add(labelLibrarian1);
		
		lblIsbn = new JLabel("ISBN");
		lblIsbn.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblIsbn.setBounds(43, 165, 60, 38);
		panelBooks.add(lblIsbn);
		
		textFieldIsbn = new JTextField();
		textFieldIsbn.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldIsbn.setBounds(113, 165, 300, 38);
		panelBooks.add(textFieldIsbn);
		textFieldIsbn.setColumns(10);
		
		lblTitle = new JLabel("Title");
		lblTitle.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblTitle.setBounds(43, 213, 60, 38);
		panelBooks.add(lblTitle);
		
		textFieldTitle = new JTextField();
		textFieldTitle.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldTitle.setColumns(10);
		textFieldTitle.setBounds(113, 213, 300, 38);
		panelBooks.add(textFieldTitle);
		
		lblAuthor = new JLabel("Author");
		lblAuthor.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblAuthor.setBounds(43, 261, 80, 38);
		panelBooks.add(lblAuthor);
		
		textFieldAuthor = new JTextField();
		textFieldAuthor.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldAuthor.setColumns(10);
		textFieldAuthor.setBounds(133, 260, 280, 38);
		panelBooks.add(textFieldAuthor);
		
		lblType = new JLabel("Type");
		lblType.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblType.setBounds(43, 308, 60, 38);
		panelBooks.add(lblType);
		
		textFieldType = new JTextField();
		textFieldType.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldType.setColumns(10);
		textFieldType.setBounds(113, 308, 300, 38);
		panelBooks.add(textFieldType);
		
		lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblCategory.setBounds(43, 356, 103, 38);
		panelBooks.add(lblCategory);
		
		textFieldCategory = new JTextField();
		textFieldCategory.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCategory.setColumns(10);
		textFieldCategory.setBounds(156, 356, 257, 38);
		panelBooks.add(textFieldCategory);
		
		lblCurrentquantity = new JLabel("CurrentQuantity");
		lblCurrentquantity.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblCurrentquantity.setBounds(43, 404, 186, 38);
		panelBooks.add(lblCurrentquantity);
		
		textFieldCurrentQ = new JTextField();
		textFieldCurrentQ.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCurrentQ.setColumns(10);
		textFieldCurrentQ.setBounds(239, 404, 174, 38);
		panelBooks.add(textFieldCurrentQ);
		
		lblTotalquantity = new JLabel("TotalQuantity");
		lblTotalquantity.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblTotalquantity.setBounds(43, 452, 186, 38);
		panelBooks.add(lblTotalquantity);
		
		textFieldTotalQ = new JTextField();
		textFieldTotalQ.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldTotalQ.setColumns(10);
		textFieldTotalQ.setBounds(239, 452, 174, 38);
		panelBooks.add(textFieldTotalQ);
		
		lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblPublisher.setBounds(43, 500, 124, 38);
		panelBooks.add(lblPublisher);
		
		textFieldPublisher = new JTextField();
		textFieldPublisher.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPublisher.setColumns(10);
		textFieldPublisher.setBounds(156, 500, 257, 38);
		panelBooks.add(textFieldPublisher);
		
		lblPublishyear = new JLabel("PublishYear");
		lblPublishyear.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblPublishyear.setBounds(43, 548, 137, 38);
		panelBooks.add(lblPublishyear);
		
		textFieldPulishYear = new JTextField();
		textFieldPulishYear.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPulishYear.setColumns(10);
		textFieldPulishYear.setBounds(190, 548, 223, 38);
		panelBooks.add(textFieldPulishYear);
		
		lblGenre = new JLabel("Genre");
		lblGenre.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblGenre.setBounds(43, 596, 80, 38);
		panelBooks.add(lblGenre);
		
		textFieldGenre = new JTextField();
		textFieldGenre.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldGenre.setColumns(10);
		textFieldGenre.setBounds(133, 596, 280, 38);
		panelBooks.add(textFieldGenre);
		
		JButton buttonSearchBook = new JButton("Search");
		buttonSearchBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchBook.setBounds(506, 163, 160, 40);
		panelBooks.add(buttonSearchBook);
		
		JButton buttonAddBook = new JButton("Add");
		buttonAddBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddBook.setBounds(506, 259, 160, 40);
		panelBooks.add(buttonAddBook);
		
		JButton buttonUpdateBook = new JButton("Update");
		buttonUpdateBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdateBook.setBounds(506, 354, 160, 40);
		panelBooks.add(buttonUpdateBook);
		
		JButton buttonDeleteBook = new JButton("Delete");
		buttonDeleteBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonDeleteBook.setBounds(506, 450, 160, 40);
		panelBooks.add(buttonDeleteBook);
		
		JLabel lblBookOperations = new JLabel("Book Operations");
		lblBookOperations.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblBookOperations.setBounds(320, 80, 200, 30);
		panelBooks.add(lblBookOperations);
		
		JLabel lblTransactions = new JLabel("Transactions");
		lblTransactions.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblTransactions.setBounds(904, 80, 200, 30);
		panelBooks.add(lblTransactions);
		
		JLabel labelIsbn2 = new JLabel("ISBN");
		labelIsbn2.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		labelIsbn2.setBounds(850, 165, 60, 38);
		panelBooks.add(labelIsbn2);
		
		textFieldIsbn2 = new JTextField();
		textFieldIsbn2.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldIsbn2.setColumns(10);
		textFieldIsbn2.setBounds(941, 165, 300, 38);
		panelBooks.add(textFieldIsbn2);
		
		JLabel lblPatron = new JLabel("Patron");
		lblPatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblPatron.setBounds(850, 261, 80, 38);
		panelBooks.add(lblPatron);
		
		textFieldPatron = new JTextField();
		textFieldPatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPatron.setColumns(10);
		textFieldPatron.setBounds(941, 261, 300, 38);
		panelBooks.add(textFieldPatron);
		
		JButton btnCheckout = new JButton("CheckOut");
		btnCheckout.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnCheckout.setBounds(850, 354, 160, 40);
		panelBooks.add(btnCheckout);
		
		JButton btnReturn = new JButton("Return");
		btnReturn.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnReturn.setBounds(1081, 354, 160, 40);
		panelBooks.add(btnReturn);
		
		JPanel panelManagement = new JPanel();
		panelManagement.setLayout(null);
		tabbedPaneMain.addTab("Management", null, panelManagement, null);
		tabbedPaneMain.setEnabledAt(2, false);
		
		labelLibrarian2 = new JLabel("Hello Librarian Ben, your ID is xxxxxx");
		labelLibrarian2.setFont(new Font("Times New Roman", Font.BOLD, 36));
		labelLibrarian2.setBounds(310, 10, 700, 60);
		panelManagement.add(labelLibrarian2);
		
		JLabel lblCardnumber = new JLabel("CardNumber");
		lblCardnumber.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblCardnumber.setBounds(43, 165, 147, 38);
		panelManagement.add(lblCardnumber);
		
		textFieldCardNumber = new JTextField();
		textFieldCardNumber.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCardNumber.setColumns(10);
		textFieldCardNumber.setBounds(200, 165, 213, 38);
		panelManagement.add(textFieldCardNumber);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblName.setBounds(43, 213, 80, 38);
		panelManagement.add(lblName);
		
		textFieldPatronName = new JTextField();
		textFieldPatronName.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPatronName.setColumns(10);
		textFieldPatronName.setBounds(133, 213, 280, 38);
		panelManagement.add(textFieldPatronName);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblPhone.setBounds(43, 261, 80, 38);
		panelManagement.add(lblPhone);
		
		textFieldPatronPhone = new JTextField();
		textFieldPatronPhone.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPatronPhone.setColumns(10);
		textFieldPatronPhone.setBounds(133, 260, 280, 38);
		panelManagement.add(textFieldPatronPhone);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblAddress.setBounds(43, 308, 92, 38);
		panelManagement.add(lblAddress);
		
		textFieldPatronAddress = new JTextField();
		textFieldPatronAddress.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPatronAddress.setColumns(10);
		textFieldPatronAddress.setBounds(133, 308, 280, 38);
		panelManagement.add(textFieldPatronAddress);
		
		JLabel lblUnpaidfees = new JLabel("UnpaidFees");
		lblUnpaidfees.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblUnpaidfees.setBounds(43, 356, 137, 38);
		panelManagement.add(lblUnpaidfees);
		
		textFieldPatronUnpaid = new JTextField();
		textFieldPatronUnpaid.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPatronUnpaid.setColumns(10);
		textFieldPatronUnpaid.setBounds(200, 356, 213, 38);
		panelManagement.add(textFieldPatronUnpaid);
		
		JButton buttonSearchPatron = new JButton("Search");
		buttonSearchPatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchPatron.setBounds(43, 450, 160, 40);
		panelManagement.add(buttonSearchPatron);
		
		JButton buttonAddPatron = new JButton("Add");
		buttonAddPatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddPatron.setBounds(250, 450, 160, 40);
		panelManagement.add(buttonAddPatron);
		
		JButton buttonUpdatePatron = new JButton("Update");
		buttonUpdatePatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdatePatron.setBounds(43, 519, 160, 40);
		panelManagement.add(buttonUpdatePatron);
		
		JButton buttonDeletePatron = new JButton("Delete");
		buttonDeletePatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonDeletePatron.setBounds(250, 519, 160, 40);
		panelManagement.add(buttonDeletePatron);
		
		JLabel lblPatronmanagement = new JLabel("PatronManagement");
		lblPatronmanagement.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblPatronmanagement.setBounds(133, 80, 223, 30);
		panelManagement.add(lblPatronmanagement);
		
		JLabel lblLibrarianmanament = new JLabel("LibrarianManament");
		lblLibrarianmanament.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblLibrarianmanament.setBounds(891, 80, 213, 30);
		panelManagement.add(lblLibrarianmanament);
		
		JLabel lblId = new JLabel("ID");
		lblId.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblId.setBounds(850, 165, 60, 38);
		panelManagement.add(lblId);
		
		textFieldLibId = new JTextField();
		textFieldLibId.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldLibId.setColumns(10);
		textFieldLibId.setBounds(941, 165, 300, 38);
		panelManagement.add(textFieldLibId);
		
		JLabel lblNameLib = new JLabel("Name");
		lblNameLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblNameLib.setBounds(850, 261, 80, 38);
		panelManagement.add(lblNameLib);
		
		textFieldLibName = new JTextField();
		textFieldLibName.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldLibName.setColumns(10);
		textFieldLibName.setBounds(941, 261, 300, 38);
		panelManagement.add(textFieldLibName);
		
		JButton buttonSearchLib = new JButton("Search");
		buttonSearchLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchLib.setBounds(874, 450, 160, 40);
		panelManagement.add(buttonSearchLib);
		
		JButton buttonAddLib = new JButton("Add");
		buttonAddLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddLib.setBounds(1081, 450, 160, 40);
		panelManagement.add(buttonAddLib);
		
		JButton buttonUpdateLib = new JButton("Update");
		buttonUpdateLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdateLib.setBounds(874, 519, 160, 40);
		panelManagement.add(buttonUpdateLib);
		
		JButton buttonDeleteLib = new JButton("Delete");
		buttonDeleteLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonDeleteLib.setBounds(1081, 519, 160, 40);
		panelManagement.add(buttonDeleteLib);
		
		JLabel lblAddressLib = new JLabel("Address");
		lblAddressLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblAddressLib.setBounds(850, 356, 92, 38);
		panelManagement.add(lblAddressLib);
		
		textFieldLibAddress = new JTextField();
		textFieldLibAddress.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldLibAddress.setColumns(10);
		textFieldLibAddress.setBounds(941, 356, 300, 38);
		panelManagement.add(textFieldLibAddress);
		
		JPanel panelReport = new JPanel();
		tabbedPaneMain.addTab("Report", null, panelReport, null);
		tabbedPaneMain.setEnabledAt(3, false);
		panelReport.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("General Reports for Our Library System");
		lblNewLabel.setBounds(375, 30, 515, 36);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		panelReport.add(lblNewLabel);
		
		JScrollPane scrollPaneReport = new JScrollPane();
		scrollPaneReport.setBounds(35, 87, 1200, 420);
		panelReport.add(scrollPaneReport);
		
		JTextArea textAreaReport = new JTextArea();
		textAreaReport.setText("Here will be the generated reports.");
		textAreaReport.setEditable(false);
		scrollPaneReport.setViewportView(textAreaReport);
		
		JButton btnAllcheckout = new JButton("AllCheckOut");
		btnAllcheckout.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnAllcheckout.setBounds(35, 539, 160, 40);
		panelReport.add(btnAllcheckout);
	}
}
