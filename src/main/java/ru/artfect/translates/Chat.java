package ru.artfect.translates;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.BiMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import ru.artfect.wynnlang.Reference;
import ru.artfect.wynnlang.StringUtil;
import ru.artfect.wynnlang.WynnLang;
import ru.artfect.wynnlang.translate.ReverseTranslation;

public class Chat extends TranslateType {
	private ClientChatReceivedEvent event;
	
	public Chat(ClientChatReceivedEvent event){
		this.event = event;
	}

	public Chat() {
		
	}

	public void translate(){
		ITextComponent rawMsg = event.getMessage();
        String message = rawMsg.getFormattedText();

        ClickEvent clickEvent = null;
        HoverEvent hoverEvent = null;
        for (ITextComponent part : rawMsg.getSiblings()) {
            Style st = part.getStyle();
            if (st.getClickEvent() != null || st.getHoverEvent() != null) {
                clickEvent = st.getClickEvent();
                hoverEvent = st.getHoverEvent();
            }
        }

        String str = message.replace("§r", "");
        String replace = StringUtil.handleString(this, str);
        if (replace != null) {
            TextComponentString msg = new TextComponentString(replace);
            if (clickEvent != null || hoverEvent != null) {
                Style style = new Style();
                style.setClickEvent(clickEvent);
                style.setHoverEvent(hoverEvent);
                msg.setStyle(style);
            }
            event.setMessage(msg);
            return;
        }
	}
	
	public String getName(){
		return "CHAT";
	}
	
	public void reverse(BiMap<String, String> translated){
		try {
	    	GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
			List<ChatLine> chatLines = (List<ChatLine>) ReverseTranslation.chatLinesF.get(chat);
	        for(int i = 0; i != chatLines.size(); i++){
	        	ChatLine cl = chatLines.get(i);
	        	String str = cl.getChatComponent().getFormattedText().replaceAll("§r", "");
	            String replace = translated.get(str);
	            if (replace != null) {
	            	chatLines.set(i, new ChatLine(cl.getUpdatedCounter(), new TextComponentString(replace), cl.getChatLineID()));
	            }
	        }
	        chat.refreshChat();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			WynnLang.sendMessage("§4Ошибка");
		}
	}
}