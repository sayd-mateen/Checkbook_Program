/* Sayd Mateen
   CSC 20
   Project */

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Person implements Serializable{
	String name;
	double bal;
	double inital;
	Vector<Vector> transactions = new Vector<Vector>();
	public Person(String getName, double getBal, double starting){
		name = getName;
		bal = getBal;
		inital = starting;
	}
	public void addTransaction(Vector<String> line){
		transactions.addElement(line);
	}
	
	public Vector<Vector> getData(){
		return transactions;
	}
}

public class CheckBook implements ActionListener{
	static CardLayout contentPaneLayout;
	static Container content;
	static String name;
	static double amount;
	static JButton createAccount;
	static JButton loadTrans;
	static JButton addTrans;
	static JButton searchTrans;
	static JButton sortTrans;
	static JButton viewDeleteTrans;
	static JButton backupTrans;
	static JButton exit; 
	static JButton create;
	static JButton cancel;
	static JButton cancel2;
	static JButton load;
	static JButton saveTrans; 
	static JButton topMenu;
	static JButton topMenu2;
	static JButton topMenu3;
	static JButton topMenu4;
	static JButton sort;
	static JButton searchButton;
	static JButton deleteSelected;
	static JRadioButton byType;
	static JRadioButton byDate;
	static JTextField account;
	static JTextField balance;
	static JTextField accountName;
	static JTextField accountName2;
	static JTextField accountAmount;
	static JTextField stringSearch;
	static JTextField dateField;
	static JTextField checkNumField;
	static JTextField transDescriptionField;
	static JTextField paymentField; 
	static JTextField depositField; 
	static JScrollPane scrollPane;
	static JScrollPane scrollPane2;
	static Stack<Person> peopleStack;
	static Vector<String> columNames = new Vector<String>();
	static Vector<Vector> data;
	static Vector<Vector> data2 = new Vector<Vector>();
	static JComboBox<String> transTypeField; 
	Person person;
	JTable abtable;
	
	
	public void transactionAdd(){
		person = peopleStack.pop();
		Vector<String> line = new Vector<String>();
		String dateNumber = dateField.getText();
		String transTypeName = (String)transTypeField.getSelectedItem();
		String checkNumber = checkNumField.getText();
		String transDesName = transDescriptionField.getText();
		line.addElement(dateNumber);
		line.addElement(transTypeName);
		line.addElement(checkNumber);
		line.addElement(transDesName);
		if(!paymentField.getText().equals("")){
			double pay = Double.parseDouble(paymentField.getText());
			amount = amount - pay;
			line.addElement(Double.toString(pay));
		}else line.addElement("");
		if(!depositField.getText().equals("")){
			double dep = Double.parseDouble(depositField.getText());
			amount = amount + dep;
			line.addElement(Double.toString(dep));
		}else line.addElement("");
		line.addElement(Double.toString(amount));
		person.bal = amount;
		balance.setText("" + amount);
		person.addTransaction(line);
		peopleStack.push(person);
		dateField.setText("");
		checkNumField.setText("");
		transDescriptionField.setText("");
		paymentField.setText("");
		depositField.setText("");
		contentPaneLayout.show(content, "Main");
			
	}
	
	public void createPerson(){
		name = accountName.getText();
		amount = Double.parseDouble(accountAmount.getText());
		account.setText(name);
		balance.setText("" + amount);
		accountName.setText("");
		accountAmount.setText("");
		contentPaneLayout.show(content, "Main");
		person = new Person(name, amount, amount);
		data = person.transactions;
		peopleStack.push(person);
	}
	
	public void backUp(){
		while(!peopleStack.empty()){
			Person get = peopleStack.pop();
			name = get.name;
			try {	FileOutputStream fos = new FileOutputStream (name, false);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(get); 
					oos.close();
			} catch(FileNotFoundException ex){ System.out.println(ex.toString());
			} catch(IOException ex){ ex.printStackTrace();}
		}
	}
	
