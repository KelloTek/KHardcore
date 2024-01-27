package fr.kellotek.khardcore.commands;

import fr.kellotek.khardcore.KHardcore;
import jdk.vm.ci.meta.Local;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReviveCmd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            player.sendMessage(KHardcore.prefix + ChatColor.RED + "You need specify a player to revive");
            return true;
        }

        if(args.length == 1){
            Player target = Bukkit.getPlayer(args[0]);

            assert target != null;
            if(target.getGameMode() == GameMode.SPECTATOR && KHardcore.instance.getUserConfig().getBoolean(player.getUniqueId() + ".isDead")) {
                boolean hasCostRevive = KHardcore.instance.hasItem(player, Material.EMERALD);
                if(hasCostRevive){
                    ItemStack removeItem = new ItemStack(Material.EMERALD, 1);
                    player.getInventory().removeItem(new ItemStack[] { removeItem });
                    player.updateInventory();

                    Location loc = Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation();
                    target.teleport(loc);

                    target.setGameMode(GameMode.SURVIVAL);

                    for (Player player1 : Bukkit.getOnlinePlayers()){
                        player1.playSound(player1, Sound.ITEM_GOAT_HORN_SOUND_0, 1f, 1f);
                    }

                    KHardcore.instance.getUserConfig().set(player.getUniqueId() + ".isDead", Boolean.FALSE);
                    try {
                        KHardcore.instance.saveUserConfig();
                    } catch (IOException ioException) {
                        throw new RuntimeException(ioException);
                    }

                    Bukkit.broadcastMessage(KHardcore.prefix + ChatColor.GRAY + target.getDisplayName() + ChatColor.WHITE + " come to be resurrected by " + ChatColor.GRAY + player.getDisplayName());
                } else {
                    player.sendMessage(KHardcore.prefix + ChatColor.RED + "You don't have item to revive");
                }
            } else {
                player.sendMessage(KHardcore.prefix + ChatColor.RED + player.getDisplayName() + " is not death");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
        List<String> tr = new ArrayList<>();

        if(args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()){
                tr.add(player.getDisplayName().toLowerCase());
            }
        }
        return tr;
    }
}
