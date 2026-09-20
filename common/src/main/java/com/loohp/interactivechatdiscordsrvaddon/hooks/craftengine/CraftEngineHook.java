package com.loohp.interactivechatdiscordsrvaddon.hooks.craftengine;

import com.loohp.interactivechat.objectholders.OfflineICPlayer;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CraftEngineHook {

    public static boolean isEngineAvailable() {
        return BukkitCraftEngine.instance() != null && BukkitItemManager.instance() != null;
    }

    public static ItemStack toClientSideItemStack(ItemStack itemStack, OfflineICPlayer icPlayer) {
        if (!isEngineAvailable() || isEmpty(itemStack)) {
            return itemStack;
        }
        ItemStack clone = itemStack.clone();
        net.momirealms.craftengine.core.entity.player.Player craftEnginePlayer = adaptPlayer(icPlayer);
        return BukkitItemManager.instance().s2c(clone, craftEnginePlayer).orElse(clone);
    }

    public static boolean isReadableResourcePack(File resourcePack) {
        return resourcePack != null && resourcePack.isFile() && resourcePack.canRead() && resourcePack.getName().toLowerCase().endsWith(".zip");
    }

    public static File getGeneratedResourcePackFile(String resourcePackName) {
        if (resourcePackName == null || resourcePackName.isEmpty() || !resourcePackName.equals(new File(resourcePackName).getName())) {
            return null;
        }
        Plugin craftEngine = Bukkit.getPluginManager().getPlugin("CraftEngine");
        if (craftEngine == null) {
            return null;
        }
        return new File(new File(craftEngine.getDataFolder(), "generated"), resourcePackName);
    }

    private static net.momirealms.craftengine.core.entity.player.Player adaptPlayer(OfflineICPlayer icPlayer) {
        if (icPlayer == null || !icPlayer.isOnline() || !icPlayer.getPlayer().isLocal()) {
            return null;
        }
        return BukkitCraftEngine.instance().platform().getPlayer(icPlayer.getPlayer().getLocalPlayer().getUniqueId());
    }

    private static boolean isEmpty(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(Material.AIR) || itemStack.getAmount() <= 0;
    }
}
