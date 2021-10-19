package io.github.TeamRickRoll.mob;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class MobController {
    Instance instance;
    public MobController(Instance instance){
        this.instance = instance;
    }

    public void spawnEntity(Pos position, EntityType entityType, Player player){
        Entity mob = new Mob(entityType, player);
        mob.setInstance(instance, position);
    }
}
