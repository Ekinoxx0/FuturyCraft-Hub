package fchub.managers;

import api.API;
import api.packet.PacketReceivedEvent;
import api.packet.server.BossBarMessagesPacket;
import api.packet.server.RequestBossBarMessages;
import fchub.Main;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by loucass003 on 2/4/17.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BossBarManager
{
	private final Listen listen = new Listen();
	private final BossBar bossBar = Bukkit.createBossBar("none", BarColor.WHITE, BarStyle.SEGMENTED_10);
	private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	private List<BossBarMessagesPacket.MessageData> messages;
	private BossBarMessagesPacket.MessageData currentMessage;
	private int messageOffset;
	private boolean firstStart = true;
	private final Object mutex = new Object();

	public void init()
	{
		Main.getInstance().getServer().getPluginManager().registerEvents(listen, Main.getInstance());
		bossBar.setVisible(false);
		Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
	}

	public class Listen implements Listener
	{
		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent e)
		{
			if (messages == null && Bukkit.getOnlinePlayers().size() == 1)
			{
				API.getInstance().getMessenger().sendPacket(new RequestBossBarMessages());
			}
			bossBar.addPlayer(e.getPlayer());
		}

		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e)
		{
			bossBar.removePlayer(e.getPlayer());
		}

		@EventHandler
		public void onReceivePacket(PacketReceivedEvent e)
		{
			if (e.getPacket() instanceof BossBarMessagesPacket)
			{
				synchronized (mutex)
				{
					messages = ((BossBarMessagesPacket) e.getPacket()).getMessages();
				}

				if (!messages.isEmpty() && firstStart)
				{
					currentMessage = messages.get(messageOffset);
					bossBar.setTitle(currentMessage.getMessage().replace("&", "§"));
					bossBar.setProgress(1);
					bossBar.setVisible(true);
					firstStart = false;
					scheduleBossBar(9, currentMessage.getTime() * 100, currentMessage);
				}
				else if(messages.isEmpty())
				{
					bossBar.setVisible(true);
					firstStart = true;
					exec.shutdown();
				}
			}
		}

	}

	public void scheduleBossBar(int counter, long delay, BossBarMessagesPacket.MessageData data)
	{
		exec.schedule
				(
						() ->
						{
							synchronized (mutex)
							{
								if (messages.isEmpty())
								{
									exec.shutdown();
								}

								if(messages.size() > 1)
									bossBar.setProgress(counter / 10.0);
								if (counter > 0)
									scheduleBossBar(counter - 1, delay, data);
								else
								{
									messageOffset++;
									if (messageOffset >= messages.size())
										messageOffset = 0;
									currentMessage = messages.get(messageOffset);
									bossBar.setTitle(currentMessage.getMessage().replace("&", "§"));
									bossBar.setProgress(1);
									scheduleBossBar(9, currentMessage.getTime() * 100, currentMessage);
								}
							}
						},
						delay,
						TimeUnit.MILLISECONDS
				);
	}


}
