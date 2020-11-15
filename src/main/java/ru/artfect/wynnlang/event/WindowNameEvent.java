package ru.artfect.wynnlang.event;


import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class WindowNameEvent extends Event {
    private ITextComponent name;

    public WindowNameEvent(ITextComponent name) {
        this.name = name;
    }

    public void setName(ITextComponent name) {
        this.name = name;
    }

    public ITextComponent getName() {
        return name;
    }
}
