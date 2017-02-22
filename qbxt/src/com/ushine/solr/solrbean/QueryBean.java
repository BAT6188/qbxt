package com.ushine.solr.solrbean;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

import com.ushine.solr.util.SolrDateUtils;

/**
 * 查询语句的bean对象
 * @author Administrator
 *
 */
public class QueryBean {
	private static Logger logger=Logger.getLogger(QueryBean.class);
	
	/**
	 * id属性常量
	 */
	public static final String ID="id";
	/**
	 * 所属类别字段常量infoType
	 */
	public static final String INFOTYPE="infoType";
	/**
	 * 人员solr的id
	 */
	public static final String PERSON_ID = "personId";
	/**
	 * 人员默认的查询字段
	 */
	public static final String PERSONSTOREALL = "personstoreAll";
	/**
	 * 业务文档solr的id
	 */
	public static final String VOCATIONALWORK_ID = "vocationalWorkStoreId";
	/**
	 * 业务文档库默认的查询字段
	 */
	public static final String VOCATIONALWORKSTOREALL = "vocationalWorkStoreAll";
	/**
	 * 外来文档solr索引主键字段outsideDocId
	 */
	public static final String OUTSIDEDOC_ID="outsideDocId";
	/**
	 * 外来文档默认的查询字段
	 */
	public static final String OUTSIDEDOCALL="outsidedocstoreAll";
	
	/**
	 * 常量字段createDate
	 */
	public static final String CREAT_EDATE = "createDate";
	/**
	 * 高亮前缀
	 */
	public static final String HIGHLIGHT_PRE="<span style='background-color:#ffd73a'>";
	/**
	 * 高亮后缀
	 */
	public static final String HIGHLIGHT_POST="</span>";
	
	/**
	 * 双引号，精确查询使用
	 */
	public static final String QUOTATION_MARK="\"";
	/**
	 * 无参私有化
	 */
	private QueryBean(){
		
	}
	/**
	 * 默认构造函数
	 * @param uid uid
	 * @param oid oid
	 * @param did did
	 * @param queryField 待查询字段，null为查询所有
	 * @param queryFieldValue 待查询字段的值
	 * @param againQueryField 进行再查询的字段，null为不进行再查询
	 * @param againQueryFieldValue 再查询字段的值
	 * @param sortField 待排序字段，null以及默认都是createDate
	 * @param sortDirection 排序方向，null以及默认都是降序
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 */
	public QueryBean(String uid, String oid, String did, String queryField, String queryFieldValue,
			String againQueryField, String againQueryFieldValue,String sortField,String sortDirection, String startDate, String endDate) {
		this.uid = uid;
		this.oid = oid;
		this.did = did;
		this.queryField = queryField;
		this.queryFieldValue = queryFieldValue;
		this.againQueryField = againQueryField;
		this.againQueryFieldValue = againQueryFieldValue;
		this.startDate = startDate;
		this.endDate = endDate;
		this.sortField=sortField;
		this.sortDirection=sortDirection;
	}
	
