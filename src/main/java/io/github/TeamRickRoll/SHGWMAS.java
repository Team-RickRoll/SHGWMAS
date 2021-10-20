package io.github.TeamRickRoll;

import io.github.TeamRickRoll.mob.MobController;
import io.github.TeamRickRoll.sounds.SoundController;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.time.Duration;
import java.util.HashMap;

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
        soundController.soundLoop();

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        // Basic player login setup
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 50, 0));
        });

        globalEventHandler.addListener(PlayerChatEvent.class, event -> {
            // Whooo it works!

            Player player = event.getPlayer();
            switch (event.getMessage()){
                case "sus":
                    player.getInventory().addItemStack(ItemStack.of(Material.BEEF));
                    player.getInventory().addItemStack(ItemStack.of(Material.BEETROOT));
                    player.getInventory().addItemStack(ItemStack.of(Material.APPLE));
                    player.getInventory().addItemStack(ItemStack.of(Material.BAMBOO));
                    player.getInventory().addItemStack(ItemStack.of(Material.BAKED_POTATO));
                    break;
                default:
                    break;

            }
        });

        // Starts the server, please dont put stuff under here :3
        minestom.start("0.0.0.0", 25565);
    }
}
