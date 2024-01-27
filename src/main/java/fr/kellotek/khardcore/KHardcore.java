package fr.kellotek.khardcore;

import fr.kellotek.khardcore.commands.ReviveCmd;
import fr.kellotek.khardcore.commands.RitualCmd;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public final class KHardcore extends JavaPlugin {

    public static KHardcore instance;
    public static String prefix = ChatColor.BLUE + "KHardcore" + ChatColor.GRAY + " - ";
    private File userFile;
    private FileConfiguration userConfig;

    @Override
    public void onEnable() {
        instance = this;

        createUserConfig();

        Objects.requireNonNull(getCommand("revive")).setExecutor(new ReviveCmd());
        Objects.requireNonNull(getCommand("ritual")).setExecutor(new RitualCmd());
        getServer().getPluginManager().registerEvents(new GameHandler(), this);

        getLogger().info("Don't forget to set the server to hardcore");

        for (World world : Bukkit.getWorlds()){
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        }
    }

    public boolean hasItem(Player player, Material item) {
        ItemStack[] inventoryContents = player.getInventory().getContents();
        for (ItemStack it : inventoryContents) {
            if (it != null && it.getType() == item)
                return true;
        }
        return false;
    }

    public FileConfiguration getUserConfig() {
        return this.userConfig;
    }

    public void createUserConfig() {
        this.userFile = new File(getDataFolder(), "users.yml");
        if (!this.userFile.exists()) {
            this.userFile.getParentFile().mkdirs();
            saveResource("users.yml", false);
        }
        this.userConfig = (FileConfiguration)new YamlConfiguration();
        try {
            this.userConfig.load(this.userFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveUserConfig() throws IOException {
        this.userConfig.save(new File(getDataFolder(), "users.yml"));
    }

    public void whichEntity(LivingEntity entity, double healthMultiplier, double attackMultiplier, double bossAttackMultiplier) {
        if(entity.getType() == EntityType.BEE || entity.getType() == EntityType.BLAZE || entity.getType() == EntityType.CAVE_SPIDER || entity.getType() == EntityType.CREEPER || entity.getType() == EntityType.DOLPHIN || entity.getType() == EntityType.DROWNED || entity.getType() == EntityType.ENDERMAN || entity.getType() == EntityType.ENDERMITE || entity.getType() == EntityType.EVOKER || entity.getType() == EntityType.FOX || entity.getType() == EntityType.GOAT || entity.getType() == EntityType.GUARDIAN || entity.getType() == EntityType.HOGLIN || entity.getType() == EntityType.HUSK || entity.getType() == EntityType.IRON_GOLEM || entity.getType() == EntityType.MAGMA_CUBE || entity.getType() == EntityType.PANDA || entity.getType() == EntityType.PHANTOM || entity.getType() == EntityType.PIGLIN || entity.getType() == EntityType.PIGLIN_BRUTE || entity.getType() == EntityType.PILLAGER || entity.getType() == EntityType.POLAR_BEAR || entity.getType() == EntityType.RAVAGER || entity.getType() == EntityType.SILVERFISH || entity.getType() == EntityType.SKELETON || entity.getType() == EntityType.SLIME || entity.getType() == EntityType.SPIDER || entity.getType() == EntityType.STRAY || entity.getType() == EntityType.VEX || entity.getType() == EntityType.VINDICATOR || entity.getType() == EntityType.WITCH || entity.getType() == EntityType.WITHER_SKELETON || entity.getType() == EntityType.WOLF || entity.getType() == EntityType.ZOGLIN || entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ZOMBIE_VILLAGER || entity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).removeModifier(attributeModifier));

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , healthMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).removeModifier(attributeModifier));
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_attack_damage" , attackMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }

        if(entity.getType() == EntityType.ALLAY || entity.getType() == EntityType.AXOLOTL || entity.getType() == EntityType.BAT || entity.getType() == EntityType.CAMEL || entity.getType() == EntityType.CAT || entity.getType() == EntityType.CHICKEN || entity.getType() == EntityType.COD || entity.getType() == EntityType.COW || entity.getType() == EntityType.DONKEY || entity.getType() == EntityType.FROG || entity.getType() == EntityType.GHAST || entity.getType() == EntityType.GLOW_SQUID || entity.getType() == EntityType.HORSE || entity.getType() == EntityType.LLAMA || entity.getType() == EntityType.MUSHROOM_COW || entity.getType() == EntityType.MULE || entity.getType() == EntityType.OCELOT || entity.getType() == EntityType.PARROT || entity.getType() == EntityType.PUFFERFISH || entity.getType() == EntityType.RABBIT || entity.getType() == EntityType.PIG || entity.getType() == EntityType.SALMON || entity.getType() == EntityType.SHEEP || entity.getType() == EntityType.SHULKER || entity.getType() == EntityType.SKELETON_HORSE || entity.getType() == EntityType.SNIFFER || entity.getType() == EntityType.SNOWMAN || entity.getType() == EntityType.SQUID || entity.getType() == EntityType.STRIDER || entity.getType() == EntityType.TADPOLE || entity.getType() == EntityType.TRADER_LLAMA || entity.getType() == EntityType.TROPICAL_FISH || entity.getType() == EntityType.TURTLE || entity.getType() == EntityType.VILLAGER || entity.getType() == EntityType.WANDERING_TRADER || entity.getType() == EntityType.ZOMBIE_HORSE) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).removeModifier(attributeModifier));

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , healthMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }

        if(entity.getType() == EntityType.ELDER_GUARDIAN || entity.getType() == EntityType.WARDEN || entity.getType() == EntityType.WITHER) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).removeModifier(attributeModifier));

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , healthMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).removeModifier(attributeModifier));
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_attack_damage" , bossAttackMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }

        if(entity.getType() == EntityType.ENDER_DRAGON) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).removeModifier(attributeModifier));

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , healthMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }

    public void entityLevel(LivingEntity entity) {
        long ticksCounter = getServer().getWorlds().get(0).getFullTime();
        long dayCounter = ticksCounter / 24000;

        entity.setCustomNameVisible(true);

        if(!(entity instanceof Player)) {
            if(dayCounter >= 0) {
                whichEntity(entity, 0.5, 0.2, 0.1);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "1");
            }

            if(dayCounter >= 21){
                whichEntity(entity, 1.0, 0.4, 0.2);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "2");
            }

            if(dayCounter >= 41){
                whichEntity(entity, 1.5, 0.6, 0.3);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "3");
            }

            if(dayCounter >= 61 ){
                whichEntity(entity, 2.0, 0.8, 0.4);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "4");
            }

            if(dayCounter >= 81){
                whichEntity(entity, 2.5, 1.0, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "5");
            }

            if(dayCounter >= 96){
                whichEntity(entity, 3.0, 1.2, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "6");
            }

            if(dayCounter >= 111){
                whichEntity(entity, 3.5, 1.4, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "7");
            }

            if(dayCounter >= 126){
                whichEntity(entity, 4.0, 1.6, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "8");
            }

            if(dayCounter >= 141){
                whichEntity(entity, 4.5, 1.8, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "9");
            }

            if(dayCounter >= 156){
                whichEntity(entity, 5.0, 2.0, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "10");
            }

            if(dayCounter >= 166){
                whichEntity(entity, 5.5, 2.2, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "11");
            }

            if(dayCounter >= 176){
                whichEntity(entity, 6.0, 2.4, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "12");
            }

            if(dayCounter >= 186){
                whichEntity(entity, 6.5, 2.6, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "13");
            }

            if(dayCounter >= 196){
                whichEntity(entity, 7.0, 2.8, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "14");
            }

            if(dayCounter >= 206){
                whichEntity(entity, 7.5, 3.0, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "15");
            }

            if(dayCounter >= 211){
                whichEntity(entity, 8.0, 3.2, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "16");
            }

            if(dayCounter >= 216){
                whichEntity(entity, 8.5, 3.4, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "17");
            }

            if(dayCounter >= 221){
                whichEntity(entity, 9.0, 3.6, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "18");
            }

            if(dayCounter >= 226){
                whichEntity(entity, 9.5, 3.8, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "19");
            }

            if(dayCounter >= 231){
                whichEntity(entity, 10.0, 4.0, 0.5);
                entity.setCustomName(ChatColor.BLUE + "Level " + ChatColor.WHITE + "20");
            }
        }
    }
}
