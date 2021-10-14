package de.chaosschwein.musicbot.cmd;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommands {

    void performCommand(Member m, Guild guild, TextChannel channel, Message message);

}
