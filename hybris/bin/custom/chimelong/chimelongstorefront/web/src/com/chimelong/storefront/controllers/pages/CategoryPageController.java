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
package com.chimelong.storefront.controllers.pages;


import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPricePopulator;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPromotionsPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chimelong.core.model.CircusVariantTicketModel;
import com.chimelong.core.model.CombinedProductEntryModel;
import com.chimelong.core.model.CombinedTicketProductModel;
import com.chimelong.core.model.DateRangeModel;
import com.chimelong.core.model.TicketProductModel;
import com.chimelong.facades.product.CircusShowTimeFacade;


/**
 * Controller for a category page
 */
@Controller
@RequestMapping(value = "/**/c")
public class CategoryPageController extends AbstractCategoryPageController
{
	private static String DATE_PATTERN = "yyyy-MM-dd";
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger LOG = Logger.getLogger(CategoryPageController.class);

	@Resource
	private Populator<ProductModel, ProductData> productPrimaryImagePopulator;
	@Resource
	private ProductPromotionsPopulator<ProductModel, ProductData> productPromotionsPopulator;
	@Resource
	private ProductPricePopulator<ProductModel, ProductData> productPricePopulator;
	@Resource
	private CircusShowTimeFacade circusShowTimeFacade;

	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String category(@PathVariable(value = "categoryCode", required = true) final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "ticketBookDate", required = false) String ticketBookDate,
			@RequestParam(value = "showTime", required = false) final String showTime, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException
	{
		if (StringUtils.isEmpty(ticketBookDate))
		{
			ticketBookDate = LocalDate.now().toString();
		}
		model.addAttribute("showTime", showTime);
		model.addAttribute("showTimes", circusShowTimeFacade.findAllCircusShowTimes());
		model.addAttribute("ticketBookDate", ticketBookDate);
		final Date ticketBookDateD = convertStringDateToDate(ticketBookDate, DATE_PATTERN);
		final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode);
		final CategoryPageModel categoryPage = getCategoryPage(category);
		if (StringUtils.isNotEmpty(ticketBookDate) && categoryCode.startsWith("cl1"))//Ticket Category
		{
			if (StringUtils.isNotEmpty(ticketBookDate) && categoryCode.startsWith("cl14"))//Circus Ticket
			{
				final List<ProductData> productDatas = new ArrayList<>();
				final List<ProductModel> products = category.getProducts();
				for (final ProductModel product : products)
				{
					if (isBookingDateTicket(product, ticketBookDateD))
					{
						if (product instanceof CircusVariantTicketModel)
						{
							if (StringUtils.isEmpty(showTime) || (StringUtils.isNotEmpty(showTime)
									&& showTime.equals(((CircusVariantTicketModel) product).getShowTime().getShowCode())))
							{
								final ProductData productData = populateTicketProduct(product, ticketBookDateD);
								productDatas.add(productData);
							}
						}
						else if (product instanceof CombinedTicketProductModel)
						{
							if (StringUtils.isEmpty(showTime)
									|| (StringUtils.isNotEmpty(showTime) && hasShowInCombined(product, showTime)))
							{
								final ProductData productData = populateTicketProduct(product, ticketBookDateD);
								productDatas.add(productData);
							}
						}
					}
				}
				storeCmsPageInModel(model, categoryPage);
				storeContinueUrl(request);
				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = new ProductCategorySearchPageData();
				searchPageData.setResults(productDatas);
				model.addAttribute("searchPageData", searchPageData);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, searchPageData));
				model.addAttribute("categoryName", category.getName());
				model.addAttribute("categoryCode", category.getCode());
				model.addAttribute("pageType", PageType.CATEGORY.name());
				model.addAttribute("userLocation", getCustomerLocationService().getUserLocation());
				updatePageTitle(category, model);

