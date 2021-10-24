package io.github.TeamRickRoll;

import io.github.TeamRickRoll.commands.StartGame;
import io.github.TeamRickRoll.commands.TestSpawn;
import io.github.TeamRickRoll.commands.gmc;
import io.github.TeamRickRoll.jumpscare.Jumpscare;
import io.github.TeamRickRoll.mob.MobController;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SHGWMAS {

    public static void main(String[] args) {
        HashMap<Entity, Boolean> canAttack = new HashMap<>();
        MinecraftServer minestom = MinecraftServer.init();

        // Mojang auth...
        MojangAuth.init();

        // World Manager
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        // The "world"
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        // Using the demo generator
        instanceContainer.setChunkGenerator(new GeneratorDemo());

        MobController mobController = new MobController(instanceContainer);
        Game game = new Game(instanceContainer);
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        // Basic player login setup
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(-275, 69, -330));
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
           if(game.getGameState() == 1){
               player.setGameMode(GameMode.SPECTATOR);
               player.teleport(new Pos(-275, 69, -330));
           }
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

        globalEventHandler.addListener(EntityAttackEvent.class, event->{
            if(!canAttack.containsKey(event.getEntity())){
                canAttack.put(event.getEntity(), true);
            }
            if(canAttack.get(event.getEntity())){
                ((LivingEntity)event.getTarget()).damage(DamageType.fromEntity(event.getEntity()), 3f);
                canAttack.put(event.getEntity(), false);
                MinecraftServer.getSchedulerManager().buildTask(() ->
                            canAttack.put(event.getEntity(), true)
                        ).delay(Duration.ofSeconds(2)).schedule();
            }

        });

        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            if(event.getBlock() == Block.DARK_OAK_DOOR || event.getBlock() == Block.SPRUCE_DOOR){
                if(Jumpscare.getChance() < 20){
                    new Jumpscare().sendJumpscare(event.getPlayer());
                }
            }
        });

        MinecraftServer.getCommandManager().register(
                new StartGame(
                     game
                )
        );
        MinecraftServer.getCommandManager().register(
                new gmc()
        );

        MinecraftServer.getCommandManager().register(
                new TestSpawn(mobController)
        );
        // Starts the server, please don't put stuff under here :3
        minestom.start("0.0.0.0", 25565);
    }
}
