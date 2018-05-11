package de.hybris.platform.powertoolsstore.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.powertoolsstore.util.DateTimeHelper;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created on 09/05/2018.
 */
public class ChimelongOrderEntryRaoUseTimePopulator implements Populator<AbstractOrderEntryModel, OrderEntryRAO> {
    @Override
    public void populate(AbstractOrderEntryModel abstractOrderEntryModel, OrderEntryRAO orderEntryRAO) throws ConversionException {
        Date startTime = abstractOrderEntryModel.getUseStartTime();
        Date endTime = abstractOrderEntryModel.getUseEndTime();

        LocalDateTime startLocalDateTime =  DateTimeHelper.getLocalDateTime(startTime).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endLocalDateTime =  DateTimeHelper.getLocalDateTime(endTime).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long days = Duration.between(startLocalDateTime, endLocalDateTime).toDays();
        orderEntryRAO.setUseStartTime(startTime);
        orderEntryRAO.setUseEndTime(endTime);
        orderEntryRAO.setDayCount(days);

    }
}