				final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(
						category.getKeywords().stream().map(keywordModel -> keywordModel.getKeyword()).collect(Collectors.toSet()));
				final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
				setUpMetaData(model, metaKeywords, metaDescription);
				return getViewPage(categoryPage);
			}
			else//Normal Ticket
			{
				final List<ProductData> productDatas = new ArrayList<>();
				final List<ProductModel> products = category.getProducts();
				for (final ProductModel product : products)
				{
					if ((product instanceof TicketProductModel || product instanceof CombinedTicketProductModel)
							&& isBookingDateTicket(product, ticketBookDateD))
					{
						final ProductData productData = populateTicketProduct(product, ticketBookDateD);
						productDatas.add(productData);
					}
				}
				storeCmsPageInModel(model, categoryPage);
				storeContinueUrl(request);
				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = new ProductCategorySearchPageData();
				searchPageData.setResults(productDatas);
				model.addAttribute("searchPageData", searchPageData);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, searchPageData));
				model.addAttribute("categoryName", category.getName());
				model.addAttribute("categoryCode", category.getCode());
				model.addAttribute("pageType", PageType.CATEGORY.name());
				model.addAttribute("userLocation", getCustomerLocationService().getUserLocation());
				updatePageTitle(category, model);

				final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(
						category.getKeywords().stream().map(keywordModel -> keywordModel.getKeyword()).collect(Collectors.toSet()));
				final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
				setUpMetaData(model, metaKeywords, metaDescription);
				return getViewPage(categoryPage);
			}
		}
		else if (StringUtils.isNotEmpty(ticketBookDate) && categoryCode.startsWith("cl2"))//Hotel Category
		{
			return performSearchAndGetResultsPage(categoryCode, searchQuery, page, showMode, sortCode, model, request, response);
		}
		return performSearchAndGetResultsPage(categoryCode, searchQuery, page, showMode, sortCode, model, request, response);
	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetFacets(categoryCode, searchQuery, page, showMode, sortCode);
	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetResultsData(categoryCode, searchQuery, page, showMode, sortCode);
	}

	//////////////////////////////////////////////
	private Date convertStringDateToDate(final String date, final String pattern)
	{
		try
		{
			return Date.from(
					LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)).atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		catch (final DateTimeParseException e)
		{
			LOG.error("Unable to parse String to Date:", e);
		}
		return null;
	}

	private boolean isBookingDateTicket(final ProductModel product, final Date date)
	{
		if (product instanceof VariantProductModel && null != ((VariantProductModel) product).getBaseProduct())
		{
			for (final DateRangeModel dateRange : ((VariantProductModel) product).getBaseProduct().getDateRanges())
			{
				if (!(date.before(dateRange.getStartingDate())) && !date.after(dateRange.getEndingDate()))
				{
					if (((VariantProductModel) product).getBaseProduct().getIsWeekend().equals(dateIsWeekends(date)))
					{
						return true;
					}
				}
			}
		}
		else
		{
			for (final DateRangeModel dateRange : product.getDateRanges())
			{
				if (!(date.before(dateRange.getStartingDate())) && !date.after(dateRange.getEndingDate()))
				{
					if (product.getIsWeekend().equals(dateIsWeekends(date)))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private Boolean dateIsWeekends(final Date date)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private ProductData populateTicketProduct(final ProductModel product, final Date date)
	{
		final ProductData productData = new ProductData();
		productData.setCode(product.getCode());
		productData.setName(product.getName(Locale.CHINESE));
		//
		productPrimaryImagePopulator.populate(product, productData);
		productPromotionsPopulator.populate(product, productData);
		productPricePopulator.populate(product, productData);
		//
		return productData;
	}

	private boolean hasShowInCombined(final ProductModel product, final String showTime)
	{
		final CombinedTicketProductModel combinedTicketProduct = (CombinedTicketProductModel) product;
		for (final CombinedProductEntryModel combinedProductEntry : combinedTicketProduct.getCombinedProductEntries())
		{
			if (combinedProductEntry.getProduct() instanceof CircusVariantTicketModel)
			{
				if (showTime.equals(((CircusVariantTicketModel) combinedProductEntry.getProduct()).getShowTime()))
				{
					return true;
				}
			}
		}
		return false;
	}

}