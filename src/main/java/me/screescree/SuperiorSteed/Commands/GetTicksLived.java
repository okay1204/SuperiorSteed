package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.utils.Format;

public class GetTicksLived extends CustomCommand {
    public GetTicksLived() {
        super("gettickslived");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Format.colorize("&cThis command can only be used by a player."));
            return true;
        }

        RayTraceResult raytraceResult = player.getWorld().rayTraceEntities(
            player.getLocation().add(0, 1, 0),
            player.getLocation().getDirection(),
            10,
            entity -> entity.getType() != EntityType.PLAYER
        );

        if (raytraceResult == null) {
            sender.sendMessage(Format.colorize("&cYou must be looking at an entity."));
            return true;
        }

        Entity entity = raytraceResult.getHitEntity();

        // send message
        sender.sendMessage(Format.colorize("&aThis entity's ticks lived is &e" + entity.getTicksLived() + "&a ticks."));

        return true;
    }
}
