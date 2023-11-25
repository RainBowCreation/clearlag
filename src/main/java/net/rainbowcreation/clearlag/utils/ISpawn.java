package net.rainbowcreation.clearlag.utils;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rainbowcreation.clearlag.Main;

public class ISpawn {
    public static void removeBlockAndSpawnAsItem(World world, BlockPos pos) {
        // Get the block state at the specified position
        BlockPos pos2 = pos.down();
        pos = pos.up();
        IBlockState blockState = world.getBlockState(pos);
        IBlockState blockState2 = world.getBlockState(pos2);
        Block block = blockState.getBlock();
        Block block2 = blockState2.getBlock();

        if (block.getBlockHardness(blockState, world, pos) < 0 || block2.getBlockHardness(blockState2, world, pos2) < 0) {
            return;
        }
        // Set the block state at the specified position to air
        world.destroyBlock(pos, false);
    }

    public static void spawnTNTWithFuse(World world, BlockPos pos, int fuseTime) {
        // Check if the world is not null and is not remote (client-side)
        if (world != null && !world.isRemote) {
            // Create a primed TNT entity at the specified position
            EntityTNTPrimed tnt = new EntityTNTPrimed(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, null);

            // Set the fuse time (in ticks)
            tnt.setFuse(fuseTime);

            // Spawn the TNT entity in the world
            world.spawnEntity(tnt);
        }
    }

    public static void spawnXPOrb(World world, double x, double y, double z, int xp) {
        EntityXPOrb xpOrb = new EntityXPOrb(world, x, y, z, xp);
        world.spawnEntity(xpOrb);
    }

    public static void spawnBlockAsItem(World world, BlockPos pos, Block block) {
        // Create an ItemStack representing the block
        ItemStack itemStack = new ItemStack(block);

        // Spawn the ItemStack as an item entity in the world
        EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
        world.spawnEntity(entityItem);
    }

    public static void spawnLightning(World world, BlockPos pos) {
        // Check if the world is not null and is not remote (client-side)
        if (world != null && !world.isRemote) {
            // Trigger a thunderstorm at the specified position
            world.getWorldInfo().setThundering(true);

            // Optionally, you can also set it to raining
            world.getWorldInfo().setRaining(true);

            // Spawn a lightning bolt at the specified position
            world.addWeatherEffect(new net.minecraft.entity.effect.EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));
        }
    }
    /*
    public static void spawnExplosion(World world, BlockPos pos) {
        spawnExplosion(world, pos,3.0f,true);
    }

    public static void spawnExplosion(World world, BlockPos pos, float strength, boolean damagesTerrain) {
        // Check if the world is not null and is not remote (client-side)
        if (world != null && !world.isRemote) {
            // Spawn an explosion at the specified position
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), strength, damagesTerrain);
        }
    }
     */
}
