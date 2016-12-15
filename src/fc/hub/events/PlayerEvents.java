package fc.hub.events;

import fc.hub.Main;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loucass003 on 13/12/16.
 */
public class PlayerEvents implements Listener
{

    public Main main;
    public BossBar bossBar;
    public int barBroadcaster = -1;
    public List<String> barMessages;
    public int currMessage;

    public PlayerEvents(Main main)
    {
        this.main = main;
        this.barMessages = new ArrayList<>();
        barMessages.add("Test");
        barMessages.add("Plop");
        barMessages.add("Bijour");
        barMessages.add("Ca va ?");
        bossBar = Bukkit.createBossBar(barMessages.get(0), BarColor.WHITE, BarStyle.SOLID);
        bossBar.setVisible(true);
        Bukkit.getOnlinePlayers().stream().filter(p -> !bossBar.getPlayers().contains(p)).forEach(p -> bossBar.addPlayer(p));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        e.setJoinMessage("");
        Player p = e.getPlayer();
        p.setSprinting(true);
        p.setWalkSpeed(0.3F);
        bossBar.addPlayer(p);

        if(Bukkit.getOnlinePlayers().size() >= 0 && barBroadcaster == -1)
        {
            barBroadcaster = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.main, () -> {
                if(currMessage >= barMessages.size())
                    currMessage = 0;
                bossBar.setTitle(barMessages.get(currMessage));
                currMessage++;
            }, 0L, 20L);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        bossBar.removePlayer(e.getPlayer());
        if(Bukkit.getOnlinePlayers().size() <= 0)
        {
            Bukkit.getScheduler().cancelTask(barBroadcaster);
        }
    }


}
