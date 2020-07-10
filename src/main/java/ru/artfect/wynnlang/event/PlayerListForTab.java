package ru.artfect.wynnlang.event;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerListForTab extends Event {

    public final List<PlayerData> playerDataList;

    public PlayerListForTab(SPacketPlayerListItem packet) {
        playerDataList = packet.getEntries().stream().map(i -> new PlayerData(i.getPing(), i.getGameMode(), i.getProfile(), i.getDisplayName())).collect(Collectors.toList());
    }

    public static class PlayerData {
        public final int ping;
        public final GameType gamemode;
        public final GameProfile profile;
        public final ITextComponent displayName;

        public PlayerData(int ping, GameType gamemode, GameProfile profile, ITextComponent displayName) {
            this.ping = ping;
            this.gamemode = gamemode;
            this.profile = profile;
            this.displayName = displayName;
        }
    }
}
