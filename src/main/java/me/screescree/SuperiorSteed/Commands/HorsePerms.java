package me.screescree.SuperiorSteed.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.Database;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class HorsePerms extends CustomCommand implements TabCompleter {
    public static final int PEOPLE_PER_PAGE = 10;
    private static final String BORDER_LINE = ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------";

    public HorsePerms() {
        super("horseperms");
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Utils.colorize("&cThis command can only be used by a player."));
            return true;
        }

        if (args.length == 0) {
            return false;
        }
        
        if (!args[0].equals("add") && !args[0].equals("remove") && !args[0].equals("list")) {
            return false;
        }

        if (!args[0].equals("list")) {
            if (args.length == 1) {
                return false;
            }
        }

        Database database = SuperiorSteed.getInstance().getDatabase();
        if (args[0].equals("add")) {
            Player reciever = Bukkit.getPlayerExact(args[1]);
            if (reciever == null) {
                sender.sendMessage(Utils.colorize("&cPlayer not found."));
                return true;
            }
            else if (reciever.getName().equals(player.getName())) {
                sender.sendMessage(Utils.colorize("&cYou cannot add yourself as a horse owner."));
                return true;
            }

            boolean success = database.addReciever(player.getUniqueId(), reciever.getUniqueId());
            if (success) {
                player.sendMessage(Utils.colorize("&aYou have added &e" + reciever.getName() + "&a to your horse permissions."));
            }
            else {
                player.sendMessage(Utils.colorize("&e" + reciever.getName() + " &cis already on your horse permissions."));
            }
        }
        else if (args[0].equals("remove")) {
            OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[1]);
            if (reciever.getName().equals(player.getName())) {
                sender.sendMessage(Utils.colorize("&cYou cannot remove yourself as a horse owner."));
                return true;
            }

            boolean success = database.removeReciever(player.getUniqueId(), reciever.getUniqueId());

            if (success) {
                player.sendMessage(Utils.colorize("&aYou have removed &e" + reciever.getName() + "&a from your horse permissions."));

                if (reciever.isOnline()) {
                    Player onlineReciever = reciever.getPlayer();
                    if (
                        onlineReciever.isInsideVehicle() &&
                        onlineReciever.getVehicle() instanceof Horse &&
                        ((Horse) onlineReciever.getVehicle()).getOwner().getUniqueId().equals(player.getUniqueId())
                    ) {
                        onlineReciever.leaveVehicle();
                        onlineReciever.sendMessage(Utils.colorize("&e" + player.getName() + " &chas just removed you from their horse permissions, and you were kicked off their horse."));
                        onlineReciever.playSound(onlineReciever.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    } 
                }
            }
            else {
                player.sendMessage(Utils.colorize("&e" + reciever.getName() + " &cis not on your horse permissions."));
            }
        }
        else if (args[0].equals("list")) {
            int page;
            if (args.length == 1) {
                page = 1;
            }
            else {
                try {
                    page = Integer.parseInt(args[1]);
                }
                catch (NumberFormatException e) {
                    player.sendMessage(Utils.colorize("&cInvalid page number."));
                    return true;
                }

                if (page < 1) {
                    page = 1;
                }
            }

            int maxPages = (int) Math.ceil(database.getNumRecievers(player.getUniqueId()) / (double) PEOPLE_PER_PAGE);
            if (maxPages == 0) {
                player.sendMessage(Utils.colorize("&cYou have no one on your horse permissions."));
                return true;
            }

            if (page > maxPages) {
                page = maxPages;
            }

            ArrayList<OfflinePlayer> recievers = database.getRecievers(player.getUniqueId(), page);

            TextComponent backArrow;
            if (page == 1) {
                backArrow = new TextComponent(Utils.colorize("&7&l<< "));
            }
            else {
                backArrow = new TextComponent(Utils.colorize("&6&l<< "));
                backArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Utils.colorize("&6Go to page " + (page - 1)))));
                backArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/horseperms list " + (page - 1)));
            }

            TextComponent forwardArrow;
            if (page == maxPages) {
                forwardArrow = new TextComponent(Utils.colorize("&7&l >>"));
            }
            else {
                forwardArrow = new TextComponent(Utils.colorize("&6&l >>"));
                forwardArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Utils.colorize("&6Go to page " + (page + 1)))));
                forwardArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/horseperms list " + (page + 1)));
            }

            TextComponent centerText = new TextComponent(Utils.colorize("&aYour horse permissions: &e(Page " + page + " of " + maxPages + ")"));

            player.sendMessage(BORDER_LINE);
            player.spigot().sendMessage(backArrow, centerText, forwardArrow);
            for (OfflinePlayer reciever : recievers) {
                player.sendMessage(Utils.colorize("&e- " + reciever.getName()));
            }
            player.sendMessage(BORDER_LINE);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("add", "remove", "list");
        }
        else if (args.length == 2 && (args[0].equals("add") || args[0].equals("remove"))) {
            // returning null sends a list of all online players to the tab completer
            return null;
        }

        return List.of();
    }
}
