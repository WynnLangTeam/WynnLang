package ru.artfect.wynnlang.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import ru.artfect.translates.Chat;
import ru.artfect.translates.TranslateType;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;

import java.util.Optional;

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
