package me.screescree.SuperiorSteed.commands;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.utils.Format;

public class HorseJumpAndSpeedTest extends CustomCommand {
    public HorseJumpAndSpeedTest() {
        super("horsejumpandspeedtest");
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

        if (args.length < 2) {
            return false;
        }

        // convert both args to an int
        double jumpStrength, movementSpeed;
        try {
            jumpStrength = Double.parseDouble(args[0]);
            movementSpeed = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Format.colorize("&cJump strength and movement speed must be doubles."));
            return true;
        }

        Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        horse.setJumpStrength(jumpStrength);
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(movementSpeed);
        horse.setTamed(true);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

        return true;
    }
}
