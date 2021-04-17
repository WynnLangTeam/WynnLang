package ru.artfect.wynnlang.mixin;

import net.minecraft.util.text.TextComponentBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextComponentBase.class)
public class TextComponentMixin implements ITextComponentHandler {
    @Inject(method = "getUnformattedText", at = @At("RETURN"), cancellable = true)
    public void getUnformattedText(CallbackInfoReturnable<String> ci) {
        handleGetUnformattedText(ci);
    }
}
