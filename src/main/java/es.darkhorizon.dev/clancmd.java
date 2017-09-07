package es.darkhorizon.dev;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class clancmd implements CommandExecutor{
	private final main plugin;
	public clancmd(final main plugin) {
		this.plugin = plugin;				
	
		new BukkitRunnable() {			
			public void run() {	
				for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {						
					if (plugin.pclans.containsKey(p.getName().toString())) {
						String clan = plugin.pclans.get(p.getName().toString());	
						if (!(plugin.getConfig().contains("clan." + clan))) {
							plugin.getConfig().set("mod." + p.getName(), null);
							plugin.saveConfig();
							plugin.reloadConfig();
						}					
					}
				} 
			}			  
		}.runTaskTimer(plugin, 0L, 5 * 20L);		
	}	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		File s = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "settings.yml");	
		FileConfiguration sfile = YamlConfiguration.loadConfiguration(s);
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		String prefix = lfile.getString("global.prefix").replaceAll("&", "§");

		if (cmd.getName().equalsIgnoreCase("clan")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefix + lfile.getString("global.only_players").replaceAll("&", "§"));
			} else {
				Player p = (Player) sender;
				if (p.hasPermission("clan.user.use")) {																	
				if(args.length == 0) {
					p.sendMessage("");
					p.sendMessage(lfile.getString("help.commands").replaceAll("&", "§"));
					p.sendMessage("");
					p.sendMessage(lfile.getString("help.help_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.create_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.join_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.invite_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.kick_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.info_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.list_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.chat_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.mode_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.promote_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.demote_cmd").replaceAll("&", "§"));
					p.sendMessage(lfile.getString("help.pvp_cmd").replaceAll("&", "§"));
					p.sendMessage("");			
					p.sendMessage("§8§l»§r §fPlugin by: §9@DarkDhz");	
					p.sendMessage("");	
				} else {
					if(args[0].equalsIgnoreCase("help")) { p.performCommand("clan");	
					
					} else if(args[0].equalsIgnoreCase("create")) { 
						if (p.hasPermission("clan.user.create")) {						
							if (args.length <= 1) { 
								p.sendMessage(prefix + lfile.getString("create.usage").replaceAll("&", "§"));		
							} else if (args.length > 2) { 
								p.sendMessage(prefix + lfile.getString("create.usage").replaceAll("&", "§"));				
							} else {
								String clan = args[1].toUpperCase();
								if (plugin.pclans.get(p.getName().toString()) == null) {							
									if (!(plugin.getConfig().contains("clan." + clan))) {
										if (args[1].length() >= sfile.getInt("clan_min_lenght") ) {
										if (args[1].length() <= sfile.getInt("clan_max_lenght") ) {													
											if (plugin.getEconomy().getBalance(p.getName()) >= sfile.getInt("cost")) {
												plugin.getEconomy().withdrawPlayer(p.getName(), sfile.getInt("cost"));
												plugin.getConfig().set("clan." + clan + ".owner", p.getName());
												plugin.getConfig().set("clan." + clan + ".members", 1);
												plugin.getConfig().set("clan." + clan + ".slots", sfile.getInt("slots"));
												plugin.getConfig().set("clan." + clan + ".level", 1);
												plugin.getConfig().set("clan." + clan + ".exp", 0);
												plugin.getConfig().set("clan." + clan + ".mode", true);
												plugin.getConfig().set("clan." + clan + ".pvp", false);
												plugin.getConfig().set("clan." + clan + ".kills", 0);
												plugin.getConfig().set("clan." + clan + ".deaths", 0);
												plugin.getConfig().set("clan." + clan + ".kdr", 0);
												plugin.saveConfig();
												plugin.reloadConfig();	
												plugin.Clans.add(clan);
												ArrayList<String> clan_members = new ArrayList<String>();
												clan_members.add(p.getName().toString());
												plugin.getConfig().set("clan." + clan + ".list", clan_members);
												plugin.getConfig().set("clanlist", plugin.Clans);
												Bukkit.broadcastMessage(prefix + lfile.getString("create.created").replaceAll("&", "§").replaceAll("%player%", p.getName()).replaceAll("%clan%", clan));
												plugin.saveConfig();
												plugin.reloadConfig();	
												clan_members.clear();										
												plugin.pclans.put(p.getName().toString(), clan);
											} else { p.sendMessage(prefix + lfile.getString("create.no_money").replaceAll("&", "§")); }
										} else { p.sendMessage(prefix + lfile.getString("create.long").replaceAll("&", "§")); }
										} else { p.sendMessage(prefix + lfile.getString("create.short").replaceAll("&", "§")); }
									} else { p.sendMessage(prefix + lfile.getString("create.exist").replaceAll("&", "§")); }	
								} else { p.sendMessage(prefix + lfile.getString("create.has_clan").replaceAll("&", "§")); }																			
							}
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}
							
					} else if(args[0].equalsIgnoreCase("join")) { 
						if (p.hasPermission("clan.user.join")) {
							if(args.length == 1) { p.sendMessage(prefix + lfile.getString("join.usage").replaceAll("&", "§"));						
							} else {
								if (plugin.pclans.get(p.getName().toString()) == null) {
									String clan = args[1].toUpperCase();
									if ((plugin.getConfig().contains("clan." + clan))) {
										if ((plugin.getConfig().getInt("clan." + clan + ".members")) < (plugin.getConfig().getInt("clan." + clan + ".slots"))) {
											int n = plugin.getConfig().getInt("clan." + clan + ".members");
											if ((plugin.getConfig().get("clan." + clan + ".mode").equals(false))) {
												p.sendMessage(prefix + lfile.getString("join.joined").replaceAll("&", "§").replaceAll("%clan%", clan));																				
												plugin.getConfig().set("clan." + clan + ".members", n+1);
												ArrayList<String> clan_members = new ArrayList<String>();
												for (String member : plugin.getConfig().getStringList("clan." + clan + ".list")) {
													clan_members.add(member.toString());
												}
												clan_members.add(p.getName().toString());
												plugin.getConfig().set("clan." + clan + ".list", clan_members);										
												plugin.saveConfig();
												plugin.reloadConfig();
												plugin.pclans.put(p.getName().toString(), clan);
											} else { 
												if (!(plugin.Invites.isEmpty())) {
												if (plugin.Invites.get(p.getName().toString()).contains(clan) ) {
													plugin.getConfig().getStringList("invites").remove(p.getName().toString());
													plugin.Invites.remove(p.getName().toString());
													p.sendMessage(prefix + lfile.getString("join.joined").replaceAll("&", "§").replaceAll("%clan%", clan));
													plugin.getConfig().set("clan." + clan + ".members", n+1);
													ArrayList<String> clan_members = new ArrayList<String>();
													for (String member : plugin.getConfig().getStringList("clan." + clan + ".list")) {
														clan_members.add(member);
													}
													clan_members.add(p.getName().toString());
													plugin.getConfig().set("clan." + clan + ".list", clan_members);
													plugin.saveConfig();
													plugin.reloadConfig();		
													plugin.pclans.put(p.getName().toString(), clan);
												} else { p.sendMessage(prefix + lfile.getString("join.invitation").replaceAll("&", "§")); }	
												} else { p.sendMessage(prefix + lfile.getString("join.invitation").replaceAll("&", "§")); }
											}																																																																											
										} else { p.sendMessage(prefix + lfile.getString("join.full").replaceAll("&", "§")); }																								
									} else { p.sendMessage(prefix + lfile.getString("global.no_exist").replaceAll("&", "§")); }
								} else { p.sendMessage(prefix + lfile.getString("join.has_clan").replaceAll("&", "§")); }							
							}	
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}
					
					} else if(args[0].equalsIgnoreCase("list")) { 
						if (p.hasPermission("clan.user.list")) {
							Inventory inventory = this.plugin.getInvMenus().openClanListInventory(p, 1);
							p.openInventory(inventory); 
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}	
						
						
					} else if (args[0].equalsIgnoreCase("invite")) {
						if (p.hasPermission("clan.user.invite")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								String clan = plugin.pclans.get(p.getName().toString());		
								if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(p.getName())) {		
									if(args.length == 1) { p.sendMessage(prefix + lfile.getString("invite.usage").replaceAll("&", "§"));										
									} else {
										if (Bukkit.getServer().getPlayer(args[1]) instanceof Player) {
											Player target = Bukkit.getServer().getPlayer(args[1]);
											if (plugin.pclans.get(target.getName().toString()) == null) {																														
												p.sendMessage(prefix + lfile.getString("invite.invited").replaceAll("&", "§").replaceAll("%player%", args[1]));																							
												target.sendMessage(prefix + lfile.getString("invite.been_invited").replaceAll("&", "§").replaceAll("%clan%", clan));
												plugin.Invites.put(target.getName().toString(), clan);																											
											} else { p.sendMessage(prefix + lfile.getString("invite.has_clan").replaceAll("&", "§")); }	
										} else { p.sendMessage(prefix + lfile.getString("invite.offline").replaceAll("&", "§")); }
									}
								} else if ((plugin.getConfig().getString("mod" + p.getName())).equals(true)) { 	
									if(args.length == 1) { p.sendMessage(prefix + lfile.getString("invite.usage").replaceAll("&", "§"));										
									} else {
										if (Bukkit.getServer().getPlayer(args[1]) instanceof Player) {
											if (plugin.pclans.get(args[1].toString()) == null) {
												p.sendMessage(prefix + lfile.getString("invite.invited").replaceAll("&", "§").replaceAll("%player%", args[1]));
												Player target = Bukkit.getServer().getPlayer(args[1]);
												target.sendMessage(prefix + lfile.getString("invite.been_invited").replaceAll("&", "§").replaceAll("%clan%", clan));
												plugin.Invites.put(target.getName().toString(), clan);							
											} else { p.sendMessage(prefix + lfile.getString("invite.has_clan").replaceAll("&", "§")); }
										} else { p.sendMessage(prefix + lfile.getString("invite.offline").replaceAll("&", "§")); }
									}
								} else { p.sendMessage(prefix + lfile.getString("global.rank").replaceAll("&", "§")); }
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }	
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}		
						
					} else if (args[0].equalsIgnoreCase("info")) {
						if (p.hasPermission("clan.user.info")) {
							if(args.length == 1) { p.sendMessage(prefix + lfile.getString("info.usage").replaceAll("&", "§")); 
							} else {
								Object clan = args[1].toUpperCase();
								if (plugin.getConfig().contains("clan." + clan)) {
									int lvl = plugin.getConfig().getInt("clan." + clan + ".level");
									Inventory inventory = this.plugin.getInvMenus().openClanInventory(p, String.valueOf(clan), lvl);
									p.openInventory(inventory); 												
								} else { p.sendMessage(prefix + lfile.getString("global.no_exist").replaceAll("&", "§")); }
							}
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}												
					} else if (args[0].equalsIgnoreCase("pvp")) {
						if (p.hasPermission("clan.user.pvp")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								String clan = plugin.pclans.get(p.getName().toString());						
								if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(p.getName())) {	
									if ((plugin.getConfig().get("clan." + clan + ".pvp")).equals(true)) {
										p.sendMessage(prefix + lfile.getString("pvp.disabled").replaceAll("&", "§"));
										plugin.getConfig().set("clan." + clan + ".pvp", false);
										plugin.saveConfig();
										plugin.reloadConfig();
									} else {
										p.sendMessage(prefix + lfile.getString("pvp.enabled").replaceAll("&", "§"));
										plugin.getConfig().set("clan." + clan + ".pvp", true);
										plugin.saveConfig();
										plugin.reloadConfig();	
									}												
								} else { p.sendMessage(prefix + lfile.getString("global.owner").replaceAll("&", "§")); }
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}		
						
					} else if (args[0].equalsIgnoreCase("leave")) {
						if (p.hasPermission("clan.user.leave")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								String clan = plugin.pclans.get(p.getName().toString());		
								if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(p.getName())) {
									p.sendMessage(prefix + lfile.getString("leave.left").replaceAll("&", "§"));
									for ( String player : plugin.getConfig().getStringList("clan." + clan + ".list")) {
										plugin.Chat.remove(player);
										plugin.pclans.remove(player);
									}	
									plugin.getConfig().set("clan." + clan, null);								
									plugin.Clans.remove(clan.toString());
									plugin.getConfig().set("clanlist", plugin.Clans);
									plugin.saveConfig();
									plugin.reloadConfig();							
								} else {
									p.sendMessage(prefix + lfile.getString("leave.left").replaceAll("&", "§"));
									plugin.getConfig().set("mod." + p.getName(), null);
									plugin.getConfig().set("clan." + clan + ".members", plugin.getConfig().getInt("clan." + clan + ".members")-1);	
									plugin.getConfig().getStringList("clan." + clan + ".list").remove(p.getName().toString());
									ArrayList<String> clan_members = new ArrayList<String>();
									for (String member : plugin.getConfig().getStringList("clan." + clan + ".list")) {
										clan_members.add(member.toString());
									}
									clan_members.remove(p.getName().toString());
									plugin.getConfig().set("clan." + clan + ".list", clan_members);	
									plugin.Chat.remove(p.getName().toString());
									plugin.pclans.remove(p.getName().toString());
									plugin.saveConfig();
									plugin.reloadConfig();
								}
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}		
						
					} else if (args[0].equalsIgnoreCase("kick")) {
						if (p.hasPermission("clan.user.kick")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								String clan = plugin.pclans.get(p.getName().toString());		
								if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(p.getName())) {																
									if(args.length == 1) { p.sendMessage(prefix + lfile.getString("kick.usage").replaceAll("&", "§"));										
									} else {
										Player target = Bukkit.getServer().getPlayer(args[1]);
										if (plugin.pclans.get(target.getName().toString()).equals(clan)) {	
											if (!(plugin.getConfig().get("clan." + clan + ".owner").equals(target))) {
												p.sendMessage(prefix + lfile.getString("kick.kicked").replaceAll("&", "§").replaceAll("%player%", args[1]));																							
												plugin.getConfig().set("mod." + target.getName(), null);
												plugin.getConfig().set("clan." + clan + ".members", plugin.getConfig().getInt("clan." + clan + ".members")-1);	
												plugin.Chat.remove(target.getName().toString());
												plugin.pclans.remove(target.getName().toString());
												plugin.saveConfig();
												plugin.reloadConfig();
											} else { p.sendMessage(prefix + lfile.getString("kick.yourself").replaceAll("&", "§")); }																																							
										} else { p.sendMessage(prefix + lfile.getString("kick.no_member").replaceAll("&", "§")); }	
									}							 
								} else { p.sendMessage(prefix + lfile.getString("global.owner").replaceAll("&", "§")); }
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}	
						
					} else if (args[0].equalsIgnoreCase("promote")) {	
						if (p.hasPermission("clan.user.promote")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								String clan = plugin.pclans.get(p.getName().toString());		
								if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(p.getName())) {	
									if (args.length == 1) { p.sendMessage(prefix + lfile.getString("promote.usage").replaceAll("&", "§"));										
									} else {
										Player target = Bukkit.getServer().getPlayer(args[1]);
										if (plugin.pclans.get(target.getName().toString()).equals(clan)) {
											if ((plugin.getConfig().get("mod." + target.getName()) != null)) {
												p.sendMessage(prefix + lfile.getString("promote.mod").replaceAll("&", "§"));											
											} else {
												plugin.getConfig().set("mod." + target.getName(), true);
												p.sendMessage(prefix + lfile.getString("promote.promoted").replaceAll("&", "§"));										
												plugin.saveConfig();
												plugin.reloadConfig();
											}
											
										} else { p.sendMessage(prefix + lfile.getString("promote.no_member").replaceAll("&", "§")); } 
									}								
								} else { p.sendMessage(prefix + lfile.getString("global.owner").replaceAll("&", "§")); }
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}	
						
					} else if (args[0].equalsIgnoreCase("demote")) {
						if (p.hasPermission("clan.user.demote")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								String clan = plugin.pclans.get(p.getName().toString());;
								if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(p.getName())) {	
									if(args.length == 1) { p.sendMessage(prefix + lfile.getString("demote.usage").replaceAll("&", "§"));										
									} else {
										Player target = Bukkit.getServer().getPlayer(args[1]);
										if (plugin.pclans.get(target.getName().toString()).equals(clan)) {
											if (!(plugin.getConfig().get("mod." + target.getName()) != null)) {
												p.sendMessage(prefix + lfile.getString("demote.no_mod").replaceAll("&", "§"));
											} else {
												plugin.getConfig().set("mod." + target.getName(), null);
												p.sendMessage(prefix + lfile.getString("demote.demoted").replaceAll("&", "§"));
												plugin.saveConfig();
												plugin.reloadConfig();
											}									
										} else { p.sendMessage(prefix + lfile.getString("demote.no_member").replaceAll("&", "§")); }
									}								
								} else { p.sendMessage(prefix + lfile.getString("global.owner").replaceAll("&", "§")); }
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}		
						
					} else if (args[0].equalsIgnoreCase("mode")) {	
						if (p.hasPermission("clan.user.mode")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								String clan = plugin.pclans.get(p.getName().toString());
								if ((plugin.getConfig().get("clan." + clan + ".owner")).equals(p.getName())) {
									if ((plugin.getConfig().get("clan." + clan + ".mode")).equals(false)) {
										p.sendMessage(prefix + lfile.getString("mode.inv").replaceAll("&", "§")); 
										plugin.getConfig().set("clan." + clan + ".mode", true);
										plugin.saveConfig();
										plugin.reloadConfig();
									} else {
										p.sendMessage(prefix + lfile.getString("mode.free").replaceAll("&", "§"));
										plugin.getConfig().set("clan." + clan + ".mode", false);
										plugin.saveConfig();
										plugin.reloadConfig();
									}
								} else { p.sendMessage(prefix + lfile.getString("global.owner").replaceAll("&", "§")); }
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}		
													
					} else if (args[0].equalsIgnoreCase("chat")) {
						if (p.hasPermission("clan.user.chat")) {
							if (plugin.pclans.get(p.getName().toString()) != null) {
								if (plugin.Chat.contains(p.getName().toString())) {
									p.sendMessage(prefix + lfile.getString("chat.disabled").replaceAll("&", "§"));
									plugin.Chat.remove(p.getName().toString());
									plugin.saveConfig();
									plugin.reloadConfig();
								} else {
									p.sendMessage(prefix + lfile.getString("chat.enabled").replaceAll("&", "§")); 
									plugin.Chat.add(p.getName().toString());
									plugin.saveConfig();
									plugin.reloadConfig();						
								}
							} else { p.sendMessage(prefix + lfile.getString("global.no_clan").replaceAll("&", "§")); }
						} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}	
								
					} else { p.performCommand("clan"); } 
				}
				} else {p.sendMessage(prefix + lfile.getString("global.perms").replaceAll("&", "§"));}
			}		
		}			
		return true;
	}			
}
