package net.iafenvoy.kookbridge.kook;

import snw.jkook.command.JKookCommand;
import snw.jkook.entity.User;
import snw.jkook.message.component.TextComponent;
import snw.jkook.plugin.BasePlugin;

public class BridgePlugin extends BasePlugin {
    @Override
    public void onLoad() {
        getLogger().info("Ping loaded successfully");
    }

    @Override
    public void onEnable() {
        new JKookCommand("mc").setExecutor((sender, args, message) -> {
                            if (sender instanceof User) { // 确保是个 Kook 用户在执行此命令, 现在更推荐 executesUser 方法
                                ((User) sender).sendPrivateMessage(
                                        new TextComponent("PONG!") // Component 表示一个聊天组件，见 snw.jkook.message.component 包
                                ); // 发送私信
                            }
                            else {
                                getLogger().info("This command is not available for console.");
                                // 这个 else 块是可选的，但为了用户体验，最好还是提醒一下
                                // 另外，我们假设此执行器是在 Plugin#onEnable 里写的，所以我们可以使用 getLogger() 。
                            }
                        }
                )
                .register(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Ping stopped");
    }
}
