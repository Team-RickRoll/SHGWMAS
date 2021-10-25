package io.github.TeamRickRoll;

import io.github.TeamRickRoll.commands.StartGame;
import io.github.TeamRickRoll.jumpscare.Jumpscare;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.resourcepack.ResourcePack;

public class SHGWMAS {
    public static void main(String[] args) {

        MinecraftServer minestom = MinecraftServer.init();
        MojangAuth.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        Game game = new Game(instanceContainer);

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(-275, 69, -330));
        });

        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
           if(event.getBlock() == Block.SPRUCE_DOOR || event.getBlock() == Block.DARK_OAK_DOOR)
               new Jumpscare().sendJumpscare(event.getPlayer());
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            if(game.getGameState() == 1){
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(new Pos(-275, 69, -330));
            }
            player.setGameMode(GameMode.ADVENTURE);
            player.setResourcePack(ResourcePack.forced("https://www.dropbox.com/sh/3t322wr4cvq8wio/AABJgUmMUiSOd_CfJvoo0Atza?dl=1", null));
        });

        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            if(instanceContainer.getPlayers().size() < 2 /* Event called before the player is removed from the players map */
                    && game.getGameState() == 1){
                // Tried to break the game huh?
                MinecraftServer.getServer().stop();
            }
        });

        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event ->{
            event.setCancelled(true);
        });

        MinecraftServer.getCommandManager().register(
                new StartGame(
                     game
                )
        );
        // Starts the server, please don't put stuff under here :3
        minestom.start("0.0.0.0", 25565);
    }
}
