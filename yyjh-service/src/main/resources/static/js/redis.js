$(document).ready(function() {

    csv_updatedata_submit()

    //选择数据源按钮弹窗
    db_btns = $("#showDrivers_choosen").find("div").find(".modal-body").find("button")

    $(db_btns[3]).click(function() {
        //REDIS弹窗
        $("#showDrivers_choosen").modal("hide")
        $("#redis_import").modal("show")

    });

    $("#redis-close").click(function () {
        $("#showDrivers_choosen").modal("show");
    })

    //连接测试
    $("#r_test").click(function() {
        redis_test()
    })


    //上传数据源
    $("#r_submit").click(function() {
        redis_load()
    })

    //在数据源列表里点击“入库”
    $("#redis_showdatabase").click(function() {
        $("#redis_database").modal("show")
        //用表格显示redis非空库号
        get_redisbase_table()
    })

    //选中库号后点击"执行入库"按钮
    $("#redis_insertdata_submit").click(function() {
        redis_insertdata_submit()

    })

    //点击redis修改按钮
    $("#redis_updatedata").click(function() {
        redis_updatedata()

    })

});


//redis连接测试
function redis_test(){
    redis_url = $("#redis_url").val()
    port = $("#redis_port").val()
    redis_password = $("#redis_password").val()
    username = $('#redis_username').val()

    var data = {
        "url": redis_url,
        "port": port,
        "password": redis_password,
        "username": username

    };


    if(redis_url==""||port==""||redis_password==""||username==""){
        alert("请输入完整连接信息！")
        return
    }

    $.ajax({
        type: "POST",
        url: "/redis/redisTest",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        dataType: "json",
        async: false,
        success: function(data) {

            if(data.code == 0) {
                alert("连接成功！")
                //清空redis数据源输入框
//               $("#url").val("")
//               $("#port").val("")
//               $("#password").val("")
//               $("#username").val("")

            } else {
                alert("连接失败！")
            }

        }

    })
}

//redis数据源上传
function redis_load() {

    redis_url = $("#redis_url").val()
    port = $("#redis_port").val()
    redis_password = $("#redis_password").val()
    username = $('#redis_username').val()

    if(redis_url==""||port==""||redis_password==""||username==""){
        alert("请输入完整连接信息！")
        return
    }


    var data = {
        "url": redis_url,
        "port": port,
        "password": redis_password,
        "username": username

    };

    $.ajax({
        type: "POST",
        url: "/redis/redisUpload",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        dataType: "json",
        async: false,
        success: function(data) {

            if(data.code == 0) {
                alert("上传成功！")

                //清空redis数据源输入框
                $("#redis_url").val("")
                $("#redis_port").val("")
                $("#redis_password").val("")
                $("#redis_username").val("")
                initTable($(elementID).bootstrapTable('getOptions').pageNumber)
                if($(elementID).bootstrapTable('getData') == '' || $(elementID).bootstrapTable('getData') == null){
                    $(elementID).bootstrapTable('prevPage')
                }


            }else if(data.code == -1){
                alert("redis连接失败！")
            }
            else {
                alert("数据重复,资源上传失败！")
            }

        }

    })

}

//获取非空库号
function get_redisbase_table() {
    // var data = {
    //     "url": "127.0.0.1",
    //     "port": 6379,
    //     "auth": "123456"
    //
    // };

    $.ajax({
        type: "GET",
        url: "/redis/getBaseNumber/"+global_id,
        dataType: "json",
        async: false,
        success: function(data) {

            show_redisbase_table(data.payload);

        }

    })

}

