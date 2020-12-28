package ru.artfect.wynnlang.event;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerNameForTabEvent extends Event {
    private ITextComponent name;

    public PlayerNameForTabEvent(ITextComponent name) {

        this.name = name;
    }

    public ITextComponent getName() {
        return name;
    }

    public void setName(ITextComponent name) {
        this.name = name;
    }
}
