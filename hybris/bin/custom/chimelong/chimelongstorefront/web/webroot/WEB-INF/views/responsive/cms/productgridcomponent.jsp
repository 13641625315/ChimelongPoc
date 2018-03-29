<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<%-- <nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}" numberPagesShown="${numberPagesShown}"/>
 --%>

<div>
    <p></p>
	<form id="dateForm${top ? '1' : '2'}" action="#" class="form-inline"
		method="get">
        <div class="form-group">
            <label class="control-label" for="dateForm${top ? '1' : '2'}"> <%--<spring:theme code="${themeMsgKey}.sortTitle"/>--%>
                门票日期:
            </label>
            <input type="date" id="ticketBookDate${top ? '1' : '2'}"
                   name="ticketBookDate" value="${ticketBookDate}" class="form-control">
        </div>
		<c:if test="${categoryCode eq 'cl14'}">
            <div class="form-group">
                <select name="showTime" class="form-control">
                    <c:forEach items="${showTimes}" var="showTimeEntry"
                               varStatus="status">
                        <option value="${showTimeEntry.showCode}"
                                <c:if test="${showTimeEntry.showCode eq showTime}">selected="selected"</c:if>>${showTimeEntry.showTime}</option>
                    </c:forEach>
                </select>
            </div>
        </c:if>
	</form>
</div>

<div class="product__listing product__grid">
	<c:forEach items="${searchPageData.results}" var="product"
		varStatus="status">
		<product:productListerGridItem product="${product}" />
	</c:forEach>
</div>

<div id="addToCartTitle" class="display-none">
	<div class="add-to-cart-header">
		<div class="headline">
			<span class="headline-text"><spring:theme
					code="basket.added.to.basket" /></span>
		</div>
	</div>
</div>

<%-- <nav:pagination top="false"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"  searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/> --%>