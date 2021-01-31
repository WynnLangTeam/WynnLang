package ru.artfect.wynnlang.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextComponentBase.class)
public class TextComponentMixin {
    @Inject(method = "getUnformattedText()Ljava/lang/String;", at = @At("RETURN"))
    public void getUnformattedText(CallbackInfoReturnable ci) {
        System.out.println("getUnformattedText " + ci.getReturnValue());
    }
}
