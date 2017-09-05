package es.darkhorizon.dev;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class aclancmd implements CommandExecutor{
	private final main plugin;
	public aclancmd(main plugin) { this.plugin = plugin; }
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		File s = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "settings.yml");	
		FileConfiguration sfile = YamlConfiguration.loadConfiguration(s);
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		String prefix = lfile.getString("global.prefix").replaceAll("&", "§");
		if (cmd.getName().equalsIgnoreCase("aclan")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefix + lfile.getString("global.only_players").replaceAll("&", "§"));
				plugin.getConfig();
			} else {
				Player p = (Player) sender;
				if (args.length == 0) {
					p.sendMessage("");
					p.sendMessage(lfile.getString("ahelp.commands").replaceAll("&", "§"));
					p.sendMessage("");
					p.sendMessage(lfile.getString("ahelp.help_cmd").replaceAll("&", "§")); 
					p.sendMessage(lfile.getString("ahelp.slots_cmd").replaceAll("&", "§")); 
					p.sendMessage(lfile.getString("ahelp.level_cmd").replaceAll("&", "§")); 
					p.sendMessage(lfile.getString("ahelp.exp_cmd").replaceAll("&", "§")); 
					p.sendMessage(lfile.getString("ahelp.erase_cmd").replaceAll("&", "§")); 
					p.sendMessage(lfile.getString("ahelp.reload_cmd").replaceAll("&", "§"));
					p.sendMessage("");			
					p.sendMessage("§8§l»§r §fPlugin by: §9@DarkDhz");	
					p.sendMessage("");
				} else {
					if(args[0].equalsIgnoreCase("help")) {
						p.performCommand("aclan");
						
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (p.hasPermission("clan.admin.reload")) {
							p.sendMessage(prefix + "§6Reloading...");
							plugin.saveConfig();
							plugin.reloadConfig();
						} else { p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§")); } 
						
					} else if (args[0].equalsIgnoreCase("plstats")) {
						if (p.hasPermission("clan.admin.plstats")) {
							p.sendMessage(prefix + "§7Num. of Clans: §b" + plugin.getConfig().getStringList("clanlist").size());				
							p.sendMessage(prefix + "§7Running at: §b" + Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort());
						} else { p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§")); } 
					
					} else if (args[0].equalsIgnoreCase("slots")) {
						if (p.hasPermission("clan.admin.slots")) {
							String clan = args[1];
							if (plugin.getConfig().getStringList("clanlist").contains(clan)) {
								if (args[2].equalsIgnoreCase("default")) {
									p.sendMessage(prefix + lfile.getString("admin.slots.default").replaceAll("&", "§").replaceAll("%clan%", clan)); 							
									plugin.getConfig().set("clan." + clan + ".slots", (sfile.getInt("slots"))+(plugin.getConfig().getInt("clan." + clan + ".level"))-1);
									plugin.saveConfig();
									plugin.reloadConfig();
								} else {
									try	{ 
										int slots = Integer.parseInt(args[3]);
										if (args[2].equalsIgnoreCase("set")) {
											if (slots >= sfile.getInt("slots")) {
												p.sendMessage(prefix + lfile.getString("admin.slots.set").replaceAll("&", "§").replaceAll("%clan%", clan).replaceAll("%slots%", args[3])); 
												plugin.getConfig().set("clan." + clan + ".slots", slots);
												plugin.saveConfig();
												plugin.reloadConfig();
											} else {p.sendMessage(prefix + lfile.getString("admin.slots.less_default").replaceAll("&", "§"));}																
										} else if (args[2].equalsIgnoreCase("add")) {
											if (slots > 0) {												
												p.sendMessage(prefix + lfile.getString("admin.slots.add").replaceAll("&", "§").replaceAll("%clan%", clan).replaceAll("%slots%", args[3])); 
												plugin.getConfig().set("clan." + clan + ".slots", plugin.getConfig().getInt("clan." + clan + ".slots")+slots);
												plugin.saveConfig();
												plugin.reloadConfig();
											} else {p.sendMessage(prefix + lfile.getString("global.negative").replaceAll("&", "§"));} 
										} else if (args[2].equalsIgnoreCase("remove")) {
											if (slots > 0) {
												if ((plugin.getConfig().getInt("clan." + clan + ".slots")-slots) >= sfile.getInt("slots")) {
													p.sendMessage(prefix + lfile.getString("admin.slots.remove").replaceAll("&", "§").replaceAll("%clan%", clan).replaceAll("%slots%", args[3])); 
													plugin.getConfig().set("clan." + clan + ".slots", plugin.getConfig().getInt("clan." + clan + ".slots")-slots);
													plugin.saveConfig();
													plugin.reloadConfig();
												} else {p.sendMessage(prefix + lfile.getString("admin.slots.less_default").replaceAll("&", "§"));}			
											} else {p.sendMessage(prefix + lfile.getString("global.negative").replaceAll("&", "§"));}
										} else {p.sendMessage(prefix + lfile.getString("admin.slots.usage").replaceAll("&", "§"));}
									} catch(NumberFormatException er) {p.sendMessage(prefix + lfile.getString("admin.slots.int").replaceAll("&", "§"));}
								}																					
							} else {p.sendMessage(prefix + lfile.getString("admin.slots.no_exist").replaceAll("&", "§"));}
						} else { p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§")); } 
						
					} else if (args[0].equalsIgnoreCase("level")) {
						if (p.hasPermission("clan.admin.level")) {
							p.sendMessage(prefix + "§cNearly!");				
						} else { p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§")); } 	
						
					} else if (args[0].equalsIgnoreCase("exp")) {
						if (p.hasPermission("clan.admin.exp")) {
							p.sendMessage(prefix + "§cNearly!");				
						} else { p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§")); } 	
					
					} else if (args[0].equalsIgnoreCase("erase")) {					
						if (p.hasPermission("clan.admin.erase")) {
							if (args.length <= 1) { 
								p.sendMessage(prefix + lfile.getString("admin.erase.usage").replaceAll("&", "§"));		
							} else if (args.length > 2) { 
								p.sendMessage(prefix + lfile.getString("admin.erase.usage").replaceAll("&", "§"));
							} else {
								String clan = args[1];
								if (plugin.getConfig().getStringList("clanlist").contains(clan)) {		
									p.sendMessage(prefix + lfile.getString("admin.erase.erased").replaceAll("&", "§").replaceAll("%clan%", clan)); 
									for ( String player : plugin.getConfig().getStringList("clan." + clan + ".list")) {
										plugin.Chat.remove(player);
										plugin.pclans.remove(player);
									}							
									plugin.getConfig().set("clan." + clan, null);								
									plugin.Clans.remove(clan.toString());							
									plugin.getConfig().set("clanlist", plugin.Clans);
									plugin.saveConfig();
									plugin.reloadConfig();								 	
								} else { p.sendMessage(prefix + lfile.getString("admin.erase.no_exist").replaceAll("&", "§")); }	
							}
						} else { p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§")); } 												
					} else { p.performCommand("aclan help"); }
				}
			}
		}
		return true;
	}
}
