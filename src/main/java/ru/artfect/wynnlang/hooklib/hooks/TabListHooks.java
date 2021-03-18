package ru.artfect.wynnlang.hooklib.hooks;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.common.MinecraftForge;
import ru.artfect.wynnlang.event.PlayerNameForTabEvent;
import ru.artfect.wynnlang.hooklib.asm.Hook;

public class TabListHooks {

    @Hook
    public static String getPlayerName(GuiPlayerTabOverlay guiPlayerTabOverlay, NetworkPlayerInfo networkPlayerInfoIn) {
        if (networkPlayerInfoIn.getDisplayName() != null) {
            PlayerNameForTabEvent event = new PlayerNameForTabEvent(networkPlayerInfoIn.getDisplayName());
            MinecraftForge.EVENT_BUS.post(event);
            return event.getName().getFormattedText();
        } else
            return ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }
}
