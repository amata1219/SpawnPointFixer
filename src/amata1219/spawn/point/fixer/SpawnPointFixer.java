package amata1219.spawn.point.fixer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnPointFixer extends JavaPlugin implements CommandExecutor {
    private String version;

    public void onEnable() {
        this.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        this.getCommand("spfix").setExecutor(this);
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "ゲーム内から実行して下さい。");
                return true;
            } else {
                this.fix((Player)sender);
                sender.sendMessage(ChatColor.AQUA + "自分のスポーン地点を削除しました。");
                return true;
            }
        } else if (this.getServer().getPlayerExact(args[0]) != null) {
            Player player = this.getServer().getPlayer(args[0]);
            this.fix(player);
            sender.sendMessage(ChatColor.AQUA + player.getName() + "さんのスポーン地点を削除しました。");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "/spfix [player]");
            return true;
        }
    }

    public void fix(Player player) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle");
            Object objEntityPlayer = getHandle.invoke(player);
            Class<?> EntityPlayer = Class.forName("net.minecraft.server." + this.version + ".EntityPlayer");
            Class<?> BlockPosition = Class.forName("net.minecraft.server." + this.version + ".BlockPosition");
            Method setRespawnPosition = EntityPlayer.getMethod("setRespawnPosition", BlockPosition, Boolean.TYPE);
            setRespawnPosition.invoke(objEntityPlayer, null, false);
        } catch (NoSuchMethodException var7) {
            var7.printStackTrace();
        } catch (SecurityException var8) {
            var8.printStackTrace();
        } catch (IllegalAccessException var9) {
            var9.printStackTrace();
        } catch (IllegalArgumentException var10) {
            var10.printStackTrace();
        } catch (InvocationTargetException var11) {
            var11.printStackTrace();
        } catch (ClassNotFoundException var12) {
            var12.printStackTrace();
        }

    }
}
