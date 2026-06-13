package model;

public interface Scope {
    // identifies the ledger bucket this scope's expenses and balances live in
    String id();
}
