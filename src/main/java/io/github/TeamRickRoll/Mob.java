package io.github.TeamRickRoll;

import com.sun.tools.javac.Main;
import kotlin.reflect.jvm.internal.impl.incremental.components.Position;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomLookAroundGoal;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Mob extends EntityCreature {

    public Mob(@NotNull EntityType entityType) {
        super(entityType);
    }

    public void meleeTargetPlayer(EntityCreature entity){
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new MeleeAttackGoal(entity, 1, Duration.ofSeconds(2)))
                        .build()
        );
    }

    public void followTargetPlayer(EntityCreature entity, Long time){
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new FollowTargetGoal(entity, Duration.ofSeconds(time)))
                        .build()
        );
    }



}
