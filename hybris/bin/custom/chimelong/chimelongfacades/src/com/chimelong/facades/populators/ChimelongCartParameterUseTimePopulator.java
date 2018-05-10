package com.chimelong.facades.populators;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * Created on 09/05/2018.
 */
public class ChimelongCartParameterUseTimePopulator implements Populator<AddToCartParams, CommerceCartParameter> {
    @Override
    public void populate(AddToCartParams addToCartParams, CommerceCartParameter commerceCartParameter) throws ConversionException {
        commerceCartParameter.setUseStartTime(addToCartParams.getUseStartTime());
        commerceCartParameter.setUseEndTime(addToCartParams.getUseEndTime());
    }
}
