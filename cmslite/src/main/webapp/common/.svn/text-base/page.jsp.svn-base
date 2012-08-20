<%@ page language="java" pageEncoding="UTF-8" %>
 <script type="text/javascript">
		   function pageselectCallback(page_id, jq){
			    jumpPage(parseInt(page_id)+1,"${page.pageSize}");
            }
            $(document).ready(function(){
                $("#Pagination").pagination('<c:if test="${page.totalCount==0}">1</c:if> <c:if test="${page.totalCount!=0}">${page.totalCount}</c:if>', {
				    first_text:"首页",
				    prev_text:"上一页",
		            next_text:"下一页",
					last_text:"尾页",
		            ellipse_text:"...",
		            items_per_page:"${page.pageSize}",
		            current_page:parseInt("${page.pageNo-1}"),
					num_edge_entries: 1,
					num_display_entries: 10,
					link_to:"#",
                    callback: pageselectCallback
                });
            });
       </script>
		    <a href="#">共${page.totalPages}页  共${page.totalCount}条</a>
		    <a href="javascript:jumpPage(0,10);" <c:if test="${page.pageSize==10}">class="current" </c:if>>10</a>
		    <a href="javascript:jumpPage(0,20);" <c:if test="${page.pageSize==20}">class="current" </c:if>>20</a>
		    <a href="javascript:jumpPage(0,50);" <c:if test="${page.pageSize==50}">class="current" </c:if>>50</a>
		    <div id="Pagination"></div>

