package io.github.TeamRickRoll.mob;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Mob extends EntityCreature {
    public Mob(@NotNull EntityType entityType, Entity target) {
        super(entityType);
        setGlowing(true);
        setCustomName(Component.text("THIS IS A NAME"));

        // Basic ai that follows the entity provided in the constructor
        // Updates its path once every 20ms
        addAIGroup(
                new EntityAIGroupBuilder()
                        // This tells the Mob what entity to actually follow
                        .addTargetSelector(new TargetSelector(this) {
                            @Override
                            public Entity findTarget() {
                                return target;
                            }
                        })
                        // The follow goal itself, grabs what entity it needs to follow from above
                        .addGoalSelector(new FollowTargetGoal(this, Duration.ofMillis(20)))
                        .build()
        );
    }
}
