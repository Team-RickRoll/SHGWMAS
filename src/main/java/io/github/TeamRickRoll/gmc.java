package io.github.TeamRickRoll;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;

public class gmc extends Command {
    public gmc() {
        super("gmc");
        setDefaultExecutor((sender, context) -> {
            switch (sender.asPlayer().getGameMode()){
                case SPECTATOR -> sender.asPlayer().setGameMode(GameMode.CREATIVE);
                case CREATIVE -> sender.asPlayer().setGameMode(GameMode.SURVIVAL);
                case SURVIVAL -> sender.asPlayer().setGameMode(GameMode.SPECTATOR);
            }
        });
    }
}
