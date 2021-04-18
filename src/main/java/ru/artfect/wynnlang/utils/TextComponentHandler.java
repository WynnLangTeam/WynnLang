package ru.artfect.wynnlang.utils;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.artfect.wynnlang.StringUtil;

public class TextComponentHandler {

    public static void handleGetUnformattedText(CallbackInfoReturnable<String> ci) {
        String text = ci.getReturnValue();
        String translatedText = StringUtil.handleString(text);
        if (translatedText != null)
            ci.setReturnValue(translatedText);
    }
}
