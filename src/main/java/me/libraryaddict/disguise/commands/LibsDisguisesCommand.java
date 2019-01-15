package me.libraryaddict.disguise.commands;

import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.utilities.translations.LibsMsg;
import me.libraryaddict.disguise.utilities.LibsPremium;
import me.totalfreedom.disguise.DisguiseBlocker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibsDisguisesCommand implements CommandExecutor, TabCompleter {
    protected ArrayList<String> filterTabs(ArrayList<String> list, String[] origArgs) {
        if (origArgs.length == 0)
            return list;

        Iterator<String> itel = list.iterator();
        String label = origArgs[origArgs.length - 1].toLowerCase();

        while (itel.hasNext()) {
            String name = itel.next();

            if (name.toLowerCase().startsWith(label))
                continue;

            itel.remove();
        }

        return list;
    }

    protected String[] getArgs(String[] args) {
        ArrayList<String> newArgs = new ArrayList<>();

        for (int i = 0; i < args.length - 1; i++) {
            String s = args[i];

            if (s.trim().isEmpty())
                continue;

            newArgs.add(s);
        }

        return newArgs.toArray(new String[0]);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            LibsDisguises disguises = LibsDisguises.getInstance();

            String version = disguises.getDescription().getVersion();

            if (!disguises.isReleaseBuild()) {
                version += "-";

                if (disguises.isNumberedBuild()) {
                    version += "b";
                }

                version += disguises.getBuildNo();
            }

            sender.sendMessage(ChatColor.DARK_GREEN + "This server is running " + "Lib's Disguises v" + version +
                    " by libraryaddict, formerly maintained by Byteflux and NavidK0." +
                    (sender.hasPermission("libsdisguises.reload") ?
                            "\nUse " + ChatColor.GREEN + "/libsdisguises " + "reload" + ChatColor.DARK_GREEN +
                                    " to reload the config. All disguises will be blown by doing this" + "." : ""));

            if (LibsPremium.isPremium()) {
                sender.sendMessage(ChatColor.DARK_GREEN + "This server supports the plugin developer!");
            }
        } else if (args.length > 0) {
            if (DisguiseBlocker.isAdmin(Bukkit.getPlayer(sender.getName()))) {
                if (args[0].equalsIgnoreCase("reload")) {
                    DisguiseConfig.loadConfig();
                    sender.sendMessage(LibsMsg.RELOADED_CONFIG.get());
                    return true;
                } else {
                    sender.sendMessage(LibsMsg.LIBS_RELOAD_WRONG.get());
                }
            } else {
                sender.sendMessage(LibsMsg.NO_PERM.get());
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] origArgs) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] args = getArgs(origArgs);

        if (args.length == 0)
            tabs.add("Reload");

        return filterTabs(tabs, origArgs);
    }
}
