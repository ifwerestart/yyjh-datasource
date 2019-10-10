$(document).ready(function() {

    csv_updatedata_submit()

    //选择数据源按钮弹窗
    db_btns = $("#showDrivers_choosen").find("div").find(".modal-body").find("button")

//	$(db_btns[3]).click(function() {
//		//REDIS弹窗
//		$("#showDrivers_choosen").modal("hide")
//		$("#redis_import").modal("show")
//
//	})
//
//	//上传数据源
//	$("#r_submit").click(function() {
//		redis_load()
//	})
//
//	//在数据源列表里redis点击“入库”
//	$("#redis_showdatabase").click(function() {
//		$("#redis_database").modal("show")
//		//用表格显示redis非空库号
//		get_redisbase_table()
//	})
//
//	//选中库号后点击"执行入库"按钮
//	$("#redis_insertdata_submit").click(function() {
//		redis_insertdata_submit()
//
//	})

    //csv入库
    $("#csv_insertdata").click(function() {
        csv_insertdata_submit()
    })

    //csv修改
    $("#csv_updatedata").click(function() {
        $("#csv_update_import").modal("show")
        // csv_updatedata_submit()
        //csv_update_data.submit()
    })

    //csv确认修改(上传信息)
    $("#c_update_submit").click(function() {
        csv_updatedata_submit()
        console.log(csv_update_data)
        if(csv_update_data.length == 0) {
            alert("请上传一个csv文件！")
            return
        }
        csv_update_data.submit()
    })

    //csv修改时的预览
    $("#c_update_preview").click(function() {
        if(csv_update_data.length == 0) {
            alert("没有相应文件可以预览！")
            return
        }

        c_update_preview()
        csv_update_data.submit()

    })

    //关闭跟新时的csv预览后开启csv修改上传页面
    $("#csv_updatepreview_close").click(function() {

        $("#csv_update_import").modal("show")
    })

    $("#csv_update_back").click(function() {
        $("#csv_update_datas").html("")

    })

});



//csv入库
function csv_insertdata_submit() {

    $.ajax({
        type: "GET",
        url: "/csv/insertCSV/"+global_id,
        dataType: "json",
        async: false,
        success: function(data) {
            if(data.code == 0) {

                alert("入库成功！")
                //              $("#redis_database").modal("hide")

            } else if(data.code == -4){
                alert("该csv文件已经入库")
            } else {
                alert("入库失败")
            }

        }

    })
}

var csv_update_data = []
var filename = ""
//csv修改(用于上传文件)
function csv_updatedata_submit() {

    $('#csv_update').fileupload({ //加载文件的插件
        url: '/csv/updateCSV',
        type: 'POST',
        dataType: 'json',
        autoUpload: false,
        acceptFileTypes: /(\.|\/)(csv)$/i,
        add: function(e, data) { //data这个数据是该fileload方法自主打包封装好的数据，想查看可以通过console   走这个方法一定会走后台，但是add是异步请求，done是执行真正执行后获得的
            console.log(data)

            filename = data.files[0].name //获取文件名的方法，一次只能选择一个文件
            //   datas = "<div>" + filename + "</div>"

            let index1 = filename.lastIndexOf(".");
            let index2 = filename.length;
            let suffix = filename.substring(index1, index2); //后缀名
            if(suffix != ".csv") {
                alert('请上传csv文件！')
            } else {
                csv_update_data = data
                let fileImage = "<img src='../img/csv.png' width='35px' height='30px'>"
                let fileSize = data.files[0].size+"字节";
                datas = "<tr><td>"+filename+"</td><td>"+fileImage+"</td><td>"+fileSize+"</td></tr>";
                $("#csv_update_datas").html(datas)
            }

        },
        done: function(e, data) { //这里的data是后台传来的数据
            result = data.result
            if(result.code == 0) {
                alert("修改成功！")
                $("#csv_update_datas").html("")
                initTable($(elementID).bootstrapTable('getOptions').pageNumber)

            } else {
                alert("修改失败")
            }

        }
    });

    $('#csv_update').bind('fileuploadsubmit', function(e, data) { //bind() 方法为被选元素添加一个或多个事件处理程序，并规定事件发生时运行的函数。这里添加fileupload上传事件
        data.formData = {
            csv_id: global_id
        };
    });

}

//修改csv文件时的预览
function c_update_preview() {
    $('#csv_update').fileupload({ //加载文件的插件
        url: '/csv/previewCSV',
        type: 'POST',
        dataType: 'json',
        autoUpload: false,
        acceptFileTypes: /(\.|\/)(csv)$/i,
        add: function(e, data) { //data这个数据是该fileload方法自主打包封装好的数据，想查看可以通过console   走这个方法一定会走后台，但是add是异步请求，done是执行真正执行后获得的
            //          console.log(data)

            filename = data.files[0].name //获取文件名的方法，一次只能选择一个文件
            datas = "<div>" + filename + "</div>"

            let index1 = filename.lastIndexOf(".");
            let index2 = filename.length;
            let suffix = filename.substring(index1, index2); //后缀名
            if(suffix != ".csv") {
                alert('请上传csv文件！')
            } else {
                csv_update_data = data
                let fileImage = "<img src='../img/csv.png' width='35px' height='30px'>"
                let fileSize = data.files[0].size+"字节";
                datas = "<tr><td>"+filename+"</td><td>"+fileImage+"</td><td>"+fileSize+"</td></tr>";
                $("#csv_update_datas").html(datas)
            }


        },
        done: function(e, data) { //这里的data是后台传来的数据
            result = data.result
            console.log(result.payload)
            $("#csv_update_import").modal("hide")
            $("#csv_update_preview").modal("show")
            init_csvpreview_table(result.payload)

        }
    });

    $('#csv_update').bind('fileuploadsubmit', function(e, data) { //bind() 方法为被选元素添加一个或多个事件处理程序，并规定事件发生时运行的函数。这里添加fileupload上传事件
        data.formData = {
            csv_interpret: ","
        };
    });

}

function init_csvpreview_table(csv_data) {
    let data = []
    data = csv_data[0].file_datas

    //根据init_filename动态生成table,注意每个文件表头可能不同
    ths = []
    console.log(data)
    if(data.length > 0) {
        data0 = data[0];
        for(key in data0) {
            th = {
                field: key,
                title: key,
                visible: true,

            }
            ths.push(th)
        }
    }

    $('#csv_update_table').bootstrapTable({
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, // 是否显示分页（*）
        sortable: false, // 是否启用排序
        sidePagination: "client", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 10, // 每页的记录行数（*）
        pageList: [10, 20, 30, 40], // 可供选择的每页的行数（*）
        search: true, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: true, // 是否显示所有的列
        showRefresh: true, // 是否显示刷新按钮
        minimumCountColumns: 2, // 最少允许的列数
        clickToSelect: true, // 是否启用点击选中行
        showToggle: false, // 是否显示详细视图和列表视图的切换按钮
        cardView: false, // 是否显示详细视图
        detailView: false,
        columns: ths
    });

    $('#csv_update_table').bootstrapTable("load", data)
}