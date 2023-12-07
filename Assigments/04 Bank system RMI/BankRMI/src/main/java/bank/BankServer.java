package bank;

public class BankServer {
    public static void main(String[] args) {
        BankServerAccount1 account1 = null;
        BankServerAccount2 account2 = null;
        BankServerAccount3 account3 = null;

        try {
            account1 = new BankServerAccount1();
            account2 = new BankServerAccount2();
            account3 = new BankServerAccount3();

            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("BankServiceAccount1", account1);
            java.rmi.Naming.rebind("BankServiceAccount2", account2);
            java.rmi.Naming.rebind("BankServiceAccount3", account3);

            System.out.println("Servidor do banco pronto.");

            // Mantenha o servidor em execução
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Código a ser executado ao encerrar o servidor
            System.out.println("Servidor do banco encerrado.");

            // Realize o unexport das contas para liberar recursos
            try {
                java.rmi.server.UnicastRemoteObject.unexportObject(account1, true);
                java.rmi.server.UnicastRemoteObject.unexportObject(account2, true);
                java.rmi.server.UnicastRemoteObject.unexportObject(account3, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
