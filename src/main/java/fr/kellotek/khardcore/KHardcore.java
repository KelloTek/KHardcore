package fr.kellotek.khardcore;

import fr.kellotek.khardcore.commands.ReviveCmd;
import fr.kellotek.khardcore.commands.RitualCmd;
import fr.theskyblockman.skylayer.LayeredPlugin;
import fr.theskyblockman.skylayer.layer.Module;
import fr.theskyblockman.skylayer.scoreboard.ScoreboardComponent;
import fr.theskyblockman.skylayer.scoreboard.ScoreboardLayer;
import fr.theskyblockman.skylayer.scoreboard.ScoreboardManagement;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public final class KHardcore extends LayeredPlugin {
    public static KHardcore instance;
    public static String prefix = ChatColor.BLUE + "KHardcore" + ChatColor.GRAY + " - ";
    private File userFile;
    private FileConfiguration userConfig;
    private int ticksPerDay = 24000;
    private long ticksElapsed = Objects.requireNonNull(Bukkit.getWorld("world")).getFullTime();
    public long dayCounter = ticksElapsed / ticksPerDay;

    @Override
    public void onEnable() {
        instance = this;

        initModules(new Module[]{new ScoreboardLayer()});
        ScoreboardManagement.makeScoreboardLocal(new ScoreboardComponent(player -> {
            if(getUserConfig() == null) return new String[0];
            return new String[]{
                    "",
                    ChatColor.GRAY + "| " + ChatColor.YELLOW + player.getDisplayName(),
                    ChatColor.WHITE + "  You Revive " + ChatColor.GREEN + getUserConfig().getInt(player.getUniqueId() + ".reviveAmount") + " players",
                    ChatColor.WHITE + "  You Died " + ChatColor.GREEN + player.getStatistic(Statistic.DEATHS) + " times",
                    " ",
                    ChatColor.GRAY + "| " + ChatColor.YELLOW + "Information",
                    ChatColor.WHITE + "  Days " + ChatColor.GREEN + dayCounter,
                    ChatColor.WHITE + "  Max Level " + ChatColor.GREEN + getMaxLevel(),
                    ChatColor.WHITE + "  Cost to Revive " + ChatColor.GREEN + getCostDay().name().replaceAll("_", " ").toLowerCase(),
                    "  ",
                    ChatColor.GRAY + "| " + ChatColor.YELLOW + "Credits",
                    ChatColor.WHITE + "  by KelloTek",
                    ChatColor.WHITE + "  github.com/KelloTek"
            };
        }, ChatColor.RED + "KHardcore"));

        new BukkitRunnable(){
            @Override
            public void run() {
                ScoreboardManagement.updateScoreboard();
            }
        }.runTaskLater(KHardcore.instance, 1L);

        new BukkitRunnable(){
            @Override
            public void run() {
                ticksElapsed = Objects.requireNonNull(Bukkit.getWorld("world")).getFullTime();
                dayCounter = ticksElapsed / ticksPerDay;
                ScoreboardManagement.updateScoreboard();
            }
        }.runTaskTimer(this, 1L, ticksPerDay);

        createUserConfig();

        Objects.requireNonNull(getCommand("revive")).setExecutor(new ReviveCmd());
        Objects.requireNonNull(getCommand("ritual")).setExecutor(new RitualCmd());
        getServer().getPluginManager().registerEvents(new GameHandler(), this);

        getLogger().info("Don't forget to set the server to hardcore");

        for (World world : Bukkit.getWorlds()){
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        }
    }

    @Override
    public void onDisable(){
        disableModules();
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

    public void whichEntity(LivingEntity entity, double health, double attack, double bossAttack) {
        if(entity.getType() == EntityType.BEE || entity.getType() == EntityType.BLAZE || entity.getType() == EntityType.CAVE_SPIDER || entity.getType() == EntityType.CREEPER || entity.getType() == EntityType.DOLPHIN || entity.getType() == EntityType.DROWNED || entity.getType() == EntityType.ENDERMAN || entity.getType() == EntityType.ENDERMITE || entity.getType() == EntityType.EVOKER || entity.getType() == EntityType.FOX || entity.getType() == EntityType.GOAT || entity.getType() == EntityType.GUARDIAN || entity.getType() == EntityType.HOGLIN || entity.getType() == EntityType.HUSK || entity.getType() == EntityType.IRON_GOLEM || entity.getType() == EntityType.MAGMA_CUBE || entity.getType() == EntityType.PANDA || entity.getType() == EntityType.PHANTOM || entity.getType() == EntityType.PIGLIN || entity.getType() == EntityType.PIGLIN_BRUTE || entity.getType() == EntityType.PILLAGER || entity.getType() == EntityType.POLAR_BEAR || entity.getType() == EntityType.RAVAGER || entity.getType() == EntityType.SILVERFISH || entity.getType() == EntityType.SKELETON || entity.getType() == EntityType.SLIME || entity.getType() == EntityType.SPIDER || entity.getType() == EntityType.STRAY || entity.getType() == EntityType.VEX || entity.getType() == EntityType.VINDICATOR || entity.getType() == EntityType.WITCH || entity.getType() == EntityType.WITHER_SKELETON || entity.getType() == EntityType.WOLF || entity.getType() == EntityType.ZOGLIN || entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ZOMBIE_VILLAGER || entity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).removeModifier(attributeModifier));

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , health, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).removeModifier(attributeModifier));
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_attack_damage" , attack, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }

        if(entity.getType() == EntityType.ALLAY || entity.getType() == EntityType.AXOLOTL || entity.getType() == EntityType.BAT || entity.getType() == EntityType.CAMEL || entity.getType() == EntityType.CAT || entity.getType() == EntityType.CHICKEN || entity.getType() == EntityType.COD || entity.getType() == EntityType.COW || entity.getType() == EntityType.DONKEY || entity.getType() == EntityType.FROG || entity.getType() == EntityType.GHAST || entity.getType() == EntityType.GLOW_SQUID || entity.getType() == EntityType.HORSE || entity.getType() == EntityType.LLAMA || entity.getType() == EntityType.MUSHROOM_COW || entity.getType() == EntityType.MULE || entity.getType() == EntityType.OCELOT || entity.getType() == EntityType.PARROT || entity.getType() == EntityType.PUFFERFISH || entity.getType() == EntityType.RABBIT || entity.getType() == EntityType.PIG || entity.getType() == EntityType.SALMON || entity.getType() == EntityType.SHEEP || entity.getType() == EntityType.SHULKER || entity.getType() == EntityType.SKELETON_HORSE || entity.getType() == EntityType.SNIFFER || entity.getType() == EntityType.SNOWMAN || entity.getType() == EntityType.SQUID || entity.getType() == EntityType.STRIDER || entity.getType() == EntityType.TADPOLE || entity.getType() == EntityType.TRADER_LLAMA || entity.getType() == EntityType.TROPICAL_FISH || entity.getType() == EntityType.TURTLE || entity.getType() == EntityType.VILLAGER || entity.getType() == EntityType.WANDERING_TRADER || entity.getType() == EntityType.ZOMBIE_HORSE) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).removeModifier(attributeModifier));

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , health, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }

        if(entity.getType() == EntityType.ELDER_GUARDIAN || entity.getType() == EntityType.WARDEN || entity.getType() == EntityType.WITHER) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , health, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).removeModifier(attributeModifier));
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_attack_damage" , bossAttack, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }

        if(entity.getType() == EntityType.ENDER_DRAGON) {
            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getModifiers().forEach(attributeModifier -> Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).removeModifier(attributeModifier));

            Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier(UUID.randomUUID(), "custom_health" , health, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            entity.setHealth(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }

    public void entityLevel(LivingEntity entity) {
        if(!(entity instanceof Player)) {
            if(dayCounter >= 0) {
                whichEntity(entity, 1.0, 0.2, 0.1);
            }

            if(dayCounter >= 144){
                whichEntity(entity, 1.0, 0.4, 0.2);
            }

            if(dayCounter >= 288){
                whichEntity(entity, 1.5, 0.6, 0.3);
            }

            if(dayCounter >= 432 ){
                whichEntity(entity, 2.0, 0.8, 0.4);
            }

            if(dayCounter >= 576){
                whichEntity(entity, 2.5, 1.0, 0.5);
            }

            if(dayCounter >= 720){
                whichEntity(entity, 3.0, 1.2, 0.5);
            }

            if(dayCounter >= 864){
                whichEntity(entity, 3.5, 1.4, 0.5);
            }

            if(dayCounter >= 1008){
                whichEntity(entity, 4.0, 1.6, 0.5);
            }

            if(dayCounter >= 1152){
                whichEntity(entity, 4.5, 1.8, 0.5);
            }

            if(dayCounter >= 1296){
                whichEntity(entity, 5.0, 2.0, 0.5);
            }

            if(dayCounter >= 1440){
                whichEntity(entity, 5.5, 2.2, 0.5);
            }

            if(dayCounter >= 1584){
                whichEntity(entity, 6.0, 2.4, 0.5);
            }

            if(dayCounter >= 1728){
                whichEntity(entity, 6.5, 2.6, 0.5);
            }

            if(dayCounter >= 1872){
                whichEntity(entity, 7.0, 2.8, 0.5);
            }

            if(dayCounter >= 2016){
                whichEntity(entity, 7.5, 3.0, 0.5);
            }

            if(dayCounter >= 2160){
                whichEntity(entity, 8.0, 3.2, 0.5);
            }

            if(dayCounter >= 2304){
                whichEntity(entity, 8.5, 3.4, 0.5);
            }

            if(dayCounter >= 2448){
                whichEntity(entity, 9.0, 3.6, 0.5);
            }

            if(dayCounter >= 2592){
                whichEntity(entity, 9.5, 3.8, 0.5);
            }

            if(dayCounter >= 2736){
                whichEntity(entity, 10.0, 4.0, 0.5);
            }
        }
    }

    public Material getCostDay(){

        if(dayCounter >= 1008){
            return Material.NETHERITE_SCRAP;
        }

        if(dayCounter >= 504){
            return Material.TOTEM_OF_UNDYING;
        }

        if(dayCounter >= 10){
            return Material.DIAMOND;
        }

        if(dayCounter >= 5){
            return Material.GOLD_INGOT;
        }

        return Material.IRON_INGOT;
    }

    public String getMaxLevel(){
        if(dayCounter >= 2736){
            return "20";
        }

        if(dayCounter >= 2448){
            return "18";
        }

        if(dayCounter >= 2304){
            return "17";
        }

        if(dayCounter >= 2160){
            return "16";
        }

        if(dayCounter >= 2016){
            return "15";
        }

        if(dayCounter >= 1872){
            return "14";
        }

        if(dayCounter >= 1728){
            return "13";
        }

        if(dayCounter >= 1584){
            return "12";
        }

        if(dayCounter >= 1440){
            return "11";
        }

        if(dayCounter >= 1296){
            return "10";
        }

        if(dayCounter >= 1152){
            return "9";
        }

        if(dayCounter >= 1008){
            return "8";
        }

        if(dayCounter >= 864){
            return "7";
        }

        if(dayCounter >= 720){
            return "6";
        }

        if(dayCounter >= 576){
            return "5";
        }

        if(dayCounter >= 432 ){
            return "4";
        }

        if(dayCounter >= 288){
            return "3";
        }

        if(dayCounter >= 144){
            return "2";
        }

        if(dayCounter >= 0) {
            return "1";
        }
        return null;
    }
}
