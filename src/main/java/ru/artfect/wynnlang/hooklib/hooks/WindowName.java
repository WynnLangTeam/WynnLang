package ru.artfect.wynnlang.hooklib.hooks;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import ru.artfect.wynnlang.event.EntityNameEvent;
import ru.artfect.wynnlang.event.WindowNameEvent;
import ru.artfect.wynnlang.hooklib.asm.Hook;
import ru.artfect.wynnlang.hooklib.asm.ReturnCondition;

public class WindowName {
    @Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
    public static ITextComponent getDisplayName(InventoryBasic inventoryBasic, @Hook.ReturnValue ITextComponent returnValue) {
        return hookWindowName(inventoryBasic, returnValue);
    }

    private static ITextComponent hookWindowName(InventoryBasic inventoryBasic, ITextComponent returnValue) {
        WindowNameEvent event = new WindowNameEvent(returnValue);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getName();
    }
}
