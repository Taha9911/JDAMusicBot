package de.chaosschwein.musicbot.listener;

import de.chaosschwein.musicbot.main.botMain;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String message = e.getMessage().getContentDisplay();

        if(e.isFromType(ChannelType.TEXT)) {
            TextChannel channel = e.getTextChannel();

            if(message.startsWith(botMain.cmdprefix)) {
                int cmdprefixa = botMain.cmdprefix.length();
                String[] args = message.substring(cmdprefixa).split(" ");
                if(args.length > 0) {
                    if(!botMain.INSTANCE.getCmdMan().perform(args[0],e.getGuild(), e.getMember(), channel, e.getMessage())) {
                        botMain.sendErrorMessage(channel);
                    }
                    e.getMessage().delete().queue();
                }
            }
        }
    }
}
