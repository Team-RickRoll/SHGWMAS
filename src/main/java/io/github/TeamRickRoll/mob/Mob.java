package io.github.TeamRickRoll.mob;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Mob extends EntityCreature {

    Entity target;
    public Mob(@NotNull EntityType entityType, Entity target, Team team) {
        super(entityType);
        this.target = target;
        setHealth(6f);
        setTeam(team);
        setGlowing(true);

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
                        .addGoalSelector(new MeleeAttackGoal(this, 1.5, Duration.ofMillis(500)))
                        .build()
        );
    }
    @Override
    public void update(long time){
        // Best variable names
        double shitz = position.x() + position.y() + position.z();
        double playerShitz = target.getPosition().x() + target.getPosition().y() + target.getPosition().z();

        if((Math.max(shitz, playerShitz) - Math.min(shitz, playerShitz)) >= 20){
            kill();
        }

        // AI
        aiTick(time);

        // Path finding
        this.getNavigator().tick();
    }
}
