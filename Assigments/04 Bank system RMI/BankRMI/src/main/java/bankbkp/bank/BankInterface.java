package bankbkp.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankInterface extends Remote {
    double getBalance() throws RemoteException;
    void deposit(double amount) throws RemoteException;
    void withdraw(double amount) throws RemoteException;
    void transfer(BankInterface toAccount, double amount) throws RemoteException;
}

