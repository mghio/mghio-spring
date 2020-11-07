package cn.mghio.service.version2;

import cn.mghio.dao.version2.StockService;
import cn.mghio.dao.version2.TradeService;
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

    private StockService stockService;

    private TradeService tradeService;

    private String owner;

    private Integer num;

    private Date orderTime;
}
