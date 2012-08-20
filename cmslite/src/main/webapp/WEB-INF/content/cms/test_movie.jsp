<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.springside.modules.security.springsecurity.SpringSecurityUtils"%>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Movie信息管理</title>
<%@ include file="/common/meta.jsp"%>
<link href="${ctx}/css/yui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/css/style.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/css/pagination.css" type="text/css" rel="stylesheet" />
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
		
		
		
		$("document").ready(function(){
			    $("#checkAll").click(function(){
			   		 //$("[name='checkbox']").attr("checked",'true');//全选
			   		 //alert(this.id);
			   		 if (this.checked){
			   			$(".check").attr("checked",'true');
			   		 }else{
			   			$(".check").removeAttr("checked");
			   		 }
			   		
			    })
		    });

		
		//批量审核
		function updateToCensoring(url){
			var url = "movie!updateToCensoring.action";
			subready(url);
		}
		//批量下线
		function offline(url){
			var url = "movie!offline.action";
			subready(url);
		}
		

		function subready(url){
			var selectedItems = new Array();
			//.是class选择器
			$(".check:checked").each(
					function() {
						selectedItems.push(this.id);
					});

			if (selectedItems.length == 0)
			    alert("Please select item(s) to delete.");
			else{
				$.ajax({
					    type: "POST",
					    url: url,
				     	data: "titles=" + encodeURIComponent(selectedItems.join('|')),
					    dataType: "text",
					    success: function (request) {
					    	//jAlert(request, "提示信息") 
					        //document.location.reload();
					    	alert(request);
					      },
					    error: function(request,error){
					        alert('Error deleting item(s), try again later.');
					      }
					    }
				    );
			}
		}
		
		
		

	</script>
</head>

<body>
<div id="doc3"><%@ include file="/common/header.jsp"%>
<div id="bd">
<div id="yui-main">
<div class="yui-b">
<form id="mainForm" action="movie.action" method="get"><input type="hidden" name="page.pageNo" id="pageNo" value="${page.pageNo}" /> <input
	type="hidden" name="page.orderBy" id="orderBy" value="${page.orderBy}" /> <input type="hidden" name="page.order" id="order" value="${page.order}" />
<input type="hidden" name="page.pageSize" id="pageSize" value="${page.pageSize}" />

<div id="message"><s:actionmessage theme="custom" cssClass="success" /></div>
<div id="filter">标题: <input type="text" name="filter_LIKES_title" value="${param['filter_LIKES_title']}" size="9" tabindex="1"
	onkeypress="if (event.keyCode == 13) {javascript:document.forms.mainForm.submit()}" /> <input type="button" value="搜索" onclick="search();"
	tabindex="2" /></div>
<div id="content">
<table id="contentTable">
	<tr>
		<th><label><input type="checkbox" checked id="checkAll" />选择</label></th>
		<th><a href="javascript:sort('title','asc')">标题</a></th>
		<th><a href="javascript:sort('genreTreeIndex','asc')">风格标识</a></th>
		<th><a href="javascript:sort('regionTreeIndex','asc')">区域标识</a></th>
		<th><a href="javascript:sort('createTime','asc')">创建时间</a></th>
		<th>操作</th>
	</tr>

	<s:iterator value="page.result">
		<tr>
			<td><input type="checkbox" class="check" checked id="${title}" /></td>
			<td>${title}&nbsp;</td>
			<td>${genreTreeIndex}&nbsp;</td>
			<td>${regionTreeIndex}&nbsp;</td>
			<td><fmt:formatDate value="${createTime}" type="both" />&nbsp;</td>
			<td><a href="movie!delete.action?id=${id}" onclick="return confirm('确定删除吗？')">删除</a></td>
		</tr>
	</s:iterator>
</table>
</div>

<div class="pagination"><%@ include file="/common/page.jsp"%> <a href="movie!input.action">增加电影</a><a href="#"
	onclick="updateToCensoring();">提交审核</a><a href="movie!input.action">批量注入</a><a href="#" onclick="offline();">批量下线</a></div>
<button onclick="subready();">aaa</button>
</form>
</div>
</div>
</div>
<%@ include file="/common/footer.jsp"%></div>
</body>
</html>
