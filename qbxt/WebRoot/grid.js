Ext.onReady(function(){
	Ext.define('person',{
		extend:'Ext.data.Model',
		fields:['id','name','infoType']
	})
	Ext.create('Ext.grid.Panel',{
		renderTo:'div1',
		width:500,
		id:'grid1',
		height:300,
		bbar: [
		  { xtype: 'button', text: 'Button 1' }
		],
		columns:[
			{text:'id',dataIndex:'id'},
			{text:'姓名',dataIndex:'name'},
			{text:'类别',dataIndex:'infoType'}
		],
		store:new Ext.data.Store({
			model:'person',
			data:[
				{id:'1',name:'董昊',infoType:'1'},
				{id:'2',name:'王总',infoType:'2'},
				{id:'3',name:'熊猫',infoType:'3'}
			]
		})
	})
	
	Ext.create('Ext.grid.Panel',{
		renderTo:'div2',
		width:500,
		id:'grid2',
		height:300,
		bbar: [{ xtype: 'button', text: '添加' ,handler:function(){
			var grid2=Ext.getCmp('grid2');
			var grid1=Ext.getCmp('grid1');
			var data;
			//把2的数据添加到1中
			grid2.getStore().each(function(record){
				console.log(record);
				var person=Ext.create('person',{
					id:record.get('id'),
					name:record.get('name'),
					infoType:record.get('infoType')
				});
				grid1.getStore().add(person);
			});
		}
		}],
		columns:[
			{text:'id',dataIndex:'id'},
			{text:'姓名',dataIndex:'name'},
			{text:'类别',dataIndex:'infoType'}
		],
		store:new Ext.data.Store({
			model:'person',
			data:[
				{id:'1',name:'董昊',infoType:'1'},
				{id:'2',name:'王总',infoType:'2'},
				{id:'3',name:'熊猫',infoType:'3'}
			]
		})
	})	
})