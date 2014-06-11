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

import dbhelper.BookHelper;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPaneMain;
	private JLabel lblWelcomeToSpicy;
	private JPanel panelLibrarian;
	private JTextField textFieldSearch;
	private JButton btnLibrarianEntrance;
	private JButton buttonAdvanced;

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
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPaneMain = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPaneMain.setBounds(0, 0, 894, 571);
		contentPane.add(tabbedPaneMain);
		
		JPanel panelMain = new JPanel();
		tabbedPaneMain.addTab("Main Page", null, panelMain, null);
		tabbedPaneMain.setEnabledAt(0, true);
		panelMain.setLayout(null);
		
		lblWelcomeToSpicy = new JLabel("Welcome to Spicy Coders' Library System");
		lblWelcomeToSpicy.setFont(new Font("Times New Roman", Font.BOLD, 36));
		lblWelcomeToSpicy.setBounds(98, 61, 684, 67);
		panelMain.add(lblWelcomeToSpicy);
		
		textFieldSearch = new JTextField();
		textFieldSearch.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 24));
		textFieldSearch.setBounds(162, 226, 400, 40);
		panelMain.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BookHelper bookHelp = new BookHelper();
			}
		});
		btnSearch.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnSearch.setBounds(584, 226, 80, 40);
		panelMain.add(btnSearch);
		
		btnLibrarianEntrance = new JButton("Librarian Entrance");
		btnLibrarianEntrance.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		btnLibrarianEntrance.setBounds(402, 320, 160, 40);
		panelMain.add(btnLibrarianEntrance);
		
		buttonAdvanced = new JButton("Advanced Search");
		buttonAdvanced.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		buttonAdvanced.setBounds(190, 320, 160, 40);
		panelMain.add(buttonAdvanced);
		
		panelLibrarian = new JPanel();
		tabbedPaneMain.addTab("Librarian", null, panelLibrarian, null);
		tabbedPaneMain.setEnabledAt(1, false);
		panelLibrarian.setLayout(null);
	}
}
