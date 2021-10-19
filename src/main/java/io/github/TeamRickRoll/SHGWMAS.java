package io.github.TeamRickRoll;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;

public class SHGWMAS {
    public static void main(String[] args) {
        MinecraftServer minestom = MinecraftServer.init();
        MojangAuth.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkGenerator(new GeneratorDemo());

        MobController mobController = new MobController(instanceContainer);

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
           final Player player = event.getPlayer();
           event.setSpawningInstance(instanceContainer);
           player.setRespawnPoint(new Pos(0, 50, 0));

        });

        minestom.start("0.0.0.0", 25565);
    }
}
