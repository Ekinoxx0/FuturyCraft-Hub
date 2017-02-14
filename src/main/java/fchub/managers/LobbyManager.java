package fchub.managers;

import fchub.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by loucass003 on 2/5/17.
 */
public class LobbyManager
{
	private final Listen listen = new Listen();

	public void init()
	{
		Main.getInstance().getServer().getPluginManager().registerEvents(listen, Main.getInstance());
	}

	public class Listen implements Listener
	{
		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent e)
		{
			Player p = e.getPlayer();
			p.setWalkSpeed(0.3F);
			p.setInvulnerable(true);
			p.setCollidable(false);
			p.setGameMode(GameMode.ADVENTURE);
		}

		@EventHandler
		public void onFoodLevelChange(FoodLevelChangeEvent e)
		{
			e.setCancelled(true);
		}
	}
}
