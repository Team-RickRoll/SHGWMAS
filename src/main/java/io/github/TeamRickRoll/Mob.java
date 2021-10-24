package io.github.TeamRickRoll;

import com.sun.tools.javac.Main;
import kotlin.reflect.jvm.internal.impl.incremental.components.Position;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomLookAroundGoal;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Mob extends EntityCreature {

    public Mob(@NotNull EntityType entityType, Entity target) {
        super(entityType);
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addTargetSelector(new TargetSelector(this) {
                            @Override
                            public Entity findTarget() {
                                return target;
                            }
                        })
                        .addGoalSelector(new FollowTargetGoal(this, Duration.ofSeconds(1)))
                        .build()
        );

    }





}
