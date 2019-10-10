$(document).ready(function () {

    initTable(1)
});

var elementID = '#view_allDatasource'
var url_change = '/datasuorce/getAllByBeginNumber'
var sidePagination_change = 'server'
var method_change = "get"
var queryParames_change = queryParamsByBegin

//修改
var globle_selected_id = new Array()

var flag = true;
var global_id;

var table_data;

$("#screen").click(function () {

    $("#view_allDatasource").bootstrapTable("destroy")
    $("#view_screenDatasource").css("display","block")

    elementID = '#view_screenDatasource'
    url_change = "/datasuorce/search"
    sidePagination_change = 'client'
    method_change = "POST"
    queryParames_change = screen_search
    initTable(1)
})

$("#abandon_screen").click(function () {
    $("#search_select").val("default")
    $("#search_input").val("")

    $("#view_screenDatasource").bootstrapTable("destroy")
//    $("#view_screenDatasource").css("display","none");

    $("#view_allDatasource").css("display","block");


    elementID = '#view_allDatasource'
    url_change = '/datasuorce/getAllByBeginNumber'
    sidePagination_change = 'server'
    method_change = "get"
    queryParames_change = queryParamsByBegin
    initTable(1)
})

function initTable(page){
    $(elementID).bootstrapTable('destroy');
    $(elementID).bootstrapTable({
        method: method_change,
        // 若以post方式提交，需要补充contentType信息，否则分页参数无法传递到controller
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        // 查询传入起始页码数，后台需要计算起始查询条数
        url :url_change,
        cache : false,
        striped: true, //是否显示行间隔色
        dataField: "rows",//controller返回json数据对应key
        pageNumber: page, //初始化加载第一页，默认第一页
        pagination:true,//是否分页
        pageSize : 10,
        pageList : [ 10, 25, 50 ],
        sidePagination: sidePagination_change, //分页方式：client客户端分页，server服务端分页（*）
        queryParamsType:'', // 设置为 ''  在这种情况下传给服务器的参数为：pageSize,pageNumber
        // 直接把pageSize,pageNumber，调用queryParamsByBegin函数
        queryParams:queryParames_change,
        clickToSelect: false,                //是否启用点击选中行
        uniqueId: "id",
        columns :	 [{
            field: 'checkbox',
            checkbox: true,
            visible:true,
            class:'check',
            //修改
            formatter: function(value, row, index) {
                for(i = 0;i<globle_selected_id.length;i++){
                    if(row.id == globle_selected_id[i]){
                        return {
                            disabled : false,//设置是否可用
                            checked : true//设置选中
                        };
                    }
                }
                return value;
            }

        } ,{
            field: 'id',
            title: "id",
            visible: true,
            class: 'id'
        },{
            field: 'userId',
            title: "创建者ID",
            visible: true
        }, {
            field: 'type',
            title: "数据源类型",
            visible: true
        }, {
            field: 'createtime',
            title: "创建时间",
            visible: true,
            formatter: function (value, row, index) {
                if(value){
                    return dateFormat(row.createtime,dateformat);
                }else {
                    return "-";
                }

            }
        }, {
            field: 'updatetime',
            title: "修改时间",
            visible: true,
            formatter: function (value, row, index) {
                if (value){
                    return dateFormat(row.updatetime,dateformat);
                } else {
                    return "-";
                }

            }
        }, {
            field: 'databaseName',
            title: "数据库名称",
            visible: true,
            class: 'editable'
        }, {
            field: 'alias',
            title: "数据源名称",
            visible: true,
            class: 'editable'
        }, {
            field: 'driver',
            title: "驱动名称",
            visible: true,
            class: 'editable'
        }, {
            field: 'url',
            title: "数据源URL",
            visible: true,
            class: 'redis_editable'
        }, {
            field: 'port',
            title: "端口号",
            visible: true,
            class: 'redis_editable'
        }, {
            field: 'auth',
            title: "认证方式",
            visible: true,
            class: 'editable'
        }, {
            field: 'username',
            title: "用户名",
            visible: true,
            class: 'redis_editable'
        }, {
            field: 'password',
            title: "密码",
            visible: true,
            class: 'redis_editable',
            formatter: function (value, row, index) {
                if (value) {
                    return value;
                } else {
                    if(row.type == 'csv' || row.type == 'excel'){
                        return '-'
                    }else{
                        return "****************"
                    }

                }
            }
        }, {
            field: 'description',
            title: "备注",
            visible: true,
            class: 'editable'
        }, {
            field: 'encode',
            title: "数据库编码格式",
            visible: true,

        }, {
            field: 'handle',
            title: "操作",
            class: 'editOperate',
            visible: true,
            formatter: function (value, row, index) {
                if (value) {
                    return value;
                } else {
                    return '<button type="button" class="btn btn-danger" id="upd_btn">修改</button>'+' <button type="button" class="btn btn-danger" id="add_btn">入库</button>'+' <button type="button" class="btn btn-danger" id="del_btn">删除</button>';
                }
            },
            events: {               // 编辑按钮组事件
                'click #upd_btn': function (event, value, row, index) {
                //    alert(11111)

                    $.ajax({
                        type:"GET",
                        url:"/TUser/updPermission",
                        contentType:"application/json;charset=utf-8",
                        async:false,
                        success:function(result){
                            if(result.code==1){
                                if("csv" == row.type ){
                                    //csv 修改
                                    //row.id
                                    global_id=row.id;

                                    $("#csv_update_import").modal("show")



                                }else if("mysql" == row.type){
                                    //mysql 修改
                                    global_id=row.id;

                                    $("#mysql-config1").modal("show");

                                    $.ajax({
                                        type:"GET",
                                        url:"/mysql/mysqlFind/"+global_id,
                                        contentType:"application/json;charset=utf-8",
                                        dataType:"json",
                                        async:false,
                                        success:function(result){
                                            $("#username1").val(result.username);
                                            $("#port1").val(result.port);
                                            $("#database-name1").val(result.database_name);
                                            $("#host1").val(result.ip);
                                            $("#password1").val(result.password);

                                        }
                                    });//ajax


                                }else if("redis" == row.type){
                                    //redis 修改
                                    //row.id
                                    global_id=row.id;
                                    flag= editRedisRow(index,row.id,flag)



                                }else if("excel" == row.type){
                                    //excel 修改
                                    //row.id
                                    global_id=row.id;
                                    $("#excel_import1").modal("show");
                                    json_id["id"]=global_id;
                                    //excel上传修改操作
                                    excel_load1("#excel_upload1");

                                }
                            }
                        },
                        error:function () {
                            alert("该账户不具有修改数据的权限！");
                        }
                    });



                },
                'click #add_btn': function (event, value, row, index) {
                    $.ajax({
                        type:"GET",
                        url:"/TUser/addPermission",
                        contentType:"application/json;charset=utf-8",
                        async:false,
                        success:function(result){
                            if(result.code==1){
                                if("csv" == row.type ){
                                    //csv 入库
                                    global_id=row.id;
                                    csv_insertdata_submit();

                                }else if("mysql" == row.type){
                                    //mysql 入库
                                    global_id=row.id;

                                    $("#mysql_oper").modal("show");
                                    mysqlFindTables();

                                }else if("redis" == row.type){
                                    //redis 入库
                                    global_id=row.id;
                                    $("#redis_database").modal("show")
                                    //用表格显示redis非空库号
                                    get_redisbase_table()


                                }else if("excel" == row.type){
                                    //excel 入库
                                    global_id=row.id;
                                    excel_enter();
                                }
                            }
                        },
                        error:function () {
                            alert("该账户不具有入库的权限！");
                        }
                    });


                },
                'click #del_btn': function (event, value, row, index) {
                    $.ajax({
                        type:"GET",
                        url:"/TUser/delPermission",
                        contentType:"application/json;charset=utf-8",
                        async:false,
                        success:function(result){
                            if(result.code==1){
                                $.ajax({
                                    type:"POST",
                                    url:"/datasuorce/delTDatasourceById/"+row.id,
                                    contentType:"application/json;charset=utf-8",
                                    async:false,
                                    success:function(data){
                                        if(data){
                                            initTable($(elementID).bootstrapTable('getOptions').pageNumber)
                                            //  $(elementID).bootstrapTable("refresh")
                                        }else{
                                            alert("删除失败")
                                        }

                                    }
                                });
                            }
                        },
                        error:function () {
                            alert("该账户不具有删除数据的权限！");
                        }
                    });

                }
            },

        }],
        //修改
        onCheck: function(row){
            globle_selected_id.push(row.id)
            //   console.log("增加"+globle_selected_id)
        },
        onUncheck: function (row) {
            var index = globle_selected_id.indexOf(row.id)
            //   console.log("index:"+index)
            globle_selected_id.splice(index,1)
            //  console.log("删除"+globle_selected_id)
        },
        onCheckAll: function (rows) {
            for(i=0;i<rows.length;i++){
                globle_selected_id.push(rows[i].id)
            }
        },
        onUncheckAll: function (rows) {
            for(i=0;i<rows.length;i++){
                let index = globle_selected_id.indexOf(rows[i].id)
                globle_selected_id.splice(index, 1)
            }

        },
        onLoadSuccess: function (data) {
            // console.log(data.length!=0)
            table_data = $(elementID).bootstrapTable('getData')
            if ((table_data == '' || table_data == null )&& data.length!=0) {
                initTable($(elementID).bootstrapTable('getOptions').pageNumber-1)
            }
        }



    });
// 隐藏主键显示
    $(elementID).bootstrapTable('hideColumn', 'innerId');
   }

