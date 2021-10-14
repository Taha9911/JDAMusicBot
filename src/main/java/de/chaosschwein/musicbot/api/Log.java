package de.chaosschwein.musicbot.api;

import de.chaosschwein.musicbot.main.botMain;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Log {

    private TextChannel channel;
    private String prefix="Log";

    public Log(String prefix){
        this.prefix=prefix;
        this.channel= botMain.getJDA().getTextChannelById(botMain.configfile.read().get("Log").toString());
    }

    public void send(String message,LogType type){
       new Message(type.getColor(),channel).send(message,prefix,new SimpleDateFormat("ss:mm:HH dd.MM.yyyy", Locale.GERMANY).format(new Date(System.currentTimeMillis())));
    }
    public enum LogType {
        SUCCESS("#00ff00"),ALERT("#ffff00"),ERROR("#ff0000"),INFO("#00ffff");

        String color="";
        LogType(String color){
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }
}
