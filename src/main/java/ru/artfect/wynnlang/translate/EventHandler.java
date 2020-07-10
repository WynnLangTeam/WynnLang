package ru.artfect.wynnlang.translate;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.artfect.translates.*;
import ru.artfect.wynnlang.Reference;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;
import ru.artfect.wynnlang.event.*;
import ru.artfect.wynnlang.utils.WynnLangTextComponent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        if (event.getType() != ChatType.GAME_INFO && Reference.onWynncraft && Reference.modEnabled)
            WynnLangTextComponent.tryToTranslate(event.getMessage(), Chat.class).ifPresent(event::setMessage);
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

    @SubscribeEvent
    public static void onBossBar(RenderGameOverlayEvent.BossInfo event) {
        if (Reference.onWynncraft && Reference.modEnabled && WynnLang.translated) {
            ITextComponent name = event.getBossInfo().getName();
            if (!(name instanceof WynnLangTextComponent))
                WynnLangTextComponent.tryToTranslate(name, BossBar.class).ifPresent(event.getBossInfo()::setName);
        }
    }
    @SubscribeEvent
    public static void onInventoryOpen(ClientContainerOpenEvent event) {
        String replace = StringUtil.handleString(InventoryName.class, event.getWindowTitle().getUnformattedText());
        if (replace != null)
            event.setWindowTitle(new WynnLangTextComponent(event.getWindowTitle(), new TextComponentString(replace)));
    }

    @SubscribeEvent
    public static void onTabShow(PlayerListForTabEvent event) {
        List<PlayerListForTabEvent.PlayerData> playerlist = event.playerDataList;
        for (int i = 0; i != playerlist.size(); i++) {
            PlayerListForTabEvent.PlayerData data = playerlist.get(i);
            if (data.displayName != null) {
                String str = data.displayName.getFormattedText();
                if (!str.isEmpty()) {
                    String replace = StringUtil.findReplace(Playerlist.class, str);
                    if (replace != null && !replace.isEmpty())
                        playerlist.set(i, new PlayerListForTabEvent.PlayerData(data.ping, data.gamemode, data.profile, new WynnLangTextComponent(data.displayName, new TextComponentString(replace))));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onScoreBoard(UpdateScoreboardEvent event) {
        String replace = StringUtil.handleString(Scoreboard.class, event.getName());
        if (replace != null)
            event.setName(replace);
    }

    @SubscribeEvent
    public static void onTitle(ShowTitleEvent event) {
        if (event.getMessage() != null) {
            String str = event.getMessage().getFormattedText().replace("§r", "");
            String replace = StringUtil.handleString(Title.class, str);
            if (replace != null)
                event.setMessage(new WynnLangTextComponent(event.getMessage(), new TextComponentString(replace)));
        }
    }
}
