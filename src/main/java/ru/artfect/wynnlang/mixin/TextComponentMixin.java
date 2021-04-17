package ru.artfect.wynnlang.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.artfect.wynnlang.StringUtil;

@Mixin(TextComponentBase.class)
public class TextComponentMixin {
    @Inject(method = "getFormattedText", at = @At("RETURN"), cancellable = true)
    public void getFormattedText(CallbackInfoReturnable<String> ci) {
        String text = ci.getReturnValue();
        String translatedText = StringUtil.handleString(text);
        if (translatedText != null)
            ci.setReturnValue(translatedText);
    }
}
