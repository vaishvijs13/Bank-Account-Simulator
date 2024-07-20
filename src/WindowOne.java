package src;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

import java.text.DecimalFormat;

public class WindowOne extends JFrame implements WindowListener, ActionListener {
  //declares object for savings and chequing account
  BankAccount savings = new BankAccount("Savings", 0, 0, 0);
  BankAccount chequing = new BankAccount("Chequing", 0, 0, 0);

  private JPanel contentPane;
  JComboBox<String> comboBox;
  public double balance;
  private String name;
  private JLabel lblDisplayBalance;
  private DecimalFormat df = new DecimalFormat("0.00");

  //returns balance in each account by referring to code in BankAccount.java
  public double findBalance(String name) {
    double balance = 0;
    if (name.equals("Chequing")) {
      balance = chequing.balance;
    }
    else if (name.equals("Savings")) {
      balance = savings.balance;
    }
    return balance;
  }

  //launches application
  public static void main (String [] args) {
    EventQueue.invokeLater(new Runnable () {
      public void run() {
        try {
          WindowOne frame = new WindowOne();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  //convert from .java to .txt
  public void serialize(String name, BankAccount account){
    try {
      if (name.equals("Chequing")) {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("chequing.txt"));
        outputStream.writeObject(account);
        outputStream.close();
      } else if (name.equals("Savings")) {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("savings.txt"));
        outputStream.writeObject(account);
        outputStream.close();
      }
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  //convert from .txt to .java
  public BankAccount deserialize(String name){
    BankAccount x = null;
    try {
      if(name.equals("Chequing")){
        FileInputStream inputStream = new FileInputStream("chequing.txt");
        ObjectInputStream reader = new ObjectInputStream(inputStream);
        x = (BankAccount)reader.readObject();
      }
      else if (name.equals("Savings")){
        FileInputStream inputStream = new FileInputStream("savings.txt");
        ObjectInputStream reader = new ObjectInputStream(inputStream);
        x = (BankAccount)reader.readObject();
      }
    }
    catch(IOException e) {
      System.out.println(e);
    }
    catch(ClassNotFoundException e) {
      System.out.println(e);
    }
    return x;
  }

  //Deserializes objects based on the data they contain in their txt files. Serializer is used to implement a data layer to persist the current balance and all transactions made on the accounts
  public WindowOne() {
    if (deserialize("Chequing") == null) {
      chequing = new BankAccount("Chequing",0,0,0);
    } else {
      chequing = deserialize("Chequing");
    }
    if (deserialize("Savings") == null){
      savings = new BankAccount("Savings",0,0,0);
    } else {
      savings = deserialize("Savings");
    }
    
    setTitle("Bank");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(0, 0, 300, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JButton btnWithdraw = new JButton("Withdraw");
    btnWithdraw.setBounds(20, 180, 110, 27);
    contentPane.add(btnWithdraw);

    btnWithdraw.addMouseListener(new MouseAdapter() { //event handler for withdraw button: will open up new window
      public void mouseClicked(MouseEvent e) {
        openWindowTwo();
      }
    });

    JButton btnDeposit = new JButton("Deposit");
    btnDeposit.setBounds(175, 180, 110, 27);
    contentPane.add(btnDeposit);

    btnDeposit.addMouseListener(new MouseAdapter() { //event handler for deposit button: will open up new window
      public void mouseClicked(MouseEvent e) {
        openWindowThree();
      }
    });

    JButton btnTransactionHistory = new JButton("Transaction History");
    btnTransactionHistory.setBounds(60, 230, 180, 27);
    contentPane.add(btnTransactionHistory);

    btnTransactionHistory.addMouseListener(new MouseAdapter() { //event handler for transaction history button: will open up new window
      public void mouseClicked(MouseEvent e) {
        openWindowFour();
      }
    });    

    JLabel lblAccount = new JLabel("Account");
    lblAccount.setBounds(20, 20, 90, 25);
    contentPane.add(lblAccount);

    JLabel lblBalance = new JLabel("Current Balance: ");
    lblBalance.setBounds(20, 80, 140, 25);
    contentPane.add(lblBalance);

    lblDisplayBalance = new JLabel(String.format("%.2f", balance)); //displays balance
    lblDisplayBalance.setBounds(150, 80, 140, 25);
    contentPane.add(lblDisplayBalance);

    String[] account = {"Select an Account", "Chequing", "Savings"}; //options to select account
    comboBox = new JComboBox<>(account);
    comboBox.setBounds(100, 20, 170, 25);
    contentPane.add(comboBox);
    comboBox.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == comboBox) {
      String accountName = (String)comboBox.getSelectedItem();
      if (accountName.equals("Chequing")) {
        name = "Chequing";
        balance = chequing.balance; //retrieves balance and displays it in either black or red, based on whether it it overdrawn
        if (balance < 0) {
          lblDisplayBalance.setForeground(Color.RED);
          lblDisplayBalance.setText(df.format(Math.abs(balance)));
        }
        else {
          lblDisplayBalance.setForeground(Color.BLACK);
          lblDisplayBalance.setText(df.format(Math.abs(balance)));
        }
      }

      else if (accountName.equals("Savings")) {
        name = "Savings";
        balance = savings.balance; //retrieves balance and displays it in either black or red, based on whether it it overdrawn
        if (balance < 0) {
          lblDisplayBalance.setForeground(Color.RED);
          lblDisplayBalance.setText(df.format(Math.abs(balance)));
        }
        else {
          lblDisplayBalance.setForeground(Color.BLACK);
          lblDisplayBalance.setText(df.format(Math.abs(balance)));
        }
      }
      else {
        lblDisplayBalance.setText(""); //displays empty string if "Select an Account" is selected
      }
    }
  }

  //opens up new window for withdrawal, deposit, and transactions
  public void openWindowTwo() {
    WindowTwo two = new WindowTwo(name, chequing, savings);
    two.addWindowListener(this);
    two.setVisible(true);
  }

  public void openWindowThree() {
    WindowThree three = new WindowThree(name, chequing, savings);
    three.addWindowListener(this);
    three.setVisible(true);
  }

  public void openWindowFour() {
    WindowFour four = new WindowFour(name, chequing, savings);
    four.addWindowListener(this);
    four.setVisible(true);
  }

  public void windowOpened(WindowEvent e) {
  }

  //serializes data when window is closed
  public void windowClosing(WindowEvent e) {
    serialize("Chequing", chequing);
    serialize("Savings", savings);
  }

  public void windowClosed(WindowEvent e) {
    if (findBalance(name) < 0) {
      lblDisplayBalance.setForeground(Color.RED);
      double balance = findBalance(name);
      lblDisplayBalance.setText(df.format(Math.abs(balance)));
    }
    else {
      lblDisplayBalance.setForeground(Color.BLACK);
      double balance = findBalance(name);
      lblDisplayBalance.setText(df.format(balance));
    }
  }

  public void windowIconified(WindowEvent e) {
  }

  public void windowDeiconified(WindowEvent e) {
  }

  public void windowActivated(WindowEvent e) {
  }

  public void windowDeactivated(WindowEvent e) {
  }
}
