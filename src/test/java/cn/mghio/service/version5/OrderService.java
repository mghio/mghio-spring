package cn.mghio.service.version5;

import cn.mghio.beans.factory.anontation.Autowired;
import cn.mghio.dao.version5.StockDao;
import cn.mghio.dao.version5.TradeDao;
import cn.mghio.stereotype.Component;
import cn.mghio.utils.MessageTracker;

/**
 * @author mghio
 * @since 2021-04-04
 */
@Component(value = "orderService")
public class OrderService {

  @Autowired
  private StockDao stockDao;

  @Autowired
  private TradeDao tradeDao;

  public OrderService() {
  }

  public StockDao getStockDao() {
    return stockDao;
  }

  public TradeDao getTradeDao() {
    return tradeDao;
  }

  public void placeOrder() {
    System.out.println("place order");
    MessageTracker.addMsg("place order");
  }

  public void placeOrderWithArithmeticException() {
    int result = 1 / 0;
    System.out.println(result);
  }
}
