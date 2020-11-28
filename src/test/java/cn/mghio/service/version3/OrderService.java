package cn.mghio.service.version3;

import cn.mghio.dao.version3.StockDao;
import cn.mghio.dao.version3.TradeDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mghio
 * @since 2020-11-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderService {

    private StockDao stockDao;

    private TradeDao tradeDao;

    private String owner;
}
