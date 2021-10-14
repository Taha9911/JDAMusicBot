package de.chaosschwein.musicbot.cmd;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.chaosschwein.musicbot.api.MusicBot;
import de.chaosschwein.musicbot.main.botMain;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message;

public class musiccmd implements ServerCommands {

    @Override
    public void performCommand(Member m, Guild guild, TextChannel channel, Message message) {
        int cmdprefixa = botMain.cmdprefix.length();
        String[] args = message.getContentDisplay().substring(cmdprefixa).split(" ");
        if(m.hasPermission(Permission.VOICE_MOVE_OTHERS)){
            de.chaosschwein.musicbot.api.Message msg = new de.chaosschwein.musicbot.api.Message("#00ff00",m);
            if(args.length>0) {
                switch (args[0]) {
                    case "start":
                        if (args.length == 2) {
                            VoiceChannel vc = botMain.musicBot.getchannel();
                            botMain.musicBot = new MusicBot(args[1], vc);
                        }
                        if (args.length == 3) {
                            botMain.musicBot = new MusicBot(args[1], botMain.getJDA().getVoiceChannelById(args[2]));
                        }
                        botMain.musicBot.start();
                        AudioTrackInfo ati = botMain.musicBot.getAudioTrackInfo();
                        msg.send("The Bot playing now:\n\n"+ati.title,"MusikBot");
                        break;
                    case "stop":
                        botMain.musicBot.stop();
                        msg.setColor("#ff00ff");
                        msg.send("The Bot is now Stopping!","MusicBot");
                        break;
                    case "vol":
                        if (args.length == 2) {
                            botMain.musicBot.voleume(Integer.parseInt(args[1]));
                            msg.setColor("#0000ff");
                            msg.send("The Volume is now at "+args[1]+"%!","MusicBot");
                        }
                        break;
                    case "bass":
                        if (args.length == 2) {
                            botMain.musicBot.bass(Integer.parseInt(args[1]));
                            msg.setColor("#0000ff");
                            msg.send("The Bass is now at "+args[1]+"%!","MusicBot");
                        }
                        break;
                    case "skip":
                        int i = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                        botMain.musicBot.skip(i);
                        msg.setColor("#0000ff");
                        msg.send("This Song was Skipped!","MusicBot");
                        break;
                    case "restart":
                        botMain.musicBot.restart();
                        msg.setColor("#ff00ff");
                        msg.send("The Bot is Restarting now!","MusicBot");
                        break;
                    case "shuffle":
                        botMain.musicBot.shuffle();
                        msg.setColor("#0000ff");
                        msg.send("The Playlist was Shuffled!","MusicBot");
                        break;
                    case "addplay":
                        if (args.length == 2) {
                            botMain.musicBot.addplay(args[1]);
                        }
                        AudioTrackInfo ati1 = botMain.musicBot.getAudioTrackInfo();
                        msg.send("The Bot playing soon:\n\n"+ati1.title,"MusikBot");
                        break;
                }
            }
        }
    }
}