// 以起始页码方式传入参数,params为table组装参数
function queryParamsByBegin(params){
 //   console.log(2222222222222)
    return{
        pageSize: params.pageSize,
        pageNumber: params.pageNumber
    }
}

function screen_search(params){//向后台传值
    let search_select = $("#search_select").val()
    if(search_select == "default"){
        search_select=""
    }
    var temp= {
        type: search_select,
        content: $("#search_input").val()
    };
    console.log(temp)
    return temp;
}

let formerbutton = `
    <button class='btn btn-danger' id='upd_btn'>修改</button>
    <button class='btn btn-danger' id='add_btn'>入库</button>
    <button class='btn btn-danger' id='del_btn'>删除</button>
`

let editFormatter = ` 
	<button class = 'btn btn-success' id='save'> 保存</button>
    <button class='btn btn-info' id='abandon'>取消</button > 
`




var datasource;
var code
//开启编辑
function editRedisRow(index,id,ff) {
    // console.log("进入edit方法时："+flag)
    if(ff){
        flag = false
        var url_value = $(elementID).bootstrapTable('getRowByUniqueId', id).url;
        var port_value = $(elementID).bootstrapTable('getRowByUniqueId', id).port;
        var username_value = $(elementID).bootstrapTable('getRowByUniqueId', id).username;
        var psw_value = $(elementID).bootstrapTable('getRowByUniqueId', id).password;
        $(elementID).bootstrapTable('updateCell', {
            index: index,
            field: "url", // 字段名称
            value: "<input id='url_input"+id+"' type='text' value='" + url_value + "'>"  // 新的值
        });

        $(elementID).bootstrapTable('updateCell', {
            index: index,
            field: "port", // 字段名称
            value: "<input id='port_input"+id+"' type='text' value='" + port_value + "'>"  // 新的值
        });

        $(elementID).bootstrapTable('updateCell', {
            index: index,
            field: "username", // 字段名称
            value: "<input id='username_input"+id+"' type='text' value='" + username_value + "'>"  // 新的值
        });

        $(elementID).bootstrapTable('updateCell', {
            index: index,
            field: "password", // 字段名称
            value: "<input id='password_input"+id+"' type='password' value='****************'>"  // 新的值
        });

        $(elementID).bootstrapTable('updateCell', {
            index: index,
            field: "handle", // 字段名称
            value: editFormatter  // 新的值
        });


        $("#username_input"+id).blur(function () {
            //改变用户名，清空密码
            if($("#username_input"+id).val() != username_value){
                $("#password_input"+id).val("")
            }
        })


        $("#save").click(function () {
            //  alert(111)
            url_input = $("#url_input"+id).val()
            port_input = $("#port_input"+id).val()
            username_input = $("#username_input"+id).val()
            password_input = $("#password_input"+id).val()

            flag = true


            if(password_input == "****************"){
                datasource = {
                    id:id,
                    url:url_input,
                    port:port_input,
                    username:username_input,
                }
            }else{
                datasource = {
                    id:id,
                    url:url_input,
                    port:port_input,
                    username:username_input,
                    password:password_input,
                }
            }

            redis_updatedata()

            if(code==0){
                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "url", // 字段名称
                    value:url_input // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "port", // 字段名称
                    value:port_input  // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "username", // 字段名称
                    value: username_input  // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "password", // 字段名称
                    value: "****************"  // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "handle", // 字段名称
                    value: formerbutton  // 新的值
                });


                $.ajax({
                    type:"POST",
                    url:"/datasuorce/updTDatasurceById",
                    data:JSON.stringify(datasource),
                    contentType:"application/json;charset=utf-8",
                    async: false,
                    dataType: 'json',
                    success:function(data){
                        if(data){
                            //    alert("修改成功")
                            initTable($(elementID).bootstrapTable('getOptions').pageNumber)
                            if($(elementID).bootstrapTable('getData') == '' || $(elementID).bootstrapTable('getData') == null){
                                $(elementID).bootstrapTable('prevPage')
                            }
                        }else {
                            alert("修改失败")
                        }
                        //   console.log(data)
                    }
                });
            }else{
                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "url", // 字段名称
                    value:url_value // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "port", // 字段名称
                    value:port_value  // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "username", // 字段名称
                    value: username_value  // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "password", // 字段名称
                    value: "****************"  // 新的值
                });

                $(elementID).bootstrapTable('updateCell', {
                    index: index,
                    field: "handle", // 字段名称
                    value: formerbutton  // 新的值
                });
            }




        })

        $("#abandon").click(function () {
            flag = true
            $(elementID).bootstrapTable('updateCell', {
                index: index,
                field: "url", // 字段名称
                value: url_value // 新的值
            });

            $(elementID).bootstrapTable('updateCell', {
                index: index,
                field: "port", // 字段名称
                value: port_value  // 新的值
            });

            $(elementID).bootstrapTable('updateCell', {
                index: index,
                field: "username", // 字段名称
                value: username_value  // 新的值
            });

            $(elementID).bootstrapTable('updateCell', {
                index: index,
                field: "password", // 字段名称
                value: psw_value  // 新的值
            });

            $(elementID).bootstrapTable('updateCell', {
                index: index,
                field: "handle", // 字段名称
                value: formerbutton  // 新的值
            });
        })



    }
    return flag;

}

