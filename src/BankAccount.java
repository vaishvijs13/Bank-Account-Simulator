package src;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;


public class BankAccount implements Serializable {
  //declaring instance variables
  private String accountNumber;
  public double balance;
  private double withdrawalFee;
  private double annualInterestRate;
  private ArrayList<Transaction> transactions = new ArrayList<>();

  //constructors
  public BankAccount(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public BankAccount(String accountNumber, double initialBalance) {
    this.accountNumber = accountNumber;
    this.balance = initialBalance;
  }

  public BankAccount(String accountNumber, double initialBalance, double withdrawalFee, double annualInterestRate) {
    this.accountNumber = accountNumber;
    this.balance = initialBalance;
    this.withdrawalFee = withdrawalFee;
    this.annualInterestRate = annualInterestRate;
  }

  //accessors for instance variables (getters)
  public String getAccountNumber() {
    return this.accountNumber;
  }

  public double getBalance() {
    return this.balance;
  }

  public double getWithdrawalFee() {
    return this.withdrawalFee;
  }

  public double getAnnualInterestRate() {
    return this.annualInterestRate;
  }

  //mutators for annualInterestRate and withdrawalFee (setters)
  public void setAnnualInterestRate(double annualInterestRate) {
    this.annualInterestRate = annualInterestRate;
  }

  public void setWithdrawalFee(double withdrawalFee) {
    this.withdrawalFee = withdrawalFee;
  }

  //first deposit method (which takes in the most parameters) is the master version
  public void deposit(LocalDateTime transactionTime, double amount, String description) {
    this.balance += amount;
    int index = -1; //index set to -1

    Transaction deposit = new Transaction(transactionTime, amount, description); //initializes deposit object with three parameters
    if (transactions.size() == 0) {
      transactions.add(deposit); //adds the deposit if the transactions list is empty
    }
    else { //iterates through existing transactions to find the correct position
      for (int i = 0; i < transactions.size(); i++) {
        Transaction currentItem = transactions.get(i);
        if (deposit.transactionTime.isBefore(currentItem.transactionTime) == true) { //if the deposit transaction time is before the current element's transaction time, sets the index and breaks out of loop
          index = i;
          break;
        }
      }

      if (index == -1) {
        transactions.add(deposit); //deposit is added to end of list if index remains -1
      }
      else {
        transactions.add(index, deposit);
      }
    }
  }

  //second and third overloaded deposit methods are linked to the master version
  public void deposit(double amount) {
    deposit(LocalDateTime.now(), amount, null);
  }

  public void deposit(double amount, String description) {
    deposit(LocalDateTime.now(), amount, description);
  }

  //first withdraw method (which takes in the most parameters) is the master version
  public void withdraw(LocalDateTime transactionTime, double amount, String description) {
    this.balance -= amount + this.withdrawalFee;
    int index = -1; //index set to -1

    Transaction withdraw = new Transaction(transactionTime, amount, description); //initializes withdraw object with three parameters
    if (transactions.size() == 0) {
      transactions.add(withdraw); //adds the withdrawal if the transactions list is empty
    }
    else { //iterates through existing transactions to find the correct position
      for (int i = 0; i < transactions.size(); i++) {
        Transaction currentItem = transactions.get(i);
        if (withdraw.transactionTime.isBefore(currentItem.transactionTime) == true) { //if the withdrawal transaction time is before the current element's transaction time, sets the index and breaks out of loop
          index = i;
          break;
        }
      }

      if (index == -1) { //withdrawal is added to end of list if index remains -1
        transactions.add(withdraw);
      }
      else {
        transactions.add(index, withdraw);
      }
    }
  }

  //second and third overloaded withdraw methods are linked to the master version
  public void withdraw(double amount) {
    withdraw(LocalDateTime.now(), amount, null);
  }

  public void withdraw(double amount, String description) {
    withdraw(LocalDateTime.now(), amount, description);
  }

  //public method isOverDrawn: returns true if balance is negative
  public boolean isOverDrawn() {
    if (this.balance < 0) {
      return true;
    }
    else {
      return false;
    }
  }

  public ArrayList<Transaction> getTransactions(LocalDateTime startTime, LocalDateTime endTime) {
    ArrayList<Transaction> list = new ArrayList<Transaction>(); //initializes object 'list' from transaction
    for (int i = 0; i < transactions.size(); i++) { // iterates through the existing transactions
      if (startTime == null && endTime != null) { //if startTime is null, but endTime is not, adds all transactions that occur before and on endTime to list
        if (transactions.get(i).getTransactionTime().isBefore(endTime)) {
          list.add(transactions.get(i));
        }
        else if (transactions.get(i).getTransactionTime().isEqual(endTime)) {
          list.add(transactions.get(i));
        }
      }

      else if (startTime != null && endTime == null) { //if endTime is null, but startTime is not, adds all transactions that occur after or on startTime to list
        if (transactions.get(i).getTransactionTime().isAfter(startTime)) {
          list.add(transactions.get(i));
        }
        else if (transactions.get(i).getTransactionTime().isEqual(startTime)) {
          list.add(transactions.get(i));
        }
      }

      else if (startTime != null && endTime != null) { //if both startTime and endTime are not null, adds all transactions that occur between startTime and endTime to list
        if (transactions.get(i).getTransactionTime().isAfter(startTime) && transactions.get(i).getTransactionTime().isBefore(endTime)) {
          list.add(transactions.get(i));
        }
        else if (transactions.get(i).getTransactionTime().isEqual(startTime) || transactions.get(i).getTransactionTime().isEqual(endTime)) {
          list.add(transactions.get(i));
        }
      }

      //if both are null, the entire list of transactions is returned
      else if (startTime == null && endTime == null) {
        return transactions;
      }
    }
    return list; //returns list between specified time range
  }

  // public method toString
  public String toString() {
    double absoluteBalance = Math.abs(this.balance); //finds absolute value of balance

    if (this.balance < 0) {
      String roundedBalance = String.format("%.2f", absoluteBalance); //rounds balance to 2 decimal places
      return("BankAccount " + this.accountNumber + ": ($" + roundedBalance + ")"); //if balance is negative, adds parentheses
    }

    else {
      String roundedBalance = String.format("%.2f", absoluteBalance);
      return("BankAccount " + this.accountNumber + ": $" + roundedBalance);
    }
  }
}
