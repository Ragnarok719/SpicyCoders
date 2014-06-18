package ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import data.*;
import dbhelper.*;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPaneMain;
	private JLabel lblWelcomeToSpicy;
	private JPanel panelBooks;
	private JTextField textFieldSearch;
	private JButton btnLibrarianEntrance;

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
	private JButton buttonSearchPatronByName;
	private JLabel lblDescription;
	private JTextField textFieldDescription;
	private JPanel panelCategory;
	private JScrollPane scrollPaneCategory;
	private JLabel lblCategoryOperations;
	private JTable tableCategory;
	private JButton btnDisplaycategory;
	private JButton btnAddCategory;
	private JButton btnGetselected;
	private JButton btnDeleteselected;
	private JButton btnUpdate;
	private JLabel lableCategoryName;
	private JTextField textFieldCategoryName;
	private JLabel lblIdnumber;
	private JLabel lblSupercategoryid;
	private JTextField textFieldCategoryId;
	private JTextField textFieldCategorySuper;
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
					DefaultTableModel tableModel = (DefaultTableModel)tableBook.getModel();
					tableModel.setColumnCount(0);
					for (Book book : result){
						String isbn=book.getIsbn()+"";
						String title=book.getTitle();
						String author="";
						ArrayList<Author> authors=book.getAuthor();
						for (Author a : authors)
							author=author + a.getName()+";";
						String description=book.getDescription();
						String type=book.getTypeName();
						CategoryHelper chelper=new CategoryHelper();
						String category=chelper.getCategory(book.getIdNumber()).getName();
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
						tableModel.addRow(new Object[]{isbn,title,author,description,type,category,currentQ,totalQ,publisher,year,genre});
					}
				}	
				else{
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
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
					tabbedPaneMain.setEnabledAt(4, true);
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
		lblIsbn.setBounds(40, 119, 60, 38);
		panelBooks.add(lblIsbn);
		
		textFieldIsbn = new JTextField();
		textFieldIsbn.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldIsbn.setBounds(110, 119, 300, 38);
		panelBooks.add(textFieldIsbn);
		textFieldIsbn.setColumns(10);
		
		lblTitle = new JLabel("Title");
		lblTitle.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblTitle.setBounds(40, 167, 60, 38);
		panelBooks.add(lblTitle);
		
		textFieldTitle = new JTextField();
		textFieldTitle.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldTitle.setColumns(10);
		textFieldTitle.setBounds(110, 167, 300, 38);
		panelBooks.add(textFieldTitle);
		
		lblAuthor = new JLabel("Author");
		lblAuthor.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblAuthor.setBounds(40, 215, 80, 38);
		panelBooks.add(lblAuthor);
		
		textFieldAuthor = new JTextField();
		textFieldAuthor.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldAuthor.setColumns(10);
		textFieldAuthor.setBounds(130, 214, 280, 38);
		panelBooks.add(textFieldAuthor);
		
		lblType = new JLabel("Type");
		lblType.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblType.setBounds(40, 262, 60, 38);
		panelBooks.add(lblType);
		
		textFieldType = new JTextField();
		textFieldType.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldType.setColumns(10);
		textFieldType.setBounds(110, 262, 300, 38);
		panelBooks.add(textFieldType);
		
		lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblCategory.setBounds(40, 310, 103, 38);
		panelBooks.add(lblCategory);
		
		textFieldCategory = new JTextField();
		textFieldCategory.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCategory.setColumns(10);
		textFieldCategory.setBounds(153, 310, 257, 38);
		panelBooks.add(textFieldCategory);
		
		lblCurrentquantity = new JLabel("CurrentQuantity");
		lblCurrentquantity.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblCurrentquantity.setBounds(40, 358, 186, 38);
		panelBooks.add(lblCurrentquantity);
		
		textFieldCurrentQ = new JTextField();
		textFieldCurrentQ.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCurrentQ.setColumns(10);
		textFieldCurrentQ.setBounds(236, 358, 174, 38);
		panelBooks.add(textFieldCurrentQ);
		
		lblTotalquantity = new JLabel("TotalQuantity");
		lblTotalquantity.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblTotalquantity.setBounds(40, 406, 186, 38);
		panelBooks.add(lblTotalquantity);
		
		textFieldTotalQ = new JTextField();
		textFieldTotalQ.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldTotalQ.setColumns(10);
		textFieldTotalQ.setBounds(236, 406, 174, 38);
		panelBooks.add(textFieldTotalQ);
		
		lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblPublisher.setBounds(40, 454, 124, 38);
		panelBooks.add(lblPublisher);
		
		textFieldPublisher = new JTextField();
		textFieldPublisher.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPublisher.setColumns(10);
		textFieldPublisher.setBounds(153, 454, 257, 38);
		panelBooks.add(textFieldPublisher);
		
		lblPublishyear = new JLabel("PublishYear");
		lblPublishyear.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblPublishyear.setBounds(40, 502, 137, 38);
		panelBooks.add(lblPublishyear);
		
		textFieldPulishYear = new JTextField();
		textFieldPulishYear.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldPulishYear.setColumns(10);
		textFieldPulishYear.setBounds(187, 502, 223, 38);
		panelBooks.add(textFieldPulishYear);
		
		lblGenre = new JLabel("Genre");
		lblGenre.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblGenre.setBounds(40, 550, 80, 38);
		panelBooks.add(lblGenre);
		
		textFieldGenre = new JTextField();
		textFieldGenre.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldGenre.setColumns(10);
		textFieldGenre.setBounds(130, 550, 280, 38);
		panelBooks.add(textFieldGenre);
		
		JButton buttonSearchBook = new JButton("SearchByISBN");
		buttonSearchBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchBook.setBounds(506, 163, 160, 40);
		panelBooks.add(buttonSearchBook);
		
		JButton buttonAddBook = new JButton("Add");
		buttonAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Book book=new Book();
				book.setIsbn(Integer.parseInt(textFieldIsbn.getText()));
				book.setTitle(textFieldTitle.getText());
				book.setDescription(textFieldDescription.getText());
				book.setCurrentQuantity(Integer.parseInt(textFieldCurrentQ.getText()));
				book.setTotalQuantity(Integer.parseInt(textFieldTotalQ.getText()));
				book.setPublishYear(Integer.parseInt(textFieldPulishYear.getText()));
				book.setTypeName(textFieldType.getText());
				String category=textFieldCategory.getText();
				CategoryHelper chelper=new CategoryHelper();
				Category cate=chelper.getCategory(category);
				book.setIdNumber(cate.getIdNumber());
				
				String authorStr=textFieldAuthor.getText();
				String publisherStr=textFieldPublisher.getText();
				String genreStr=textFieldGenre.getText();
				String[] authors=authorStr.split(",");
				String[] publishers=publisherStr.split(",");
				String[] genres=genreStr.split(",");
				ArrayList<Author> authorList=new ArrayList<Author>();
				ArrayList<Publisher> publisherList=new ArrayList<Publisher>();
				ArrayList<SearchGenre> genreList=new ArrayList<SearchGenre>();
				for (String str:authors){
					Author author=new Author();
					author.setName(str);
					authorList.add(author);
				}
				for (String str:publishers){
					Publisher publisher=new Publisher();
					publisher.setName(str);
					publisherList.add(publisher);
				}
				for (String str:genres){
					SearchGenre genre=new SearchGenre();
					genre.setName(str);
					genreList.add(genre);
				}
				book.setAuthor(authorList);
				book.setPublisher(publisherList);
				book.setSearchGenre(genreList);
				
				BookHelper helper=new BookHelper();
				helper.addBook(book);		
				JOptionPane.showMessageDialog(null, "Add book with isbn "+book.getIsbn(), "Add book", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonAddBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddBook.setBounds(506, 259, 160, 40);
		panelBooks.add(buttonAddBook);
		
		JButton buttonUpdateBook = new JButton("Update");
		buttonUpdateBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Book book=new Book();
				book.setIsbn(Integer.parseInt(textFieldIsbn.getText()));
				book.setTitle(textFieldTitle.getText());
				book.setDescription(textFieldDescription.getText());
				book.setCurrentQuantity(Integer.parseInt(textFieldCurrentQ.getText()));
				book.setTotalQuantity(Integer.parseInt(textFieldTotalQ.getText()));
				book.setPublishYear(Integer.parseInt(textFieldPulishYear.getText()));
				book.setTypeName(textFieldType.getText());
				String category=textFieldCategory.getText();
				CategoryHelper chelper=new CategoryHelper();
				Category cate=chelper.getCategory(category);
				book.setIdNumber(cate.getIdNumber());
				
				String authorStr=textFieldAuthor.getText();
				String publisherStr=textFieldPublisher.getText();
				String genreStr=textFieldGenre.getText();
				String[] authors=authorStr.split(",");
				String[] publishers=publisherStr.split(",");
				String[] genres=genreStr.split(",");
				ArrayList<Author> authorList=new ArrayList<Author>();
				ArrayList<Publisher> publisherList=new ArrayList<Publisher>();
				ArrayList<SearchGenre> genreList=new ArrayList<SearchGenre>();
				for (String str:authors){
					Author author=new Author();
					author.setName(str);
					authorList.add(author);
				}
				for (String str:publishers){
					Publisher publisher=new Publisher();
					publisher.setName(str);
					publisherList.add(publisher);
				}
				for (String str:genres){
					SearchGenre genre=new SearchGenre();
					genre.setName(str);
					genreList.add(genre);
				}
				book.setAuthor(authorList);
				book.setPublisher(publisherList);
				book.setSearchGenre(genreList);
				
				BookHelper helper=new BookHelper();
				helper.updateBook(book);		
				JOptionPane.showMessageDialog(null, "Update book with isbn "+book.getIsbn(), "Update book", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonUpdateBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdateBook.setBounds(506, 354, 160, 40);
		panelBooks.add(buttonUpdateBook);
		
		JButton buttonDeleteBook = new JButton("Delete");
		buttonDeleteBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Book book=new Book();
				book.setIsbn(Integer.parseInt(textFieldIsbn.getText()));
				BookHelper helper=new BookHelper();
				helper.deleteBook(book);		
				JOptionPane.showMessageDialog(null, "Delete book with isbn "+book.getIsbn(), "Delete book", JOptionPane.PLAIN_MESSAGE);
			}
		});
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
		btnCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckOutHelper helper=new CheckOutHelper();
				BookHelper bhelper=new BookHelper();
				int isbn=Integer.parseInt(textFieldIsbn2.getText());
				Book book=bhelper.getBook(isbn);
				int patron=Integer.parseInt(textFieldPatron.getText());
				int librarian=currentLibrarian.getIdNumber();
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				try {
				helper.checkOut(book,ts,patron,librarian);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		btnCheckout.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnCheckout.setBounds(850, 354, 160, 40);
		panelBooks.add(btnCheckout);
		
		JButton btnReturn = new JButton("Return");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckOutHelper helper=new CheckOutHelper();
				int isbn=Integer.parseInt(textFieldIsbn2.getText());
				int patron=Integer.parseInt(textFieldPatron.getText());
				int librarian=currentLibrarian.getIdNumber();
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				try {
					helper.returnBook(ts,isbn,patron,librarian);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		btnReturn.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnReturn.setBounds(1081, 354, 160, 40);
		panelBooks.add(btnReturn);
		
		lblDescription = new JLabel("Description");
		lblDescription.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblDescription.setBounds(40, 598, 137, 38);
		panelBooks.add(lblDescription);
		
		textFieldDescription = new JTextField();
		textFieldDescription.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldDescription.setColumns(10);
		textFieldDescription.setBounds(183, 598, 483, 38);
		panelBooks.add(textFieldDescription);
		
		panelCategory = new JPanel();
		panelCategory.setLayout(null);
		tabbedPaneMain.addTab("Category", null, panelCategory, null);
		tabbedPaneMain.setEnabledAt(2, false);
		
		scrollPaneCategory = new JScrollPane();
		scrollPaneCategory.setBounds(10, 106, 519, 546);
		panelCategory.add(scrollPaneCategory);
		
		tableCategory = new JTable();
		tableCategory.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "IDNumber", "SuperCategoryID"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPaneCategory.setViewportView(tableCategory);
		
		lblCategoryOperations = new JLabel("Category Operations");
		lblCategoryOperations.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblCategoryOperations.setBounds(464, 30, 288, 36);
		panelCategory.add(lblCategoryOperations);
		
		btnDisplaycategory = new JButton("DisplayCategory");
		btnDisplaycategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CategoryHelper categoryHelper = new CategoryHelper();
				ArrayList<Category> result=categoryHelper.getAllCategory();
				if ((result!=null) && (!result.isEmpty())){
					DefaultTableModel tableModel = (DefaultTableModel)tableCategory.getModel();
					tableModel.setColumnCount(0);
					for (Category category : result){
						String name=category.getName();
						String id=category.getIdNumber()+"";
						String superId=category.getSuperCategoryId()+"";	
						tableModel.addRow(new Object[]{name,id,superId});
					}
				}	
				else{
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDisplaycategory.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnDisplaycategory.setBounds(640, 160, 160, 40);
		panelCategory.add(btnDisplaycategory);
		
		btnAddCategory = new JButton("AddCategory");
		btnAddCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Category category=new Category();
				category.setIdNumber(Integer.parseInt(textFieldCategoryId.getText()));
				category.setName(textFieldCategoryName.getText());
				category.setSuperCategoryId(Integer.parseInt(textFieldCategorySuper.getText()));				
				CategoryHelper helper=new CategoryHelper();
				helper.addCategory(category);		
				JOptionPane.showMessageDialog(null, "Add category "+category.getName(), "Add category", JOptionPane.PLAIN_MESSAGE);
			}
		});
		btnAddCategory.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnAddCategory.setBounds(640, 238, 160, 40);
		panelCategory.add(btnAddCategory);
		
		btnGetselected = new JButton("GetSelected");
		btnGetselected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected=tableCategory.getSelectedRow();
				textFieldCategoryName.setText(tableCategory.getValueAt(selected, 0).toString());
				textFieldCategoryId.setText(tableCategory.getValueAt(selected, 1).toString());
				textFieldCategorySuper.setText(tableCategory.getValueAt(selected, 2).toString());
			}
		});
		btnGetselected.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnGetselected.setBounds(640, 318, 160, 40);
		panelCategory.add(btnGetselected);
		
		btnDeleteselected = new JButton("DeleteSelected");
		btnDeleteselected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected=tableCategory.getSelectedRow();
				int id=Integer.parseInt(tableCategory.getValueAt(selected, 1).toString());
				CategoryHelper helper=new CategoryHelper();
				helper.deleteCategory(id);
				JOptionPane.showMessageDialog(null, "Delete category with ID"+id, "Delete category", JOptionPane.PLAIN_MESSAGE);
			}
		});
		btnDeleteselected.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnDeleteselected.setBounds(640, 398, 160, 40);
		panelCategory.add(btnDeleteselected);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Category category=new Category();
				category.setIdNumber(Integer.parseInt(textFieldCategoryId.getText()));
				category.setName(textFieldCategoryName.getText());
				category.setSuperCategoryId(Integer.parseInt(textFieldCategorySuper.getText()));				
				CategoryHelper helper=new CategoryHelper();
				int selected=tableCategory.getSelectedRow();
				int id=Integer.parseInt(tableCategory.getValueAt(selected, 1).toString());
				helper.updateCategory(id,category);		
				JOptionPane.showMessageDialog(null, "Update category "+category.getName(), "Update category", JOptionPane.PLAIN_MESSAGE);
			}
		});
		btnUpdate.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnUpdate.setBounds(640, 478, 160, 40);
		panelCategory.add(btnUpdate);
		
		lableCategoryName = new JLabel("Name");
		lableCategoryName.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lableCategoryName.setBounds(884, 150, 147, 38);
		panelCategory.add(lableCategoryName);
		
		textFieldCategoryName = new JTextField();
		textFieldCategoryName.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCategoryName.setColumns(10);
		textFieldCategoryName.setBounds(884, 205, 280, 38);
		panelCategory.add(textFieldCategoryName);
		
		lblIdnumber = new JLabel("IDNumber");
		lblIdnumber.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblIdnumber.setBounds(884, 271, 147, 38);
		panelCategory.add(lblIdnumber);
		
		lblSupercategoryid = new JLabel("SuperCategoryID");
		lblSupercategoryid.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		lblSupercategoryid.setBounds(888, 390, 195, 38);
		panelCategory.add(lblSupercategoryid);
		
		textFieldCategoryId = new JTextField();
		textFieldCategoryId.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCategoryId.setColumns(10);
		textFieldCategoryId.setBounds(884, 331, 280, 38);
		panelCategory.add(textFieldCategoryId);
		
		textFieldCategorySuper = new JTextField();
		textFieldCategorySuper.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldCategorySuper.setColumns(10);
		textFieldCategorySuper.setBounds(884, 450, 280, 38);
		panelCategory.add(textFieldCategorySuper);
		
		JPanel panelManagement = new JPanel();
		panelManagement.setLayout(null);
		tabbedPaneMain.addTab("Management", null, panelManagement, null);
		tabbedPaneMain.setEnabledAt(3, false);
		
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
		
		JButton buttonSearchPatronByNum = new JButton("SearchByNumber");
		buttonSearchPatronByNum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PatronHelper helper=new PatronHelper();
				String keyword=textFieldCardNumber.getText();
				ArrayList<Patron> results=helper.searchPatron("cardNumber", keyword);
				if ((results!=null) && (!results.isEmpty())){
					Patron patron=results.get(0);
					textFieldPatronName.setText(patron.getName());
					textFieldPatronPhone.setText(patron.getPhone()+"");
					textFieldPatronAddress.setText(patron.getAddress());
					textFieldPatronUnpaid.setText(patron.getUnpaidFees()+"");
				}	
				else{
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonSearchPatronByNum.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchPatronByNum.setBounds(43, 450, 160, 40);
		panelManagement.add(buttonSearchPatronByNum);
		
		JButton buttonAddPatron = new JButton("Add");
		buttonAddPatron.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Patron patron=new Patron();
				patron.setCardNumber(Integer.parseInt(textFieldCardNumber.getText()));
				patron.setName(textFieldPatronName.getText());
				patron.setPhone(Integer.parseInt(textFieldPatronPhone.getText()));
				patron.setAddress(textFieldPatronAddress.getText());
				patron.setUnpaidFees(Integer.parseInt(textFieldPatronUnpaid.getText()));
				PatronHelper helper=new PatronHelper();
				helper.addPatron(patron);		
				JOptionPane.showMessageDialog(null, "Add patron with card number "+patron.getCardNumber(), "Add librarian", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonAddPatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddPatron.setBounds(250, 450, 160, 40);
		panelManagement.add(buttonAddPatron);
		
		JButton buttonUpdatePatron = new JButton("Update");
		buttonUpdatePatron.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Patron patron=new Patron();
				patron.setCardNumber(Integer.parseInt(textFieldCardNumber.getText()));
				patron.setName(textFieldLibName.getText());
				patron.setPhone(Integer.parseInt(textFieldPatronPhone.getText()));
				patron.setAddress(textFieldPatronAddress.getText());
				patron.setUnpaidFees(Integer.parseInt(textFieldPatronUnpaid.getText()));
				PatronHelper helper=new PatronHelper();
				helper.updatePatron(patron.getCardNumber(), patron);		
				JOptionPane.showMessageDialog(null, "Update patron with ID "+patron.getCardNumber(), "Update patron", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonUpdatePatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdatePatron.setBounds(250, 519, 160, 40);
		panelManagement.add(buttonUpdatePatron);
		
		JButton buttonDeletePatron = new JButton("Delete");
		buttonDeletePatron.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PatronHelper helper=new PatronHelper();
				int id=Integer.parseInt(textFieldCardNumber.getText());
				helper.deletePatron(id);	
				JOptionPane.showMessageDialog(null, "Delete patron with ID "+id, "Delete patron", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonDeletePatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonDeletePatron.setBounds(250, 586, 160, 40);
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
		
		JButton buttonSearchLibId = new JButton("SearchByID");
		buttonSearchLibId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LibrarianHelper helper=new LibrarianHelper();
				String keyword=textFieldLibId.getText();
				ArrayList<Librarian> results=helper.searchLibrarian("idName", keyword);
				if ((results!=null) && (!results.isEmpty())){
					Librarian lib=results.get(0);
					textFieldLibName.setText(lib.getName());
					textFieldLibAddress.setText(lib.getAddress());
				}	
				else{
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonSearchLibId.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchLibId.setBounds(874, 450, 160, 40);
		panelManagement.add(buttonSearchLibId);
		
		JButton buttonAddLib = new JButton("Add");
		buttonAddLib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Librarian lib=new Librarian();
				lib.setIdNumber(Integer.parseInt(textFieldLibId.getText()));
				lib.setName(textFieldLibName.getText());
				lib.setAddress(textFieldLibAddress.getText());
				LibrarianHelper helper=new LibrarianHelper();
				helper.addLibrarian(lib);		
				JOptionPane.showMessageDialog(null, "Add librarian with ID "+lib.getIdNumber(), "Add librarian", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonAddLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddLib.setBounds(1081, 450, 160, 40);
		panelManagement.add(buttonAddLib);
		
		JButton buttonUpdateLib = new JButton("Update");
		buttonUpdateLib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Librarian lib=new Librarian();
				lib.setIdNumber(Integer.parseInt(textFieldLibId.getText()));
				lib.setName(textFieldLibName.getText());
				lib.setAddress(textFieldLibAddress.getText());
				LibrarianHelper helper=new LibrarianHelper();
				helper.updateLibrarian(lib.getIdNumber(), lib);		
				JOptionPane.showMessageDialog(null, "Update librarian with ID "+lib.getIdNumber(), "Update librarian", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonUpdateLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdateLib.setBounds(1081, 519, 160, 40);
		panelManagement.add(buttonUpdateLib);
		
		JButton buttonDeleteLib = new JButton("Delete");
		buttonDeleteLib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibrarianHelper helper=new LibrarianHelper();
				int id=Integer.parseInt(textFieldLibId.getText());
				helper.deleteLibrarian(id);	
				JOptionPane.showMessageDialog(null, "Delete librarian with ID "+id, "Delete librarian", JOptionPane.PLAIN_MESSAGE);
			}
		});
		buttonDeleteLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonDeleteLib.setBounds(1081, 586, 160, 40);
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
		
		JButton buttonSearchLibName = new JButton("SearchByName");
		buttonSearchLibName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibrarianHelper helper=new LibrarianHelper();
				String keyword=textFieldLibId.getText();
				ArrayList<Librarian> results=helper.searchLibrarian("Name", keyword);
				if ((results!=null) && (!results.isEmpty())){
					Librarian lib=results.get(0);
					textFieldLibName.setText(lib.getName());
					textFieldLibAddress.setText(lib.getAddress());
				}	
				else{
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonSearchLibName.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchLibName.setBounds(874, 519, 160, 40);
		panelManagement.add(buttonSearchLibName);
		
		buttonSearchPatronByName = new JButton("SearchByName");
		buttonSearchPatronByName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PatronHelper helper=new PatronHelper();
				String keyword=textFieldPatronName.getText();
				ArrayList<Patron> results=helper.searchPatron("name", keyword);
				if ((results!=null) && (!results.isEmpty())){
					Patron patron=results.get(0);
					textFieldCardNumber.setText(patron.getCardNumber()+"");
					textFieldPatronPhone.setText(patron.getPhone()+"");
					textFieldPatronAddress.setText(patron.getAddress());
					textFieldPatronUnpaid.setText(patron.getUnpaidFees()+"");
				}	
				else{
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonSearchPatronByName.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchPatronByName.setBounds(43, 519, 160, 40);
		panelManagement.add(buttonSearchPatronByName);
		
		JPanel panelReport = new JPanel();
		tabbedPaneMain.addTab("Report", null, panelReport, null);
		tabbedPaneMain.setEnabledAt(4, false);
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
		
		JButton btnAllcheckout = new JButton("AllCheckOuts");
		btnAllcheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportHelper helper=new ReportHelper();
				
			}
		});
		btnAllcheckout.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnAllcheckout.setBounds(35, 539, 160, 40);
		panelReport.add(btnAllcheckout);
		
		JButton btnAlloverduecos = new JButton("AllOverdueCOs");
		btnAlloverduecos.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnAlloverduecos.setBounds(226, 539, 160, 40);
		panelReport.add(btnAlloverduecos);
		
		JButton btnGenrecounts = new JButton("GenreCounts");
		btnGenrecounts.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnGenrecounts.setBounds(421, 539, 160, 40);
		panelReport.add(btnGenrecounts);
		
		JButton btnToppublisher = new JButton("TopPublishers");
		btnToppublisher.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnToppublisher.setBounds(618, 539, 160, 40);
		panelReport.add(btnToppublisher);
		
		JButton btnOutofstockbooks = new JButton("OutOfStockBooks");
		btnOutofstockbooks.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnOutofstockbooks.setBounds(817, 539, 160, 40);
		panelReport.add(btnOutofstockbooks);
	}
}
