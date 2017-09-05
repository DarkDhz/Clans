package es.darkhorizon.dev;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class menus {
	private final main plugin;
	public menus(main plugin) { this.plugin = plugin; }
	public Inventory openClanInventory (final Player p, final String clan, final Integer lvl) {
		File g = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "gui.yml");	
		final FileConfiguration gfile = YamlConfiguration.loadConfiguration(g);
		final Inventory inv = Bukkit.createInventory(p, 9 * 1, gfile.getString("info.title").replaceAll("&", "§") + " §f§l»§r §c§l" + clan);
		final ArrayList<String> Lore = new ArrayList<String>();
		
		final ItemStack playerSkull = this.getHead(p);
		new BukkitRunnable() {			
			public void run() {					
				if (p.getOpenInventory().getTitle().contains(gfile.getString("info.title").replaceAll("&", "§") + " §f§l»§r ")) {				
					ItemMeta meta = playerSkull.getItemMeta();
					
					meta.setDisplayName(gfile.getString("info.members.name").replaceAll("&", "§").replaceAll("%members%", plugin.getConfig().getString("clan." + clan + ".members")).replaceAll("%slots%", plugin.getConfig().getString("clan." + clan + ".slots")));				
					Lore.clear();
					for ( String lr : gfile.getStringList("info.members.lore")) {
						Lore.add(lr.replaceAll("&", "§"));
					}	
					meta.setLore(Lore);
					playerSkull.setItemMeta(meta);	
					inv.setItem(1, playerSkull);
					inv.getItem(1).addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					
					//LVL
					ItemStack item2 = new ItemStack(Material.EXP_BOTTLE);
					ItemMeta meta2 = item2.getItemMeta(); 
					meta2.setDisplayName(gfile.getString("info.lvl.name").replaceAll("&", "§").replaceAll("%level%", plugin.getConfig().getString("clan." + clan + ".level")));
					Lore.clear();
					for ( String lr : gfile.getStringList("info.lvl.lore")) {
						Lore.add(lr.replaceAll("&", "§").replaceAll("%exp%", plugin.getConfig().getString("clan." + clan + ".exp")).replaceAll("%lvlup%", "" + 50*(plugin.getConfig().getInt("clan." + clan + ".level"))));
					}	
					meta2.setLore(Lore);
					item2.setItemMeta(meta2);	
					inv.setItem(4, item2);
					inv.getItem(4).addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					
					//PVP
					ItemStack item3 = new ItemStack(Material.DIAMOND_SWORD);
					ItemMeta meta3 = item3.getItemMeta();
					meta3.setDisplayName(gfile.getString("info.stats.name").replaceAll("&", "§"));
					Lore.clear();
					for ( String lr : gfile.getStringList("info.stats.lore")) {
						Lore.add(lr.replaceAll("&", "§").replaceAll("%kills%", plugin.getConfig().getString("clan." + clan + ".kills")).replaceAll("%deaths%", plugin.getConfig().getString("clan." + clan + ".deaths")).replaceAll("%kdr%", plugin.getConfig().getString("clan." + clan + ".kdr")));
					}	
					meta3.setLore(Lore);
					item3.setItemMeta(meta3);	
					inv.setItem(7, item3);
					inv.getItem(7).addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					
				} else { cancel(); }			
			}			  
		}.runTaskTimer(plugin, 0L, 5 * 20L);
		
		return inv; 
		
	}
	public Inventory openMemberInventory (final Player p, String clan) {
		File g = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "gui.yml");	
		final FileConfiguration gfile = YamlConfiguration.loadConfiguration(g);
		Inventory inv = Bukkit.createInventory(p, 9 * 6, gfile.getString("members.title").replaceAll("&", "§") + " §f§l»§r §c§l" + clan);

		final ArrayList<String> Lore = new ArrayList<String>();
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(gfile.getString("members.back.name").replaceAll("&", "§"));				
		Lore.clear();
		for ( String lr : gfile.getStringList("members.back.lore")) {
			Lore.add(lr.replaceAll("&", "§"));
		}	
		meta.setLore(Lore);
		item.setItemMeta(meta);	
		inv.setItem(0, item);
		
		
		for (int i = 1; i < 18; i++) {
			if (i != 4) {
				item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
				meta = item.getItemMeta();
				meta.setDisplayName(gfile.getString("members.panel.name").replaceAll("&", "§")); 
				Lore.clear();
				for ( String lr : gfile.getStringList("members.panel.lore")) {
					Lore.add(lr.replaceAll("&", "§"));
				}
				meta.setLore(Lore);
				item.setItemMeta(meta);	
				inv.setItem(i, item);
			}
		}
		final ItemStack playerSkull = this.getHead(p);
		meta = playerSkull.getItemMeta();
		final String owner = plugin.getConfig().getString("clan." + clan + ".owner");
		meta.setDisplayName(gfile.getString("members.member.name").replaceAll("&", "§").replaceAll("%member%", owner));
		Lore.clear();
		String status = Bukkit.getServer().getOfflinePlayer(owner).isOnline() ? gfile.getString("members.status.online").replaceAll("&", "§"):gfile.getString("members.status.offline").replaceAll("&", "§"); 
		for ( String lr : gfile.getStringList("members.member.lore")) {
			Lore.add(lr.replaceAll("&", "§").replaceAll("%status%", status));
		}		
		meta.setLore(Lore);
		playerSkull.setItemMeta(meta);	
		inv.setItem(4, playerSkull);
		inv.getItem(4).addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		final ItemMeta skullMeta = playerSkull.getItemMeta();		
		new BukkitRunnable() {			
			public void run() {	
				if ((p.getOpenInventory().getTitle()).contains(gfile.getString("members.title").replaceAll("&", "§"))) {
					skullMeta.setDisplayName(gfile.getString("members.member.name").replaceAll("&", "§").replaceAll("%member%", owner));
					Lore.clear();
					String status = Bukkit.getServer().getOfflinePlayer(owner).isOnline() ? gfile.getString("members.status.online").replaceAll("&", "§"):gfile.getString("members.status.offline").replaceAll("&", "§"); 
					for ( String lr : gfile.getStringList("members.member.lore")) {
						Lore.add(lr.replaceAll("&", "§").replaceAll("%status%", status));
					}
					skullMeta.setLore(Lore);
					playerSkull.setItemMeta(skullMeta);
					p.getOpenInventory().setItem(4, playerSkull);
				} else { cancel(); }			
			}			  
		}.runTaskTimer(plugin, 0L, 5 * 20L);
		return inv; 		
	}	
	public Inventory openClanListInventory (Player p, int pag) {
		File g = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "gui.yml");	
		FileConfiguration gfile = YamlConfiguration.loadConfiguration(g);
		File s = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "settings.yml");	
		FileConfiguration sfile = YamlConfiguration.loadConfiguration(s);
		Inventory inv = Bukkit.createInventory(p, 9 * 6, gfile.getString("list.title").replaceAll("&", "§") + " PG." + pag);
		ArrayList<String> clans = new ArrayList<String>();
		for (String clan : plugin.getConfig().getStringList("clanlist")) {
			clans.add(clan.toString());
		}
		ArrayList<String> Lore = new ArrayList<String>();
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(gfile.getString("list.next_pg.name").replaceAll("&", "§"));					
		Lore.clear();
		for ( String lr : gfile.getStringList("list.next_pg.lore")) {
			Lore.add(lr.replaceAll("&", "§"));
		}	
		meta.setLore(Lore);
		item.setItemMeta(meta);	
		inv.setItem(53, item);
		
		if (pag >= 2) {
			item = new ItemStack(Material.ARROW);
			meta = item.getItemMeta();
			meta.setDisplayName(gfile.getString("list.return_pg.name").replaceAll("&", "§"));					
			Lore.clear();
			for ( String lr : gfile.getStringList("list.return_pg.lore")) {
				Lore.add(lr.replaceAll("&", "§"));
			}	
			meta.setLore(Lore);
			item.setItemMeta(meta);	
			inv.setItem(45, item);
		}
			
		for (int i = 0; i < 45; i++) {
			int size = clans.size();
			if (i+(45*(pag-1)) < size) {	
				item = new ItemStack((Material.SKULL_ITEM), 1, (short) 3);
				meta = item.getItemMeta();
				meta.setDisplayName("§a§L" + clans.get(i+(45*(pag-1))));					
				Lore.clear();
				for ( String lr : gfile.getStringList("list.clan.lore")) {
					Lore.add(lr.replaceAll("&", "§"));
				}
				meta.setLore(Lore);
				item.setItemMeta(meta);	
				inv.setItem(i, item);
			} else {
				item = new ItemStack((Material.STAINED_GLASS_PANE), 1, (short) 14);
				meta = item.getItemMeta();
				meta.setDisplayName(gfile.getString("list.none.name").replaceAll("&", "§"));					
				Lore.clear();
				for ( String lr : gfile.getStringList("list.none.lore")) {
					Lore.add(lr.replaceAll("&", "§").replaceAll("%cost%", sfile.getString("cost")));
				}							
				meta.setLore(Lore);
				item.setItemMeta(meta);	
				inv.setItem(i, item);
			}
		}
		return inv; 		
	}	
	public ItemStack getHead(Player p) {
		ItemStack item = new ItemStack((Material.SKULL_ITEM), 1, (short) 3);
		SkullMeta sm = (SkullMeta) item.getItemMeta();
        sm.setOwner(p.getName());	
		return item;
	}
	
	
}
