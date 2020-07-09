package ru.artfect.wynnlang.translate;

import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.artfect.translates.ItemLore;
import ru.artfect.translates.ItemName;
import ru.artfect.wynnlang.Reference;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;
import ru.artfect.wynnlang.utils.WynnLangTextComponent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        if (event.getType() != ChatType.GAME_INFO && Reference.onWynncraft && Reference.modEnabled)
            WynnLangTextComponent.tryToTranslate(event.getMessage()).ifPresent(event::setMessage);
    }

    @SubscribeEvent
    public static void onItemToltip(ItemTooltipEvent event) {
        if (WynnLang.translated) {
            List<String> toolTip = event.getToolTip();
            String itemName = toolTip.get(0);
            String nameReplace = StringUtil.handleString(ItemName.class, itemName);
            if (nameReplace != null)
                toolTip.set(0, nameReplace);

            for (int j = 1; j < toolTip.size(); j++) {
                String replace = StringUtil.handleString(ItemLore.class, toolTip.get(j));
                if (replace != null)
                    toolTip.set(j, replace);
            }
        }


    }
}
