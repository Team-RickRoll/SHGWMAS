package io.github.TeamRickRoll;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.instance.Instance;

import java.time.Duration;

public class MobController {

    Instance instance;
    public MobController(Instance instance){
        this.instance = instance;
    }

    public void spawnEntity(Pos position, EntityType entityType, Entity target) {

        Entity entity = new Mob(entityType, target);
        entity.setInstance(instance, position);
    }




}
