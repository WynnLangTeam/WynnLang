package ru.artfect.wynnlang;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import ru.artfect.wynnlang.translate.ReverseTranslation;
import ru.artfect.wynnlang.utils.WynnLangTextComponent;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String handleString(String str) {
        for (WynnLang.TextType textType : WynnLang.TextType.values()) {
            String translatedText = handleString(textType, str);
            if (translatedText != null)
                return translatedText;
        }
        return null;
    }

    public static String handleString(WynnLang.TextType textType, String str) {
        String s = str.replace("§r", "");
        String replace = findReplace(textType, s);
        if (replace != null) {
            return replace.isEmpty() ? null : replaceFound(textType, s, replace);
        } else {
            Log.addString(textType, s);
            return null;
        }

    }

    private static String replaceFound(WynnLang.TextType textType, String str, String replace) {
        if (Reference.modEnabled && !ReverseTranslation.enabled) {
            ReverseTranslation.translated.get(textType).put(replace, str);
            return replace;
        } else {
            ReverseTranslation.translated.get(textType).put(str, replace);
            return null;
        }

    }

    public static String findReplace(WynnLang.TextType textType, String str) {
        String replace = WynnLang.common.get(textType).get(str);
        return replace == null ? findReplaceRegex(WynnLang.regex.get(textType), str) : replace;
    }

    private static String findReplaceRegex(Map<Pattern, String> map, String str) {
        for (Pattern pat : map.keySet()) {
            Matcher mat = pat.matcher(str);
            if (mat.matches()) {
                String repl = map.get(pat);
                for (int gr = 0; gr != mat.groupCount() + 1; gr++) {
                    repl = repl.replace("(r" + gr + ")", mat.group(gr));
                }
                return repl;
            }
        }
        return null;
    }

    public static Optional<String> tryToTranslate(String original, WynnLang.TextType textType) {
        if (Reference.onWynncraft && Reference.modEnabled && WynnLang.translated)
            return Optional.ofNullable(handleString(textType, original));
        else
            return Optional.empty();

    }

    public static Optional<WynnLangTextComponent> tryToTranslate(ITextComponent original, WynnLang.TextType textType) {
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

        String replace = handleString(textType, str);
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
}
