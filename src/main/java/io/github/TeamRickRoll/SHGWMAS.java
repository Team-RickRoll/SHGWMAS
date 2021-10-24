package io.github.TeamRickRoll;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.nio.file.Path;
import java.util.Set;


public class SHGWMAS {
    public static void main(String[] args) {
        MinecraftServer minestom = MinecraftServer.init();
        MojangAuth.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        //instanceContainer.setChunkGenerator(new GeneratorDemo());

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 200, 0));
        });
        globalEventHandler.addListener(PlayerChatEvent.class, event -> {
            Player player = event.getPlayer();
            var secs = player.getInstance().getChunkAt(player.getPosition().sub(0,1,0)).getSections();
            secs.forEach((i, s) -> {
                StringBuilder sb = new StringBuilder(s.getBlockLight().length);
                for (var b :
                        s.getBlockLight()) {
                    sb.append(b);
                }
                System.out.println(i + " | " + s.getBlockLight().length + " | " + sb);
            });
        });

        minestom.start("0.0.0.0", 25565);
    }
}
