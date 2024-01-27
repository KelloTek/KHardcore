package fr.kellotek.khardcore;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class GameHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (!KHardcore.instance.getUserConfig().contains(String.valueOf(player.getUniqueId()))){
            KHardcore.instance.getUserConfig().set(player.getUniqueId() + ".isDead", false);
        }
        try{
            KHardcore.instance.saveUserConfig();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        e.setJoinMessage(ChatColor.GREEN + "+ " + ChatColor.WHITE + player.getDisplayName() + ChatColor.GRAY + " (" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();

        e.setQuitMessage(ChatColor.RED + "- " + ChatColor.WHITE + player.getDisplayName());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();

        if(player.getGameMode() == GameMode.SPECTATOR && KHardcore.instance.getUserConfig().getBoolean(player.getUniqueId() + ".isDead")){
            e.setFormat(ChatColor.GRAY + "☠ " + player.getDisplayName() + " > " + e.getMessage());
        } else {
            e.setFormat(ChatColor.BLUE + player.getDisplayName() + ChatColor.GRAY + " > " + ChatColor.WHITE + e.getMessage());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getEntity().getPlayer();
        assert player != null;

        KHardcore.instance.getUserConfig().set(player.getUniqueId() + ".isDead", true);
        try {
            KHardcore.instance.saveUserConfig();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        e.setDeathMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + player.getDisplayName() + " is dead");
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        Entity entity = e.getEntity();

        if(entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;

            KHardcore.instance.entityLevel(livingEntity);
        }
    }
}
