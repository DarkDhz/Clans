package es.darkhorizon.dev;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

public class placeholders implements Listener {
	private final main plugin;
	public placeholders(final main plugin) { 
		this.plugin = plugin; 		
		new BukkitRunnable() {			
			public void run() {	
				PlaceholderAPI.registerPlaceholder(plugin, "clans.clan", new PlaceholderReplacer() {
					public String onPlaceholderReplace(
							PlaceholderReplaceEvent event) {
						Player p = event.getPlayer();
						if (plugin.pclans.get(p.getName().toString()) == null) {
							return "No Clan!";
						}
						return plugin.pclans.get(p.getName().toString());
					}
				}); 
				
				PlaceholderAPI.registerPlaceholder(plugin, "clans.level", new PlaceholderReplacer() {
					public String onPlaceholderReplace(
							PlaceholderReplaceEvent event) {
						Player p = event.getPlayer();
						if (plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".level") == null) {
							return "";
						}
						return plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".level");
					}
				}); 
				
				PlaceholderAPI.registerPlaceholder(plugin, "clans.slots", new PlaceholderReplacer() {
					public String onPlaceholderReplace(
							PlaceholderReplaceEvent event) {
						Player p = event.getPlayer();
						if (plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".slots") == null) {
							return "";
						}
						return plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".slots");
					}
				});
				
				PlaceholderAPI.registerPlaceholder(plugin, "clans.owner", new PlaceholderReplacer() {
					public String onPlaceholderReplace(
							PlaceholderReplaceEvent event) {
						Player p = event.getPlayer();
						if (plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".owner") == null) {
							return "";
						}
						return plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".owner");
					}
				}); 
				
				PlaceholderAPI.registerPlaceholder(plugin, "clans.kills", new PlaceholderReplacer() {
					public String onPlaceholderReplace(
							PlaceholderReplaceEvent event) {
						Player p = event.getPlayer();
						if (plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".kills") == null) {
							return "";
						}
						return plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".kills");
					}
				}); 
				
				PlaceholderAPI.registerPlaceholder(plugin, "clans.deaths", new PlaceholderReplacer() {
					public String onPlaceholderReplace(
							PlaceholderReplaceEvent event) {
						Player p = event.getPlayer();
						if (plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".deaths") == null) {
							return "";
						}
						return plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".deaths");
					}
				}); 
				
				PlaceholderAPI.registerPlaceholder(plugin, "clans.kdr", new PlaceholderReplacer() {
					public String onPlaceholderReplace(
							PlaceholderReplaceEvent event) {
						Player p = event.getPlayer();
						if (plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".kdr") == null) {
							return "";
						}
						return plugin.getConfig().getString("clan." + plugin.pclans.get(p.getName().toString()) + ".kdr");
					}
				}); 
			}			  
		}.runTaskTimer(plugin, 0L, 5 * 20L);
	}
}
