package io.github.TeamRickRoll.commands;

import io.github.TeamRickRoll.mob.MobController;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.EntityType;

public class TestSpawn extends Command {
    public TestSpawn(MobController mobController) {
        super("ts");
        setDefaultExecutor((sender, context) -> {
            mobController.spawnEntity(sender.asPlayer().getPosition(), EntityType.ZOMBIE, sender.asPlayer());
        });
    }
}