	private String uid;
	private String oid;
	private String did;
	private String queryField;
	private String queryFieldValue;
	private String againQueryField;
	private String againQueryFieldValue;
	private String startDate;
	private String endDate;
	private String sortField;
	private String sortDirection;

	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getQueryField() {
		return queryField;
	}

	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}

	public String getQueryFieldValue() {
		return queryFieldValue;
	}

	public void setQueryFieldValue(String queryFieldValue) {
		this.queryFieldValue = queryFieldValue;
	}

	public String getAgainQueryField() {
		return againQueryField;
	}

	public void setAgainQueryField(String againQueryField) {
		this.againQueryField = againQueryField;
	}

	public String getAgainQueryFieldValue() {
		return againQueryFieldValue;
	}

	public void setAgainQueryFieldValue(String againQueryFieldValue) {
		this.againQueryFieldValue = againQueryFieldValue;
	}
	/**
	 * 返回solr的查询语句
	 * @param clazz dao层的对象
	 * @return SolrQuery对象
	 */
	public SolrQuery getSolrQuery(Class clazz){
		SolrQuery query=new SolrQuery(initQuery(clazz));
		//排序字段
		if(null==sortField){
			sortField=CREAT_EDATE;
		}
		
		if (StringUtils.equalsIgnoreCase(sortDirection, "asc")) {
			query.addSort(sortField, ORDER.asc);
		}else {
			//降序
			query.addSort(sortField, ORDER.desc);
		}
		
		logger.info(clazz.getSimpleName()+"排序字段："+sortField);
		//设置过滤条件，针对权限
		if (null == uid && null == oid && null != did) {
			// 读取所属部门
			query.setFilterQueries("did:\""+did+"\"");
		}
		if (null == did && null == uid && null != oid) {
			// 读取所属组织
			query.setFilterQueries("oid:\""+oid+"\"");
		}
		if (null == oid && null == did && null != uid) {
			// 读取个人数据
			query.setFilterQueries("uid:\""+uid+"\"");
		}
		logger.info(clazz.getSimpleName()+"完整的查询语句："+query.toString());
		return query;
	}
	/**
	 * 依据类库获得query语句用于查询
	 * @param clazz dao层对应的store
	 * @return String
	 */
	public String initQuery(Class clazz){
		StringBuffer queryBuffer=null;
		//开始和结束时间
		long startTime=SolrDateUtils.getStartTimeMillis(startDate);
		long endTime=SolrDateUtils.getEndTimeMillis(endDate);
		//String.format
		//空串
		switch (ClassUtils.getShortClassName(clazz)) {
		//人员库
		case "PersonStore":
			queryBuffer=initQueryBuffer(PERSONSTOREALL);
			break;
		//业务文档
		case "VocationalWorkStore":
			queryBuffer=initQueryBuffer(VOCATIONALWORKSTOREALL);
			break;
		}
		//时间范围
		queryBuffer.append(" AND ").append(String.format(CREAT_EDATE+":[%s TO %s]", startTime,endTime));
		//查询语句
		logger.info(clazz.getSimpleName()+"基本的查询语句："+queryBuffer.toString());
		return queryBuffer.toString();
	}
	/**
	 * 精确查询语句拼接
	 * @param copyField 每个solr库中的任意字段
	 * @return StringBuffer
	 */
	protected StringBuffer initQueryBuffer(String copyField){
		StringBuffer queryBuffer=new StringBuffer();
		//第一次查询
		getQueryBuffer(copyField, queryBuffer);
		//为第二次查询准备
		getAgainQueryBuffer(copyField, queryBuffer);
		return queryBuffer;
	}
	/**
	 * 拼接查询字段
	 * @param copyField 每个solr库中的任意字段
	 * @param queryBuffer StringBuffer
	 */
	private void getQueryBuffer(String copyField, StringBuffer queryBuffer) {
		if (StringUtils.isNotBlank(queryFieldValue)&&StringUtils.isNotBlank(queryField)) {
			//查询字段、查询的关键字都不是空串
			//精确查询
			queryBuffer.append(queryField).append(":").append(QUOTATION_MARK)
				.append(queryFieldValue).append(QUOTATION_MARK);
		}else if(StringUtils.isBlank(queryField)&&StringUtils.isNotBlank(queryFieldValue)){
			//查询内容不是空，但查询字段为空
			queryBuffer.append(copyField).append(":").append(QUOTATION_MARK)
				.append(queryFieldValue).append(QUOTATION_MARK);
		}else {
			//通配符
			queryBuffer.append(copyField).append(":").append("*");
		}
	}
	/**
	 * 拼接再查询
	 * @param copyField 全字段
	 * @param queryBuffer StringBuffer
	 */
	private void getAgainQueryBuffer(String copyField, StringBuffer queryBuffer) {
		if (StringUtils.isNotBlank(againQueryField)&&StringUtils.isNotBlank(againQueryFieldValue)) {
			//AND进行拼接，也是精确查询
			queryBuffer.append(" AND ").append(againQueryField).append(":")
				.append(QUOTATION_MARK).append(againQueryFieldValue).append(QUOTATION_MARK);
		}else if(StringUtils.isBlank(againQueryField)&&StringUtils.isNotBlank(againQueryFieldValue)){
			//关键字不是空但查询字段是空，全字段查询
			queryBuffer.append(" AND ").append(copyField).append(":")
				.append(QUOTATION_MARK).append(againQueryFieldValue).append(QUOTATION_MARK);
		}
		//其他情况不考虑
	}
}
