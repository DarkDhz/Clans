package es.darkhorizon.dev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;

import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin implements Listener{
    private static Economy economy = null;	
	private static main plugin;
	private menus invmenu;
	ArrayList<String> Clans = new ArrayList<String>();		
	ArrayList<String> Chat = new ArrayList<String>();
	HashMap<String, String> pclans = new HashMap<String, String>();
	HashMap<String, String> Invites = new HashMap<String, String>();
	String plversion = "2.0.1";
	public static main getPlugin() {return plugin;}
	@Override
	public void onEnable() {
		plugin = this;
		this.Clans.clear();		
		for (String clan : this.getConfig().getStringList("clanlist")) {this.Clans.add(clan);}		
		for (String clan : this.getConfig().getStringList("clanlist")) {for (String p : this.getConfig().getStringList("clan." +  clan + ".list")) {this.pclans.put(p.toString(), clan);}}
		// Tiene Clan >> (plugin.pclans.get(entity.getName().toString()) != null)
		// No tiene Clan >> (plugin.pclans.get(entity.getName().toString()) == null)
		// Obtener Clan >> plugin.pclans.get(p.getName().toString());
		setupEconomy();		
		Bukkit.getPluginManager().registerEvents(new event(this), this);
		Bukkit.getPluginCommand("clan").setExecutor(new clancmd(this));
		Bukkit.getPluginCommand("aclan").setExecutor(new aclancmd(this));	
		Bukkit.getPluginManager().registerEvents(this, this);
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();	
		getConfig().set("version", plversion);
		saveConfig();
		reloadConfig();	
		
		//UPDATER
		
		try {
			URL url = new URL("http://149.56.99.114/clans/version.yml");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
			File file = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "update.yml");
			if (!file.exists()) {file.createNewFile();}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = br.readLine()) != null) {bw.write(inputLine);}
			bw.close();
			br.close();
		} catch (MalformedURLException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		
		try {
			URL url = new URL("http://149.56.99.114/clans/link.yml");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
			File file = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "link.yml");
			if (!file.exists()) {file.createNewFile();}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = br.readLine()) != null) {bw.write(inputLine);}
			bw.close();
			br.close();
		} catch (MalformedURLException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}	
		
		File u = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "update.yml");	
		FileConfiguration ufile = YamlConfiguration.loadConfiguration(u);
		
		File lk = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "link.yml");	
		FileConfiguration lkfile = YamlConfiguration.loadConfiguration(u);
		
		if (ufile.getString("version").equalsIgnoreCase(plversion)) {	
			console.sendMessage("§8§l§m===========================================================================");
			console.sendMessage("");
			console.sendMessage("§8[§4§lCLANS§r§8] §7You're running the Version §b§l" + plversion + "§r §7of Clans!");	
			console.sendMessage("");
			console.sendMessage("§8§l§m===========================================================================");			
		} else {
			console.sendMessage("§8§l§m===========================================================================");
			console.sendMessage("");
			console.sendMessage("§8[§4§lCLANS§r§8] §bYou're running and §c§lOLD§r §bVersion of Clans!");	
			console.sendMessage("§8[§4§lCLANS§r§8] §bPlease download the last Version at:");	
			console.sendMessage("§8[§4§lCLANS§r§8] §e" + lkfile.getString("link"));	
			console.sendMessage("");
			console.sendMessage("§8§l§m===========================================================================");		
		}	
		try{if(u.delete()){}else{}}catch(Exception e){e.printStackTrace(); }
		try{if(lk.delete()){}else{}}catch(Exception e){e.printStackTrace(); }
		
		//-UPDATER
		
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		if (!l.exists()) { try {

				lfile.createSection("global");
				lfile.set("global.prefix", "&8[&4&lCLANS&r&8] &r");
                lfile.set("global.only_players", "&cOnly for players!");
                lfile.set("global.no_clan", "&cYou need to be in a clan to use this command!");
                lfile.set("global.rank", "&cYou need to be mod or owner of the clan to use this command!");
                lfile.set("global.owner", "&cYou need to be owner of the clan to use this command!");
                lfile.set("global.no_exist", "&cThis clan doesn't exist!");
                lfile.set("global.perms", "&cYou don't have permissions!");
                lfile.set("global.negative", "&cThis number can't be negative or 0!");
                
                lfile.createSection("help");
                lfile.set("help.commands", "&8&l»&r &9&lCommands:&r");
                lfile.set("help.help_cmd", "&8&l»&r &b/clan help &7Show all clan commands.");
                lfile.set("help.create_cmd", "&8&l»&r &b/clan create <clan> &7Create a clan.");
                lfile.set("help.join_cmd", "&8&l»&r &b/clan join <clan> &7Join a clan.");
                lfile.set("help.invite_cmd", "&8&l»&r &b/clan invite <player> &7Invite a player to join your clan.");
                lfile.set("help.kick_cmd", "&8&l»&r &b/clan kick <player> &7Kick a player from your clan.");
                lfile.set("help.info_cmd", "&8&l»&r &b/clan info <clan> &7Show info of a clan.");
                lfile.set("help.list_cmd", "&8&l»&r &b/clan list &7List of all clans.");
                lfile.set("help.chat_cmd", "&8&l»&r &b/clan chat &7Changes your chat mode.");
                lfile.set("help.mode_cmd", "&8&l»&r &b/clan mode &7Changes the join mode of the clan.");
                lfile.set("help.promote_cmd", "&8&l»&r &b/clan promote &7Promotes a member of the clan.");
                lfile.set("help.demote_cmd", "&8&l»&r &b/clan demote &7Demotes a mod of the clan.");
                lfile.set("help.pvp_cmd", "&8&l»&r &b/clan pvp &7Changes clan pvp mode.");     
                
                lfile.createSection("ahelp");
                lfile.set("ahelp.commands", "&8&l»&r &9&lCommands:&r");
                lfile.set("ahelp.help_cmd", "&8&l»&r &b/aclan help &7Show all admin commands.");
                lfile.set("ahelp.slots_cmd", "&8&l»&r &b/aclan slots <set/default/add/remove> <int> &7Modify a clans slots.");
                lfile.set("ahelp.level_cmd", "&8&l»&r &b/aclan level <> <int> &7Modify a clans level.");
                lfile.set("ahelp.exp_cmd", "&8&l»&r &b/aclan exp <> <int> &7Modify a clans exp.");
                lfile.set("ahelp.erase_cmd", "&8&l»&r &b/aclan erase <clan> &7Remove a clan.");
                lfile.set("ahelp.reload_cmd", "&8&l»&r &b/aclan reload &7Reload plugin files.");
                
                lfile.createSection("create");
                lfile.set("create.usage", "&7Correct usage: /clan create <clan>");
                lfile.set("create.created", "&f%player% &7has created the clan: &c%clan%");
                lfile.set("create.no_money", "&cYou don't have money to create a clan!");
                lfile.set("create.long", "&cThe name of the clan is too long!");
                lfile.set("create.short", "&cThe name of the clan is too short!");
                lfile.set("create.exist", "&cThis clan is already created!");
                lfile.set("create.has_clan", "&cYou're already a member from other clan!");
                
                lfile.createSection("join");
                lfile.set("join.usage", "&7Correct usage: /clan join <clan>");
                lfile.set("join.joined", "&7You have joined: &b%clan%");
                lfile.set("join.invitation", "&cYou need an invitation to join this clan!");
                lfile.set("join.full", "&cThis clan is full!");
                lfile.set("join.has_clan", "&cYou're already a member from other clan!");
                
                lfile.createSection("invite");
                lfile.set("invite.usage", "&7Correct usage: /clan invite <user>");
                lfile.set("invite.invited", "&7You have invited &b%player% &7to your clan!");
                lfile.set("invite.been_invited", "&7You have been invited to clan: &b%clan%");
                lfile.set("invite.has_clan", "&cThis player is already in a clan!");
                lfile.set("invite.offline", "&cThat player is not online!");
                          
                lfile.createSection("info");
                lfile.set("info.usage", "&7Correct usage: /clan info <clan>");
                
                lfile.createSection("pvp");
                lfile.set("pvp.enabled", "&7PvP Mode: &6Enabled!");
                lfile.set("pvp.disabled", "&7PvP Mode: &cDisabled!");
                
                lfile.createSection("leave");
                lfile.set("leave.left", "&cYou have left your clan!");
                
                lfile.createSection("kick");
                lfile.set("kick.usage", "&7Correct usage: /clan kick <user>");
                lfile.set("kick.kicked", "&7You have kick &b%player% &7out of your clan!");
                lfile.set("kick.yourself", "&cYou can't kick yourself!");
                lfile.set("kick.no_member", "&cThis player is not in your clan!");
                
                lfile.createSection("promote");
                lfile.set("promote.usage", "&7Correct usage: /clan promote <user>");
                lfile.set("promote.mod", "&cThat player is already a mod of your clan!");
                lfile.set("promote.promoted", "&7That player is now a mod of your clan!");
                lfile.set("promote.no_member", "&cThat player is not in your clan!");
                
                lfile.createSection("demote");
                lfile.set("demote.usage", "&7Correct usage: /clan demote <user>");
                lfile.set("demote.no_mod", "&cThat player is not a mod of your clan!");
                lfile.set("demote.demoted", "&7That player is now a member of your clan!");
                lfile.set("demote.no_member", "&cThat player is not in your clan!");
                
                lfile.createSection("mode");
                lfile.set("mode.inv", "&7Clan Mode: &cInvitation!");
                lfile.set("mode.free", "&7Clan Mode: &6Free!");
                
                lfile.createSection("chat");
                lfile.set("chat.enabled", "&7Clan chat: &6Enabled!");
                lfile.set("chat.disabled", "&7Clan chat: &cDisabled!");              
                
                lfile.createSection("event");
                lfile.set("event.pvp.same_clan", "&b%player% &7is a member of your clan!");
                lfile.set("event.pvp.levelup", "&c%clan% &7has reached level &b%level%&7!");
                
                lfile.createSection("admin");
                
                lfile.set("admin.slots.usage", "&7Correct usage: /aclan slots <clan> <set/default/add/remove> <int>!");
                lfile.set("admin.slots.no_exist", "&cThat clan doesn't exist!");
                lfile.set("admin.slots.less_default", "&cSlots can't be less than default slots number!");
                lfile.set("admin.slots.int", "&cArgument 3 must be an integer!");
                lfile.set("admin.slots.set", "&7Slots of clan &b%clan% &7are now &c%slots%&7!");
                lfile.set("admin.slots.default", "&7Slots of clan &b%clan% &7are now the defaults!");
                lfile.set("admin.slots.add", "&c%slots% &7slots have been added to clan &b%clan%&7!");
                lfile.set("admin.slots.remove", "&c%slots% &7slots have been removed from clan &b%clan%&7!");
                
                lfile.set("admin.level.usage", "&7Correct usage: /aclan level <clan> <set/reset/add/remove> <int>!");
                lfile.set("admin.level.no_exist", "&cThat clan doesn't exist!");
                lfile.set("admin.level.less", "&cClan level can't be less than 1!");
                lfile.set("admin.level.set", "Nearly!");
                lfile.set("admin.level.reset", "Nearly!");
                lfile.set("admin.level.add", "Nearly!");
                lfile.set("admin.level.remove", "Nearly!");
                
                lfile.set("admin.exp.usage", "&7Correct usage: /aclan exp <clan> <set/reset/add/remove> <int>!");
                lfile.set("admin.exp.no_exist", "&cThat clan doesn't exist!");
                lfile.set("admin.exp.less", "&cClan exp can't be less than 0!");
                lfile.set("admin.exp.set", "Nearly!");
                lfile.set("admin.exp.reset", "Nearly!");
                lfile.set("admin.exp.add", "Nearly!");
                lfile.set("admin.exp.remove", "Nearly!");
                
                lfile.set("admin.erase.usage", "&7Correct usage: /aclan erase <clan>");
                lfile.set("admin.erase.no_exist", "&cThat clan doesn't exist!");
                lfile.set("admin.erase.erased", "&7You've deleted the clan: &b%clan%&7!");
                
				lfile.save(l);			
			} catch (IOException exception) { exception.printStackTrace(); } }
		
		File s = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "settings.yml");	
		FileConfiguration sfile = YamlConfiguration.loadConfiguration(s);
		if (!s.exists()) { try {
			
				sfile.set("cost", 100000.00);
				sfile.set("slots", 6);
				sfile.set("maxlvl", 100);
				sfile.set("clan_min_lenght", 3);
				sfile.set("clan_max_lenght", 6);
				sfile.set("chat.format", "&8[&4%rank%&c%clan%&8] &r");
				sfile.set("chat.owner_prefix", "**");
				sfile.set("chat.mod_prefix", "*");
				sfile.set("clan_chat.format", "&8[&f&l%clan%&r&8] &c%player% &8&l»&r &f%message%");
				
				sfile.save(s);					
		} catch (IOException exception) { exception.printStackTrace(); } }  
		
		File g = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "gui.yml");	
		FileConfiguration gfile = YamlConfiguration.loadConfiguration(g);
		if (!g.exists()) { try {
				ArrayList<String> Lore = new ArrayList<String>();
				
				//INFO
				
				gfile.createSection("info");
				gfile.set("info.title", "&4&LCLAN");
				gfile.set("info.members.name", "&a&LMembers &8[&c%members%&7/&c%slots%&8]");
                Lore = new ArrayList<String>();
                Lore.clear();
				Lore.add("&8");
				Lore.add("&7Click to view the list!");
				Lore.add("&8");
                gfile.set("info.members.lore", Lore);
                
                gfile.set("info.lvl.name", "&6&LLevel &8[&c%level%&r&8]");
                Lore = new ArrayList<String>();
                Lore.clear();
                Lore.add("&8");
				Lore.add("&7Exp: &c%exp%&7/&c%lvlup%");
				Lore.add("&8");
                gfile.set("info.lvl.lore", Lore);
                
                gfile.set("info.stats.name", "&c&LStats");
                Lore = new ArrayList<String>();
                Lore.clear();
                Lore.add("&8");
				Lore.add("&7Kills: &c%kills%");
				Lore.add("&7Death: &c%deaths%");
				Lore.add("&7KDR: &c%kdr%");
				Lore.add("&8");
				gfile.set("info.stats.lore", Lore);
                
				//MEMBERS
				
                gfile.createSection("members");
                gfile.set("members.title", "&4&LMEMBERS");
                gfile.set("members.status.online", "&eOnline");
                gfile.set("members.status.offline", "&cOffline");
                gfile.set("members.back.name", "&c&LBack");
                Lore = new ArrayList<String>();
                Lore.clear();
        		Lore.add("&8");
        		Lore.add("&7Click to return to clan info!");
        		Lore.add("&8");
                gfile.set("members.back.lore", Lore);
                
                gfile.set("members.panel.name", "&f&LClan Members");
                Lore = new ArrayList<String>();
                Lore.clear();
        		Lore.add("&8");
        		Lore.add("&7Seeing Clan members!");
        		Lore.add("&8");
                gfile.set("members.panel.lore", Lore);
                
                gfile.set("members.member.name", "&a&L%member%");
                Lore = new ArrayList<String>();
                Lore.clear();
                Lore.add("&8");		 
        		Lore.add("&7Status: %status%");
        		Lore.add("&8");
                gfile.set("members.member.lore", Lore);
                
                //LIST
                
                gfile.createSection("list");  
                gfile.set("list.title", "&4&LCLANS&e&l");
                gfile.set("list.next_pg.name", "&f&L»");
                Lore = new ArrayList<String>();
                Lore.clear();
        		Lore.add("&8");
        		Lore.add("&7Click to go to the next page!");
        		Lore.add("&8");
                gfile.set("list.next_pg.lore", Lore);
                
                gfile.set("list.return_pg.name", "&f&L«");
                Lore = new ArrayList<String>();
                Lore.clear();
        		Lore.add("&8");
        		Lore.add("&7Click to go back to another page!");
        		Lore.add("&8");
                gfile.set("list.return_pg.lore", Lore);
                
                gfile.set("list.clan.name", "Nearly!");
                Lore = new ArrayList<String>();
                Lore.clear();
                Lore.add("&8");
				Lore.add("&7Click to view more info!");
				Lore.add("&8");
                gfile.set("list.clan.lore", Lore);
                
                gfile.set("list.none.name", "&cClan not Found");
                Lore = new ArrayList<String>();
                Lore.clear();
                Lore.add("&8");
				Lore.add("&7If you want to create a clan");
				Lore.add("&7use this command: &b&l/clan create <clan>");
				Lore.add("&8");
				Lore.add("&eCost: &f%cost%");
				Lore.add("&8");
                gfile.set("list.none.lore", Lore);
                
				gfile.save(g);			
			} catch (IOException exception) { exception.printStackTrace(); } }  
		
		this.invmenu = new menus(this);
		
		super.onEnable();
		if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			PlaceholderAPI.registerPlaceholder(this, "clans.clan",
					new PlaceholderReplacer() {
						public String onPlaceholderReplace(
								PlaceholderReplaceEvent event) {
							Player player = event.getPlayer();
							if (plugin.pclans.get(player.getName().toString()) == null) {
								return "No Clan!";
							}
							return plugin.pclans.get(player.getName().toString());
						}
					});
		}
		
	}

	@Override
	public void onDisable() {
		getLogger().info("[CLANS] Plugin Disabled!");
	}
	
	public static Economy getEconomy() {
		return economy;
	}
	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	public menus getInvMenus() {
		return this.invmenu; 
	}
}

