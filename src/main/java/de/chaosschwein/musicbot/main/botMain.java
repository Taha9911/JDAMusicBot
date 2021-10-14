package de.chaosschwein.musicbot.main;

import javax.security.auth.login.LoginException;

import de.chaosschwein.musicbot.api.MusicBot;
import de.chaosschwein.musicbot.api.Message;
import de.chaosschwein.musicbot.listener.CommandListener;
import de.chaosschwein.musicbot.manager.CommandManager;
import de.chaosschwein.musicbot.manager.FileManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class botMain {

    public static @NotNull botMain INSTANCE;
    public static @NotNull JDA shardManager;

    public static @NotNull MusicBot musicBot;
    public static @NotNull FileManager musicBotFile;
    public static @NotNull FileManager configfile;

    public static @NotNull String cmdprefix;

    private final @NotNull CommandManager cmdMan;
    public @NotNull JDABuilder jdaBuilder;

    public static void main(String @NotNull [] args) throws LoginException, InterruptedException {
        new botMain();
    }

    public botMain() throws InterruptedException, LoginException {

        INSTANCE = this;

        configfile = new FileManager("config");
        HashMap<String,Object> configdset = new HashMap<>();

        configdset.put("BotKey","ODk4MjI1MzExNzYwNjcwODAw.YWhHgg.abk1oKpqfaRFmQdaUMRnJUlpakM");
        configdset.put("Command","!");
        configdset.put("Watching","Chaos Bot");
        configdset.put("Log","850056223600672818");

        configfile.writedefault(configdset);

        HashMap<String, Object> configread = configfile.read();

        cmdprefix = configread.containsKey("Command") ? configread.get("Command").toString() : "!";

        jdaBuilder = JDABuilder.createDefault(configread.get("BotKey").toString());
        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        String watching = configread.containsKey("Watching") ? configread.get("Watching").toString() : "Chaos BOT";
        jdaBuilder.setActivity(Activity.watching(watching));
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);


        this.cmdMan = new CommandManager();

        jdaBuilder.addEventListeners(new CommandListener());

        shardManager = jdaBuilder.build();
        shardManager.awaitReady();


        musicBotFile = new FileManager("music");
        HashMap<String,Object> musicdset = new HashMap<>();

        musicdset.put("Link","https://www.youtube.com/watch?v=8NzYo0jmYek");
        musicdset.put("Active",false);
        musicdset.put("Channel","850055745713078272");

        musicBotFile.writedefault(musicdset);

        HashMap<String,Object> musicfileread= musicBotFile.read();

        musicBot = new MusicBot(musicfileread.get("Link").toString(), getJDA().getVoiceChannelById(musicfileread.get("Channel").toString()));


        if(Boolean.parseBoolean(musicfileread.get("Active").toString())){
            musicBot.start();
        }



        shutdown();

        System.out.println("Bot Fertig gestartet");
    }

    public void shutdown() {
        new Thread(() -> {
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        if (shardManager != null) {
                            jdaBuilder.setStatus(OnlineStatus.OFFLINE);
                            shardManager.shutdown();
                        }
                        reader.close();
                        break;
                    } else {
                        System.out.println("Use 'exit' to shutdown the bot");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CommandManager getCmdMan() {
        return cmdMan;
    }

    public static void sendErrorMessage(TextChannel channel) {
        new Message("#FF0000",channel).send("Es ist ein Fehler aufgetreten! Kontaktieren Sie bitte den Support.");
    }

    public static @NotNull JDA getJDA() {
        return shardManager;
    }


}
