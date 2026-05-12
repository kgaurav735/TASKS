import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanTest {

    public static void main(String[] args) {

        List<LoanAccount> accounts = new ArrayList<>();

        // Overdue with positive balance -> should be included
        accounts.add(new LoanAccount(
                "ACC001",
                new Date(System.currentTimeMillis() - 86400000), // yesterday
                5000.0
        ));

        // Overdue but zero balance -> should NOT be included
        accounts.add(new LoanAccount(
                "ACC002",
                new Date(System.currentTimeMillis() - 86400000),
                0.0
        ));

        // Future due date -> should NOT be included
        accounts.add(new LoanAccount(
                "ACC003",
                new Date(System.currentTimeMillis() + 86400000), // tomorrow
                3000.0
        ));

        // Null due date -> should NOT throw exception
        accounts.add(new LoanAccount(
                "ACC004",
                null,
                4000.0
        ));

        LoanService service = new LoanService();

        List<LoanAccount> overdueLoans = service.getOverdueLoans(accounts);

        System.out.println("Overdue Loans:");

        for (LoanAccount loan : overdueLoans) {
            System.out.println(
                    "Account ID: " + loan.getAccountId()
                            + ", Balance: " + loan.getOutstandingBalance()
            );
        }
    }
}

class LoanService {

    public List<LoanAccount> getOverdueLoans(List<LoanAccount> accounts) {

        List<LoanAccount> result = new ArrayList<>();

        if (accounts == null || accounts.isEmpty()) {
            return result;
        }

        Date now = new Date();

        for (LoanAccount account : accounts) {

            if (account == null) {
                continue;
            }

            Date dueDate = account.getDueDate();

            if (dueDate == null) {
                continue;
            }

            if (dueDate.before(now)
                    && account.getOutstandingBalance() > 0.0) {

                result.add(account);
            }
        }

        return result;
    }
}

class LoanAccount {

    private String accountId;
    private Date dueDate;
    private double outstandingBalance;

    public LoanAccount(String accountId, Date dueDate, double outstandingBalance) {
        this.accountId = accountId;
        this.dueDate = dueDate;
        this.outstandingBalance = outstandingBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }
}