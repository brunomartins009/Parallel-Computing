package bankbkp.bank;

import java.rmi.Naming;
import java.util.Scanner;

public class BankClient {
    public static void main(String[] args) {
        try {
            BankInterface account1 = (BankInterface) Naming.lookup("rmi://localhost/BankServiceAccount1");
            BankInterface account2 = (BankInterface) Naming.lookup("rmi://localhost/BankServiceAccount2");
            BankInterface account3 = (BankInterface) Naming.lookup("rmi://localhost/BankServiceAccount3");

            Scanner scanner = new Scanner(System.in);

            int choice;
            do {
                System.out.println("\nEscolha uma operação:");
                System.out.println("1. Consultar Saldo");
                System.out.println("2. Depositar");
                System.out.println("3. Sacar");
                System.out.println("4. Transferir");
                System.out.println("0. Sair");
                System.out.print("Opção: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Digite o número da conta para consultar o saldo (1, 2, 3): ");
                        int accountNumberBalance = scanner.nextInt();
                        printBalance(accountNumberBalance, account1, account2, account3);
                        break;
                    case 2:
                        System.out.print("Digite o número da conta para fazer o depósito (1, 2, 3): ");
                        int accountNumberDeposit = scanner.nextInt();
                        System.out.print("Digite o valor a ser depositado: ");
                        double depositAmount = scanner.nextDouble();
                        doDeposit(accountNumberDeposit, account1, account2, account3, depositAmount);
                        break;
                    case 3:
                        System.out.print("Digite o número da conta para fazer o saque (1, 2, 3): ");
                        int accountNumberWithdraw = scanner.nextInt();
                        System.out.print("Digite o valor a ser sacado: ");
                        double withdrawAmount = scanner.nextDouble();
                        doWithdraw(accountNumberWithdraw, account1, account2, account3, withdrawAmount);
                        break;
                    case 4:
                        System.out.print("Digite o número da conta de origem para transferir (1, 2, 3): ");
                        int accountNumberFrom = scanner.nextInt();
                        System.out.print("Digite o número da conta de destino para transferir (1, 2, 3): ");
                        int accountNumberTo = scanner.nextInt();
                        System.out.print("Digite o valor a ser transferido: ");
                        double transferAmount = scanner.nextDouble();
                        doTransfer(accountNumberFrom, accountNumberTo, account1, account2, account3, transferAmount);
                        break;
                    case 0:
                        System.out.println("Saindo do aplicativo.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } while (choice != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printBalance(int accountNumber, BankInterface account1, BankInterface account2, BankInterface account3) {
        try {
            switch (accountNumber) {
                case 1:
                    System.out.println("Saldo da conta 1: " + account1.getBalance());
                    break;
                case 2:
                    System.out.println("Saldo da conta 2: " + account2.getBalance());
                    break;
                case 3:
                    System.out.println("Saldo da conta 3: " + account3.getBalance());
                    break;
                default:
                    System.out.println("Número de conta inválido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doDeposit(int accountNumber, BankInterface account1, BankInterface account2, BankInterface account3, double amount) {
        try {
            switch (accountNumber) {
                case 1:
                    account1.deposit(amount);
                    System.out.println("Depósito concluído. Novo saldo da conta 1: " + account1.getBalance());
                    break;
                case 2:
                    account2.deposit(amount);
                    System.out.println("Depósito concluído. Novo saldo da conta 2: " + account2.getBalance());
                    break;
                case 3:
                    account3.deposit(amount);
                    System.out.println("Depósito concluído. Novo saldo da conta 3: " + account3.getBalance());
                    break;
                default:
                    System.out.println("Número de conta inválido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doWithdraw(int accountNumber, BankInterface account1, BankInterface account2, BankInterface account3, double amount) {
        try {
            switch (accountNumber) {
                case 1:
                    account1.withdraw(amount);
                    System.out.println("Saque concluído. Novo saldo da conta 1: " + account1.getBalance());
                    break;
                case 2:
                    account2.withdraw(amount);
                    System.out.println("Saque concluído. Novo saldo da conta 2: " + account2.getBalance());
                    break;
                case 3:
                    account3.withdraw(amount);
                    System.out.println("Saque concluído. Novo saldo da conta 3: " + account3.getBalance());
                    break;
                default:
                    System.out.println("Número de conta inválido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doTransfer(int accountNumberFrom, int accountNumberTo, BankInterface account1, BankInterface account2, BankInterface account3, double amount) {
        try {
            BankInterface fromAccount = null;
            BankInterface toAccount = null;

            switch (accountNumberFrom) {
                case 1:
                    fromAccount = account1;
                    break;
                case 2:
                    fromAccount = account2;
                    break;
                case 3:
                    fromAccount = account3;
                    break;
                default:
                    System.out.println("Número de conta de origem inválido.");
                    return;
            }

            switch (accountNumberTo) {
                case 1:
                    toAccount = account1;
                    break;
                case 2:
                    toAccount = account2;
                    break;
                case 3:
                    toAccount = account3;
                    break;
                default:
                    System.out.println("Número de conta de destino inválido.");
                    return;
            }

            fromAccount.transfer(toAccount, amount);
            System.out.println("Transferência concluída. Novo saldo da conta " + accountNumberFrom + ": " + fromAccount.getBalance());
            System.out.println("Novo saldo da conta " + accountNumberTo + ": " + toAccount.getBalance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
