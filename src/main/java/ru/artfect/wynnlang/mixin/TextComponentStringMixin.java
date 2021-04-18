package ru.artfect.wynnlang.mixin;

import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static ru.artfect.wynnlang.utils.TextComponentHandler.handleGetUnformattedText;

@Mixin(TextComponentString.class)
public class TextComponentStringMixin {
    @Inject(method = "getUnformattedComponentText", at = @At("RETURN"), cancellable = true)
    public void getUnformattedComponentText(CallbackInfoReturnable<String> ci) {
        handleGetUnformattedText(ci);
    }
}
