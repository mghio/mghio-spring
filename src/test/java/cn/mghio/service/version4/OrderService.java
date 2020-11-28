package cn.mghio.service.version4;

import cn.mghio.beans.factory.anontation.Autowired;
import cn.mghio.dao.version4.StockDao;
import cn.mghio.dao.version4.TradeDao;
import cn.mghio.stereotype.Component;
import lombok.Data;

/**
 * @author mghio
 * @since 2020-11-25
 */
@Data
@Component(value = "orderService")
public class OrderService {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private TradeDao tradeDao;

}
