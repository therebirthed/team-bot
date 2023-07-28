package de.rbredstone;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        String token = "TOKEN";

        String prefix = "?";

        String status;
        status = "das Team an";

        //Build Bot
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching(status));

        //Register Listener
        builder.addEventListeners(new TeamCommand());
        builder.addEventListeners(new EmbedCommand());

        //Enable required Intents
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);

        JDA bot = builder.build();
        Guild server = bot.awaitReady().getGuildById("GUILD ID");

        //Commands
        //team
        server.updateCommands().addCommands(
                Commands.slash("team", "Sieh dir die Informationen eines User an")
                        .addOption(OptionType.USER, "user", "User, von dem du die Info sehen willst", true),
                Commands.slash("createembed", "Erstelle ein Embed")
                        .addOption(OptionType.STRING, "title", "Titel des Embeds", true)
                        .addOption(OptionType.STRING, "desc", "Beschreibung des Embeds", true)
                        .addOption(OptionType.STRING, "color", "Farbe des Embeds (hexcode)", false)
                        .addOption(OptionType.STRING, "image", "Bild des Embeds", false)
                        .addOption(OptionType.STRING, "thumbnail", "Thumbnail des Embeds", false)
                        .addOption(OptionType.STRING, "author", "Autor des Embeds", false)

        ).queue();

        System.out.println("Bot gestartet!");

    }

    public static TextChannel getTextChannelById(JDA jda, long channelId) {
        return jda.getTextChannelById(channelId);

    }

}