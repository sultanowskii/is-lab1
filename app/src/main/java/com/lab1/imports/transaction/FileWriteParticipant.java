package com.lab1.imports.transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Getter;

@Getter
public class FileWriteParticipant implements TransactionParticipant {
    private final Path file;
    private final byte[] data;
    private State state;

    public FileWriteParticipant(Path file, byte[] data) {
        this.file = file;
        this.data = data;
        this.state = State.WORKING;
    }

    @Override
    public State prepare() {
        // throw new TransactionException("OH NO!!!");
        if (this.file.toFile().exists()) {
            this.state = State.ABORTED;
            throw new TransactionException("Can't save to file " + file.toString() + ": it already exists.");
        }

        try {
            Files.write(this.file, data);
            this.state = State.PREPARED;
        } catch (IOException e) {
            this.state = State.ABORTED;
            throw new TransactionException(e.getMessage());
        }

        return this.state;
    }

    @Override
    public State abort() {
        this.file.toFile().delete();
        this.state = State.ABORTED;
        return this.state;
    }

    @Override
    public State commit() {
        this.state = State.COMMITTED;
        return this.state;
    }
}
