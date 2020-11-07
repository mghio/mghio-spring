package cn.mghio.service.version3;

import cn.mghio.dao.version3.StockService;
import cn.mghio.dao.version3.TradeService;
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

    private StockService stockService;

    private TradeService tradeService;

    private String owner;
}
