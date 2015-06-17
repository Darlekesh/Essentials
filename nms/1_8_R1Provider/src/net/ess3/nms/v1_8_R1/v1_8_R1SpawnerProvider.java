package net.ess3.nms.v1_8_R1;

import net.ess3.nms.SpawnerProvider;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class v1_8_R1SpawnerProvider extends SpawnerProvider {
    @Override
    public ItemStack setEntityType(ItemStack is, EntityType type) {
        net.minecraft.server.v1_8_R1.ItemStack itemStack;
        CraftItemStack craftStack = CraftItemStack.asCraftCopy(is);
        itemStack = CraftItemStack.asNMSCopy(craftStack);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
            itemStack.setTag(tag);
        }
        if (!tag.hasKey("BlockEntityTag")) {
            tag.set("BlockEntityTag", new NBTTagCompound());
        }
        tag = itemStack.getTag().getCompound("BlockEntityTag");
        tag.setString("EntityId", type.getName());
        ItemStack bukkitItemStack = CraftItemStack.asCraftMirror(itemStack).clone();
        ItemMeta meta = bukkitItemStack.getItemMeta();
        String displayName;
        if (entityToDisplayName.containsKey(type)) {
            displayName = entityToDisplayName.get(type);
        } else {
            displayName = type.getName();
        }
        meta.setDisplayName(ChatColor.RESET + displayName + " Spawner");
        bukkitItemStack.setItemMeta(meta);
        return bukkitItemStack;
    }

    @Override
    public EntityType getEntityType(ItemStack is) {
        net.minecraft.server.v1_8_R1.ItemStack itemStack;
        CraftItemStack craftStack = CraftItemStack.asCraftCopy(is);
        itemStack = CraftItemStack.asNMSCopy(craftStack);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null || !tag.hasKey("BlockEntityTag")) {
            throw new IllegalArgumentException();
        }
        String name = tag.getCompound("BlockEntityTag").getString("EntityId");
        return EntityType.fromName(name);
    }

    @Override
    public String getHumanName() {
        return "CraftBukkit 1.8 NMS-based provider";
    }
}
