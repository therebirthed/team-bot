package de.rbredstone;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TeamCommand extends ListenerAdapter {

    List<User> userList = new ArrayList<>();
    private MessageChannel targetChannel;

    public void onSlashCommandInteraction(SlashCommandInteractionEvent ev) {
        if (ev.getName().equals("team")) {
            if (ev.getMember().hasPermission(Permission.MANAGE_ROLES)) {

                Member member = ev.getOption("user").getAsMember();
                userList.add(member.getUser());

                Role role = ev.getGuild().getRolesByName("Team", true).get(0);

                if (!member.getRoles().contains(role)) {

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl());
                    embed.setTitle(member.getEffectiveName() + " (" + member.getId() + ")");
                    embed.setThumbnail(member.getEffectiveAvatarUrl());
                    embed.setColor(Color.decode("#e81224"));
                    embed.setDescription("Möchtest du " + member.getAsMention() + " einstellen?");

                    Button button = Button.success("Einstellen", "User einstellen").withEmoji(Emoji.fromUnicode("✅"));

                    ev.replyEmbeds(embed.build()).setActionRow(button).queue();

                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl());
                    embed.setTitle(member.getEffectiveName() + " (" + member.getId() + ")");
                    embed.setThumbnail(member.getEffectiveAvatarUrl());
                    embed.setColor(Color.decode("#e81224"));
                    embed.setDescription("Möchtest du " + member.getAsMention() + " kündigen?");

                    Button button2 = Button.danger("Kündigen", "User kündigen").withEmoji(Emoji.fromUnicode("❌"));

                    ev.replyEmbeds(embed.build()).setActionRow(button2).queue();
                }
            } else {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Keine Berechtigung!");
                embed.setColor(Color.decode("#e81224"));
                embed.setDescription("Du hast keine Berechtigung, diesen Befehl auszuführen!");

                ev.getChannel().sendMessageEmbeds(embed.build()).queue();
            }

        }

    }

    public void onButtonInteraction(ButtonInteractionEvent ev) {
        if (ev.getButton().getId().equals("Einstellen")) {
            for (User user : userList) {
            Guild guild = ev.getGuild();
            Role role = guild.getRolesByName("Team", true).get(0);
            Role role2 = guild.getRolesByName("Azubi", true).get(0);

            if(user != null) {

                guild.addRoleToMember(user, role).queue();
                guild.addRoleToMember(user, role2).queue();
                ev.reply("Der User " + user.getAsMention() + ", wurde eingestellt!").queue();

                ActionRow disabledRow = ActionRow.of(ev.getMessage().getButtons().stream().map(Button::asDisabled).collect(Collectors.toList()));
                ev.getMessage().editMessageComponents(disabledRow).queue();

            }

            }
            userList.remove(userList.get(0));
        }
        if (ev.getButton().getId().equals("Kündigen")) {
            Guild guild = ev.getGuild();
            List<String> rollenToRemove = Arrays.asList("Team", "Azubi", "Moderator", "Sr. Moderator", "Modleitung", "Administrator", "Serverleitung", "Teamleitung", "Leiter für Bewerbungen", "Leiter für Ausbildungen", "Leiter für Teambesprechungen", "Leiter für To-Dos");

            for (User user : userList) {
                if (user != null) {
                    for (Role role : guild.getRoles()) {
                        if (rollenToRemove.contains(role.getName()) && user.getJDA().getRoles().contains(role)) {
                            guild.removeRoleFromMember(user, role).queue();
                        }
                    }
                }
            }
            ev.reply("Dem User " + userList.get(0).getAsMention() + ", wurde gekündigt!").queue();

            ActionRow disabledRow = ActionRow.of(ev.getMessage().getButtons().stream().map(Button::asDisabled).collect(Collectors.toList()));
            ev.getMessage().editMessageComponents(disabledRow).queue();

            userList.remove(userList.get(0));

        }
    }

    public void setTargetChannel(MessageChannel channel) {
        this.targetChannel = channel;
    }
}