package ru.artfect.wynnlang;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import ru.artfect.wynnlang.translate.ReverseTranslation;
import ru.artfect.wynnlang.utils.WynnLangTextComponent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static int fullCacheCapacity = WynnLang.regex.values().stream().mapToInt(Map::size).sum() / 100 + WynnLang.common.values().stream().mapToInt(Map::size).sum() / 100;
    private static Map<String, Optional<String>> fullCache = new LinkedHashMap<String, Optional<String>>(fullCacheCapacity, 0.7F, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Optional<String>> eldest) {
            return size() > fullCacheCapacity;
        }
    };

    public static String handleString(String str) {
        return fullCache.computeIfAbsent(str, key -> {
            for (WynnLang.TextType textType : WynnLang.TextType.values()) {
                String translatedText = handleString(textType, str);
                if (translatedText != null)
                    return Optional.of(translatedText);
            }
            return Optional.empty();
        }).orElse(null);
    }

    public static String handleString(WynnLang.TextType textType, String str) {
        String s = str.replace("§r", "");
        String replace = findReplace(textType, s);

        if (textType == WynnLang.TextType.CHAT && str.contains("By order of Her Majesty of Fruma, no one is allowed to cross the border into her lands, punishable by death.")) {
            System.out.println("test " + replace);
        }

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
        Map<String, String> orDefault = WynnLang.common.get(textType);
        String replace = orDefault.get(str);
        return replace == null ? regexpCache.computeIfAbsent(str, key -> findReplaceRegex(WynnLang.regex.get(textType), key)) : replace;
    }

    private static int cacheCapacity = WynnLang.regex.values().stream().mapToInt(Map::size).sum() / 100;
    private static Map<String, String> regexpCache = new LinkedHashMap<String, String>(cacheCapacity, 0.7F, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > cacheCapacity;
        }
    };

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
