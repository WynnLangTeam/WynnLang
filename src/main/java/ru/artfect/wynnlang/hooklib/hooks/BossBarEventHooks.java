package ru.artfect.wynnlang.hooklib.hooks;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraftforge.common.MinecraftForge;
import ru.artfect.wynnlang.event.BossBarEvent;
import ru.artfect.wynnlang.hooklib.asm.Hook;
import ru.artfect.wynnlang.hooklib.asm.ReturnCondition;

public class BossBarEventHooks {
    @Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
    public static ITextComponent getName(BossInfo bossInfo, @Hook.ReturnValue ITextComponent returnValue) {
        BossBarEvent event = new BossBarEvent(returnValue);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getName();
    }
}
