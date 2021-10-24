package io.github.TeamRickRoll.jumpscare;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Jumpscare {

    public ItemStack createItem(){
        return ItemStack.builder(Material.CARVED_PUMPKIN)
                .build();
    }

    public @NotNull Runnable createTask(Player player, int chance){
        return () -> {
            try {
                if(getChance() <= chance) {
                    player.setHelmet(createItem());
                    // Plays custom sound from the texture pack
                    player.playSound(Sound.sound(Key.key("custom.jumpscare"), Sound.Source.MASTER, 100f, 1f));
                    // lazy luuul
                    Thread.sleep(800);
                    player.setHelmet(ItemStack.AIR);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    public void sendJumpscare(Player player){
        MinecraftServer.getSchedulerManager().buildTask(createTask(player, 100)).delay(2, TimeUnit.SECOND).schedule();
    }

    public void sendRandomJumpscares(Player player){
        MinecraftServer.getSchedulerManager().buildTask(createTask(player, 30)).repeat(10, TimeUnit.SECOND).schedule();
    }

    public static int getChance(){
        return new Random().nextInt(100);
    }

}
