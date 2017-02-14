package fchub;

import fchub.managers.BossBarManager;
import fchub.managers.LobbyManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by loucass003 on 2/3/17.
 */
public class Main extends JavaPlugin
{
	private static Main instance;
	private static BossBarManager bossBarManager;
	private static LobbyManager lobbyManager;

	public Main()
	{
		instance = this;
		bossBarManager = new BossBarManager();
		lobbyManager = new LobbyManager();
	}

	@Override
	public void onEnable()
	{
		bossBarManager.init();
		lobbyManager.init();
	}

	@Override
	public void onDisable()
	{

	}

	public static Main getInstance()
	{
		return instance;
	}
}