package bankbkp.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankServerAccount3 extends UnicastRemoteObject implements BankInterface {
    private double balance;

    public BankServerAccount3() throws RemoteException {
        balance = 0.0; // Saldo inicial
    }

    @Override
    public double getBalance() throws RemoteException {
        return balance;
    }

    @Override
    public void deposit(double amount) throws RemoteException {
        balance += amount;
        System.out.println("Depósito de " + amount + " realizado. Novo saldo da conta 3: " + balance);
    }

    @Override
    public void withdraw(double amount) throws RemoteException {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Saque de " + amount + " realizado. Novo saldo da conta 3: " + balance);
        } else {
            System.out.println("Saldo insuficiente para saque.");
        }
    }

    @Override
    public void transfer(BankInterface toAccount, double amount) throws RemoteException {
        if (amount <= balance) {
            withdraw(amount);
            toAccount.deposit(amount);
            System.out.println("Transferência concluída. Novo saldo da conta 3: " + balance);
        } else {
            System.out.println("Saldo insuficiente para transferência.");
        }
    }
}

