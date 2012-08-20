package cn.com.supertv.cmslite.web.cms;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import cn.com.supertv.cmslite.entity.cms.Oplog;
import cn.com.supertv.cmslite.web.CrudActionSupport;
@Namespace("/cms")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "oplog.action", type = "redirect") })
public class OplogAction   extends CrudActionSupport<Oplog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Oplog getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
