package io.github.TeamRickRoll.commands;

import io.github.TeamRickRoll.Game;
import net.minestom.server.command.builder.Command;

public class StartGame extends Command {
    public StartGame(Game game) {
        super("startgame", "sg");
        setDefaultExecutor(((sender, context) -> {
            if(game.getGameState() == 0){
                game.startGame();
                game.setGameState(1); // 0 - not in progress, 1 - in progress
            }
        }));
    }
}
