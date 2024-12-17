package com.lab1.imports.transaction;

import java.nio.file.Path;

import com.lab1.common.error.ServiceUnavailableException;
import com.lab1.imports.S3Service;

public class S3Participant implements TransactionParticipant {
    private S3Service s3Service;
    private String key;
    private Path filePath;
    private State state;
    private boolean uploaded;

    public S3Participant(S3Service s3Service, String key, Path filePath) {
        this.s3Service = s3Service;
        this.key = key;
        this.filePath = filePath;
        this.state = State.WORKING;
        this.uploaded = false;
    }

    @Override
    public State prepare() throws Exception {
        try {
            this.s3Service.uploadFile(this.key, this.filePath);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Can't save the uploaded archive. Try again later");
        }
        this.state = State.PREPARED;
        uploaded = true;
        return this.state;
    }

    @Override
    public State abort() {
        if (this.uploaded) {
            this.s3Service.deleteFile(this.key);
        }
        this.state = State.ABORTED;
        return this.state;
    }

    @Override
    public State commit() {
        this.state = State.COMMITTED;
        return this.state;
    }
}
