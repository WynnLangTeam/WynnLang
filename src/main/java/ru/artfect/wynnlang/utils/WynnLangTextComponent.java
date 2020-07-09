package ru.artfect.wynnlang.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import ru.artfect.translates.Chat;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;

import java.util.Optional;

public class WynnLangTextComponent extends TextComponentBase {
    public static Optional<WynnLangTextComponent> tryToTranslate(ITextComponent original) {
        String str = original.getFormattedText();

        ClickEvent clickEvent = null;
        HoverEvent hoverEvent = null;
        for (ITextComponent part : original.getSiblings()) {
            Style st = part.getStyle();
            if (st.getClickEvent() != null || st.getHoverEvent() != null) {
                clickEvent = st.getClickEvent();
                hoverEvent = st.getHoverEvent();
            }
        }

        String replace = StringUtil.handleString(Chat.class, str);
        if (replace != null) {
            TextComponentString msg = new TextComponentString(replace);
            if (clickEvent != null || hoverEvent != null) {
                Style style = new Style();
                style.setClickEvent(clickEvent);
                style.setHoverEvent(hoverEvent);
                msg.setStyle(style);
            }
            return Optional.of(new WynnLangTextComponent(original, msg));
        }
        return Optional.empty();
    }

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
