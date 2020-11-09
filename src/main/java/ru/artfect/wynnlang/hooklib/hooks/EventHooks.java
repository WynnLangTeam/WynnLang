package ru.artfect.wynnlang.hooklib.hooks;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraftforge.common.MinecraftForge;
import ru.artfect.wynnlang.event.EntityNameEvent;
import ru.artfect.wynnlang.hooklib.asm.Hook;
import ru.artfect.wynnlang.hooklib.asm.ReturnCondition;

public class EventHooks {
    @Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
    public static ITextComponent getDisplayName(Entity entity, @Hook.ReturnValue ITextComponent returnValue) {
        EntityNameEvent event = new EntityNameEvent(entity, returnValue);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getName();
    }

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static int hashCode(Style style) {
        int i = hashCodeOfNullable(style.color);
        i = 31 * i + hashCodeOfNullable(style.bold);
        i = 31 * i + hashCodeOfNullable(style.italic);
        i = 31 * i + hashCodeOfNullable(style.underlined);
        i = 31 * i + hashCodeOfNullable(style.strikethrough);
        i = 31 * i + hashCodeOfNullable(style.obfuscated);
        i = 31 * i + hashCodeOfNullable(style.clickEvent);
        i = 31 * i + hashCodeOfNullable(style.hoverEvent);
        i = 31 * i + hashCodeOfNullable(style.insertion);
        return i;
    }
    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static int hashCode(TextComponentBase textComponentBase) {
        return 31 * hashCodeOfNullable(textComponentBase.style) + hashCodeOfNullable(textComponentBase.siblings);
    }

    private static int hashCodeOfNullable(Object v) {
        return v == null ? 0 : v.hashCode();
    }
}
