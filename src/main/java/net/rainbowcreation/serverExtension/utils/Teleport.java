package net.rainbowcreation.serverExtension.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class Teleport extends Teleporter {
    private final WorldServer worldServer;
    private double x, y, z;
    public Teleport(WorldServer worldServer, double x, double y, double z) {
        super(worldServer);
        this.worldServer = worldServer;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void teleport(EntityPlayer player, int dimension, double x, double y, double z) {
        System.out.println("1");
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        System.out.println("2");
        MinecraftServer server = player.getEntityWorld().getMinecraftServer();
        System.out.println("2");
        WorldServer worldServer = server.getWorld(dimension);
        System.out.println("3");

        if (dimension != player.dimension) {
            System.out.println("4");
            if (worldServer == null)

            server.getPlayerList().transferPlayerToDimension(playerMP, dimension, new Teleport(worldServer, x, y, z));
            System.out.println("5");
        }
        System.out.println("6");
        player.setPositionAndUpdate(x, y, z);
    }
}
