package cn.mghio.service.version2;

import cn.mghio.dao.version2.StockService;
import cn.mghio.dao.version2.TradeService;

import java.util.Date;

/**
 * @author mghio
 * @since 2020-11-05
 */
public class OrderService {

    private StockService stockService;

    private TradeService tradeService;

    private String owner;

    private Integer num;

    private Date orderTime;

    public StockService getStockService() {
        return stockService;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public TradeService getTradeService() {
        return tradeService;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
