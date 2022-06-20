package com.ruserious99.mini_npc.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            CraftPlayer craftPlayer = (CraftPlayer) player;
            ServerPlayer serverPlayer = craftPlayer.getHandle();

            MinecraftServer server = serverPlayer.getServer();
            ServerLevel level = serverPlayer.getLevel();

            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Block Break");
            String signature = "wJPswoHZEuhiicWCZ6gZ1fqnOiHY1MbJ6PPA+lBgMX6IJ94eVx/OwOj6KRXW9EZP3XE/tkp15ymuuxNuufhDGGrtacnVPVsPSfB8RVhGHuPz/ttCaFLO2KmP+iG0P99PTpp9HvcD/HxDSL1zuc/wK2TRDflOmR2b8DD1pjw156Y0rpe1IPCVtTDzw4T8KQ4eJ0GQPCXI7QCNNbeIWlRktixMd/RFPJvKGh6VEX+UP32sF1VZhVVrotZliueFzuYG4Oz6Xr6qBuejd97TbgoMjubB8Ym59kFIKgIr7vBJ4MF9KLn30L3k0FIg8Ab6qjiD8cMo50br83eLUu/L2kYQK708nUvkGhjypy3/fqgPDMZldpq+OKxYJpTPyHsoby6AJ8HOWWKbnj35X4G50+bG1TwmZLPHPC7+jgGfrNJpar/rkf2Rnirm+jfOMMUaaO5fJo25rFEQExpEUboKtYgoHeXVDJqWR+lpmoTggdTRCR9duPLoGmbVhVaiLCe8HxiRPdtzlDOFbL75Wscg4L2zbFTd76/sBrNd+SgBuDUvBh8S2/uLsrKOe5ioBbtj6jKmRR+QoRS6VWqw+hAQzPoVXzC3vstgzi0mFVt/A0RyQ+1HXZBD4vn7GhaacBXWeQ1RSok6k8Cfu1CxE+Ou/XySyXB+t2ZYmAOT8OxFg37J52M=";
            String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY1NTYxNzk2MzQzMiwKICAicHJvZmlsZUlkIiA6ICJlOTUzODllY2MzZWE0MTRkYjVmNzYxZjEyZWY1Mzg2NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSdVNlcmlvdXM5OSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYTNiOWEyMDgyNTdjMTE3OWY1MTc0MDdjOWNhOGVlMGMxZjczZjczODg5YjNlMzY1NzNmZjU2ZDIwNjc2MTNiIgogICAgfQogIH0KfQ==";

            gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

            ServerPlayer blockNPC = new ServerPlayer(Objects.requireNonNull(server), level, gameProfile);
            blockNPC.setPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());

            ServerGamePacketListenerImpl serverGamePacketListener = serverPlayer.connection;

            //playerInfoPacket
            serverGamePacketListener.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, blockNPC));

            //spawnPacket
            serverGamePacketListener.send(new ClientboundAddPlayerPacket(blockNPC));

            //armor and items inhand
            ItemStack itemInHand = new ItemStack(Material.DIAMOND_SWORD);
            serverGamePacketListener.send(new ClientboundSetEquipmentPacket(blockNPC.getBukkitEntity().getEntityId(),
                    List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(itemInHand)))));



        }

        return false;
    }
}
