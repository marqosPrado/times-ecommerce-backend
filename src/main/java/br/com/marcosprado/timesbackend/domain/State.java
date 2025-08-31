package br.com.marcosprado.timesbackend.domain;

public class State {
    private int id;
    private String state;

    private State(
            String state
    ) {
        this.state = state;
    }

    public static State create(
            String state
    ) {
        return new State(state);
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
