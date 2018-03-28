/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.chimelong.core.product.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.chimelong.core.model.CircusShowTimeModel;
import com.chimelong.core.product.dao.CircusShowTimeDao;


public class DefaultCircusShowTimeDao extends DefaultGenericDao<CircusShowTimeModel> implements CircusShowTimeDao
{

	public DefaultCircusShowTimeDao()
	{
		super("CircusShowTime");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.chimelong.core.product.dao.CircusShowTimeDao#findAllCircusShowTimes()
	 */
	@Override
	public Set<CircusShowTimeModel> findAllCircusShowTimes()
	{
		final List<CircusShowTimeModel> result = find();
		return Collections.unmodifiableSet(new HashSet<CircusShowTimeModel>(result));
	}

}
