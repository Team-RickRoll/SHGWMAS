package io.github.TeamRickRoll;

import io.github.TeamRickRoll.mob.MobController;
import io.github.TeamRickRoll.sounds.SoundController;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;

public class SHGWMAS {
    public static void main(String[] args) {
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
        SoundController soundController = new SoundController(instanceContainer);


        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        // Basic player login setup
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 50, 0));
        });

        // Right click block event used for debugging and testing
        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            // Whooo it works!
            soundController.soundFrenzy();
        });

        // Stars the server, please dont put stuff under here :3
        minestom.start("0.0.0.0", 25565);
    }
}
