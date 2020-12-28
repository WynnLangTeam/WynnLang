package ru.artfect.wynnlang;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import ru.artfect.translates.BossBar;
import ru.artfect.translates.TranslateType;
import ru.artfect.wynnlang.translate.ReverseTranslation;
import ru.artfect.wynnlang.utils.WynnLangTextComponent;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String handleString(TranslateType type, String str) {
        return handleString(type.getClass(), str);
    }

    public static String handleString(Class<? extends TranslateType> type, String str) {
        String s = str.replace("§r", "");
        if (type == BossBar.class)
            System.out.println(s.replace('§','&'));
        String replace = findReplace(type, s);
        if (replace != null) {
            return replace.isEmpty() ? null : replaceFound(type, s, replace);
        } else {
            Log.addString(type, s);
            return null;
        }

    }

    private static String replaceFound(TranslateType type, String str, String replace) {
        return replaceFound(type.getClass(), str, replace);
    }

    private static String replaceFound(Class<? extends TranslateType> type, String str, String replace) {
        if (Reference.modEnabled && !ReverseTranslation.enabled) {
            ReverseTranslation.translated.get(type).put(replace, str);
            return replace;
        } else {
            ReverseTranslation.translated.get(type).put(str, replace);
            return null;
        }

    }

    public static String findReplace(Class<? extends TranslateType> type, String str) {
        String replace = WynnLang.common.get(type).get(str);
        return replace == null ? findReplaceRegex(WynnLang.regex.get(type), str) : replace;
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

    public static Optional<String> tryToTranslate(String original, Class<? extends TranslateType> type) {
        if (Reference.onWynncraft && Reference.modEnabled && WynnLang.translated)
            return Optional.ofNullable(handleString(type, original));
        else
            return Optional.empty();

    }

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

        String replace = handleString(type, str);
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
