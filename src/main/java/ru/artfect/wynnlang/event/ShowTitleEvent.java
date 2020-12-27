package ru.artfect.wynnlang.event;

import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ShowTitleEvent extends Event {


    private SPacketTitle.Type type;
    private ITextComponent message;

    public SPacketTitle.Type getType() {
        return type;
    }

    public void setType(SPacketTitle.Type type) {
        this.type = type;
    }

    public ITextComponent getMessage() {
        return message;
    }

    public void setMessage(ITextComponent message) {
        this.message = message;
    }

    public ShowTitleEvent(SPacketTitle.Type type, ITextComponent message) {
        this.type = type;
        this.message = message;
    }
}
