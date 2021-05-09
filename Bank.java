// By: Emily Dayanghirang
// Date: April 9, 2021
// Performs a simple functionality of a bank

import java.util.Scanner;
import java.text.DecimalFormat;

public class Bank {
    static BankAccount[] accounts = new BankAccount[1]; //This array will point to all the Bank.java.BankAccount objects
    static Scanner scnr = new Scanner(System.in);
    static int noOfAccs = 0;           //Keeps track of the total number of accounts
    static DecimalFormat moneyFormat = new DecimalFormat("$0.00");

    public static void main(String[] args) {
        bankMenu(); //call bankMenu
    }

    //bankMenu runs the main part of the program, until user selects 'Q'
    static void bankMenu() {
        int currentIndex = -1;	//This variable tells us whether we are pointing to a valid account
        String userSelection;
        boolean quit = false;

        do {
            //display menu and ask for user selection
            printMenu(currentIndex);
            userSelection = scnr.next();

            switch(userSelection.toUpperCase()) {
                case "O": //Open account
                    //make sure you have enough space or double size OF accounts array
                    //make sure the account number is not a duplicate. Assign array index to account
                    //set the current index;
                    //increment the number of accounts

                    accounts[noOfAccs] = openAcc(noOfAccs); //Appends new account
                    currentIndex = noOfAccs - 1;

                    if (noOfAccs == accounts.length) {
                        accounts = resize();
                    }
                    break;
                case "D": //Deposit
                    //deposit only if currentIndex is not -1. you are depositing into a particular account
                    if (currentIndex != -1) {
                        accounts[currentIndex].deposit();
                    }
                    else
                    {
                        if (noOfAccs > 0) {
                            System.out.println("\nPlease select an account.");
                        }
                        else {
                            System.out.println("\nThere are no current accounts in the bank.\n" +
                                    "Please open an account.");
                        }
                    }
                    break;
                case "S": //Select account
                    //look for account and if it exists, set currentIndex to it
                    if (noOfAccs > 0) {
                        currentIndex = selectAcc(currentIndex);
                    }
                    else {
                        System.out.println("\nThere are no current accounts in the bank.\n" +
                                "Please open an account.");
                    }
                    break;
                case "C": //Close account
                    //if currentindex is not -1 close the account and reset currentIndex
                    if (currentIndex != -1) {
                        closeAcc(currentIndex);
                        /* After closing an account,
                           the program deselects any account
                           by having currentIndex's value
                           return to its programmer-defined
                           default value of -1
                        */
                        currentIndex = -1;
                        break;
                    }
                    else
                    {
                        if (noOfAccs > 0) {
                            System.out.println("\nPlease select an account.");
                        }
                        else {
                            System.out.println("\nThere are no current accounts in the bank.\n" +
                                    "Please open an account.");
                        }
                    }
                    break;
                case "W": //Withdraw
                    //if current index is not -1, withdraw
                    if (currentIndex != -1) {
                        accounts[currentIndex].withdraw();
                    }
                    else {
                        if (noOfAccs > 0) {
                            System.out.println("\nPlease select an account.");
                        }
                        else {
                            System.out.println("\nThere are no current accounts in the bank.\n" +
                                    "Please open an account.");
                        }
                    }
                    break;
                case "L": //List accounts
                    //traverse through all the accounts and display their information
                    if (noOfAccs > 0) {
                        listAccounts();
                    }
                    else {
                        System.out.println("\nThere are no current accounts in the bank.\n" +
                                            "Please open an account.");
                    }
                    break;
                case "Q": //Quit
                    //end the program
                    quit = true;
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("\nInvalid command. Try again.");
                    break;
            }
        } while(!quit);
    }

