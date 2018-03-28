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
package com.chimelong.core.product.service.impl;

import java.util.Set;

import com.chimelong.core.model.CircusShowTimeModel;
import com.chimelong.core.product.dao.CircusShowTimeDao;
import com.chimelong.core.product.service.CircusShowTimeService;


public class DefaultCircusShowTimeService implements CircusShowTimeService
{
	private CircusShowTimeDao circusShowTimeDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.chimelong.core.product.service.CircusShowTimeService#findAllCircusShowTimes()
	 */
	@Override
	public Set<CircusShowTimeModel> findAllCircusShowTimes()
	{
		return circusShowTimeDao.findAllCircusShowTimes();
	}

	/**
	 * @return the circusShowTimeDao
	 */
	public CircusShowTimeDao getCircusShowTimeDao()
	{
		return circusShowTimeDao;
	}

	/**
	 * @param circusShowTimeDao
	 *           the circusShowTimeDao to set
	 */
	public void setCircusShowTimeDao(final CircusShowTimeDao circusShowTimeDao)
	{
		this.circusShowTimeDao = circusShowTimeDao;
	}


}