	public void loadUp(){
		name = accountName2.getText();
		try {
			FileInputStream fis = new FileInputStream (name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			person = (Person) ois.readObject();
			peopleStack.push(person);
			amount = person.bal;
			account.setText(name);
			balance.setText("" + amount);
			accountName2.setText("");
			data = person.getData();
			contentPaneLayout.show(content, "Main");
		} catch(EOFException ex){
		} catch(Exception ex) {ex.printStackTrace(); }
	}
	
	public void deleteRow(){
		Vector<Vector> newData = new Vector<Vector>();
		data.remove(abtable.getSelectedRow());
		double total = person.inital;
		for(Vector v: data){
			v.remove(6);
			String getPay = (String)v.remove(4);
			String getDep = (String)v.remove(4);
			if(!getPay.equals("")){
				double num =  Double.parseDouble(getPay);
				total = total - num;
				v.addElement(Double.toString(num));
			}else v.addElement("");
			if(!getDep.equals("")){
				double num =  Double.parseDouble(getDep);
				total = total + num;
				v.addElement(Double.toString(num));
			}else v.addElement("");
			v.addElement(Double.toString(total));
			newData.addElement(v);
		}
		amount = total;
		person.bal = total;
		balance.setText("" + amount);
		data = newData;
		showTable();
	}
	
	public void showTable(){
		abtable = new JTable(data, columNames);
		JScrollPane tmp = new JScrollPane(abtable);
		scrollPane.setViewport(tmp.getViewport());
	}
	
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		
		if(source==createAccount) contentPaneLayout.show(content, "Add");
		if(source==cancel || source==cancel2 || source == topMenu || source == topMenu2 || source == topMenu3 || source == topMenu4) contentPaneLayout.show(content, "Main");
		if(source==loadTrans) contentPaneLayout.show(content, "Load");
		if(source==addTrans) contentPaneLayout.show(content, "Trans");
		if(source==sortTrans) contentPaneLayout.show(content, "Sort");
		if(source==searchTrans) contentPaneLayout.show(content, "Search");
		if(source==viewDeleteTrans) {
			contentPaneLayout.show(content, "View");
			showTable();
		}
		if(source==exit) System.exit(0);
		if(source==create) createPerson();
		if(source==saveTrans) transactionAdd();
		if(source==backupTrans) backUp();
		if(source==load) loadUp();
		if(source==deleteSelected) deleteRow();
		
		//For search table
		JTable abbtable = new JTable(data2, columNames);
		JScrollPane temp = new JScrollPane(abbtable);
		scrollPane2.setViewport(temp.getViewport());
		
	}
	
