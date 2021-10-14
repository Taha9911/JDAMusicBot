package de.chaosschwein.musicbot.manager;

import de.chaosschwein.musicbot.cmd.ServerCommands;
import de.chaosschwein.musicbot.cmd.help;
import de.chaosschwein.musicbot.cmd.musiccmd;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public ConcurrentHashMap<String, ServerCommands> commands;

    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();
        this.commands.put("start",new musiccmd());
        this.commands.put("stop",new musiccmd());
        this.commands.put("restart",new musiccmd());
        this.commands.put("skip",new musiccmd());
        this.commands.put("addplay",new musiccmd());
        this.commands.put("shuffle",new musiccmd());
        this.commands.put("vol",new musiccmd());
        this.commands.put("bass",new musiccmd());

        this.commands.put("help",new help());
    }

    public boolean perform(String command, Guild guild, Member m, TextChannel channel, Message message) {

        ServerCommands cmd;
        if((cmd = this.commands.get(command.toLowerCase())) != null) {
            cmd.performCommand(m, guild,channel, message);
            return true;
        }

        return false;
    }



}
