package ru.artfect.wynnlang;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;
import ru.artfect.wynnlang.command.RuCommand;
import ru.artfect.wynnlang.command.WynnLangCommand;
import ru.artfect.wynnlang.translate.MessageHandler;
import ru.artfect.wynnlang.translate.ReverseTranslation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, clientSideOnly = true)
public class WynnLang {
    public static boolean translated = true;

    public static Map<TextType, HashMap<String, String>> common = new HashMap<>();
    public static Map<TextType, HashMap<Pattern, String>> regex = new HashMap<>();

    public static void sendMessage(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(Reference.CHAT_PREFIX + " " + message));
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) throws IllegalAccessException, InstantiationException, IOException {
        Reference.keyBindings[0] = new KeyBinding("Показ оригинальных строк", Keyboard.KEY_F8, "WynnLang");
        ClientRegistry.registerKeyBinding(Reference.keyBindings[0]);

        Reference.modFile = event.getSourceFile();

        MinecraftForge.EVENT_BUS.register(new Network());
        MinecraftForge.EVENT_BUS.register(new MessageHandler());

        Config.loadConfigFromFile();
        Log.init();
        StringLoader.load();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException, IllegalAccessException, InstantiationException {
        new ReverseTranslation();
        ClientCommandHandler.instance.registerCommand(new WynnLangCommand(new UpdateManager()));
        ClientCommandHandler.instance.registerCommand(new RuCommand());

        Reference.ruChat = new RuChat();
        RuChat.startTimer();
    }

    public enum TextType {
        BOSSBAR,
        CHAT,
        ENTITY_NAME,
        INVENTORY_NAME,
        ITEM_LORE,
        ITEM_NAME,
        PLAYERLIST,
        SCOREBOARD,
        TITLE
    }
}