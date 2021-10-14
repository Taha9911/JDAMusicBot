package de.chaosschwein.musicbot.api;


import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.chaosschwein.musicbot.api.MusikBack.GuildMusicManager;
import de.chaosschwein.musicbot.main.botMain;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import de.chaosschwein.musicbot.api.Log.LogType;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class MusicBot {

     private String url="https://www.youtube.com/watch?v=8NzYo0jmYek";
     private VoiceChannel vc;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioManager audioManager;
    private AudioTrackInfo audioTrackInfo;

    public MusicBot(String url, VoiceChannel vc){
        if(url!=null&& !url.equals("")) {
            this.url = url;
            botMain.musicBotFile.write("Link",url);
        }
        this.vc = vc;
        botMain.musicBotFile.write("Channel",vc.getId());
        this.musicManagers = new HashMap<>();
        this.audioManager= vc.getGuild().getAudioManager();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.playerManager);
        AudioSourceManagers.registerLocalSource(this.playerManager);
    }

    private static final Log Log = new Log("MusicBot");

    public AudioTrackInfo getAudioTrackInfo() {
        return audioTrackInfo;
    }

    public void start(){
        GuildMusicManager musicManager = getGuildAudioPlayer(vc.getGuild());
        playerManager.loadItemOrdered(musicManager, this.url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioTrackInfo=track.getInfo();
                connectToFirstVoiceChannel();
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                audioTrackInfo=firstTrack.getInfo();
                connectToFirstVoiceChannel();
                play(musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                Log.send("Bot Started, Nothing found by "+url, LogType.ERROR);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                Log.send("Bot ERROR, Could not play ", LogType.ERROR);
            }
        });
        botMain.musicBotFile.write("Active",true);
    }

    public VoiceChannel getchannel(){
        return this.vc;
    }

    public void newchannel(VoiceChannel vc){
        this.vc=vc;
    }

    public String getUrl() {return this.url;}

    public void addplay(String url){
        botMain.musicBotFile.write("Link",url);
        GuildMusicManager musicManager = getGuildAudioPlayer(vc.getGuild());
        playerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioTrackInfo=track.getInfo();
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                audioTrackInfo=firstTrack.getInfo();
                play(musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                Log.send("Bot Started, Nothing found by "+url, LogType.ERROR);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                Log.send("Bot ERROR, Could not play ", LogType.ERROR);
            }
        });
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private void play(GuildMusicManager musicManager,AudioTrack track) {
        musicManager.scheduler.startOrQueue(track,false);
    }

    public void voleume(int vol) {
        getGuildAudioPlayer(vc.getGuild()).scheduler.setVolume(vol);
    }

    public void bass(int vol) {
        getGuildAudioPlayer(vc.getGuild()).scheduler.bassBoost(vol);
    }

    public void skip(int i) {
        GuildMusicManager musicManager = getGuildAudioPlayer(vc.getGuild());
        if(i>0) {
            musicManager.scheduler.skipTo(i);
        }else{
            musicManager.scheduler.nextTrack();
        }
        Log.send("Skipped to next track. ", LogType.INFO);
    }

    public void shuffle(){
        getGuildAudioPlayer(vc.getGuild()).scheduler.shufflePlaylist();
        Log.send("Playlist was Shuffled. ", LogType.INFO);
    }

    private void connectToFirstVoiceChannel() {
        if (!this.audioManager.isConnected()) {
            this.audioManager.openAudioConnection(vc);
        }
    }

    public void stop(){
        if(this.audioManager.isConnected()) {
            getGuildAudioPlayer(vc.getGuild()).scheduler.destroy();
            this.audioManager.closeAudioConnection();
            Log.send("Bot Stopped", LogType.INFO);
            botMain.musicBotFile.write("Active",false);
        }
    }

    public void restartStream(){
        if(this.audioManager.isConnected()) {
            try {
                getGuildAudioPlayer(vc.getGuild()).scheduler.clearPlaylist();
                sleep(1000);
            } catch (InterruptedException e) {
            }
            GuildMusicManager musicManager = getGuildAudioPlayer(vc.getGuild());
            playerManager.loadItemOrdered(musicManager, this.url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    play(musicManager, track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    AudioTrack firstTrack = playlist.getSelectedTrack();

                    if (firstTrack == null) {
                        firstTrack = playlist.getTracks().get(0);
                    }
                    play(musicManager, firstTrack);
                }

                @Override
                public void noMatches() {
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                }
            });
        }
    }

    public void restart(){
            stop();
            try {
                sleep(2000);
            } catch (InterruptedException e) { }
            start();
    }
}
