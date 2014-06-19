package ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import data.Author;
import data.Book;
import data.Category;
import data.CheckOut;
import data.Librarian;
import data.Patron;
import data.Publisher;
import data.SearchGenre;
import dbhelper.BookHelper;
import dbhelper.CategoryHelper;
import dbhelper.CheckOutHelper;
import dbhelper.LibrarianHelper;
import dbhelper.PatronHelper;
import dbhelper.ReportHelper;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private JTextArea textAreaReport;
	private JTextArea textAreaIsbn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
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
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BookHelper bookHelper = new BookHelper();
				DefaultTableModel tableModel = (DefaultTableModel)tableBook.getModel();
				tableModel.setRowCount(0);
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
			@Override
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
					labelLibrarian1.setText("Hello "+currentLibrarian.getName()+", your ID is "+currentLibrarian.getIdNumber()+".");
					labelLibrarian2.setText("Hello "+currentLibrarian.getName()+", your ID is "+currentLibrarian.getIdNumber()+".");
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
				"ISBN", "Title", "Author", "Description", "Type", "Category", "CurrentQuantity", "TotalQuantity", "Publisher", "PublishYear", "Genre"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false
			};
			@Override
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
		labelLibrarian1.setBounds(310, 10, 810, 60);
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
		buttonSearchBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookHelper helper=new BookHelper();
				Long isbn=Long.parseLong(textFieldIsbn.getText().trim());
				Book book=helper.getBook(isbn);
				if (book!=null){
					textFieldTitle.setText(book.getTitle());
					textFieldDescription.setText(book.getDescription());
					textFieldCurrentQ.setText(book.getCurrentQuantity()+"");
					textFieldTotalQ.setText(book.getTotalQuantity()+"");
					textFieldPulishYear.setText(book.getPublishYear()+"");
					textFieldType.setText(book.getTypeName());
					CategoryHelper chelper=new CategoryHelper();
					String category=chelper.getCategory(book.getIdNumber()).getName();
					textFieldCategory.setText(category);
					String author="";
					String publisher="";
					String genre="";
					if(book.getAuthor() != null && book.getAuthor().size() > 0) {
						for (Author a:book.getAuthor())
							author=author+a.getName()+", ";
						author = author.substring(0, author.length()-2);
					}
					if(book.getPublisher() != null && book.getPublisher().size() > 0) {
						for (Publisher p:book.getPublisher())
							publisher=publisher+p.getName() + ", ";
						publisher = publisher.substring(0, publisher.length()-2);
					}
					if(book.getSearchGenre() != null && book.getSearchGenre().size() > 0) {
						for (SearchGenre g:book.getSearchGenre())
							genre=genre+g.getName() + ", ";
						genre = genre.substring(0, genre.length()-2);
					}
					textFieldAuthor.setText(author);
					textFieldPublisher.setText(publisher);
					textFieldGenre.setText(genre);
				}
				else{
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonSearchBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonSearchBook.setBounds(506, 163, 160, 40);
		panelBooks.add(buttonSearchBook);
		
		JButton buttonAddBook = new JButton("Add");
		buttonAddBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Book book=new Book();
				book.setIsbn(Long.parseLong(textFieldIsbn.getText().trim()));
				book.setTitle(textFieldTitle.getText().trim());
				book.setDescription(textFieldDescription.getText().trim());
				book.setCurrentQuantity(Integer.parseInt(textFieldCurrentQ.getText().trim()));
				book.setTotalQuantity(Integer.parseInt(textFieldTotalQ.getText().trim()));
				book.setPublishYear(Integer.parseInt(textFieldPulishYear.getText().trim()));
				book.setTypeName(textFieldType.getText().trim());
				String category=textFieldCategory.getText();
				CategoryHelper chelper=new CategoryHelper();
				Category cate=chelper.getCategory(category);
				if(cate != null)
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
					author.setName(str.trim());
					authorList.add(author);
				}
				for (String str:publishers){
					Publisher publisher=new Publisher();
					publisher.setName(str.trim());
					publisherList.add(publisher);
				}
				for (String str:genres){
					SearchGenre genre=new SearchGenre();
					genre.setName(str.trim());
					genreList.add(genre);
				}
				book.setAuthor(authorList);
				book.setPublisher(publisherList);
				book.setSearchGenre(genreList);
				
				BookHelper helper=new BookHelper();
				boolean flag=helper.addBook(book);	
				if (flag)
					JOptionPane.showMessageDialog(null, "Add book with isbn "+book.getIsbn(), "Add book", JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Failed to insert the book into database!", "Error",JOptionPane.ERROR_MESSAGE);
			}
		});
		buttonAddBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddBook.setBounds(506, 259, 160, 40);
		panelBooks.add(buttonAddBook);
		
		JButton buttonUpdateBook = new JButton("Update");
		buttonUpdateBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Book book=new Book();
				book.setIsbn(Long.parseLong(textFieldIsbn.getText().trim()));
				book.setTitle(textFieldTitle.getText().trim());
				book.setDescription(textFieldDescription.getText().trim());
				book.setCurrentQuantity(Integer.parseInt(textFieldCurrentQ.getText().trim()));
				book.setTotalQuantity(Integer.parseInt(textFieldTotalQ.getText().trim()));
				book.setPublishYear(Integer.parseInt(textFieldPulishYear.getText().trim()));
				book.setTypeName(textFieldType.getText().trim());
				String category=textFieldCategory.getText();
				CategoryHelper chelper=new CategoryHelper();
				Category cate=chelper.getCategory(category);
				if(cate != null)
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
					author.setName(str.trim());
					authorList.add(author);
				}
				for (String str:publishers){
					Publisher publisher=new Publisher();
					publisher.setName(str.trim());
					publisherList.add(publisher);
				}
				for (String str:genres){
					SearchGenre genre=new SearchGenre();
					genre.setName(str.trim());
					genreList.add(genre);
				}
				book.setAuthor(authorList);
				book.setPublisher(publisherList);
				book.setSearchGenre(genreList);
				
				BookHelper helper=new BookHelper();
				boolean flag=helper.updateBook(book);		
				if (flag)
					JOptionPane.showMessageDialog(null, "Update book with isbn "+book.getIsbn(), "Update book", JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Failed to update the book in the database!", "Error",JOptionPane.ERROR_MESSAGE);
			}
		});
		buttonUpdateBook.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdateBook.setBounds(506, 354, 160, 40);
		panelBooks.add(buttonUpdateBook);
		
		JButton buttonDeleteBook = new JButton("Delete");
		buttonDeleteBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Book book=new Book();
				book.setIsbn(Long.parseLong(textFieldIsbn.getText().trim()));
				BookHelper helper=new BookHelper();
				boolean flag=helper.deleteBook(book);		
				if (flag)
					JOptionPane.showMessageDialog(null, "Delete book with isbn "+book.getIsbn(), "Delete book", JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Failed to delete the book in the database!", "Error",JOptionPane.ERROR_MESSAGE);
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
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckOutHelper helper=new CheckOutHelper();
				BookHelper bhelper=new BookHelper();
				long isbn=Long.parseLong(textFieldIsbn2.getText().trim());
				Book book=bhelper.getBook(isbn);
				int patron=Integer.parseInt(textFieldPatron.getText().trim());
				int librarian=currentLibrarian.getIdNumber();
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				boolean flag=false;
				try {
				flag=helper.checkOut(book,ts,patron,librarian);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				if (flag)
					JOptionPane.showMessageDialog(null, "Check out the book with isbn="+book.getIsbn()+" for patron whose id="+patron, "CheckOut",JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Failed to checkout the book in the database!", "Error",JOptionPane.ERROR_MESSAGE);
			}
		});
		btnCheckout.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnCheckout.setBounds(850, 354, 160, 40);
		panelBooks.add(btnCheckout);
		
		JButton btnReturn = new JButton("Return");
		btnReturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckOutHelper helper=new CheckOutHelper();
				long isbn=Long.parseLong(textFieldIsbn2.getText().trim());
				int patron=Integer.parseInt(textFieldPatron.getText().trim());
				int librarian=currentLibrarian.getIdNumber();
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				boolean flag=false;
				try {
					flag=helper.returnBook(ts,isbn,patron,librarian);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				if (flag)
					JOptionPane.showMessageDialog(null, "Return the book with isbn="+isbn+" for patron whose id="+patron, "Return",JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Failed to return the book into database!", "Error",JOptionPane.ERROR_MESSAGE);
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
			@Override
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
			@Override
			public void actionPerformed(ActionEvent e) {
				CategoryHelper categoryHelper = new CategoryHelper();
				ArrayList<Category> result=categoryHelper.getAllCategory();
				if ((result!=null) && (!result.isEmpty())){
					DefaultTableModel tableModel = (DefaultTableModel)tableCategory.getModel();
					tableModel.setRowCount(0);
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
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Category category=new Category();
				category.setIdNumber(Integer.parseInt(textFieldCategoryId.getText().trim()));
				category.setName(textFieldCategoryName.getText().trim());
				category.setSuperCategoryId(Integer.parseInt(textFieldCategorySuper.getText().trim()));				
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
			@Override
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
			@Override
			public void actionPerformed(ActionEvent e) {
				int selected=tableCategory.getSelectedRow();
				int id=Integer.parseInt(tableCategory.getValueAt(selected, 1).toString().trim());
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
			@Override
			public void actionPerformed(ActionEvent e) {
				Category category=new Category();
				category.setIdNumber(Integer.parseInt(textFieldCategoryId.getText().trim()));
				category.setName(textFieldCategoryName.getText().trim());
				category.setSuperCategoryId(Integer.parseInt(textFieldCategorySuper.getText().trim()));				
				CategoryHelper helper=new CategoryHelper();
				int selected=tableCategory.getSelectedRow();
				int id=Integer.parseInt(tableCategory.getValueAt(selected, 1).toString().trim());
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
		labelLibrarian2.setBounds(310, 10, 811, 60);
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
			@Override
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
			@Override
			public void actionPerformed(ActionEvent e) {
				Patron patron=new Patron();
				patron.setCardNumber(Integer.parseInt(textFieldCardNumber.getText().trim()));
				patron.setName(textFieldPatronName.getText().trim());
				patron.setPhone(Integer.parseInt(textFieldPatronPhone.getText().trim()));
				patron.setAddress(textFieldPatronAddress.getText().trim());
				patron.setUnpaidFees(Integer.parseInt(textFieldPatronUnpaid.getText().trim()));
				PatronHelper helper=new PatronHelper();
				// check constraint
				if (patron.getCardNumber() >= 100000 && patron.getCardNumber() <= 999999) {					
					helper.addPatron(patron);		
					JOptionPane.showMessageDialog(null, "Add patron with card number "+patron.getCardNumber(), "Add librarian", JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Card Number must be within the ranges 100000-999999!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonAddPatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddPatron.setBounds(250, 450, 160, 40);
		panelManagement.add(buttonAddPatron);
		
		JButton buttonUpdatePatron = new JButton("Update");
		buttonUpdatePatron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Patron patron=new Patron();
				patron.setCardNumber(Integer.parseInt(textFieldCardNumber.getText().trim()));
				patron.setName(textFieldPatronName.getText().trim());
				patron.setPhone(Integer.parseInt(textFieldPatronPhone.getText().trim()));
				patron.setAddress(textFieldPatronAddress.getText().trim());
				patron.setUnpaidFees(Integer.parseInt(textFieldPatronUnpaid.getText().trim()));
				PatronHelper helper=new PatronHelper();
				// check constraint
				if (patron.getCardNumber() >= 100000 && patron.getCardNumber() <= 999999) {
					helper.updatePatron(patron.getCardNumber(), patron);	
					JOptionPane.showMessageDialog(null, "Update patron with ID "+patron.getCardNumber(), "Update patron", JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Card Number must be within the ranges 100000-999999!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonUpdatePatron.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdatePatron.setBounds(250, 519, 160, 40);
		panelManagement.add(buttonUpdatePatron);
		
		JButton buttonDeletePatron = new JButton("Delete");
		buttonDeletePatron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PatronHelper helper=new PatronHelper();
				int id=Integer.parseInt(textFieldCardNumber.getText().trim());
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
		
		JLabel lblLibrarianmanament = new JLabel("LibrarianManagement");
		lblLibrarianmanament.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblLibrarianmanament.setBounds(891, 80, 248, 30);
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
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LibrarianHelper helper=new LibrarianHelper();
				String keyword=textFieldLibId.getText();
				ArrayList<Librarian> results=helper.searchLibrarian("idNumber", keyword);
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
			@Override
			public void actionPerformed(ActionEvent e) {
				Librarian lib=new Librarian();
				lib.setIdNumber(Integer.parseInt(textFieldLibId.getText().trim()));
				lib.setName(textFieldLibName.getText().trim());
				lib.setAddress(textFieldLibAddress.getText().trim());
				LibrarianHelper helper=new LibrarianHelper();
				// check constraint
				if (lib.getIdNumber() >= 1000 && lib.getIdNumber() <= 9999) {
					helper.addLibrarian(lib);		
					JOptionPane.showMessageDialog(null, "Add librarian with ID "+lib.getIdNumber(), "Add librarian", JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "idNumber must be within the ranges 1000-9999!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonAddLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAddLib.setBounds(1081, 450, 160, 40);
		panelManagement.add(buttonAddLib);
		
		JButton buttonUpdateLib = new JButton("Update");
		buttonUpdateLib.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Librarian lib=new Librarian();
				lib.setIdNumber(Integer.parseInt(textFieldLibId.getText().trim()));
				lib.setName(textFieldLibName.getText().trim());
				lib.setAddress(textFieldLibAddress.getText().trim());
				LibrarianHelper helper=new LibrarianHelper();
				// check constraint
				if (lib.getIdNumber() >= 1000 && lib.getIdNumber() <= 9999) {
					helper.updateLibrarian(lib.getIdNumber(), lib);		
					JOptionPane.showMessageDialog(null, "Update librarian with ID "+lib.getIdNumber(), "Update librarian", JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "idNumber must be within the ranges 100-9999!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonUpdateLib.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonUpdateLib.setBounds(1081, 519, 160, 40);
		panelManagement.add(buttonUpdateLib);
		
		JButton buttonDeleteLib = new JButton("Delete");
		buttonDeleteLib.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LibrarianHelper helper=new LibrarianHelper();
				int id=Integer.parseInt(textFieldLibId.getText().trim());
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
			@Override
			public void actionPerformed(ActionEvent e) {
				LibrarianHelper helper=new LibrarianHelper();
				String keyword=textFieldLibName.getText();
				ArrayList<Librarian> results=helper.searchLibrarian("Name", keyword);
				if ((results!=null) && (!results.isEmpty())){
					Librarian lib=results.get(0);
					textFieldLibName.setText(lib.getName());
					textFieldLibId.setText(lib.getIdNumber()+"");
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
			@Override
			public void actionPerformed(ActionEvent e) {
				PatronHelper helper=new PatronHelper();
				String keyword=textFieldPatronName.getText();
				ArrayList<Patron> results=helper.searchPatron("name", keyword);
				if ((results!=null) && (!results.isEmpty())){
					Patron patron=results.get(0);
					textFieldCardNumber.setText(patron.getCardNumber()+"");
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
		scrollPaneReport.setBounds(35, 87, 962, 420);
		panelReport.add(scrollPaneReport);
		
		textAreaReport = new JTextArea();
		textAreaReport.setText("Here will be the generated reports.");
		textAreaReport.setEditable(false);
		textAreaReport.setLineWrap(true);
		textAreaReport.setWrapStyleWord(true);
		scrollPaneReport.setViewportView(textAreaReport);
		
		JButton btnAllcheckout = new JButton("AllCheckOuts");
		btnAllcheckout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportHelper helper=new ReportHelper();
				String timestr="1900-01-01 00:00:00";
				Timestamp current=new Timestamp(System.currentTimeMillis());
				Timestamp start=Timestamp.valueOf(timestr);
				ArrayList<CheckOut> checkouts=new ArrayList<CheckOut>();
				try {
					checkouts=helper.getAllCheckOuts(start, current);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textAreaReport.setText("");
				String title="The following is the generated report of all the checkouts in our library system.\n"
						+ "The columns are ISBN of book, start date, end date, patron's card number and librarian's id respectively.\n"
						+ "We have "+checkouts.size()+" tuples in total.\n";
				textAreaReport.append(title);
				for (CheckOut co : checkouts){
					String info=""+co.getIsbn()+"  "+co.getStart()+"  "+co.getEnd()+"  "+co.getCardNumber()+"  "+co.getIdNumber()+"\n";
					textAreaReport.append(info);
				}				
			}
		});
		btnAllcheckout.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnAllcheckout.setBounds(35, 539, 160, 40);
		panelReport.add(btnAllcheckout);
		
		JButton btnAlloverduecos = new JButton("AllOverdueCOs");
		btnAlloverduecos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportHelper helper=new ReportHelper();
				ArrayList<CheckOut> checkouts=new ArrayList<CheckOut>();
				try {
					checkouts=helper.getOverdueCO(0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textAreaReport.setText("");
				String title="The following is the generated report of all the overdued checkouts in our library system.\n"
						+ "The columns are ISBN of book, start date, end date, patron's card number and librarian's id respectively.\n"
						+ "We have "+checkouts.size()+" tuples in total.\n";
				textAreaReport.append(title);
				for (CheckOut co : checkouts){
					String info=""+co.getIsbn()+"  "+co.getStart()+"  "+co.getEnd()+"  "+co.getCardNumber()+"  "+co.getIdNumber()+"\n";
					textAreaReport.append(info);
				}			
			}
		});
		btnAlloverduecos.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnAlloverduecos.setBounds(226, 539, 160, 40);
		panelReport.add(btnAlloverduecos);
		
		JButton btnGenrecounts = new JButton("GenreCounts");
		btnGenrecounts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportHelper helper=new ReportHelper();
				Map<String, Integer> map=new HashMap<String, Integer>();
				try {
					map=helper.getGenreCounts();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textAreaReport.setText("");
				String title="The following is the generated report of  all the genres of books that have been checked out and a count for each genre. The counts are in decreasing order.\n"
						+ "The columns are genre name and counts.\n"+ "We have "+map.size()+" tuples in total.\n";
				textAreaReport.append(title);
				for(Map.Entry<String, Integer> entry:map.entrySet()){     
					String info=""+entry.getKey()+"  "+entry.getValue()+"\n";
					textAreaReport.append(info);
				}  
			}
		});
		btnGenrecounts.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnGenrecounts.setBounds(421, 539, 160, 40);
		panelReport.add(btnGenrecounts);
		
		JButton btnToppublisher = new JButton("TopPublishers");
		btnToppublisher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportHelper helper=new ReportHelper();
				HashMap<String, Integer> map=new HashMap<String, Integer>();
				try {
					map=helper.getTopPublishers();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textAreaReport.setText("");
				String title="The following is the generated report of the name(s) of the top publisher(s) and the number of books the publishers are associated with.\n"
						+ "The top publisher has the most number of books with the publisher name in the library. "
						+ "The columns are publisher name and counts.\n"+ "We have "+map.size()+" tuples in total.\n";;
				textAreaReport.append(title);
				for(Map.Entry<String, Integer> entry:map.entrySet()){     
					String info=""+entry.getKey()+"  "+entry.getValue()+"\n";
					textAreaReport.append(info);
				}  
			}
		});
		btnToppublisher.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnToppublisher.setBounds(618, 539, 160, 40);
		panelReport.add(btnToppublisher);
		
		JButton btnOutofstockbooks = new JButton("OutOfStockBooks");
		btnOutofstockbooks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportHelper helper=new ReportHelper();
				ArrayList<String> results=new ArrayList<String>();
				try {
					results=helper.getOutofStockBooks();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textAreaReport.setText("");
				String title="The following is the generated report of titles of the books that are not available (out of stock) at the library.\n"
						+ "We have "+results.size()+" tuples in total.\n";
				textAreaReport.append(title);
				for(String str:results){     
					textAreaReport.append(str+"\n");
				}  
			}
		});
		btnOutofstockbooks.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnOutofstockbooks.setBounds(817, 539, 160, 40);
		panelReport.add(btnOutofstockbooks);
		
		JButton btnSuperpatrons = new JButton("SuperPatrons");
		btnSuperpatrons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportHelper helper=new ReportHelper();				
				int count=textAreaIsbn.getLineCount();
				long[] isbn=new long[count];
				String isbns=textAreaIsbn.getText();
				String[] isbnss=isbns.split("\n");
				try{
					for (int i=0;i<count;i++)
						isbn[i]=Long.parseLong(isbnss[i].trim());
				}catch(NumberFormatException ee){
					JOptionPane.showMessageDialog(null, "Invalid input!", "Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				ArrayList<Patron> results=new ArrayList<Patron>();
				try {
					results=helper.getSuperPatrons(isbn);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if ((results!=null) && (!results.isEmpty())){	
					textAreaReport.setText("");
					String title="The following is the generated report of patrons who have checked out all the books with the corresponding inputted ISBNs.\n"
							+ "The columns are card number, number, phone, address and unpaid fees respectively.\n"
							+ "We have "+results.size()+" tuples in total.\n";
					textAreaReport.append(title);
					for(Patron p:results){    
						String str=""+p.getCardNumber()+"  "+p.getName()+"  "+p.getPhone()+"  "+p.getAddress()+"  "+p.getUnpaidFees()+"\n";
						textAreaReport.append(str);
					}  
				}
				else
					JOptionPane.showMessageDialog(null, "No result found!", "Error",JOptionPane.ERROR_MESSAGE);
			}
		});
		btnSuperpatrons.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnSuperpatrons.setBounds(1022, 539, 160, 40);
		panelReport.add(btnSuperpatrons);
		
		textAreaIsbn = new JTextArea();
		textAreaIsbn.setText("Input ISBNs here.");
		textAreaIsbn.setBounds(1036, 88, 199, 419);
		panelReport.add(textAreaIsbn);
		
		JButton btnCheckouttimeforpatronswithlargestunpaid = new JButton("CheckOutTimeForPatronsWithLargestUnpaid");
		btnCheckouttimeforpatronswithlargestunpaid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ReportHelper helper=new ReportHelper();
				Map<String, Double> map=new HashMap<String, Double>();
				try {
					map=helper.getTimeForLargestUnpaid();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textAreaReport.setText("");
				String title="The following is the generated report of average book checkout time for each patrons that have the largest unpaid fees.\n"
						+ "The columns are patron name and average checkout time in days.\n"+ "We have "+map.size()+" tuples in total.\n";;
				textAreaReport.append(title);
				for(Map.Entry<String, Double> entry:map.entrySet()){     
					String info=""+entry.getKey()+"  "+entry.getValue()+"\n";
					textAreaReport.append(info);
				}  
			}
		});
		btnCheckouttimeforpatronswithlargestunpaid.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnCheckouttimeforpatronswithlargestunpaid.setBounds(35, 601, 351, 40);
		panelReport.add(btnCheckouttimeforpatronswithlargestunpaid);
	}
}
