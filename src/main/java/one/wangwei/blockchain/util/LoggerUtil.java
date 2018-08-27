package one.wangwei.blockchain.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志工具
 *
 * @author wangwei
 * @date 2018/08/27
 */
public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger("peerbase.logging");

    static {
        LOGGER.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        setHandlersLevel(Level.ALL);
    }

    public static void setHandlersLevel(Level level) {
        Handler[] handlers = LOGGER.getHandlers();
        for (Handler h : handlers) {
            h.setLevel(level);
        }
        LOGGER.setLevel(level);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

}
