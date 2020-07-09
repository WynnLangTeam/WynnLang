package ru.artfect.wynnlang.translate;

import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.artfect.translates.Chat;
import ru.artfect.wynnlang.Reference;
import ru.artfect.wynnlang.utils.WynnLangTextComponent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        if (event.getType() != ChatType.GAME_INFO && Reference.onWynncraft && Reference.modEnabled)
            WynnLangTextComponent.tryToTranslate(event.getMessage()).ifPresent(event::setMessage);
    }
}
