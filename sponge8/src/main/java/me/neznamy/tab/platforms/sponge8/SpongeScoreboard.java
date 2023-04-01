package me.neznamy.tab.platforms.sponge8;

import lombok.NonNull;
import me.neznamy.tab.api.chat.IChatBaseComponent;
import me.neznamy.tab.shared.player.Scoreboard;
import org.spongepowered.api.scoreboard.*;
import org.spongepowered.api.scoreboard.criteria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;

import java.util.Collection;

public class SpongeScoreboard extends Scoreboard<SpongeTabPlayer> {

    public SpongeScoreboard(SpongeTabPlayer player) {
        super(player);
    }

    @Override
    public void setDisplaySlot(DisplaySlot slot, @NonNull String objective) {
        player.getPlayer().scoreboard().objective(objective).ifPresent(
                o -> player.getPlayer().scoreboard().updateDisplaySlot(o, convertDisplaySlot(slot)));
    }

    private static org.spongepowered.api.scoreboard.displayslot.DisplaySlot convertDisplaySlot(DisplaySlot slot) {
        switch (slot) {
            case PLAYER_LIST: return DisplaySlots.LIST.get();
            case SIDEBAR: return DisplaySlots.SIDEBAR.get();
            default: return DisplaySlots.BELOW_NAME.get();
        }
    }

    @Override
    public void registerObjective0(@NonNull String objectiveName, @NonNull String title, boolean hearts) {
        Objective objective = Objective.builder()
                .name(objectiveName)
                .displayName(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(title), player.getVersion()))
                .objectiveDisplayMode(hearts ? ObjectiveDisplayModes.HEARTS : ObjectiveDisplayModes.INTEGER)
                .criterion(Criteria.DUMMY)
                .build();
        player.getPlayer().scoreboard().addObjective(objective);
    }

    @Override
    public void unregisterObjective0(@NonNull String objectiveName) {
        player.getPlayer().scoreboard().objective(objectiveName).ifPresent(o ->
                player.getPlayer().scoreboard().removeObjective(o));
    }

    @Override
    public void updateObjective0(@NonNull String objectiveName, @NonNull String title, boolean hearts) {
        Objective obj = player.getPlayer().scoreboard().objective(objectiveName).orElseThrow(IllegalStateException::new);
        obj.setDisplayName(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(title), player.getVersion()));
        obj.setDisplayMode(hearts ? ObjectiveDisplayModes.HEARTS.get() : ObjectiveDisplayModes.INTEGER.get());
    }

    @Override
    public void registerTeam0(@NonNull String name, String prefix, String suffix, String visibility, String collision, Collection<String> players, int options) {
        Team team = Team.builder()
                .name(name)
                .displayName(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(name), player.getVersion()))
                .prefix(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(prefix), player.getVersion()))
                .suffix(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(suffix), player.getVersion()))
                .allowFriendlyFire((options & 0x01) != 0)
                .canSeeFriendlyInvisibles((options & 0x02) != 0)
                .collisionRule(convertCollisionRule(collision))
                .nameTagVisibility(convertVisibility(visibility))
                .build();
        for (String member : players) {
            team.addMember(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(member), player.getVersion()));
        }
        player.getPlayer().scoreboard().registerTeam(team);
    }

    @Override
    public void unregisterTeam0(@NonNull String name) {
        player.getPlayer().scoreboard().team(name).ifPresent(Team::unregister);
    }

    @Override
    public void updateTeam0(@NonNull String name, String prefix, String suffix, String visibility, String collision, int options) {
        Team team = player.getPlayer().scoreboard().team(name).orElse(null);
        if (team == null) return;
        team.setDisplayName(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(name), player.getVersion()));
        team.setPrefix(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(prefix), player.getVersion()));
        team.setSuffix(Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(suffix), player.getVersion()));
        team.setAllowFriendlyFire((options & 0x01) != 0);
        team.setCanSeeFriendlyInvisibles((options & 0x02) != 0);
        team.setCollisionRule(convertCollisionRule(collision));
        team.setNameTagVisibility(convertVisibility(visibility));
    }

    private static CollisionRule convertCollisionRule(String rule) {
        switch (rule) {
            case "always": return CollisionRules.ALWAYS.get();
            case "never": return CollisionRules.NEVER.get();
            case "pushOtherTeams": return CollisionRules.PUSH_OTHER_TEAMS.get();
            case "pushOwnTeam": return CollisionRules.PUSH_OWN_TEAM.get();
            default: throw new IllegalArgumentException();
        }
    }

    private static Visibility convertVisibility(String visibility) {
        switch (visibility) {
            case "always": return Visibilities.ALWAYS.get();
            case "never": return Visibilities.NEVER.get();
            case "hideForOtherTeams": return Visibilities.HIDE_FOR_OTHER_TEAMS.get();
            case "hideForOwnTeam": return Visibilities.HIDE_FOR_OWN_TEAM.get();
            default: throw new IllegalArgumentException();
        }
    }

    @Override
    public void setScore0(@NonNull String objective, @NonNull String playerName, int score) {
        player.getPlayer().scoreboard().objective(objective).ifPresent(o -> o.findOrCreateScore(
                Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(playerName), 
                        player.getVersion())).setScore(score));
    }

    @Override
    public void removeScore0(@NonNull String objective, @NonNull String playerName) {
        player.getPlayer().scoreboard().objective(objective).ifPresent(o -> o.removeScore(
                Sponge8TAB.getAdventureCache().get(IChatBaseComponent.optimizedComponent(playerName), player.getVersion())));
    }
}
