package de.chaosschwein.musicbot.api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class Message {

    private Color color;
    private TextChannel channel=null;
    private User user=null;

    public Message(String colorcode,TextChannel channel){
        this.color=Color.decode(colorcode);
        this.channel=channel;
    }
    public Message(String colorcode,User user){
        this.color=Color.decode(colorcode);
        this.user=user;
    }
    public Message(String colorcode,Member member){
        this.color=Color.decode(colorcode);
        this.user=member.getUser();
    }

    public void setColor(String colorcode){
        this.color=Color.decode(colorcode);
    }

    private void sm(MessageEmbed med){
        if(channel==null){
            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage(med).queue();
            });
        }else{
            channel.sendMessage(med).queue();
        }
    }

    public void send(String message){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(color);
        sm(builder.build());
    }

    public void send(String message, String title) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(color);
        builder.setTitle(title);
        sm(builder.build());
    }

    public void send(String message, String title, String footer) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(message);
        builder.setColor(color);
        builder.setFooter(footer);
        sm(builder.build());
    }

    public void sendURL(String message, String url,String name) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(color);
        builder.setAuthor(name, null, url);
        sm(builder.build());
    }

    public void sendURL(String message,String url, String Title, String name) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(color);
        builder.setTitle(Title, url);
        builder.setAuthor(name, null, url);
        sm(builder.build());
    }

    public void sendURL(String message,String url, String Title, String name, String footer) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(color);
        builder.setTitle(Title, null);
        builder.setAuthor(name, null, url);
        builder.setFooter(footer);
        sm(builder.build());
    }
}
