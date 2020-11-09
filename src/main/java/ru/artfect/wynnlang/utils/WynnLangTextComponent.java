package ru.artfect.wynnlang.utils;

import com.google.common.collect.Iterators;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import ru.artfect.translates.TranslateType;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static net.minecraft.util.text.TextComponentBase.createDeepCopyIterator;

public class WynnLangTextComponent implements ITextComponent {
    public static Optional<WynnLangTextComponent> tryToTranslate(ITextComponent original, Class<? extends TranslateType> type) {
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

        String replace = StringUtil.handleString(type, str);
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
    public ITextComponent setStyle(Style style) {
        original.setStyle(style);
        translated.setStyle(style);
        return this;
    }

    @Override
    public Style getStyle() {
        return (currentComponent()).getStyle();
    }

    @Override
    public ITextComponent appendText(String text) {
        return appendSibling(new TextComponentString(text));
    }

    @Override
    public ITextComponent appendSibling(ITextComponent component) {
        component.getStyle().setParentStyle(getStyle());
        original.getSiblings().add(component);
        translated.getSiblings().add(component);
        return this;
    }

    @Override
    public String getUnformattedComponentText() {
        return currentComponent().getUnformattedComponentText();
    }

    private ITextComponent currentComponent() {
        System.out.println("currentComponent "+WynnLang.translated);
        return WynnLang.translated ? translated : original;
    }

    @Override
    public String getUnformattedText() {
        return currentComponent().getUnformattedText();
    }

    @Override
    public String getFormattedText() {
        return currentComponent().getFormattedText();
    }

    @Override
    public List<ITextComponent> getSiblings() {
        return currentComponent().getSiblings();
    }

    @Override
    public ITextComponent createCopy() {
        return new WynnLangTextComponent(original, translated);
    }

    @Override
    public Iterator<ITextComponent> iterator() {
        return Iterators.concat(Iterators.forArray(currentComponent()), createDeepCopyIterator(currentComponent().getSiblings()));
    }
}
