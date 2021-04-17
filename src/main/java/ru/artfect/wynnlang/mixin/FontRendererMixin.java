package ru.artfect.wynnlang.mixin;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {
    @ModifyVariable(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At("HEAD"))
    public String drawString(String text) {
        if (WynnLang.ready) {
            String translatedText = StringUtil.handleString(text);
            return translatedText != null ? translatedText : text;
        } else
            return text;
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"))
    public String getStringWidth(String text) {
        if (WynnLang.ready) {
            String translatedText = StringUtil.handleString(text);
            return translatedText != null ? translatedText : text;
        } else
            return text;
    }
}
