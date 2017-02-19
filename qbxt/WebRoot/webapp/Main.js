// 填充图片的本地引用  ExtJs 3.4
// Ext.BLANK_IMAGE_URL = 'extjs/resources/images/default/s.gif';

// 配置类路径，动态加载JS文件
// 或者用setPath设置匹配路径，Ext.Loader.setPath('App.ux', 'lib'); 
Ext.Loader.setConfig({enabled: true,
	paths: {//'类名前缀':'所在路径'
		'Ushine': 'webapp'
	}
});

//通过匹配会自动加载JS文件'webapp/Menu.js'
Ext.require([
 	'Ushine.base.TopFrame',
	'Ushine.base.CenterFrame',
	'Ushine.base.Menu',
	'Ushine.base.TitleBar',
	'Ushine.buttons.Button',
	'Ushine.buttons.IconButton',
	'Ushine.buttons.OperButton',
	'Ushine.buttons.MiniButton',
	'Ushine.win.Window',
	'Ushine.system.Operations',
	'Ushine.system.OrganizManage',
	'Ushine.system.RoleManage',
	'Ushine.system.log.Log',
	'Ushine.system.SystemStatus',
	'Ushine.system.log.LogGridPanel',
	'Ushine.infoTypeManage.InfoTypeManage',
	'Ushine.infoTypeManage.InfoTypeSet',
	'Ushine.infoTypeManage.InfoTypeSetGridPanel',
	'Ushine.utils.CluesTempDataGridPanel',
	'Ushine.cluesInfo.CluePerson',
	'Ushine.cluesInfo.CluePersonInfoGridPanel',
	'Ushine.utils.PersonStoreFileGridPanel',
	'Ushine.personInfo.PersonInfos',
	'Ushine.personInfo.PersonInfoManage',
	'Ushine.docInfo.MediaNetworkBook',
	'Ushine.docInfo.MediaNetworkGridPanel',
	'Ushine.docInfo.ServiceDocGridPanel',
	'Ushine.docInfo.ServiceDocument',
	'Ushine.docInfo.ForeignDocGridPanel',
	'Ushine.docInfo.ForeignDocument',
	'Ushine.docInfo.ForeignDocFileGridPanel',
	'Ushine.docInfo.LeaderTalkGridPanel',
	'Ushine.docInfo.LeaderTalk',
	'Ushine.docInfo.LeadSpeakStoreFileGridPanel',
	'Ushine.docInfo.ServiceDocStoreFileGridPanel',
	'Ushine.personInfo.PersonInfoGridPanel',
	'Ushine.cluesInfo.CluesPenel',
	'Ushine.utils.SelectExistingDataGridPanel',
	'Ushine.cluesInfo.CluesGridPanel',
	'Ushine.dataSearch.DataSearchPenel',
	'Ushine.dataSearch.DataSearchPenelGridPanel',
	'Ushine.dataSearch.PersonStoreSearch',
	'Ushine.dataSearch.PersonStoreSearchGridPanel',
	'Ushine.dataSearch.OutsideDocStoreSearch',
	'Ushine.dataSearch.OutsideDocStoreSearchGridPanel',
	'Ushine.dataSearch.VocationalDocStoreSearch',
	'Ushine.dataSearch.VocationalDocStoreSearchGridPanel',
	'Ushine.dataSearch.LeadSpeakStoreSearch',
	'Ushine.dataSearch.LeadSpeakStoreSearchGridPanel',
	'Ushine.dataSearch.ClueStoreSearch',
	'Ushine.dataSearch.ClueStoreSearchGridPanel',
	//数据搜索
	'Ushine.storesearch.PersonStoreGridPanel',
	'Ushine.storesearch.ServiceDocStoreGridPanel',
	'Ushine.storesearch.OutsideDocStoreGridPanel',
	'Ushine.storesearch.LeadSpeakStoreGridPanel',
	'Ushine.storesearch.ClueStoreGridPanel'
]);

//创建命名空间
Ext.namespace('Ushine');
//创建应用程序
Ushine.Main = function() {
	//监听所有的ajax请求
	Ext.Ajax.on('requestcomplete', function(conn,response,options){
		 var result = Ext.JSON.decode(response.responseText);
		 if(result.status==-999){
			 notLogin(result);
		 }
	} , this);
	Ext.getDoc().on("contextmenu", function(e){
        e.stopEvent();
    });
	return {
		// 初始化应用程序, 创建viewport, 并采用border布局
		init: function() {
			Ext.QuickTips.init();
			Ext.Ajax.request({
			    url: 'getPerson.do',
			    success: function(response,request){
			    	var text = response.responseText;
    			    var obj=Ext.JSON.decode(text);
    			    Ext.util.Cookies.set('orgAdd', obj.orgAdd);
    			    Ext.util.Cookies.set('code', obj.code);
					Ext.viewport = new Ext.container.Viewport({
						layout: 'border', // 边界布局
						baseCls: 'blue-body',
						//autoScroll:true, 
						//minHeight:955,
						//minWidth:1920,
						autoScroll:false,
						items: [
							new Ushine.base.TopFrame(obj), 
							Ext.create('Ushine.base.CenterFrame'), 
						{
							// 下边的版本信息Panel
							id: 'bottom_frame',
							region: 'south',
							xtype: 'toolbar',
							baseCls: 'blue-body',
							height: 30,
							border: false,
							items:[{
								style: 'margin: 0px 0px 0px 80px; color:#f5f5f5',
								xtype:'label',
								text:'本次登录IP：'+obj.ip
							},'->',{
								style: 'color:#f5f5f5;',
								xtype:'label',
								text:'Version：'+obj.version
							}]
						}],
						listeners: {
							afterrender: function(){
								//Ext.create('Ushine.message.MessageBox', {
									//id: 'messageBox',
									//renderTo: 'system_message_window'
								//});
							},
							call_data: function(category, id){
								//console.log('Call data: category='+category+' id='+id);
								Ext.getCmp('content_frame').removeAll();
								if(category==1){
									Ext.getCmp('content_frame').add(new Ushine.suspicion.SuspicionBoth(id));
								}else if(category==2){
									Ext.getCmp('content_frame').add(new Ushine.depcontro.DeployBoth(id));
								}else if(category==4){
									Ext.getCmp('content_frame').add(new Ushine.middleTable.MiddleTable(id));
								}else if(category==5){
									Ext.getCmp('content_frame').add(new Ushine.export.ExportTableManage());
								}else{
									Ext.getCmp('content_frame').add(new Ushine.analysis.FlowView(null,null,null,id));
								}
							}
						}
					});
    			     
			    },
                failure: function(form, request) {
                	Ext.create('Ushine.utils.Msg').onWarn('服务器连接失败，请联系管理员');
                }
			});
		}
		
	}; //*** RETURN END ***/
}();

/**
 * Session验证
 */


/**
 * 没有登录跳转到登录页面
 */
function notLogin(obj) {
	Ext.MessageBox.show({
		title: '提示信息',
		msg: obj.msg,
		buttons: Ext.MessageBox.OK,
		icon: Ext.MessageBox.ERROR,
		fn: function() {
			location.href="./login.jsp";
		}
	});
}