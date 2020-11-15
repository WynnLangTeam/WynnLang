package ru.artfect.wynnlang.translate;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import ru.artfect.wynnlang.event.PlayerListForTabEvent;
import ru.artfect.wynnlang.event.ShowTitleEvent;
import ru.artfect.wynnlang.event.UpdateScoreboardEvent;

import java.util.stream.Collectors;

public class MessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null)
            return;

        if (msg instanceof SPacketTitle) {
            SPacketTitle p = (SPacketTitle) msg;
            ShowTitleEvent event = new ShowTitleEvent(p.getType(), p.getMessage(), p.getFadeInTime(), p.getDisplayTime(), p.getFadeOutTime());
            MinecraftForge.EVENT_BUS.post(event);
            msg = new SPacketTitle(event.getType(), event.getMessage(), event.getFadeInTime(), event.getDisplayTime(), event.getFadeOutTime());
        } else if (msg instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem p = (SPacketPlayerListItem) msg;
            PlayerListForTabEvent event = new PlayerListForTabEvent(p);
            MinecraftForge.EVENT_BUS.post(event);
            p.getEntries().clear();
            p.getEntries().addAll(event.playerDataList.stream().map(i -> p.new AddPlayerData(i.profile, i.ping, i.gamemode, i.displayName)).collect(Collectors.toList()));
        } else if (msg instanceof SPacketUpdateScore) {
            SPacketUpdateScore p = (SPacketUpdateScore) msg;
            UpdateScoreboardEvent event = new UpdateScoreboardEvent(p.getPlayerName(), p.getObjectiveName(), p.getScoreValue());
            MinecraftForge.EVENT_BUS.post(event);
            net.minecraft.scoreboard.Scoreboard sb = Minecraft.getMinecraft().player.getWorldScoreboard();
            Score score = new Score(sb, new ScoreObjective(sb, event.getObjective(), IScoreCriteria.DUMMY), event.getName());
            score.setScorePoints(event.getValue());
            msg = new SPacketUpdateScore(score);
        }
        super.channelRead(ctx, msg);
    }
}
