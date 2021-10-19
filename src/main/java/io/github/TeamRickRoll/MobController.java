package io.github.TeamRickRoll;

import kotlin.reflect.jvm.internal.impl.incremental.components.Position;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;

public class MobController {

    Instance instance;
    public MobController(Instance instance){
        this.instance = instance;
    }

    public void spawnEntity(Pos position, EntityType entityType){

        Entity entity = new Mob(entityType);
        entity.setInstance(instance, position);

    }
}
