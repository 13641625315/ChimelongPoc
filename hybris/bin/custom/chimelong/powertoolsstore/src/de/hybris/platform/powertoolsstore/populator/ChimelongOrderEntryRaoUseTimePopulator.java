package de.hybris.platform.powertoolsstore.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * Created on 09/05/2018.
 */
public class ChimelongOrderEntryRaoUseTimePopulator implements Populator<AbstractOrderEntryModel, OrderEntryRAO> {
    @Override
    public void populate(AbstractOrderEntryModel abstractOrderEntryModel, OrderEntryRAO orderEntryRAO) throws ConversionException {
        orderEntryRAO.setUseStartTime(abstractOrderEntryModel.getUseStartTime());
        orderEntryRAO.setUseEndTime(abstractOrderEntryModel.getUseEndTime());
    }
}
