package fr.kellotek.khardcore.commands;

import fr.kellotek.khardcore.KHardcore;
import fr.theskyblockman.skylayer.scoreboard.ScoreboardManagement;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RitualCmd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            boolean hasCostRitual = KHardcore.instance.hasItem(player, Material.NETHER_STAR);
            if (hasCostRitual){
                ItemStack removeItem = new ItemStack(Material.NETHER_STAR, 1);
                player.getInventory().removeItem(removeItem);
                player.updateInventory();
                for (Player target : Bukkit.getOnlinePlayers()){
                    int numberRevive = Bukkit.getOnlinePlayers().size();
                    if(isPlayerDead() && KHardcore.instance.getUserConfig().getBoolean(target.getUniqueId() + ".isDead")){
                        KHardcore.instance.getUserConfig().set(player.getUniqueId() + ".reviveAmount", KHardcore.instance.getUserConfig().getInt(player.getUniqueId() + ".reviveAmount") + numberRevive);

                        Location loc = new Location(Bukkit.getWorld("world"), KHardcore.instance.getUserConfig().getDouble(target.getUniqueId() + ".locationX"), KHardcore.instance.getUserConfig().getDouble(target.getUniqueId() + ".locationY"), KHardcore.instance.getUserConfig().getDouble(target.getUniqueId() + ".locationZ"));
                        target.teleport(loc);

                        ScoreboardManagement.updateScoreboard(target);

                        target.setGameMode(GameMode.SURVIVAL);
                        target.playSound(target, Sound.ITEM_GOAT_HORN_SOUND_0, 1f, 1f);

                        KHardcore.instance.getUserConfig().set(target.getUniqueId() + ".isDead", Boolean.FALSE);
                        try {
                            KHardcore.instance.saveUserConfig();
                        } catch (IOException ioException) {
                            throw new RuntimeException(ioException);
                        }

                        Bukkit.broadcastMessage(KHardcore.prefix + ChatColor.GRAY + target.getDisplayName() + ChatColor.WHITE + " come to be resurrected by " + ChatColor.GRAY + player.getDisplayName());
                    }
                }
            } else {
                player.sendMessage(KHardcore.prefix + ChatColor.RED + "You don't have item to ritual");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
        return null;
    }

    public boolean isPlayerDead(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getGameMode() == GameMode.SPECTATOR){
                return true;
            }
        }
        return false;
    }
}