$("#batch_del").click(function () {
    //修改
    $.ajax({
        type:"GET",
        url:"/TUser/delPermission",
        contentType:"application/json;charset=utf-8",
        async:false,
        success:function(result){
            if(result.code==1){
                if (globle_selected_id.length > 0) {
                    $.ajax({
                        type:"POST",
                        url:"/datasuorce/batchDelById",
                        data: JSON.stringify(globle_selected_id),
                        traditional: true,  //解决传递数组的问题
                        async: false,
                        dataType: 'json',
                        contentType:"application/json;charset=utf-8",
                        success:function(data){
                            if (data) {
                                initTable($(elementID).bootstrapTable('getOptions').pageNumber)
                                globle_selected_id.length = 0
                            }else{
                                alert("删除失败，请稍后重试")
                            }
                        }
                    });
                }
                else {
                    alert("请至少选择一个数据源");
                }
            }
        },
        error:function () {
            alert("该账户不具有批量删除的权限！");
        }
    });


})

$("#datasource_back").click(function () {

    initTable($(elementID).bootstrapTable('getOptions').pageNumber)
 //   $(elementID).bootstrapTable('selectPage',parseInt($(elementID).bootstrapTable().totalPages))
   // initTable( $(elementID).bootstrapTable('selectPage',parseInt($(elementID).bootstrapTable().totalPages)))
  //  $(elementID).bootstrapTable('selectPage',parseInt($(elementID).bootstrapTable().totalPages))


})







