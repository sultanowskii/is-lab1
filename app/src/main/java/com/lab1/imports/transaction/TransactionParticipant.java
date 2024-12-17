package com.lab1.imports.transaction;

public interface TransactionParticipant {
    State prepare() throws Exception;
    State abort();
    State commit();
}
