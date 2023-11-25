package net.rainbowcreation.clearlag.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.rainbowcreation.clearlag.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IEntity {

    public static List<Entity> getAllEntities(World world) {
        // Create a bounding box that encompasses the entire world
        double minX = Double.NEGATIVE_INFINITY;
        double minY = Double.NEGATIVE_INFINITY;
        double minZ = Double.NEGATIVE_INFINITY;
        double maxX = Double.POSITIVE_INFINITY;
        double maxY = Double.POSITIVE_INFINITY;
        double maxZ = Double.POSITIVE_INFINITY;

        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));

        return entityList;
    }

    public static List<List<String>> groupEntitiesByNameTag(World world, List<Entity> entityList, double radius) {
        List<List<String>> groupedEntities = new ArrayList<>();

        Map<Class<? extends Entity>, List<Entity>> entityMap = new HashMap<>();

        // Group entities by their class type
        for (Entity entity : entityList) {
            if (entity instanceof EntityAnimal) {
                Class<? extends Entity> entityClass = entity.getClass();
                entityMap.computeIfAbsent(entityClass, k -> new ArrayList<>()).add(entity);
            }
        }

        // Collect modifications to the original list
        List<Modification> modifications = new ArrayList<>();

        // Check each group within the specified radius
        for (List<Entity> entities : entityMap.values()) {
            for (Entity entity : entities) {
                // Check if the entity is already part of a group
                if (isEntityInAnyGroup(entity, groupedEntities)) {
                    continue;
                }

                // Group entities within the specified radius
                Modification modification = groupEntitiesWithinRadius(entity, entities, radius);
                if (modification != null) {
                    modifications.add(modification);
                }
            }
        }

        // Apply modifications to the original list
        for (Modification modification : modifications) {
            modification.apply();
        }

        return groupedEntities;
    }

    private static Modification groupEntitiesWithinRadius(Entity anchorEntity, List<Entity> entities, double radius) {
        int count = 0;

        for (Entity entity : entities) {
            if (entity != anchorEntity && entity.getDistance(anchorEntity) <= radius) {
                count++;
            }
        }

        if (count > 0) {
            String entityName = anchorEntity.getClass().getSimpleName() + " X" + (count + 1);
            Main.LOGGER.info(entityName);
            anchorEntity.setCustomNameTag(entityName);
            anchorEntity.setAlwaysRenderNameTag(true);
            entities.removeIf(entity -> entity.getDistance(anchorEntity) <= radius);
            return new Modification(entities, entityName);
        } else {
            return null;
        }
    }

    private static boolean isEntityInAnyGroup(Entity entity, List<List<String>> groupedEntities) {
        for (List<String> entityGroup : groupedEntities) {
            if (entityGroup.contains(entity.getCustomNameTag())) {
                return true;
            }
        }
        return false;
    }

    private static class Modification {
        private final List<Entity> entities;
        private final String newName;

        public Modification(List<Entity> entities, String newName) {
            this.entities = entities;
            this.newName = newName;
        }

        public void apply() {
            for (Entity entity : entities) {
                entity.setCustomNameTag(newName);
                entity.setAlwaysRenderNameTag(true);
            }
        }
    }
}
