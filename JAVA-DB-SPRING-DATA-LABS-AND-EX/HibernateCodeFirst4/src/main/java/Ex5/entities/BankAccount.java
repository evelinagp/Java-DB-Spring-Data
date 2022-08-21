package Ex5.entities;

import javax.persistence.*;

@Entity
@Table(name = "_05_bank_account")
//@DiscriminatorValue(value = "Bank Account")
@PrimaryKeyJoinColumn(name = "id")
public class BankAccount extends BasicBillingDetail {

    private String bankName;
    private String swiftName;

    public BankAccount() {
    }

    @Column(name = "bank_name")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "swift_name")
    public String getSwiftName() {
        return swiftName;
    }

    public void setSwiftName(String swiftName) {
        this.swiftName = swiftName;
    }
}
