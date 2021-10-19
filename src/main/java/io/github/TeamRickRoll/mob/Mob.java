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
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addTargetSelector(new TargetSelector(this) {
                            @Override
                            public Entity findTarget() {
                                return target;
                            }
                        })
                        .addGoalSelector(new FollowTargetGoal(this, Duration.ofMillis(20)))
                        .build()
        );
    }
}
