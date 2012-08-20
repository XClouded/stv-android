package cn.com.supertv.cmslite.entity.cms;

public enum MediaStateEnum {
	EDITABLE, CENSORING, INGESTABLE, INJECTION, INGESTED, ZOMBIE;
	public String getLabel() {
		switch (this) {
		case EDITABLE:
			return "编辑";
		case CENSORING:
			return "待审核";
		case INGESTABLE:
			return "待发布";
		case INJECTION:
			return "注入中";
		case INGESTED:
			return "已发布";
		case ZOMBIE:
			return "已删除";
		}
		return super.toString();
	}
}
