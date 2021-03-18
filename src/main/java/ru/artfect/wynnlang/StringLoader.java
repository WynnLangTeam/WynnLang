package ru.artfect.wynnlang;

import com.google.common.collect.HashBiMap;
import net.minecraftforge.fml.common.Loader;
import ru.artfect.wynnlang.translate.ReverseTranslation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StringLoader {
    public static void load() throws InstantiationException, IllegalAccessException {
        loadType(WynnLang.TextType.CHAT);
        loadType(WynnLang.TextType.ENTITY_NAME);
        loadType(WynnLang.TextType.ITEM_LORE);
        loadType(WynnLang.TextType.ITEM_NAME);
        loadType(WynnLang.TextType.PLAYERLIST);
        loadType(WynnLang.TextType.PLAYERLIST);
        loadType(WynnLang.TextType.TITLE);
        loadType(WynnLang.TextType.INVENTORY_NAME);
        loadType(WynnLang.TextType.BOSSBAR);
        loadType(WynnLang.TextType.SCOREBOARD);

        generateMobBossBarFromRegularNames();

        if (Loader.isModLoaded("wynnexp")) {
            WynnLang.common.get(WynnLang.TextType.ITEM_NAME).remove("§dQuest Book");
            HashMap<String, String> loreMap = WynnLang.common.get(WynnLang.TextType.ITEM_LORE);
            loreMap.remove("§a\u2714§7 Class Req: Mage/Dark Wizard");
            loreMap.remove("§a\u2714§7 Class Req: Warrior/Knight");
            loreMap.remove("§a\u2714§7 Class Req: Archer/Hunter");
            loreMap.remove("§a\u2714§7 Class Req: Shaman/Skyseer");
        }
    }

    private static void generateMobBossBarFromRegularNames() {
        HashMap<Pattern, String> bossBar = WynnLang.regex.get(WynnLang.TextType.BOSSBAR);
        HashMap<String, String> entity = WynnLang.common.get(WynnLang.TextType.ENTITY_NAME);
        entity.forEach((p, r) -> {
            // p = "§aStray Page§6 [Lv. 1]"
            // r = "§aБлуждающая Страница§6 [Ур. 1]"

            // p' = "§aStray Page - §c(\d+)§4❤"
            // r' = "§aБлуждающая Страница - §c(r1)§4❤"

            if (p.matches("§.+§6 \\[Lv\\. [0-9]+\\]") && r.matches("§.+§6 \\[Ур. [0-9]+\\]")) {
                String baseOriginalName = p.substring(0, p.indexOf("§6"));
                String baseTranslatedName = r.substring(0, r.indexOf("§6"));
                bossBar.put(Pattern.compile(baseOriginalName.replace("?", "\\?") + " - §c(\\d+)§4❤"), baseTranslatedName + " - §c(r1)§4❤");
            }
        });
    }

    private static void loadType(WynnLang.TextType type) throws IllegalAccessException, InstantiationException {
        Log.loadLogFile(type);
        loadList(type);
    }

    private static void loadList(WynnLang.TextType textType) throws InstantiationException, IllegalAccessException {
        try {
            WynnLang.common.put(textType, new HashMap<>());
            WynnLang.regex.put(textType, new HashMap<>());
            ReverseTranslation.translated.put(textType, HashBiMap.create());
            String name = textType.name();
            System.out.println(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(WynnLang.class.getResourceAsStream("/" + name + "/list.txt"), StandardCharsets.UTF_8));
            String line;
            HashMap map = WynnLang.common.get(textType);
            while ((line = br.readLine()) != null) {
                loadFile(name + "/" + line, map, false);
            }
            if (!Loader.isModLoaded("wynntils")) {
                loadFile(name + "/wynntils/regex.txt", WynnLang.regex.get(textType), true);
                loadFile(name + "/wynntils/common.txt", WynnLang.common.get(textType), false);
            }
            loadFile(name + "/regex.txt", WynnLang.regex.get(textType), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFile(String fileName, Map map, boolean regex) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(WynnLang.class.getResourceAsStream("/" + fileName), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            String[] text = line.split("@");
            if (text.length == 2)
                map.put(regex ? Pattern.compile(text[0]) : text[0], text[1].equals(" ") ? "" : text[1]);
        }
    }
}
