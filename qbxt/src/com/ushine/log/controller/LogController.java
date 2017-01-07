package com.ushine.log.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hy.log_manageClient.model.PojoViewLogModel;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.log.service.ILogManageService;
import com.ushine.log.utils.StringUtils;

/**
 * 日志记录控制器
 * 
 * @author leibin
 * 
 */
@Controller
public class LogController {
	private static Log log = LogFactory.getLog(LogController.class);

	@Autowired
	public ILogManageService logManageService;
	
	@RequestMapping(value = "/findLog", method = RequestMethod.GET)
	public @ResponseBody
	String findLog(@RequestParam("page") int nextPage,@RequestParam int start,@RequestParam("limit") int size, @RequestParam String userName,
			@RequestParam String userCode, @RequestParam String IP,
			@RequestParam String runTime, @RequestParam String endTime,
			@RequestParam String type,HttpServletRequest request) {
		log.debug("进入日志记录控制器 findLog方法");
		try {
			//日志查看权限
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			List<String> list = sessionMgr.getPermitResOperCode(request);
			ViewObject vo = new ViewObject(ViewObject.RET_FAILURE,
					"您没有对该资源操作的权限.");
			if (list != null && list.size() > 0) {
				if (!"0x0000".equals(list.get(0))) {
					PojoViewLogModel logModel = new PojoViewLogModel();
					logModel.setNextPage(nextPage);
					logModel.setUserName(StringUtils.isNull(userName)==true?null:new String(userName.getBytes("iso8859-1"),"utf-8"));
					logModel.setUserCode(StringUtils.isNull(userCode)==true?null:new String(userCode.getBytes("iso8859-1"),"utf-8"));
					//暂时用IP字段存参数
					logModel.setIP(StringUtils.isNull(IP)==true?null:new String(IP.getBytes("iso8859-1"),"utf-8"));
					logModel.setType(StringUtils.isNull(type)==true?null:new String(type.getBytes("iso8859-1"),"utf-8"));
					runTime = runTime.replace("T", " ");
					endTime = endTime.replace("T", " ");
					endTime = endTime.replace("00:00:00", "23:59:59");
					logModel.setRunTime(runTime);
					logModel.setEndTime(endTime);
					logModel.setNextPage(nextPage);
					logModel.setNum(size);
					return logManageService.queryLog(logModel);
				}else{
					return vo.toJSon();
				}
			}else{
				return vo.toJSon();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public ILogManageService getLogManageService() {
		return logManageService;
	}
	public void setLogManageService(ILogManageService logManageService) {
		this.logManageService = logManageService;
	}
}
