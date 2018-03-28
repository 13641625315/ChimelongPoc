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
package com.chimelong.facades.product;

import java.util.List;

import com.chimelong.facades.product.data.CircusShowTimeData;


public interface CircusShowTimeFacade
{
	List<CircusShowTimeData> findAllCircusShowTimes();
}
