package ru.artfect.wynnlang.hooklib.hooks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.common.MinecraftForge;
import ru.artfect.wynnlang.event.ShowTitleEvent;
import ru.artfect.wynnlang.hooklib.asm.Hook;
import ru.artfect.wynnlang.hooklib.asm.ReturnCondition;

import java.awt.*;

public class TitleEventHooks {

    private static ITextComponent actionBar;
    private static ITextComponent title;
    private static ITextComponent subTitle;

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static void renderRecordOverlay(GuiIngameForge guiIngameForge, int width, int height, float partialTicks) {
        if (guiIngameForge.overlayMessageTime > 0) {
            mc().profiler.startSection("overlayMessage");
            float hue = (float) guiIngameForge.overlayMessageTime - partialTicks;
            int opacity = (int) (hue * 256.0F / 20.0F);
            if (opacity > 255) opacity = 255;

            if (opacity > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (width / 2), (float) (height - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                int color = (guiIngameForge.animateOverlayMessageColor ? Color.HSBtoRGB(hue / 50.0F, 0.7F, 0.6F) & 0xFFFFFF : 0xFFFFFF);
                mc().fontRenderer.drawString(actionBar.getFormattedText(), -mc().fontRenderer.getStringWidth(actionBar.getUnformattedText()) / 2, -4, color | (opacity << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc().profiler.endSection();
        }
    }

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static void renderTitle(GuiIngameForge guiIngameForge, int width, int height, float partialTicks) {
        if (guiIngameForge.titlesTimer > 0) {
            mc().profiler.startSection("titleAndSubtitle");
            float age = (float) guiIngameForge.titlesTimer - partialTicks;
            int opacity = 255;

            if (guiIngameForge.titlesTimer > guiIngameForge.titleFadeOut + guiIngameForge.titleDisplayTime) {
                float f3 = (float) (guiIngameForge.titleFadeIn + guiIngameForge.titleDisplayTime + guiIngameForge.titleFadeOut) - age;
                opacity = (int) (f3 * 255.0F / (float) guiIngameForge.titleFadeIn);
            }
            if (guiIngameForge.titlesTimer <= guiIngameForge.titleFadeOut)
                opacity = (int) (age * 255.0F / (float) guiIngameForge.titleFadeOut);

            opacity = MathHelper.clamp(opacity, 0, 255);

            if (opacity > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (width / 2), (float) (height / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int l = opacity << 24 & -16777216;
                if (title != null)
                    guiIngameForge.getFontRenderer().drawString(title.getFormattedText(), (float) (-guiIngameForge.getFontRenderer().getStringWidth(title.getUnformattedText()) / 2), -10.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                if (subTitle != null)
                    guiIngameForge.getFontRenderer().drawString(subTitle.getFormattedText(), (float) (-guiIngameForge.getFontRenderer().getStringWidth(subTitle.getUnformattedText()) / 2), 5.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc().profiler.endSection();
        }
    }

    private static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    @Hook
    public static void setOverlayMessage(GuiIngame guiIngame, String message, boolean animateColor) {
        ShowTitleEvent event = new ShowTitleEvent(SPacketTitle.Type.ACTIONBAR, new TextComponentString(message));
        MinecraftForge.EVENT_BUS.post(event);
        actionBar = event.getMessage();
    }

    @Hook
    public static void displayTitle(GuiIngame guiIngame, String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
        if (title != null) {
            ShowTitleEvent event = new ShowTitleEvent(SPacketTitle.Type.TITLE, new TextComponentString(title));
            MinecraftForge.EVENT_BUS.post(event);
            TitleEventHooks.title = event.getMessage();

        } else if (subTitle != null) {
            ShowTitleEvent event = new ShowTitleEvent(SPacketTitle.Type.SUBTITLE, new TextComponentString(subTitle));
            MinecraftForge.EVENT_BUS.post(event);
            TitleEventHooks.subTitle = event.getMessage();
        }
    }
}
