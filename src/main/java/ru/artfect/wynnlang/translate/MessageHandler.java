package ru.artfect.wynnlang.translate;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import ru.artfect.wynnlang.event.ClientContainerOpenEvent;
import ru.artfect.wynnlang.event.UpdateScoreboardEvent;

public class MessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null)
            return;

        if (msg instanceof SPacketOpenWindow) {
            SPacketOpenWindow p = (SPacketOpenWindow) msg;
            ClientContainerOpenEvent event = new ClientContainerOpenEvent(p.getWindowId(), p.getGuiId(), p.getWindowTitle(), p.getSlotCount(), p.getEntityId());
            MinecraftForge.EVENT_BUS.post(event);
            msg = new SPacketOpenWindow(event.getWindowId(), event.getInventoryType(), event.getWindowTitle(), event.getSlotCount(), event.getEntityId());
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
