package EJ1;

import java.util.concurrent.Semaphore;

public class Cuenta {

    double saldo;
    String id;
    Semaphore usandose=new Semaphore(1);
    public Cuenta(String id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }
    void sacaDinero(double amount) {
        saldo -= amount;
    }

    void meteDinero(double amount) {
        saldo += amount;
    }

    void transferencia(Cuenta desde, double amount) throws InterruptedException {
        if(desde.saldo>=amount){
            desde.sacaDinero(amount);
            this.meteDinero(amount);
            System.out.println("Transferencia de " + amount + " de " + desde.id + " a " + this.id);
        }
    }
}
