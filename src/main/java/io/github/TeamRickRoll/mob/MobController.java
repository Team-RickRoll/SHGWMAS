package io.github.TeamRickRoll.mob;

import io.github.TeamRickRoll.sounds.SoundController;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.scoreboard.Team;

public class MobController {
    // The "world" to spawn the mob in
    Instance instance;
    Team team = null;
    public MobController(Instance instance) {
        this.instance = instance;
        team = MinecraftServer.getTeamManager().createTeam("MobTeam");
        team.setTeamColor(NamedTextColor.DARK_RED);
        for(Player player : instance.getPlayers()){
            player.getPlayerConnection().sendPacket(team.createTeamsCreationPacket());
        }
    }

    public void spawnEntity(Pos position, EntityType entityType, Player player) {
        SoundController.spook(player);
        Entity mob = new Mob(entityType, player, team);
        mob.setInstance(instance, position);
    }
}
