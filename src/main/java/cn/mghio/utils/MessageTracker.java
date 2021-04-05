package cn.mghio.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author mghio
 * @since 2021-04-04
 */
public class MessageTracker {

  // 有序
  private static final List<String> MESSAGES = new LinkedList<>();

  public static void addMsg(String msg) {
    MESSAGES.add(msg);
  }

  public static void cleanMsg() {
    MESSAGES.clear();
  }

  public static List<String> getMsgs() {
    return MESSAGES;
  }

}
