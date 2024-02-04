import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WindowFour extends JFrame {
  private JPanel contentPane;
  private JTextField txtStart;
  private JTextField txtEnd;
  private JTextArea transactions;
  private BankAccount savings;
  private BankAccount chequing;
  private String name;
  private JScrollPane scrollPane;
  
  public WindowFour(String name, BankAccount chequing, BankAccount savings) {
    this.name = name;
    this.chequing = chequing;
    this.savings = savings;
    setTitle("Transaction History");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(0, 0, 300, 300);
    contentPane = new JPanel();
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JLabel lblInputDirections = new JLabel("Input your start and end date:"); //asks to input start and end date
    lblInputDirections.setBounds(15, 10, 300, 25);
    contentPane.add(lblInputDirections);

    JLabel lblStart = new JLabel("Start Date (yyyy-MM-dd): ");
    lblStart.setBounds(15, 50, 190, 25);
    contentPane.add(lblStart);
    txtStart = new JTextField(); //text field for start date
    txtStart.setBounds(200, 50, 90, 25);
    contentPane.add(txtStart);
    
    JLabel lblEnd = new JLabel("End Date (yyyy-MM-dd): ");
    lblEnd.setBounds(15, 90, 180, 25);
    contentPane.add(lblEnd);
    txtEnd = new JTextField(); //text field for end date
    txtEnd.setBounds(200, 90, 90, 25);
    contentPane.add(txtEnd);

    transactions = new JTextArea(); //scrollable text area for displayed transactions to be viewed
    transactions.setEditable(false);
    scrollPane = new JScrollPane(transactions);
    scrollPane.setBounds(10, 200, 280, 80);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    contentPane.add(scrollPane);

    JButton btnViewTransactions = new JButton("View Transactions");
    btnViewTransactions.setBounds(65, 130, 170, 25);
    btnViewTransactions.addMouseListener(new MouseAdapter() { //event handler for when the View Transactions button is clicked
      public void mouseClicked(MouseEvent e) {
        history();
      }
    });
    contentPane.add(btnViewTransactions);

    JButton btnDone = new JButton("Done");
    btnDone.addMouseListener(new MouseAdapter() { //event handler for Done button: goes back to main window
      public void mouseClicked(MouseEvent e) {
        done();
      }
    });    
    btnDone.setBounds(105, 160, 90, 25);
    contentPane.add(btnDone);
  }

  public void history() {
    transactions.setText("");
    ArrayList<Transaction> transactionsInRange = new ArrayList(); //array list that will hold deposits and withdrawals
    String startTime = String.valueOf(txtStart.getText()); //converts user input to start time
    String endTime = String.valueOf(txtEnd.getText()); //converts user input to end time
    DateTimeFormatter formatting = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //input format

    //no input means null
    if (startTime.equals("")) {
      startTime = null;
    }

    if (endTime.equals("")) {
      endTime = null;
    }

    //stores transactions in chequing or savings account if dates are specified
    if (startTime != null && endTime != null) {
      LocalDate startDate = LocalDate.parse(startTime, formatting);
      LocalDate endDate = LocalDate.parse(endTime, formatting);
      LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalDateTime.now().toLocalTime());
      LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalDateTime.now().toLocalTime());

      if (name.equals("Chequing")) {
        ArrayList<Transaction> transactions = chequing.getTransactions(startDateTime, endDateTime);
        transactionsInRange = transactions;
      }
      else if (name.equals("Savings")) {
        ArrayList<Transaction> transactions = savings.getTransactions(startDateTime, endDateTime);
        transactionsInRange = transactions;
      }
    }

    //stores transactions in chequing or savings account if only start date is specified
    else if (startTime != null && endTime == null) {
      LocalDate startDate = LocalDate.parse(startTime, formatting);
      LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalDateTime.now().toLocalTime());

      if (name.equals("Chequing")) {
        ArrayList<Transaction> transactions = chequing.getTransactions(startDateTime, null);
        transactionsInRange = transactions;
      }
      else if (name.equals("Savings")) {
        ArrayList<Transaction> transactions = savings.getTransactions(startDateTime, null);
        transactionsInRange = transactions;
      }
    }

    //stores transactions in chequing or savings account if only end date is specified
    else if (startTime == null && endTime != null) {
      LocalDate endDate = LocalDate.parse(endTime, formatting);
      LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalDateTime.now().toLocalTime());

      if (name.equals("Chequing")) {
        ArrayList<Transaction> transactions = chequing.getTransactions(null, endDateTime);
        transactionsInRange = transactions;
      }
      else if (name.equals("Savings")) {
        ArrayList<Transaction> transactions = savings.getTransactions(null, endDateTime);
        transactionsInRange = transactions;
      }
    }

    //stores transactions in chequing or savings account if no dates are specified (ie. all transactions)
    else if (startTime == null && endTime == null) {
      if (name.equals("Chequing")) {
        ArrayList<Transaction> transactions = chequing.getTransactions(null, null);
        transactionsInRange = transactions;
      }
      else if (name.equals("Savings")) {
        ArrayList<Transaction> transactions = savings.getTransactions(null, null);
        transactionsInRange = transactions;
      }
    }
    
    DateTimeFormatter format= DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
    for (int i = 0; i<transactionsInRange.size(); i++) { //for loop until all transactions are outputted
      double balance = transactionsInRange.get(i).getAmount();
      String finalBalance = String.format("%.2f", balance);
      transactions.append(transactionsInRange.get(i).getDescription() + ": " + transactionsInRange.get(i).getTransactionTime().format(format) + ": $" + finalBalance + "\n"); //formats transactions in user friendly manner
    }
  }
  
  public void done() { //exits window when "done" is clicked
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }
}
