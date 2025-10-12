
package org.billing.service;

import org.billing.domain.UsageRecord;

import java.math.BigDecimal;
import java.util.Objects;

public class BillingResult {
    private final UsageRecord record;
    private final BigDecimal cost;
    private final BigDecimal oldBalance;
    private final BigDecimal newBalance;
    private final boolean success; // Исправлено: final, используется параметр
    private final String note; // Исправлено: final, используется параметр

    public BillingResult(UsageRecord record, BigDecimal cost, BigDecimal oldBalance, BigDecimal newBalance, boolean success, String note) {
        this.record = record;
        this.cost = cost;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.success = success; // Исправлено: берём значение из параметра
        this.note = note != null ? note : ""; // Исправлено: используем параметр, защита от null
    }

    public UsageRecord getRecord() {
        return record;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public BigDecimal getOldBalance() {
        return oldBalance;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getNote() {
        return note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingResult that = (BillingResult) o;
        return success == that.success && // Исправлено: == для boolean
                Objects.equals(record, that.record) &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(oldBalance, that.oldBalance) &&
                Objects.equals(newBalance, that.newBalance) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(record, cost, oldBalance, newBalance, success, note);
    }

    @Override
    public String toString() {
        return "BillingResult{" +
                "record=" + record +
                ", cost=" + cost +
                ", oldBalance=" + oldBalance +
                ", newBalance=" + newBalance +
                ", success=" + success +
                ", note='" + note + '\'' +
                '}';
    }
}
