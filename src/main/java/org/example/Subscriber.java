package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Subscriber {
    private final String phoneNumber;
    private BigDecimal balance;
    private String tariffCode;
    private BigDecimal amount;

    public Subscriber(String phoneNumber, BigDecimal balance, String tariffCode, BigDecimal amount) {
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.tariffCode = tariffCode;
        this.amount = amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public String getTariffCode() {
        return tariffCode;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void debit(BigDecimal amount) {

        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null|| getClass() != obj.getClass()) {
            return false;
        }
        Subscriber subscriber = (Subscriber) obj;
        return Objects.equals(this.phoneNumber, subscriber.phoneNumber);

    }

    @Override
    public String toString()
    {
        return "Amount: " + amount + ", Balance: " + balance ;

    }





}
