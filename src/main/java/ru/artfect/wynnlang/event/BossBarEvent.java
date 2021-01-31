package ru.artfect.wynnlang.event;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BossBarEvent extends Event {

    private ITextComponent name;

    public BossBarEvent(ITextComponent name) {
        this.name = name;
    }

    public ITextComponent getName() {
        return name;
    }

    public void setName(ITextComponent name) {
        this.name = name;
    }
}
