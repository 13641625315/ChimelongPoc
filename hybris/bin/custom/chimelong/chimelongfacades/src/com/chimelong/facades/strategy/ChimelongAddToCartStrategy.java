package com.chimelong.facades.strategy;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;

import java.util.Date;

/**
 * Created on 09/05/2018.
 */
public class ChimelongAddToCartStrategy extends DefaultCommerceAddToCartStrategy {


    @Override
    protected CartEntryModel addCartEntry(CommerceCartParameter parameter, long actualAllowedQuantityChange) throws CommerceCartModificationException {
        CartEntryModel entry =  super.addCartEntry(parameter, actualAllowedQuantityChange);
        Date useStartTime = parameter.getUseStartTime();
        Date useEndTime = parameter.getUseEndTime();
        entry.setUseStartTime(useStartTime);
        entry.setUseEndTime(useEndTime);
        return entry;
    }
}
