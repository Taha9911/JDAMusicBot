package de.chaosschwein.musicbot.cmd;

import de.chaosschwein.musicbot.main.botMain;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class help implements ServerCommands {

    @Override
    public void performCommand(Member m, Guild guild, TextChannel channel, Message message) {
        de.chaosschwein.musicbot.api.Message msg = new de.chaosschwein.musicbot.api.Message("#8f7c00",m);
        String cp = botMain.cmdprefix;

        msg.send(cp+"start | Start the Musicbot\n" +
                        cp+"stop | Stop the Musicbot\n" +
                        cp+"restart | Restart the Musicbot\n" +
                        cp+"addplay | Add a Song to the Playlist\n" +
                        cp+"skip | Skip the current Song\n" +
                        cp+"shuffle | Suffle the playlist\n"+
                        cp+"vol | Set the Volume\n" +
                        cp+"bass | Set the Bass","Help Command");
    }
}
