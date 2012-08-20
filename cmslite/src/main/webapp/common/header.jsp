<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="org.springside.modules.security.springsecurity.SpringSecurityUtils" %>
<%@ include file="/common/taglibs.jsp" %>
<div id="hd">
	<div id="title">
		<h1>CMS模拟测试工具</h1>
	</div>
	<div id="menu">
		<ul>
			<li><a href="${ctx}/cms/tv.action">TV管理</a></li>
			<li><a href="${ctx}/cms/ktv.action">KTV管理</a></li>
			<li><a href="${ctx}/cms/movie.action">电影管理</a></li>
			<li><a href="${ctx}/report/test/report-index.action">报表工具</a></li>
			<li><a href="${ctx}/account/user.action">帐号列表</a></li>
			<li><a href="${ctx}/account/role.action">角色列表</a></li>
			<li><a href="${ctx}/j_spring_security_logout">退出登录</a></li>
		</ul>
	</div>
</div>