//用bootstrap table显示非空库号
function show_redisbase_table(base_number) {
    //所有非空库
    all_base_number = base_number.all_database
    //已存在的库
    exist_base_number = base_number.exist_database
    console.log(exist_base_number)

    //  var columns_data = []    //表头
    //  let col1 = new Object();
    //  col1.field = "is_insert"
    //  col1.visible = true
    //  col1.title = "<input type='checkbox' id='select_all' name='item'/>"+"是否入库"
    //  columns_data.push(col1)    //生成表头
    //
    //  let col2 = new Object();
    //  col2.field = "database_number"
    //  col2.visible = true
    //  col2.title = "库号"
    //  columns_data.push(col2)   //生成表头
    //
    //
    //表头
    var tableColumns = [{
        field: 'is_insert',
        title: "<input type='checkbox' id='select_all' onclick='selectAll()'  />" + "是否入库",
        sortable: true
    }, {
        field: 'database_number',
        title: '库号',
        sortable: true
    }];

    $('#redis_selectdatabase_table').bootstrapTable("destroy") //去掉所有行
    $('#redis_selectdatabase_table').bootstrapTable({
        striped: false, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, // 是否显示分页（*）
        sortable: false, // 是否启用排序
        sidePagination: "client", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 10, // 每页的记录行数（*）
        pageList: [10, 20, 30, 40], // 可供选择的每页的行数（*）
        search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, // 是否显示所有的列
        showRefresh: false, // 是否显示刷新按钮
        minimumCountColumns: 2, // 最少允许的列数
        clickToSelect: false, // 是否启用点击选中行
        showToggle: false, // 是否显示详细视图和列表视图的切换按钮
        cardView: false, // 是否显示详细视图
        detailView: false,
        columns: tableColumns
    })

    var datas = [] //表数据

    //生成表数据
    for(i = 0; i < all_base_number.length; i++) {
        let base = new Object();
        base.is_insert = "<input type='checkbox' id='select_" + all_base_number[i] + "' value='" + all_base_number[i] + "' name='chks'/>"
        base.database_number = all_base_number[i]

        for(j = 0; j < exist_base_number.length; j++) {
            if(exist_base_number[j] == all_base_number[i]) {

                base.is_insert = "<input type='checkbox' disabled=false  id='select_" + all_base_number[i] + "' value='" + all_base_number[i] + "' name='chks'/>" +
                    "<label style='color:red;font-size:12px'>已入库</label>"
                break
            }
        }

        datas.push(base)
    }

    $('#redis_selectdatabase_table').bootstrapTable("load", datas) //去掉所有行

}

var redis_url = "127.0.0.1"
var port = 6379
var auth = "123456"
var alias

//redis入库
function redis_insertdata_submit() {
    //选中的库号
    let database = ""
    //判断库是否被选中
    $.each($("[name='chks']"), function() {
        if(this.checked) {
            let id = $(this).val();
            database = database + id + ","
        }
    });
    database = database.substring(0, database.length - 1)
    alias = database
    if(alias == "") {
        alert("请选择库号！")
        return
    }

    data = {
        "url": redis_url,
        "port": port,
        "auth": auth,
        "alias": alias

    }

    $.ajax({
        type: "GET",
        url: "/redis/insertRedis/"+global_id+"/" + alias,
        dataType: "json",
        async: false,
        success: function(data) {
            if(data.code == 0) {

                alert("入库成功！")
                $("#redis_database").modal("hide")

            } else {
                alert("入库失败")
            }

        }

    })

}

//库号全选事件
function selectAll() {
    if($('#select_all').is(':checked')) {
        $("[name='chks']").prop("checked", true); //全选
        for(i = 0; i < exist_base_number.length; i++) {
            let select_id = 'select_' + exist_base_number[i]
            $("[id=" + select_id + "]").prop("checked", false);
        }

    } else {
        $("[name='chks']").prop("checked", false); //取消全选
    }
}

//redis数据源修改
function redis_updatedata() {
    // redis_datasource = {
    //     "url": "127.0.0.1",
    //     "port": 6378,
    //     "password": "123456",
    //     "username": "root",
    //     "id": 6
    // }

   redis_datasource = datasource

    $.ajax({
        type: "POST",
        url: "/redis/redisUpdate",

        data: {"redis_datasource":JSON.stringify(redis_datasource)},
        dataType: "json",
        async: false,
        success: function(data) {
            if(data.code == 0) {

                alert("修改成功！")
            //    $("#redis_database").modal("hide")

            }else if(data.code == -4) {
                alert("修改失败,已有相同数据源")
            }else{
                alert("修改失败,无法连接数据库")
            }
            code = data.code
        }

    })
}