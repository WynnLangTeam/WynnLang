package ru.artfect.wynnlang.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import ru.artfect.wynnlang.WynnLang;

public class WynnLangTextComponent extends TextComponentBase {
    public final ITextComponent original;
    public final ITextComponent translated;

    public WynnLangTextComponent(ITextComponent original, ITextComponent translated) {
        this.original = original;
        this.translated = translated;
    }

    @Override
    public String getUnformattedComponentText() {
        return (WynnLang.translated ? translated : original).getUnformattedComponentText();
    }

    @Override
    public ITextComponent createCopy() {
        return new WynnLangTextComponent(original, translated);
    }
}
