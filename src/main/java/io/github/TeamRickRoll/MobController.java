package io.github.TeamRickRoll;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;

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
