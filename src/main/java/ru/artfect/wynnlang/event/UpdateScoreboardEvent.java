package ru.artfect.wynnlang.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateScoreboardEvent extends Event {
    private String name;
    private String objective;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public UpdateScoreboardEvent(String name, String objective, int value) {
        this.name = name;
        this.objective = objective;
        this.value = value;
    }
}
