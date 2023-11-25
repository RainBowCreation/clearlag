package net.rainbowcreation.clearlag.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;

public class IChunk {
    public static Map<Long, Integer> processMap(Map<Long, Integer> inputMap) {
        Map<Long, Integer> resultMap = new HashMap<>();

        // Iterate over the entries in the inputMap
        for (Map.Entry<Long, Integer> entry : inputMap.entrySet()) {
            // Decrease the value by 1
            int newValue = entry.getValue() - 1;

            // If the new value is greater than 0, add it to the resultMap
            if (newValue > 0) {
                resultMap.put(entry.getKey(), newValue);
            }
            // If the new value is 0 or negative, do not add it to the resultMap (remove it).
        }

        return resultMap;
    }

    public static AxisAlignedBB getChunkBoundingBox(Chunk chunk) {
        int chunkX = chunk.x * 16;
        int chunkZ = chunk.z * 16;
        return new AxisAlignedBB(chunkX, 0, chunkZ, chunkX + 16, 256, chunkZ + 16);
    }


    public static int countTileEntitiesInChunk(net.minecraft.world.chunk.Chunk chunk) {
        int count = 0;
        for (TileEntity tileEntity : chunk.getTileEntityMap().values()) {
            // Customize this check based on the types of tile entities you want to consider
            if (tileEntity != null) {
                count++;
            }
        }
        return count;
    }

    public static long getChunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX & 0xFFFFFFFFL) | (((long) chunkZ & 0xFFFFFFFFL) << 32);
    }
}
