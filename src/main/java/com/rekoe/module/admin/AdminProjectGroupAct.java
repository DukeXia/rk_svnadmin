package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.integration.shiro.annotation.NutzRequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.PjGr;
import com.rekoe.module.BaseAction;
import com.rekoe.service.ProjectGroupService;

@IocBean
@At("/admin/project/group")
public class AdminProjectGroupAct extends BaseAction {

	@Inject
	private ProjectGroupService projectGroupService;

	@At
	@Ok("fm:template.admin.project_group.list")
	@NutzRequiresPermissions(value = "project.group:view", name = "SVN浏览账号", tag = "SVN账号管理", enable = true)
	public Pagination list(@Param(value = "pageNumber", df = "1") Integer page, @Param("pj") String pj, HttpServletRequest req) {
		req.setAttribute("pj", pj);
		return projectGroupService.getObjListByPager(page, 20, Cnd.where("pj", "=", pj));
	}

	@At
	@Ok("fm:template.admin.project_group.add")
	@NutzRequiresPermissions(value = "project.group:add", name = "添加项目用户组", tag = "SVN账号管理", enable = true)
	public String add(@Param("pj") String pj) {
		return pj;
	}

	@At
	@Ok("json")
	@RequiresPermissions("project.group:add")
	public Message o_save(@Param("::pgu.") PjGr group, HttpServletRequest req) {
		PjGr old = projectGroupService.fetch(Cnd.where("pj", "=", group.getPj()).and("gr", "=", group.getGr()));
		if (Lang.isEmpty(old)) {
			projectGroupService.insert(group);
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}

	@At
	@Ok("json")
	@NutzRequiresPermissions(value = "project.group:delete", name = "删除项目用户组", tag = "SVN账号管理", enable = true)
	public Message delete(@Param("pj") String pj, @Param("gr") String gr, HttpServletRequest req) {
		projectGroupService.delete(pj, gr);
		return Message.success("ok", req);
	}
}
