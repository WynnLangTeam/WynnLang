package ru.artfect.wynnlang.hooklib.hooks;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.text.ITextComponent;
import ru.artfect.wynnlang.hooklib.asm.Hook;
import ru.artfect.wynnlang.hooklib.asm.ReturnCondition;

public class WindowName {
    @Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
    public static ITextComponent getDisplayName(InventoryBasic inventoryBasic, @Hook.ReturnValue ITextComponent returnValue) {
        return hookWindowName(inventoryBasic, returnValue);
    }

    private static ITextComponent hookWindowName(InventoryBasic inventoryBasic, @Hook.ReturnValue ITextComponent returnValue) {
        //EntityNameEvent event = new WindowNameEvent(entity, returnValue);
        //MinecraftForge.EVENT_BUS.post(event);
        //return event.getName();
        System.out.println(returnValue);
        return returnValue;
    }
}
