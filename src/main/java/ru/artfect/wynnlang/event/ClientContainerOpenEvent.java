package ru.artfect.wynnlang.event;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ClientContainerOpenEvent extends Event {

    public ClientContainerOpenEvent(int windowId, String inventoryType, ITextComponent windowTitle, int slotCount, int entityId) {
        this.windowId = windowId;
        this.inventoryType = inventoryType;
        this.windowTitle = windowTitle;
        this.slotCount = slotCount;
        this.entityId = entityId;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public int getWindowId() {
        return windowId;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public ITextComponent getWindowTitle() {
        return windowTitle;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public void setWindowTitle(ITextComponent windowTitle) {
        this.windowTitle = windowTitle;
    }

    private int windowId;
    private String inventoryType;
    private ITextComponent windowTitle;
    private int slotCount;
    private int entityId;
}
