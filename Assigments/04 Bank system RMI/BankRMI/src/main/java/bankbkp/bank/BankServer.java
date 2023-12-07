package bankbkp.bank;

public class BankServer {
    public static void main(String[] args) {
        try {
            BankServerAccount1 account1 = new BankServerAccount1();
            BankServerAccount2 account2 = new BankServerAccount2();
            BankServerAccount3 account3 = new BankServerAccount3();

            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("BankServiceAccount1", account1);
            java.rmi.Naming.rebind("BankServiceAccount2", account2);
            java.rmi.Naming.rebind("BankServiceAccount3", account3);

            System.out.println("Servidor do banco pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Código a ser executado ao encerrar o servidor
            System.out.println("Servidor do banco encerrado.");
        }
    }
}
