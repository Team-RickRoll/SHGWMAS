package io.github.TeamRickRoll.jumpscare;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.time.Duration;
import java.util.Random;

public class Jumpscare {
    ItemStack jumpscareOverlay = ItemStack.builder(Material.CARVED_PUMPKIN).build();

    public void sendJumpscare(Player player){
        player.setHelmet(jumpscareOverlay);
        // Plays custom sound from the texture pack
        player.playSound(Sound.sound(Key.key("custom.jumpscare"), Sound.Source.MASTER, 100f, 1f));

        MinecraftServer.getSchedulerManager().buildTask(() ->{
            player.setHelmet(ItemStack.AIR);
        }).delay(Duration.ofSeconds(1)).schedule();
    }

    public static int getChance(){
        return new Random().nextInt(100);
    }

}
