package ru.artfect.wynnlang.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.artfect.wynnlang.StringUtil;

public interface ITextComponentHandler {

    default void handleGetUnformattedText(CallbackInfoReturnable<String> ci) {
        String text = ci.getReturnValue();
        String translatedText = StringUtil.handleString(text);
        System.out.println(text);
        if (translatedText != null)
            ci.setReturnValue(translatedText);
    }
}
