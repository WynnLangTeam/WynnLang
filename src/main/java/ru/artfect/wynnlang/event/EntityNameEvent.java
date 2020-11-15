package ru.artfect.wynnlang.event;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EntityNameEvent extends Event {
    private final Entity entity;
    private ITextComponent name;

    public EntityNameEvent(Entity entity, ITextComponent name) {
        this.entity = entity;
        this.name = name;
    }

    public void setName(ITextComponent name) {
        this.name = name;
    }

    public ITextComponent getName() {
        return name;
    }

    public Entity getEntity() {
        return entity;
    }
}
