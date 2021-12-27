package lee.code.npc;

import lee.code.npc.database.Cache;
import lee.code.npc.lists.SupportedVillagerProfessions;
import lee.code.npc.lists.SupportedVillagerTypes;
import lee.code.npc.lists.Values;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PU {

    private final Pattern hexRegex = Pattern.compile("\\&#[a-fA-F0-9]{6}");

    public String format(String message) {
        if (message == null) return "";
        Matcher matcher = hexRegex.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end()).replaceAll("&", "");
            message = message.replace("&" + color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = hexRegex.matcher(message);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

    public Component formatC(String message) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        return Component.empty().decoration(TextDecoration.ITALIC, false).append(serializer.deserialize(message));
    }

    public String unFormat(String format) {
        return format.replaceAll("§", "&");
    }

    public String formatEntityLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public Location unFormatEntityLocation(String location) {
        String[] split = location.split(",", 6);
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), (float) Double.parseDouble(split[4]), (float) Double.parseDouble(split[5]));
    }

    public String buildStringFromArgs(String[] args, int start) {
        StringBuilder w = new StringBuilder();
        for(int i = start; i < args.length; i++) {
            w.append(args[i]).append(" ");
        }
        w = new StringBuilder(w.substring(0, w.length() - 1));
        return w.toString();
    }

    public void runCommand(Player player, String command, String type) {
        switch (type) {
            case "CONSOLE" -> {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                String run = command.replaceAll("%player%", player.getName());
                Bukkit.dispatchCommand(console, run);
            }
            case "PLAYER" -> player.chat(command);
        }
    }

    public void removeNPCs() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        Cache cache = plugin.getCache();

        List<Location> locations = cache.getNPCLocations();
        if (!locations.isEmpty()) {
            for (Location location : locations) {
                location.getChunk().load();
                for (Entity entity : location.getChunk().getEntities()) {
                    if (entity instanceof Villager) {
                        String customName = entity.getCustomName();
                        if (customName != null) {
                            String name = unFormat(entity.getCustomName());
                            if (cache.isNPC(name)) {
                                entity.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeNPC(Location location, String name) {
        for (Entity entity : location.getChunk().getEntities()) {
            if (entity instanceof Villager) {
                String customName = entity.getCustomName();
                if (customName != null) {
                    String npcName = unFormat(entity.getCustomName());
                    if (npcName.equals(name)) {
                        entity.remove();
                        return;
                    }
                }
            }
        }
    }

    public String selectNPC(Player player) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        Cache cache = plugin.getCache();
        UUID uuid = player.getUniqueId();

        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity) {
                if (getLookingAt(player, (LivingEntity) entity)) {
                    String customName = entity.getCustomName();
                    if (customName != null) {
                        String name = unFormat(customName);
                        if (cache.isNPC(name)) {
                            cache.setNPCSelected(uuid, name);
                            return name;
                        }
                    }
                }
            }
        }
        return "n";
    }

    private boolean getLookingAt(Player player, LivingEntity entity){
        Location eye = player.getEyeLocation();
        Vector toEntity = entity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        return dot > 0.99D;
    }

    public List<String> getVillagerTypes() {
        return EnumSet.allOf(SupportedVillagerTypes.class).stream().map(SupportedVillagerTypes::name).collect(Collectors.toList());
    }

    public List<String> getVillagerProfessions() {
        return EnumSet.allOf(SupportedVillagerProfessions.class).stream().map(SupportedVillagerProfessions::name).collect(Collectors.toList());
    }

    public void addPlayerClickDelay(UUID uuid) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        plugin.getData().addPlayerClickDelay(uuid);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskLater(plugin, () -> plugin.getData().removePlayerClickDelay(uuid), Values.CLICK_DELAY.getValue());
    }

    public boolean containOnlyNumbers(String string) {
        return string.matches("[0-9]+");
    }
}
