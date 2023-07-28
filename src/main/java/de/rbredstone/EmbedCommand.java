package de.rbredstone;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.lang.reflect.Member;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class EmbedCommand extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent ev) {
        if (ev.getName().equals("createembed")) {
            if (ev.getMember().hasPermission(Permission.ADMINISTRATOR)) {

                String title = ev.getOption("title").getAsString();
                String description = ev.getOption("desc").getAsString();
                int color = Optional.ofNullable(ev.getOption("color")).map(OptionMapping::getAsInt).orElse(0);
                String image = Optional.ofNullable(ev.getOption("image")).map(OptionMapping::getAsString).orElse(null);
                String thumbnail = Optional.ofNullable(ev.getOption("thumbnail")).map(OptionMapping::getAsString).orElse(null);
                String author = Optional.ofNullable(ev.getOption("author")).map(OptionMapping::getAsString).orElse(null);

                if(image != null) {
                    try {
                        URL url = new URL(image);
                    } catch (MalformedURLException e) {
                        ev.reply("Ungültige Image URL angegeben!").setEphemeral(true).queue();
                        return;
                    }
                }

                if(thumbnail != null) {
                    try {
                        URL url = new URL(thumbnail);
                    } catch (MalformedURLException e) {
                        ev.reply("Ungültige Thumbnail URL angegeben!").setEphemeral(true).queue();
                        return;
                    }

                }

                // Create the embed
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(title);
                embed.setDescription(description);
                embed.setColor(color);
                embed.setImage(image);
                embed.setThumbnail(thumbnail);
                embed.setAuthor(author);

                // Send the embed
                ev.replyEmbeds(embed.build()).queue();
            }
        }
    }
}
