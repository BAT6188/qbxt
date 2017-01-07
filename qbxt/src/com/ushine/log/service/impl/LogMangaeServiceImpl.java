package com.ushine.log.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hy.log_manageClient.logging.Logger;
import com.hy.log_manageClient.model.PojoViewLogModel;
import com.ushine.log.service.ILogManageService;

/**
 * 日志操作实现类
 * 
 * @author leiBin
 * 
 */
@Service
@Transactional
public class LogMangaeServiceImpl implements ILogManageService {

	private static Log log = LogFactory.getLog(LogMangaeServiceImpl.class);

	@Transactional(readOnly = true)
	public String queryLog(PojoViewLogModel model) throws Exception {
		log.debug("查询日志记录");
		String str = Logger.getInstance().find(model);
//		PagingObject<PojoModel> pagingObject = new PagingObject<PojoModel>();
//		List<PojoModel> list = new ArrayList<PojoModel>();
//		Paging paging = null;
//		if (str != null) {
//			JSONObject object = JSONObject.fromObject(str);
//			//取得服务器响应回来的数据
//			Object pagingObj = object.get("paging");
//			
//			Object datasObj = object.get("datas");
//			//将服务器响应回来的数据转化成本地数据
//			paging = (Paging) JSONObject.toBean(JSONObject.fromObject(pagingObj), Paging.class);
//			
//			Object[] listDatasObj = (Object[]) JSONArray.toArray(JSONArray.fromObject(datasObj));
//			
//			for(Object pojoModelObj : listDatasObj){
//				PojoModel pojoModel = (PojoModel) JSONObject.toBean(JSONObject.fromObject(pojoModelObj), PojoModel.class);
//				list.add(pojoModel);
//			}
//			pagingObject.setPaging(paging);
//			pagingObject.setArray(list);
//		}
		return str;
	}

//	public String voToJSon(PagingObject<PojoModel> vo) {
//		JSONObject root = new JSONObject();
//		Paging paging = vo.getPaging();
//
//		for (PojoModel model : vo.getArray()) {
//			if (model.getType() == MyFinalUtils.LOG_OPERATION_TYPE_LOGIN) {
//				model.setTypeStr("登陆系统");
//			}
//			if (model.getType() == MyFinalUtils.LOG_OPERATION_TYPE_LOGOUT) {
//				model.setTypeStr("登出系统");
//			}
//			if (model.getType() == MyFinalUtils.LOG_OPERATION_TYPE_ADD) {
//				model.setTypeStr("新增记录");
//			}
//			if (model.getType() == MyFinalUtils.LOG_OPERATION_TYPE_DELETE) {
//				model.setTypeStr("删除记录");
//			}
//			if (model.getType() == MyFinalUtils.LOG_OPERATION_TYPE_QUERY) {
//				model.setTypeStr("查询记录");
//			}
//			if (model.getType() == MyFinalUtils.LOG_OPERATION_TYPE_UPDATE) {
//				model.setTypeStr("修改记录");
//			}
//		}
//		vo.setPaging(paging);
//		root.element("paging", vo.getPaging());
//
//		root.element("datas", JSONArray.fromObject(vo.getArray()));
//		return root.toString();
//	}
}
