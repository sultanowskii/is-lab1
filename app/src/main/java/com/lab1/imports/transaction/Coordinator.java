package com.lab1.imports.transaction;

import java.util.ArrayList;
import java.util.List;

public class Coordinator {
    private List<TransactionParticipant> participants;
    private ArrayList<TransactionParticipant> prepared;

    public Coordinator(TransactionParticipant... participants) {
        this.participants = List.of(participants);
        this.prepared = new ArrayList<>();
    }

    public void perform() throws Exception {
        for (var p : participants) {
            try {
                var state = p.prepare();
                if (state.equals(State.ABORTED)) {
                    abort();
                    return;
                }
                this.prepared.add(p);
            } catch (Exception e) {
                abort();
                throw e;
            }
        }

        commit();
    }

    private void abort() {
        for (var p : participants) {
            p.abort();
        }
    }

    private void commit() {
        for (var p : participants) {
            p.commit();
        }
    }
}
