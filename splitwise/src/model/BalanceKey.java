package model;

public class BalanceKey {
    User A;
    User B;

    public BalanceKey(User A, User B) {
        if (A.getId() > B.getId()) {
            this.A = A;
            this.B = B;
        } else {
            this.A = B;
            this.B = A;
        }
    }
}
