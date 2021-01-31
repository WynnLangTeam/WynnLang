package ru.artfect.wynnlang;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Log {
    public static boolean enabled = true;
    private static final String LOG_PATH = Minecraft.getMinecraft().gameDir + "/config/WynnLang/Logs/";

    private static Map<WynnLang.TextType, List<String>> oldStr = new HashMap<>();
    private static Map<WynnLang.TextType, List<String>> newStr = new HashMap<>();

    public static void init() throws IOException {
        initLogs();

        Multithreading.schedule(() -> {
            try {
                saveAndSend();
            } catch (IOException | InstantiationException | IllegalAccessException ignored) {

            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    public static void initLogs() throws IOException {
        Path lpath = Paths.get(LOG_PATH);
        if (!Files.exists(lpath)) {
            Files.createDirectories(lpath);
        }
    }

    public static void saveAndSend() throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException {
        if (!Reference.onWynncraft) {
            return;
        }
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("UUID", Minecraft.getMinecraft().getSession().getProfile().getId().toString()));
        params.add(new BasicNameValuePair("ver", Reference.VERSION));

        for (WynnLang.TextType textType : newStr.keySet()) {
            List<String> strList = newStr.get(textType);
            if (strList.isEmpty()) {
                continue;
            }
            params.add(new BasicNameValuePair(textType.name(), new Gson().toJson(strList)));
            try {
                Files.write(Paths.get(LOG_PATH + textType.name() + ".txt"), strList, StandardCharsets.UTF_8,
                        StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            } catch (IOException ignored) {

            }
            strList.clear();
        }

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://" + Reference.SERVER);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        client.execute(httpPost);
        client.close();
    }

    public static void loadLogFile(WynnLang.TextType textType) {
        try {
            Path p = Paths.get(LOG_PATH + textType.name() + ".txt");
            if (p.toFile().exists()) {
                oldStr.put(textType, Files.readAllLines(p));
            } else {
                oldStr.put(textType, new ArrayList<>());
                p.toFile().createNewFile();
            }
            newStr.put(textType, new ArrayList<>());
        } catch (IOException ignored) {

        }
    }

    public static void addString(WynnLang.TextType textType, String str) {
        if (enabled && !oldStr.get(textType).contains(str)) {
            oldStr.get(textType).add(str);
            newStr.get(textType).add(str);
        }
    }
}
