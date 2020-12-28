package ru.artfect.wynnlang.translate;

import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.artfect.translates.*;
import ru.artfect.wynnlang.Reference;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;
import ru.artfect.wynnlang.event.*;
import ru.artfect.wynnlang.utils.WynnLangTextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        if (event.getType() != ChatType.GAME_INFO && Reference.onWynncraft && Reference.modEnabled && WynnLang.translated)
            StringUtil.tryToTranslate(event.getMessage(), Chat.class).ifPresent(event::setMessage);
    }

    @SubscribeEvent
    public static void onItemToltip(ItemTooltipEvent event) {
        if (Reference.onWynncraft && Reference.modEnabled && WynnLang.translated) {
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

    private static Map<ITextComponent, Optional<WynnLangTextComponent>> bossNameCache = new HashMap<>();

    @SubscribeEvent
    public static void onBossBar(BossBarEvent event) {
        bossNameCache.computeIfAbsent(event.getName(), fromName -> StringUtil.tryToTranslate(fromName, BossBar.class))
                .ifPresent(event::setName);
    }

    private static Map<ITextComponent, Optional<WynnLangTextComponent>> entityNamesCache = new HashMap<>();

    @SubscribeEvent
    public static void onEntityName(EntityNameEvent event) {
        entityNamesCache.computeIfAbsent(event.getName(), fromName -> StringUtil.tryToTranslate(fromName, Entity.class))
                .ifPresent(event::setName);
    }

    @SubscribeEvent
    public static void onInventoryOpen(ClientContainerOpenEvent event) {
        StringUtil.tryToTranslate(event.getWindowTitle().getUnformattedText(), InventoryName.class)
                .ifPresent(replace -> event.setWindowTitle(new WynnLangTextComponent(event.getWindowTitle(), new TextComponentString(replace))));
    }

    @SubscribeEvent
    public static void onTabShow(PlayerNameForTabEvent event) {
        StringUtil.tryToTranslate(event.getName(), Playerlist.class)
                .ifPresent(event::setName);
    }

    @SubscribeEvent
    public static void onScoreBoard(UpdateScoreboardEvent event) {
        StringUtil.tryToTranslate(event.getName(), Scoreboard.class)
                .ifPresent(event::setName);
    }

    @SubscribeEvent
    public static void onTitle(ShowTitleEvent event) {
        StringUtil.tryToTranslate(event.getMessage().getFormattedText().replace("§r", ""), Title.class)
                .ifPresent(replace -> event.setMessage(new WynnLangTextComponent(event.getMessage(), new TextComponentString(replace))));
    }

}