	public static void main(String[] args){	
		columNames.add("Date");
		columNames.add("Trans. Type");
		columNames.add("Check No.");
		columNames.add("Trans. Description");
		columNames.add("Payment/Debit(-)");
		columNames.add("Deposit/Credit(+)");
		columNames.add("Balance");	
		
		peopleStack = new Stack<Person>();
		JFrame frm = new JFrame("CheckBook");
		content = frm.getContentPane();
		content.setLayout(contentPaneLayout=new CardLayout());
		ActionListener AL = new CheckBook();
		
		//Start of Main Page Card  
		JPanel mainCard = new JPanel(new BorderLayout());
		JPanel panelMainTop = new JPanel(); 
		JPanel panelMainCenter = new JPanel();
		JPanel panelMainLower = new JPanel(new GridLayout(2, 4));
		
		JLabel title = new JLabel("Use The Buttons Below To Manage Transactions");
		title.setFont(new Font("Serif", Font.PLAIN, 24));  
		account = new JTextField("", 10);
		account.setEditable(false);
		balance = new JTextField("0.0", 10);
		balance.setHorizontalAlignment(JTextField.RIGHT);
 		balance.setEditable(false);
		
		createAccount = new JButton("Create a New Account");
		loadTrans = new JButton("Load a Trans from a file");
		addTrans = new JButton("Add New Transaction");
		searchTrans = new JButton("Search Transactions");
		sortTrans = new JButton("Sort Transactions");
		viewDeleteTrans = new JButton("View/Delete Transactions");
		backupTrans = new JButton("Backup Transactions");
		exit = new JButton("Exit");
		
		createAccount.addActionListener(AL);
		loadTrans.addActionListener(AL);
		addTrans.addActionListener(AL);
		searchTrans.addActionListener(AL);
		sortTrans.addActionListener(AL);
		viewDeleteTrans.addActionListener(AL);
		backupTrans.addActionListener(AL);
		exit.addActionListener(AL);
		
		panelMainTop.add(title, JLabel.CENTER); 
		panelMainCenter.add(new JLabel("Account Name:", JLabel.CENTER));	
		panelMainCenter.add(account);
		panelMainCenter.add(new JLabel("Balance:", JLabel.CENTER)); 
		panelMainCenter.add(balance);
		panelMainLower.add(createAccount);
		panelMainLower.add(loadTrans);
		panelMainLower.add(addTrans);
		panelMainLower.add(searchTrans);
		panelMainLower.add(sortTrans);
		panelMainLower.add(viewDeleteTrans);
		panelMainLower.add(backupTrans);
		panelMainLower.add(exit);
		
		mainCard.add(panelMainTop, BorderLayout.NORTH);
		mainCard.add(panelMainCenter, BorderLayout.CENTER);
		mainCard.add(panelMainLower, BorderLayout.SOUTH);
		content.add("Main", mainCard);
		contentPaneLayout.show(content, "Main");
		
		
		//Start of Create Account Card
		JPanel addCard = new JPanel();
		JPanel line1 = new JPanel();
		JPanel line2 = new JPanel();
		JPanel line3 = new JPanel();
		
		addCard.setLayout(new GridLayout(4, 1));
		JLabel addTitle = new JLabel("Create an Account", JLabel.CENTER);
		addTitle.setFont(new Font("Serif", Font.PLAIN, 24)); 
		accountName = new JTextField("", 10);
		accountAmount = new JTextField("", 10);
		create = new JButton("Create");
		cancel = new JButton("Cancel");
		
		accountName.addActionListener(AL);
		accountAmount.addActionListener(AL);
		create.addActionListener(AL);
		cancel.addActionListener(AL);
		
		line1.add(new JLabel("Account Name:", JLabel.CENTER));
		line1.add(accountName);
		line2.add(new JLabel("Initial Amount:", JLabel.CENTER));
		line2.add(accountAmount);
		line3.add(create);
		line3.add(cancel);
		
		addCard.add(addTitle);
		addCard.add(line1);
		addCard.add(line2);
		addCard.add(line3);
		content.add("Add", addCard);
		
		
		//Start of Load Transaction 
		JPanel loadCard = new JPanel();
		JPanel loadLine1 = new JPanel();
		JPanel loadLine2 = new JPanel();
		
		loadCard.setLayout(new GridLayout(3, 1));
		JLabel addTitle2 = new JLabel("Load Transactions From a File", JLabel.CENTER);
		addTitle2.setFont(new Font("Serif", Font.PLAIN, 24));
		accountName2 = new JTextField("", 10);
		load = new JButton("Load");
		cancel2 = new JButton("Cancel");
		
		accountName2.addActionListener(AL);
		load.addActionListener(AL);
		cancel2.addActionListener(AL);
		
		loadLine1.add(new JLabel("Account Name:", JLabel.CENTER));
		loadLine1.add(accountName2);
		loadLine2.add(load);
		loadLine2.add(cancel2);
		
		loadCard.add(addTitle2);
		loadCard.add(loadLine1);
		loadCard.add(loadLine2);
		content.add("Load", loadCard);
		
		
		//Start of Add New Transaction 
		JPanel addTransCard = new JPanel(new BorderLayout());
		JPanel transTop = new JPanel(new GridLayout(6, 2));
		JPanel transLine1 = new JPanel();
		String[] transComboBox = {"Deposit", "Automatic deposit", "ATM Withdrawal", "Check, or Debit Card"};
		
		JLabel date = new JLabel("Date");
		JLabel transType = new JLabel("Trans. Type");
		JLabel checkNum = new JLabel("Check No.");
		JLabel transDescription = new JLabel("Trans.Description");
		JLabel payment = new JLabel("Payment/Debit(-)");
		JLabel deposit = new JLabel("Deposit/Credit(+)");
		transTypeField = new JComboBox<String>(transComboBox);
		transTypeField.setEditable(false);
		
	    date.setHorizontalAlignment(JLabel.RIGHT);
	    transType.setHorizontalAlignment(JLabel.RIGHT);
	    checkNum.setHorizontalAlignment(JLabel.RIGHT);
	    transDescription.setHorizontalAlignment(JLabel.RIGHT);
	    payment.setHorizontalAlignment(JLabel.RIGHT);
	    deposit.setHorizontalAlignment(JLabel.RIGHT);
		
		
		dateField = new JTextField("");
		checkNumField = new JTextField("");
		transDescriptionField = new JTextField("");
		paymentField = new JTextField("");
		depositField = new JTextField("");
		saveTrans = new JButton("Save New Transaction");
		topMenu = new JButton("Top Menu");
		
		transTypeField.addActionListener(AL);
		saveTrans.addActionListener(AL);
		topMenu.addActionListener(AL);
		
		transLine1.add(saveTrans);
		transLine1.add(topMenu);
		
		transTop.add(date);
		transTop.add(dateField);
		transTop.add(transType);
		transTop.add(transTypeField);
		transTop.add(checkNum);
		transTop.add(checkNumField);
		transTop.add(transDescription);
		transTop.add(transDescriptionField);
		transTop.add(payment);
		transTop.add(paymentField);
		transTop.add(deposit);
		transTop.add(depositField);
		
		addTransCard.add(transTop, BorderLayout.CENTER);
		addTransCard.add(transLine1, BorderLayout.SOUTH);
		content.add("Trans", addTransCard);
		
		//Start of Sort Transaction 
		JPanel addSortCard = new JPanel(new BorderLayout());
		JPanel sortCenter = new JPanel();
		JPanel sortBottom = new JPanel();
		
		JLabel addTitle3 = new JLabel("Sort Transactions", JLabel.CENTER);
		addTitle3.setFont(new Font("Serif", Font.PLAIN, 24));
		
		byType = new JRadioButton("By Type");
		byDate = new JRadioButton("By Date");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(byType);
		buttonGroup.add(byDate);
		sort = new JButton("Sort");
		topMenu2 = new JButton("Top Menu");
		
		byType.addActionListener(AL);
		byDate.addActionListener(AL);
		sort.addActionListener(AL);
		topMenu2.addActionListener(AL);
		
		sortCenter.add(byType);
		sortCenter.add(byDate);
		sortBottom.add(sort);
		sortBottom.add(topMenu2);
		addSortCard.add(addTitle3, BorderLayout.NORTH);
		addSortCard.add(sortCenter, BorderLayout.CENTER);
		addSortCard.add(sortBottom, BorderLayout.SOUTH);
		content.add("Sort", addSortCard);
		
		//Start of Search Transactions 
		JPanel addSearchCard = new JPanel(new BorderLayout());
		JPanel bottomLine = new JPanel(new BorderLayout());
		JPanel bottomLine1 = new JPanel();
		JPanel bottomLine2 = new JPanel();
		
		JLabel addTitle4 = new JLabel("Search Transactions by Transaction Date/Type/Check No./Description", JLabel.CENTER);
		JLabel stringSearchLabel = new JLabel("Search String");
		stringSearch = new JTextField("", 10);
		scrollPane2 = new JScrollPane();
		searchButton = new JButton("Search");
		topMenu3 = new JButton("Top Menu");
		
		stringSearch.addActionListener(AL);
		searchButton.addActionListener(AL);
		topMenu3.addActionListener(AL);
		
		bottomLine1.add(stringSearchLabel);
		bottomLine1.add(stringSearch);
		bottomLine2.add(searchButton);
		bottomLine2.add(topMenu3);
		
		bottomLine.add(bottomLine1,BorderLayout.NORTH);
		bottomLine.add(bottomLine2,BorderLayout.SOUTH);
		
		addSearchCard.add(addTitle4, BorderLayout.NORTH);
		addSearchCard.add(scrollPane2, BorderLayout.CENTER);
		addSearchCard.add(bottomLine, BorderLayout.SOUTH);
		content.add("Search", addSearchCard);
		
		//Start of view/Delete Transactions 
		JPanel addViewDeleteCard = new JPanel(new BorderLayout());
		JPanel bottomL1 = new JPanel();
		
		JLabel addTitle5 = new JLabel("Transactions Currently in the CheckBook ", JLabel.CENTER);
		scrollPane = new JScrollPane();
		deleteSelected = new JButton("Delete Selected");
		topMenu4 = new JButton("Top Menu");
		
		deleteSelected.addActionListener(AL);
		topMenu4.addActionListener(AL);
		
		bottomL1.add(deleteSelected);
		bottomL1.add(topMenu4);
		
		addViewDeleteCard.add(addTitle5, BorderLayout.NORTH);
		addViewDeleteCard.add(scrollPane, BorderLayout.CENTER);
		addViewDeleteCard.add(bottomL1, BorderLayout.SOUTH);
		content.add("View", addViewDeleteCard);
				
		frm.pack();
		frm.setSize(718,220);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
	
}
