package com.lab1.imports.transaction;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.Getter;

@Getter
public class DBParticipant implements TransactionParticipant {
    private State state;
    private Runnable f;
    private TransactionStatus transaction;
    private PlatformTransactionManager transactionManager;
    private static ReentrantLock lock = new ReentrantLock();

    public DBParticipant(Runnable f, PlatformTransactionManager transactionManager) {
        this.state = State.WORKING;
        this.f = f;
        this.transactionManager = transactionManager;
        this.transaction = null;
    }

    @Override
    public State prepare() {
        lock.lock();

        var def = new DefaultTransactionDefinition();
        def.setName("Transaction" + UUID.randomUUID().toString());
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        this.transaction = transactionManager.getTransaction(def);

        try {
            f.run();
            this.state = State.PREPARED;
        } catch (Exception e) {
            lock.unlock();
            this.state = State.ABORTED;
            throw e;
        }
        
        return this.state;
    }


    @Override
    public State abort() {
        if (this.transaction != null) {
            transactionManager.rollback(this.transaction);
        }
        this.state = State.ABORTED;
        return this.state;
    }

    @Override
    public State commit() {
        try {
            transactionManager.commit(this.transaction);
            this.state = State.COMMITTED;
            lock.unlock();
        } catch (Exception e) {
            lock.unlock();
            this.state = State.FAILED;
            throw e;
        }
        return this.state;
    }
}
