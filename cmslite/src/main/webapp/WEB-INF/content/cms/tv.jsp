<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.springside.modules.security.springsecurity.SpringSecurityUtils" %>
<%@ include file="/common/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TV信息管理</title>
	<%@ include file="/common/meta.jsp" %>
	<link href="${ctx}/css/yui.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/css/style.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/css/pagination.css" type="text/css" rel="stylesheet"/>
	<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/js/table.js" type="text/javascript"></script>
	<script src="${ctx}/js/jquery.pagination.js" type="text/javascript"></script>
	<script>
		$(document).ready(function() {
			$(".mainNav a").attr("class","");
			$("#n6").attr("class","actived");
			$("#subNav602").attr("class","actived");
			$(".secondNav div").each(function(){
				$(this).hide();
				$("#subNav6").show();
			});
		});
	</script>
</head>

<body>
<div id="doc3">
<%@ include file="/common/header.jsp" %>
<div id="bd">
	<div id="yui-main">
	<div class="yui-b">
	<form id="mainForm" action="tv.action" method="get">
		<input type="hidden" name="page.pageNo" id="pageNo" value="${page.pageNo}"/>
		<input type="hidden" name="page.orderBy" id="orderBy" value="${page.orderBy}"/>
		<input type="hidden" name="page.order" id="order" value="${page.order}"/>
		<input type="hidden" name="page.pageSize" id="pageSize" value="${page.pageSize}"/>

		<div id="message"><s:actionmessage theme="custom" cssClass="success"/></div>
		<div id="filter">
			标题: <input type="text" name="filter_LIKES_title" value="${param['filter_LIKES_title']}" size="9" tabindex="1" onkeypress="if (event.keyCode == 13) {javascript:document.forms.mainForm.submit()}"/>
			<input type="button" value="搜索" onclick="search();" tabindex="2"/>
		</div>
		<div id="content">
			<table id="contentTable">
				<tr>
					<th><a href="javascript:sort('title','asc')">标题</a></th>
					<th><a href="javascript:sort('genreTreeIndex','asc')">风格标识</a></th>
					<th><a href="javascript:sort('regionTreeIndex','asc')">区域标识</a></th>
					<th><a href="javascript:sort('episodeNumber','asc')">剧集数</a></th>
					<th><a href="javascript:sort('createTime','asc')">创建时间</a></th>
					<th>操作</th>
				</tr>

				<s:iterator value="page.result">
					<tr>
						<td>${title}&nbsp;</td>
						<td>${genreTreeIndex}&nbsp;</td>
						<td>${regionTreeIndex}&nbsp;</td>
						<td>${episodeNumber}&nbsp;</td>
						<td><fmt:formatDate value="${createTime}" type="both"/>&nbsp;</td>
						<td>
						<a href="tv!input.action?id=${id}">详情</a>&nbsp; 
						<c:if test='${state=="EDITABLE"}'>
						<a href="tv!updateToCensoring.action?id=${id}" onclick="return confirm('确定批量提交审核？')">批量提交审核</a>
						</c:if>
						<c:if test='${state=="CENSORING"}'>
						<a href="tv!updateToIngestable.action?id=${id}" onclick="return confirm('确定批量审核吗？')">批量审核</a>
						</c:if>
						<c:if test='${state=="INGESTABLE"}'>
						<a href="tv!updateToIngested.action?id=${id}" onclick="return confirm('确定批量注入吗？')">批量注入</a>
						</c:if>
						<c:if test='${state=="INGESTED"}'>
						<a href="tv!offline.action?id=${id}" onclick="return confirm('确定批量下线吗？')">批量下线</a>
						</c:if>
						<c:if test='${state=="ZOMBIE"}'>
						<a href="tv!delete.action?id=${id}" onclick="return confirm('确定删除吗？')">删除</a>
						</c:if>
					   </td>
					</tr>
				</s:iterator>
			</table>
		</div>

		<div class="pagination">
		    <%@ include file="/common/page.jsp" %>
				<a href="tv!input.action">增加TV</a>
		</div>
	</form>
	</div>
	</div>
</div>
<%@ include file="/common/footer.jsp" %>
</div>
</body>
</html>
