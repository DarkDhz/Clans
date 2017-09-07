package es.darkhorizon.dev;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class event implements Listener {
	private final main plugin;
	public event(main plugin) { this.plugin = plugin; }		
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		String prefix = lfile.getString("global.prefix").replaceAll("&", "§");
		Entity entity = event.getEntity();
		Entity attacker = event.getDamager();
		String clan = plugin.pclans.get(entity.getName().toString());
		if (entity instanceof Player && attacker instanceof Player) {
			if (plugin.pclans.get(entity.getName().toString()) != null) {	
			if (plugin.pclans.get(attacker.getName().toString()) != null) {						
				if (clan.contains(plugin.pclans.get(attacker.getName().toString()))) {
					if ((plugin.getConfig().get("clan." + clan + ".pvp" )).equals(false)) {
						event.setCancelled(true);
						attacker.sendMessage(prefix + lfile.getString("event.pvp.same_clan").replaceAll("&", "§").replaceAll("%player%", entity.getName()));
					}				
				}		
			} 
		}
		}
	}
	@EventHandler
	public void playerDeath(PlayerDeathEvent event) {
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		String prefix = lfile.getString("global.prefix").replaceAll("&", "§");
		if(event.getEntity() instanceof Player) {
			Player victim = event.getEntity();
			Player killer = victim.getKiller();
			if (plugin.pclans.get(victim.getName().toString()) != null) {			
				String clan_victim = plugin.pclans.get(victim.getPlayer().getName().toString());			
				int ds = plugin.getConfig().getInt("clan." + clan_victim + ".deaths");
				int kl = plugin.getConfig().getInt("clan." + clan_victim + ".kills");
				plugin.getConfig().set("clan." + clan_victim + ".deaths", ds+1 );
				plugin.getConfig().set("clan." + clan_victim + ".kdr", kl/ds );
			}
			if (plugin.pclans.get(killer.getName().toString()) != null) {
				String clan_killer = plugin.pclans.get(killer.getPlayer().getName().toString());	
				int ds = plugin.getConfig().getInt("clan." + clan_killer + ".deaths");
				int kl = plugin.getConfig().getInt("clan." + clan_killer + ".kills");
				plugin.getConfig().set("clan." + clan_killer + ".kills", kl+1 );
				int exp = plugin.getConfig().getInt("clan." + clan_killer + ".exp");
				int level = plugin.getConfig().getInt("clan." + clan_killer + ".level");
				plugin.getConfig().set("clan." + clan_killer + ".exp", exp+1 );
				plugin.getConfig().set("clan." + clan_killer + ".kdr", kl/ds );
				if (exp >= 50*level) {
					plugin.getConfig().set("clan." + clan_killer + ".level", level+1 );
					plugin.getConfig().set("clan." + clan_killer + ".exp", 0 );
					int sl = plugin.getConfig().getInt("clan." + clan_killer + ".slots");
					plugin.getConfig().set("clan." + clan_killer + ".slots", sl+1 );	
					String lvl = plugin.getConfig().getString("clan." + clan_killer + ".level");
					Bukkit.broadcastMessage(prefix + lfile.getString("event.pvp.levelup").replaceAll("&", "§").replaceAll("%clan%", clan_killer).replaceAll("%level%", lvl));
				}
			}			
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		File g = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "gui.yml");	
		FileConfiguration gfile = YamlConfiguration.loadConfiguration(g);
		Player player = (Player) event.getWhoClicked(); 
		ItemStack clicked = event.getCurrentItem(); 
		Inventory inventory = event.getInventory();
		if(event.getCurrentItem() == null){
			return;
		}
		if (inventory.getName().contains(gfile.getString("info.title").replaceAll("&", "§") + " §f§l»§r ")) {	
			event.setCancelled(true);
			if (clicked.getType() == Material.SKULL_ITEM) {  
				String clanArgs[] = inventory.getName().split("»§r §c§l");
				Inventory inventory2 = this.plugin.getInvMenus().openMemberInventory(player, String.valueOf(clanArgs[1]));
				player.openInventory(inventory2); 
			} 						
		} else if (inventory.getName().contains(gfile.getString("members.title").replaceAll("&", "§"))) {
			event.setCancelled(true);
			if (clicked.getType() == Material.ARROW) {
				String clanArgs[] = inventory.getName().split("»§r §c§l");
				int lvl = plugin.getConfig().getInt("clan." + clanArgs[1] + ".level");
				player.openInventory(this.plugin.getInvMenus().openClanInventory(player, clanArgs[1].replaceAll(" ", ""), lvl));
			}		
		} else if (inventory.getName().contains(gfile.getString("list.title").replaceAll("&", "§"))) {
			event.setCancelled(true);
			if (clicked.getType() == Material.SKULL_ITEM) {  
				String clanArgs[] = clicked.getItemMeta().getDisplayName().split("§L");
				int lvl = plugin.getConfig().getInt("clan." + String.valueOf(clanArgs[1]) + ".lvl");
				player.openInventory(this.plugin.getInvMenus().openClanInventory(player, String.valueOf(clanArgs[1]), lvl));
			} else if (clicked.getType() == Material.ARROW) {
				if (clicked.getItemMeta().getDisplayName().equals(gfile.getString("list.next_pg.name").replaceAll("&", "§"))) {
					String clanArgs[] = inventory.getName().split("PG.");
					int pg = Integer.valueOf(clanArgs[1]);
					player.openInventory(this.plugin.getInvMenus().openClanListInventory(player, pg+1));
				} else if (clicked.getItemMeta().getDisplayName().equals(gfile.getString("list.return_pg.name").replaceAll("&", "§"))) {
					String clanArgs[] = inventory.getName().split("PG.");
					int pg = Integer.valueOf(clanArgs[1]);
					player.openInventory(this.plugin.getInvMenus().openClanListInventory(player, pg-1));
				}			
			}
		}
	}
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event){	
		File s = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "settings.yml");	
		FileConfiguration sfile = YamlConfiguration.loadConfiguration(s);
		Player p = event.getPlayer();
		if (!(plugin.Chat.contains(p.getName().toString()))) {
			if (plugin.pclans.get(p.getName().toString()) != null) {				
				String clan = plugin.pclans.get(event.getPlayer().getName().toString());				
				if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(event.getPlayer().getName())) {
					event.getPlayer().setDisplayName(sfile.getString("chat.format").replaceAll("&", "§").replaceAll("%clan%", clan).replaceAll("%rank%", sfile.getString("chat.owner_prefix").replaceAll("&", "§")) + event.getPlayer().getDisplayName());
				} else if ((plugin.getConfig().get("mod." + p.getName()) != null)) {
					event.getPlayer().setDisplayName(sfile.getString("chat.format").replaceAll("&", "§").replaceAll("%clan%", clan).replaceAll("%rank%", sfile.getString("chat.mod_prefix").replaceAll("&", "§")) + event.getPlayer().getDisplayName());
				} else { event.getPlayer().setDisplayName(sfile.getString("chat.format").replaceAll("&", "§").replaceAll("%clan%", clan).replaceAll("%rank%", "§r") + event.getPlayer().getDisplayName()); }						
			}	
		} else {
			event.setCancelled(true);
			String clan = plugin.pclans.get(event.getPlayer().getName().toString());		
			for (Player player : Bukkit.getOnlinePlayers()) { 	
				if (plugin.pclans.get(player.getName().toString()).equals(clan)) {
					player.sendMessage(sfile.getString("clan_chat.format").replaceAll("&", "§").replaceAll("%clan%", clan).replaceAll("%player%", p.getName()).replaceAll("%message%", event.getMessage()));
				}					
			}
		}			
	}	
}
