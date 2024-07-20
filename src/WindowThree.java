package src;
import javax.swing.*;

import java.awt.event.*;

public class WindowThree extends JFrame {
  private JPanel contentPane;
  private JTextField txtDeposit;
  protected JTextField textField;
  private BankAccount chequing;
  private BankAccount savings;
  private String name;

  //creates the frame
  public WindowThree(String name, BankAccount chequing, BankAccount savings) {
    this.name = name;
    this.chequing = chequing;
    this.savings = savings;
    setTitle("Deposit");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(0, 0, 300, 300);
    contentPane = new JPanel();
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JLabel lblInputOne = new JLabel("Please provide the amount");
    lblInputOne.setBounds(20, 10, 280, 45);
    contentPane.add(lblInputOne);

    JLabel lblInputTwo = new JLabel("to be deposited:"); //follows format of example interface
    lblInputTwo.setBounds(20, 40, 280, 45);
    contentPane.add(lblInputTwo);

    txtDeposit = new JTextField();
    txtDeposit.setText("0.00");
    txtDeposit.setBounds(70, 120, 145, 25);
    txtDeposit.setColumns(10);
    contentPane.add(txtDeposit);

    JButton btnOK = new JButton("OK");
    btnOK.addMouseListener(new MouseAdapter() { //event handler when OK button is clicked
      public void mouseClicked(MouseEvent e) {
          try {
            depositMoney();
            done();
          }
          catch (Exception e1) {
            JOptionPane.showMessageDialog(WindowThree.this, "Please enter a valid number or select an account!"); //pop-up message (dialog) if a number is not entered or an account is not selected
          }
        }
    });
    btnOK.setBounds(25, 170, 85, 20);
    contentPane.add(btnOK);

    JButton btnCancel = new JButton("Cancel");
    btnCancel.addMouseListener(new MouseAdapter() { //event handler when Cancel button is clicked
      public void mouseClicked(MouseEvent e) {
        done();
      }
    });
    btnCancel.setBounds(180, 170, 85, 20);
    contentPane.add(btnCancel);
  }

  //deposits money into the selected account, referring to deposit method in BankAccount.java
  public void depositMoney() {
    double balance = Double.valueOf(txtDeposit.getText());
    if (name.equals("Chequing")) {
      chequing.deposit(balance, "Deposit"); //description is added with transactions
    }
    else {
      savings.deposit(balance, "Deposit");;
    }
  }

  //closes window and reroutes to the main menu
  public void done(){
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }
}