    //Print the menu, takes index of currently selected account
    static void printMenu(int currentIndex) {
        //display menu
        //if index is not -1 display the account information
        System.out.println("\nO: Open account\n" +
                        "D: Deposit\n" +
                        "S: Select account\n" +
                        "C: Close account\n" +
                        "W: Withdraw\n" +
                        "L: List all accounts\n" +
                        "Q: Quit\n");

        System.out.print("current account selected: ");

        if (currentIndex != -1) {
            System.out.println(accounts[currentIndex].getAcc() +
                               "\tBalance: " + moneyFormat.format(accounts[currentIndex].getBalance()));
        }
        else {
            System.out.println("NONE");
        }

        System.out.print("\nEnter command: ");
    }


    static BankAccount openAcc(int index) {
        //Grab account number
        //validate for duplicate account number
        //Grab balance
        //create new account and return the object so that the array index can point to the
        //newly created object
        int newAccNum;
        double initialBalance;
        boolean duplicate;

        do {
            duplicate = false;

            System.out.print("Enter new account number: ");
            newAccNum = scnr.nextInt();

            for (int i = 0; i < noOfAccs; ++i)
            {
                if (accounts[i].getAcc() == newAccNum)
                {
                    System.out.println("\nThere is already an existing account\n" +
                            "that has the account number that you provided.\n" +
                            "Please try again.\n");

                    duplicate = true;
                }
            }
        } while (duplicate);

        System.out.print("Enter initial balance: ");
        initialBalance = scnr.nextDouble();

        accounts[index] = new BankAccount(newAccNum, initialBalance);
        ++noOfAccs;

        return accounts[index];
    }

    static BankAccount[] resize() {
        //resize array. Double the size
        BankAccount[] temp = new BankAccount[2 * noOfAccs];

        System.arraycopy(accounts, 0, temp, 0, noOfAccs);

        accounts = temp;

        return accounts;
    }

    static void listAccounts() {
        //Go through all the accounts using a for loop and display their content
        double total = 0;

        System.out.println("");

        for (int i = 0; i < noOfAccs; ++i) {
            System.out.println((i+1) + ") Acct#: " +
                    accounts[i].getAcc() + "\tBal: " +
                    moneyFormat.format(accounts[i].getBalance()));
            total += accounts[i].getBalance();
        }

        System.out.println("\nTotal Bank assets: " + moneyFormat.format(total));
    }

    static int selectAcc(int currentIndex) {
        //ask for the account number, check to see if it exists and return index
        int accNum;
        boolean foundAcc = false;

        do {
            System.out.print("Enter account number: ");
            accNum = scnr.nextInt();

            for (int i = 0; i < noOfAccs; ++i) {
                if (accounts[i].getAcc() == accNum) {
                    currentIndex = i;
                    foundAcc = true;
                }
            }

            if (!foundAcc) {
                System.out.println("\nAccount number was not found\n" +
                        "Please try again.\n");
            }

        } while (!foundAcc);

        return currentIndex;
    }

    static void closeAcc(int currentIndex) {
        //move last account to the index that needs to be deleted
        //set last account to null
        //decrement noOfAccts variable
        if (noOfAccs > 1) {
            accounts[currentIndex] = accounts[noOfAccs - 1];
            accounts[noOfAccs - 1] = null;
        }
        --noOfAccs;
    }
}

class BankAccount {
    private int accNbr;
    private double balance;

    BankAccount(int accNbr, double balance) {
        //set instance variables
        this.accNbr = accNbr;
        this.balance = balance;
    }

    int getAcc() {
        //return accountNumber
        return accNbr;
    }

    double getBalance() {
        //return balance
        return balance;
    }

    void deposit() {
        //add to deposit
        double deposit;
        System.out.print("Enter amount of deposit: ");
        deposit = Bank.scnr.nextDouble();
        balance += deposit;
    }

    void withdraw () {
        //withdraw as long as there is still $1 in the account
        double withdraw;
        System.out.print("Enter amount to withdraw: ");
        withdraw = Bank.scnr.nextDouble();
        while ((balance - withdraw) < 1) {
            System.out.println("\nYou are withdrawing too much: try again");
            System.out.print("\nEnter amount to withdraw: ");
            withdraw = Bank.scnr.nextDouble();
        }
        balance -= withdraw;
    }
}
