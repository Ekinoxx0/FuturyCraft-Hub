package fc.hub;

import api.API;
import fc.hub.events.PlayerEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Created by loucass003 on 12/12/16.
 */
public class Main extends JavaPlugin
{
    public static Main instance;
    public API api;

    public Main()
    {
        instance = this;
        api = API.getInstance();
        api.useQueueManager(false);
    }

    @Override
    public void onEnable()
    {
        getLogger().log(Level.INFO, "Enabled !");
        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
    }

    @Override
    public void onDisable()
    {
        getLogger().log(Level.INFO, "Disabled !");
    }

}
