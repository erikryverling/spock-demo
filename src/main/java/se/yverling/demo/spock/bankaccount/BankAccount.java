package se.yverling.demo.spock.bankaccount;

public class BankAccount {
    private final String id;

    public BankAccount(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
