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
    @Inject(method = "getUnformattedText()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    public void getUnformattedText(CallbackInfoReturnable<String> ci) {
        String translatedText = StringUtil.handleString(ci.getReturnValue());
        if (translatedText != null)
            ci.setReturnValue(translatedText);
    }
}
