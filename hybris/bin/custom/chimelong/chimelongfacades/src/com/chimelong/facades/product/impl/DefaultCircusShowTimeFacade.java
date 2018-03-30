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
package com.chimelong.facades.product.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chimelong.core.model.CircusShowTimeModel;
import com.chimelong.core.product.service.CircusShowTimeService;
import com.chimelong.facades.product.CircusShowTimeFacade;
import com.chimelong.facades.product.data.CircusShowTimeData;


public class DefaultCircusShowTimeFacade implements CircusShowTimeFacade
{
	private CircusShowTimeService circusShowTimeService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.chimelong.facades.product.CircusShowTimeFacade#findAllCircusShowTimes()
	 */
	@Override
	public List<CircusShowTimeData> findAllCircusShowTimes()
	{
		final Set<CircusShowTimeModel> circusShowTimeModels = circusShowTimeService.findAllCircusShowTimes();
		final List<CircusShowTimeData> circusShowTimeDatas = new ArrayList<CircusShowTimeData>();
		CircusShowTimeData circusShowTimeData = new CircusShowTimeData();
		circusShowTimeData.setShowCode("");
		circusShowTimeData.setShowTime("全部");
		circusShowTimeDatas.add(circusShowTimeData);
		for (final CircusShowTimeModel circusShowTimeModel : circusShowTimeModels)
		{
			circusShowTimeData = new CircusShowTimeData();
			circusShowTimeData.setShowCode(circusShowTimeModel.getCode());
			circusShowTimeData.setShowTime(circusShowTimeModel.getStartTime() + "-" + circusShowTimeModel.getEndTime());
			circusShowTimeDatas.add(circusShowTimeData);
		}
		return circusShowTimeDatas;
	}

	/**
	 * @return the circusShowTimeService
	 */
	public CircusShowTimeService getCircusShowTimeService()
	{
		return circusShowTimeService;
	}

	/**
	 * @param circusShowTimeService
	 *           the circusShowTimeService to set
	 */
	public void setCircusShowTimeService(final CircusShowTimeService circusShowTimeService)
	{
		this.circusShowTimeService = circusShowTimeService;
	}

}
