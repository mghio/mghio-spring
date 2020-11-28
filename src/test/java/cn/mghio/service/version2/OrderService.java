package cn.mghio.service.version2;

import cn.mghio.dao.version2.StockDao;
import cn.mghio.dao.version2.TradeDao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mghio
 * @since 2020-11-05
 */
@Data
@NoArgsConstructor
public class OrderService {

    private StockDao stockDao;

    private TradeDao tradeDao;

    private String owner;

    private Integer num;

    private Date orderTime;
}
