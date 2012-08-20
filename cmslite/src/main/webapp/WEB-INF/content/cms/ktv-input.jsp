<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>KTV管理</title>
<%@ include file="/common/meta.jsp"%>
<link href="${ctx}/css/yui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/css/style.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
<script>
		$(document).ready(function() {
			$(".mainNav a").attr("class","");
			$("#n6").attr("class","actived");
			$("#subNav603").attr("class","actived");
			$(".secondNav div").each(function(){
				$(this).hide();
				$("#subNav6").show();
			});
		});
	</script>

<script>
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#serviceId").focus();
		//$("#myDiv").
		//为inputForm注册validate函数
		$("#inputForm").validate({
			rules: {
				number:{required:true,digits:true,min:1},
				product:"required",
				regionTreeIndex:"required",
				providerId:"required",
				ftpId:"required",
				videoFtpPath:"required",
				productId:"required",
				regionTreeIndex:"required"
			},
			messages: {
				number: "不能为空,且为正整数",
				product:"不能为空",
				regionTreeIndex:"不能为空",
				providerId:"不能为空",
				ftpId:"不能为空",
				videoFtpPath:"不能为空",
				productId:"不能为空",
				regionTreeIndex:"不能为空"
			}
		});
	});
	
	/***
	function hiddenButton(){
		var id = "<%=request.getParameter("id")%>";
		if(!(id==null||id=="null")){
			document.getElementById("savebutton").style.display="none";
		}else{
			document.getElementById("savebutton").style.display="";
		}
		
	}
	***/
	
	</script>
</head>

<body>
<div id="doc3"><%@ include file="/common/header.jsp"%>
<div id="bd">
<div id="yui-main">
<div class="yui-b">
<h2><s:if test="id == null">创建</s:if><s:else>修改</s:else>KTV</h2>
<form id="inputForm" action="ktv!save.action" method="post"><input type="hidden" name="id" value="${id}" />
<table class="noborder">
	<tr>
		<td>开始序号:</td>
		<td><input type="text" name=beginKtvNum  size="40" id="beginKtvNum" value="${beginKtvNum}" /></td>
	</tr>
	<tr>
		<td colspan="2"><s:radio name="recommended"  list="%{#{false:'不推荐',true:'推荐'}}" value="false" label="是否推荐"></s:radio></td>
	</tr>
	<tr>
		<td>生成数目:</td>
		<td><input type="text" name="number" size="40" id="number" value="${number}" /></td>
	</tr>
	<tr>
		<td>分类:</td>
		<td><input type="text" name="genreTreeIndex" size="40" id="genreTreeIndex" value="${regionTreeIndex}" /></td>
	</tr>
	<tr>
		<td>曲种:</td>
		<td><input type="text" name="regionTreeIndex" size="40" id="regionTreeIndex" value="${regionTreeIndex}" /></td>
	</tr>
	<tr>
		<td>供应商标识:</td>
		<td><input type="text" name="providerId" size="40" id="providerId" value="${providerId}" /></td>
	</tr>
	<tr>
		<td>产品标识:</td>
		<td><input type="text" name="productId" size="40" id="productId" value="${productId}" /></td>
	</tr>
	
	<tr>
		<td>FTP标识:</td>
		<td><input type="text" name="ftpId" size="40" id="ftpId" value="${ftpId}" /></td>
	</tr>

	<tr>
		<td>视频FTP路径:</td>
		<td><input type="text" name="videoFtpPath" size="40" id="videoFtpPath" value="${videoFtpPath}" /></td>
	</tr>
	<tr>
		<td>海报FTP路径:</td>
		<td><input type="text" name="posterFtpPath" size="40" id="posterFtpPath" value="${posterFtpPath}" /></td>
	</tr>

	<tr>
		<td><input class="button" id="savebutton"  type="submit" value="提交" /> &nbsp; <input class="button" type="button" value="返回" onclick="history.back()" /></td>
	</tr>
</table>
</form>
</div>
</div>
</div>
<%@ include file="/common/footer.jsp"%></div>
</body>
</html>
