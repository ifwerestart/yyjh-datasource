
var dateformat="";
$(document).ready(function(){

    $.ajax({
        type: "GET",
        url: "/TUser/selDateFormat",
        contentType: "application/json;charset=utf-8",
        async: false,
        success: function (result) {
            dateformat=result.dateformat;
            console.log(dateformat)
        }
    });

    showInfo();

    $("#add-btn").click(function () {
        $.ajax({
            type:"GET",
            url:"/TUser/addPermission",
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(result){
                if(result.code==1){
                    $("#showDrivers_choosen").modal("show");
                }
            },
            error:function () {
                alert("该账户不具有添加数据源的权限！");
            }
        });

    })

    //选择数据源按钮弹窗
    db_btns=$("#showDrivers_choosen").find("div").find(".modal-body").find("button");

    //CSV弹窗
    $(db_btns[0]).click(function(){
        $("#showDrivers_choosen").modal("hide");
        $("#csv_import").modal("show");
        //初始化csv的Object
        init_csv_datas();
    });

    //cp

    $("#back").click(function () {
        window.location.href='/TUser/logout';
    })
    $("#datasmanage").click(function () {
        $("#datasmanage").css("color", "#5CB85C");
        $("#permissionmanage").css("color", "");
        $("#datemanage").css("color", "");
        $("#infomanage").css("color", "");
        $.ajax({
            type:"GET",
            url:"/TUser/datasmanage",
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(result){
                if(result.code==1){

                    $("#datascontent").css("display", "block");
                    $("#permissioncontent").css("display", "none");
                    $("#datecontent").css("display", "none");
                    $("#infocontent").css("display", "none");
                    initTable(1)
                }
            },
            error:function () {
                alert("该账户不具有访问数据源管理的权限！");
                $("#datascontent").css("display", "none");
                $("#permissioncontent").css("display", "none");
                $("#datecontent").css("display", "none");
                $("#infocontent").css("display", "none");
            }
        });

    });
    $("#permissionmanage").click(function () {
        $("#datasmanage").css("color", "");
        $("#permissionmanage").css("color", "#5CB85C");
        $("#datemanage").css("color", "");
        $("#infomanage").css("color", "");
        $.ajax({
            type:"GET",
            url:"/TUser/permissionmanage",
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(result){
                if(result.code==1){

                    $("#datascontent").css("display", "none");
                    $("#permissioncontent").css("display", "block");
                    $("#datecontent").css("display", "none");
                    $("#infocontent").css("display", "none");
                }
            },
            error:function () {
                alert("该账户不具有访问权限管理的权限！");
                $("#datascontent").css("display", "none");
                $("#permissioncontent").css("display", "none");
                $("#datecontent").css("display", "none");
                $("#infocontent").css("display", "none");
            }
        });
    });

    $("#datemanage").click(function () {
        $("#datasmanage").css("color", "");
        $("#permissionmanage").css("color", "");
        $("#datemanage").css("color", "#5CB85C");
        $("#infomanage").css("color", "");
        $.ajax({
            type:"GET",
            url:"/TUser/datemanage",
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(result){
                if(result.code==1){

                    $("#datascontent").css("display", "none");
                    $("#permissioncontent").css("display", "none");
                    $("#datecontent").css("display", "block");
                    $("#infocontent").css("display", "none");
                }
            },
            error:function () {
                alert("该账户不具有访问日期格式配置的权限！");
                $("#datascontent").css("display", "none");
                $("#permissioncontent").css("display", "none");
                $("#datecontent").css("display", "none");
                $("#infocontent").css("display", "none");
            }
        });

    });
    
    $("#setdate").click(function () {
        var date=$("input[name='date']:checked").val();
        var data={
            "date":date
        }
        $.ajax({
            type: "GET",
            url: "/TUser/setDateFormat",
            data:{"json":JSON.stringify(data)},
            dataType:"json",
            contentType: "application/json;charset=utf-8",
            async: false,
            success: function () {
                alert("日期格式配置成功！")
            }
        });
        $.ajax({
            type: "GET",
            url: "/TUser/selDateFormat",
            contentType: "application/json;charset=utf-8",
            async: false,
            success: function (result) {
                dateformat=result.dateformat;
                console.log(dateformat)
            }
        });

    })


    $("#infomanage").click(function () {
        showInfo();
    })

    //cp

    //EXCEL弹窗
    $(db_btns[1]).click(function(){
        $("#showDrivers_choosen").modal("hide");
        $("#excel_import").modal("show");
        init_excel_datas();
    });

    //MYSQL弹窗
    $(db_btns[2]).click(function(){
        $("#showDrivers_choosen").modal("hide");
    });

    //REDIS弹窗
    $(db_btns[3]).click(function(){
        $("#showDrivers_choosen").modal("hide");
    });

    //CSV操作
    csv_load("#csv_upload");
    //excel操作
    excel_load("#excel_upload");

    //csv全部上传
    $("#w_submit").click(function () {
        csv_url("#csv_upload","/csv/csvUpload");
        csv_datas.submit();

    });

    //csv全部预览
    $("#w_preview").click(function () {
        csv_url("#csv_upload","/csv/previewCSV");
        csv_datas.submit();

    });

    //excel全部预览
    $("#excel_allpreview").click(function () {
        excel_url("#excel_upload","/excel/previewExcel");
        excel_datas.submit();

    });

    //excel全部上传
    $("#excel_allsubmit").click(function () {
        excel_url("#excel_upload","/excel/excelUpload");
        excel_datas.submit();
    });

    //关闭全部预览时开启csv上传页面
    $("#csv_preview_close").click(function(){
        $("#csv_import").modal("show");
    });

    //关闭全部预览时开启excel上传页面
    $("#excel_preview_close").click(function(){
        $("#excel_import").modal("show");
    });

    //关闭文件导入时开启数据源选择页面
    $("#excel_import_close").click(function(){
        $("#showDrivers_choosen").modal("show");
    });

    //关闭文件导入时开启数据源选择页面
    $("#csv_import_close").click(function(){
        $("#showDrivers_choosen").modal("show");
    });
});

var csv_datas=[];//多文件处理
var index=-1;

var excel_datas=[];
var excel_index=-1;
var excel_interpret=[];


Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}

function dateFormat(time,dateformat) {
    var curTime;
    if (dateformat!=""){
        if(dateformat=="yyyyMMdd hhmmss"){
            var timeDate=new Date(time);
            var year=timeDate.getFullYear()+"年";
            var month=timeDate.getMonth()+1+"月";
            var date=timeDate.getDate()+"日";
            var hour=timeDate.getHours()+"时";
            var minute=timeDate.getMinutes()+"分";
            var second=timeDate.getSeconds()+"秒";
            curTime=year+month+date+" "+hour+minute+second;
        }else if( dateformat=="yyyyMMdd"){
            var timeDate=new Date(time);
            var year=timeDate.getFullYear()+"年";
            var month=timeDate.getMonth()+1+"月";
            var date=timeDate.getDate()+"日";
            curTime=year+month+date;
        }else{
            var timeDate=(new Date(time)).getTime();
            curTime=new Date(timeDate).format(dateformat);
        }
    }else {
        curTime=time;
    }
    return curTime;
}
//cp

//初始化CSV的Object
function init_csv_datas(){
    csv_datas=[];
    index=-1;
    $("#table_datas").html("");
}

function init_excel_datas(){
    excel_datas=[];
    excel_interpret=[];
    excel_index=-1;
    $("#excel_datas").html("");
    $("#excel_datas").bootstrapTable("destroy");

}

function csv_load(element_id){
    //alert("this is csv_load()")

    $(element_id).fileupload({
        url : '/csv/csvUpload',
        type : 'POST',
        dataType : 'json',
        autoUpload : false,
        acceptFileTypes : /(\.|\/)(csv)$/i,
        allowedFileExtensions: ['csv']  ,  //限定文件的类型
        add : function(e, data) {
            if(index==-1){
                csv_datas=data;
            }
            index++;
            //显示文件全名，作业显示改成bootstrap table
            filename=data.files[0].name;
            var fileSize=data.files[0].size+"字节";
            fileType=data.files[0].type;
            fileImage="<img src='../img/csv.png' width='35px' height='30px'>"

            //console.log("size="+fileSize+"  type="+fileType);


            pre_data="<tr><td>"+filename+"</td><td>"+fileImage+"</td><td>"+fileSize+"</td></tr>";

            $("#table_datas").html($("#table_datas").html()+pre_data);

            //datas="<div>"+filename+"</div>";
            //$("#csv_datas").html($("#csv_datas").html()+datas);

            csv_datas.files[index]=data.files[0];
            //console.log(csv_datas);
        },
        done : function(e, data) {
            result=data.result;
            if(result.code==0){
                //alert("this is done")
                if(result.hasOwnProperty("payload") && result.payload!=null){

                    $("#csv_import").modal("hide");
                    $("#csv_preview").modal("show");
                    //显示
                    create_csv_btngroup(result.payload);

                }else{
                    flag= confirm("上传成功，是否继续");
                    if(flag){
                        init_csv_datas();
                    }else{
                        $("#csv_import").modal("hide");
                        //刷新数据源列表
                    }
                }

            }else{
                alert("操作失败");
            }
        }
        ,
         fail : function(e, data) {
             alert("上传失败");
         }
    });
}

var radio_count=0;
function excel_load(element_id){

    $(element_id).fileupload({
        //url : '/excel/excelUpload',
        type : 'POST',
        dataType : 'json',
        autoUpload : false,
        acceptFileTypes : /(\.|\/)(xls|xlsx)$/i,
        add : function(e, data) {
            if(excel_index==-1){
                excel_datas=data;
            }
            excel_index++;
            radio_count++;
            //显示文件全名，作业显示改成bootstrap table
            //console.log(data);
            filename=data.files[0].name;

            var row_data={};
            row_data["excel_filename"]=filename;
            row_data["excel_png"] ="<img src='../img/xlsx.png'>"
            excel_filter="<div>是否过滤？</div>"
            excel_filter+="<div>";
            excel_filter+="<label class=\"radio-inline\"><input class=\"yes-or-no\" type='radio' onclick=\"judgeFilter(this)\"  value='yes'  name='filter"+filename+"'>&nbsp;&nbsp;&nbsp;&nbsp;是</label>"
            excel_filter+="<label class=\"radio-inline\"><input class=\"yes-or-no\" type='radio' onclick=\"judgeFilter(this)\" value='no' name='filter"+filename+"' checked>&nbsp;&nbsp;&nbsp;否</label>"
            excel_filter+="</div>";
            row_data["excel_filter"]=excel_filter;
            row_data["open_config"]="<button id='config"+radio_count+"' class=\"btn btn-default\" disabled=\"disabled\"   onclick=\"open_config('"+filename+"')\">打开配置</button>"

            init_excel_table_datas();

            $("#excel_datas").bootstrapTable("append",row_data);


            //初始化默认过滤条件
            this_excel_interpret={"filename":filename,"isfilter":false}
            excel_interpret.push(this_excel_interpret)

            excel_datas.files[excel_index]=data.files[0];


        },
        done : function(e, data) {
            result=data.result;
            //console.log(result)
            if(result.code==0){
                //alert("this is done")
                if(result.hasOwnProperty("payload") && result.payload!=null){

                    $("#excel_import").modal("hide");
                    $("#excel_preview").modal("show");
                    //显示
                    create_excel_btngroup(result.payload);

                }else{
                    flag= confirm("上传成功，是否继续");
                    if(flag){
                        init_excel_datas();
                    }else{
                        $("#excel_import").modal("hide");
                        //刷新数据源列表
                    }
                }

            }else{
                alert("操作失败");
            }
        }
        ,
        fail : function(e, data) {
            alert("上传失败");
        }
    });
}

function excel_url(element_id, url) {
    $(element_id).bind('fileuploadsubmit', function(e, data) {

        data.formData = {
            excel_interpret: JSON.stringify(excel_interpret)
        };
    });
    $(element_id).fileupload(
        'option',
        'url',
        url
    );
}

function csv_url(element_id, url) {
    $(element_id).bind('fileuploadsubmit', function(e, data) {
        var separator = ",";
        data.formData = {
            csv_interpret: separator
        };
    });
    $(element_id).fileupload(
        'option',
        'url',
        url
    );
}

var globle_csv_datas = [];
var globle_excel_datas = [];

function create_csv_btngroup(csv_datas) {
    globle_csv_datas = csv_datas
    btn_group = "<div class=\"btn-group\" role=\"group\">"
    for(i = 0; i < globle_csv_datas.length; i++) {
        data = globle_csv_datas[i];
        console.log(data);
        if(i == 0) {
            btn_group += "<button onclick=\"create_csv_table(this)\" type=\"button\" class=\"btn btn-primary\">" + data.file_name + "</button>"
        } else {
            btn_group += "<button onclick=\"create_csv_table(this)\" type=\"button\" class=\"btn btn-default\">" + data.file_name + "</button>"
        }
    }
    btn_group += "</div>"
    $("#csv_btngroup").html(btn_group)
    //默认表格
    if(globle_csv_datas != []){
        init_csv_table(globle_csv_datas[0].file_name)
    }
}

function create_excel_btngroup(excel_datas) {
    globle_excel_datas = excel_datas
    btn_group = "<div class=\"btn-group\" role=\"group\">"
    for(i = 0; i < globle_excel_datas.length; i++) {
        data = globle_excel_datas[i];
        //console.log(data);
        if(i == 0) {
            btn_group += "<button onclick=\"create_excel_table(this)\" type=\"button\" class=\"btn btn-primary\">" + data.file_name + "</button>"
        } else {
            btn_group += "<button onclick=\"create_excel_table(this)\" type=\"button\" class=\"btn btn-default\">" + data.file_name + "</button>"
        }


    }
    btn_group += "</div>";
    $("#excel_btngroup").html(btn_group);
    //下拉列表显示表的sheet
    select_group="<select id=\"sheet\">";

    for(j=0;j<globle_excel_datas[0].file_datas.length;j++){
        select_group+="<option>"+ globle_excel_datas[0].file_datas[j].sheet_name+"</option>";
    }
    select_group+="</select>";

    $("#select-group").html(select_group);

    $("#sheet").change(function(){
        //下拉框一被改变要触发的事件
        var sheet_name = $("#sheet option:selected").text();

        //表格
        init_excel_table(globle_excel_datas[0].file_name,sheet_name);
    });

    //默认表格
    if(globle_csv_datas != []){
        init_excel_table(globle_excel_datas[0].file_name,globle_excel_datas[0].file_datas[0].sheet_name)
    }
}

function create_csv_table(btn) {
    btns = $(btn).siblings()

    for(i=0;i<btns.length;i++){
        $(btns[i]).attr("class","btn btn-default")
    }
    $(btn).attr("class","btn btn-primary")

    //表格
    filename=$(btn).text();
    init_csv_table(filename);

}


function create_excel_table(btn) {
    btns = $(btn).siblings()

    for(i=0;i<btns.length;i++){
        $(btns[i]).attr("class","btn btn-default")
    }
    $(btn).attr("class","btn btn-primary");

    filename=$(btn).text();

    //下拉列表显示表的sheet
    select_group="<select id=\"sheet\">";
    for(i = 0; i < globle_excel_datas.length; i++) {
        data = globle_excel_datas[i];

        if(data.file_name == filename){
            for(j=0;j<data.file_datas.length;j++){
                sheet=data.file_datas[j];
                select_group+="<option>"+ sheet.sheet_name+"</option>";
            }
            select_group+="</select>";

            break;
        }

    }
    $("#select-group").html(select_group);
    init_excel_table(filename,$("#sheet option:selected").text());

    $("#sheet").change(function(){
        //要触发的事件
        var sheet_name = $("#sheet option:selected").text();
        //表格
        init_excel_table(filename,sheet_name);
    });




}


function init_csv_table(init_filename) {
    var data=[];
    for(i = 0;i<globle_csv_datas.length;i++){
        if(globle_csv_datas[i].file_name == init_filename){
            data =globle_csv_datas[i].file_datas;
            break;
        }
    }

    console.log(data);
    //根据init_filename动态生成table，注意每个文件表头可能不同
    ths=[];

    //获取data数组里的第一个jsonObject的长度
    var len=Object.keys(data[0]).length;
    // var len=0;
    // for(var ever in data[0]) {
    //     len++;
    // }


    if(data.length>0){
        data0=data[0];
        for(key in data0){
            th={
                field:key,
                title:key,
                visible:true
            };
            ths.push(th);

        }
    }


    $("#csv_table").bootstrapTable({
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 20, 30, 40],         //可供选择的每页的行数（*）
        search: true,                      //是否显示表格搜索
        strictSearch: false,
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        columns:ths
    });
    $("#csv_table").bootstrapTable("load",data);
}

function init_excel_table(init_filename,init_sheetname) {
    var data=[];
    for(i = 0;i<globle_excel_datas.length;i++){
        if(globle_excel_datas[i].file_name == init_filename){
            dd=globle_excel_datas[i];
            for(j=0;j<dd.file_datas.length;j++){
                if(dd.file_datas[j].sheet_name==init_sheetname){
                    data =dd.file_datas[j].sheet_datas;
                    break;
                }
            }

        }
    }

    //console.log(data);
    //根据init_filename动态生成table，注意每个文件表头可能不同
    ths=[];

    //获取data数组里的第一个jsonObject的长度
    //var len=Object.keys(data[0]).length;

    if(data.length>0){
        data0=data[0];
        for(key in data0){
            th={
                field:key,
                title:key,
                visible:true
            };
            ths.push(th);

        }
    }


    $("#excel_table").bootstrapTable({
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 20, 30, 40],         //可供选择的每页的行数（*）
        search: true,                      //是否显示表格搜索
        strictSearch: false,
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        columns:ths
    });
    $("#excel_table").bootstrapTable("load",data);
}

function  init_excel_table_datas() {
    $("#excel_datas").bootstrapTable({
        striped: false,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 20, 30, 40],         //可供选择的每页的行数（*）
        search: false,                      //是否显示表格搜索
        strictSearch: false,
        showColumns: false,                  //是否显示所有的列（选择显示的列）
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 3,             //最少允许的列数
        clickToSelect: false,                //是否启用点击选中行
        showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        columns: [{
            field: 'excel_filename',
            title:"文件名称",
            visible:true
        }, {
            field: 'excel_png',
            title:"文件图片",
            visible:true
        }, {
            field: 'excel_filter',
            title:"是否过滤",
            visible:true
        }, {
            field: 'open_config',
            title:"操作设置",
            visible:true
        }]
    });


}

var this_ex_interpret={};

function open_config(file_name) {

    $("#open_config").modal("show");
    $("#excel_import").modal("hide");

    for(var i=0;i<excel_interpret.length;i++){
        if(file_name==excel_interpret[i].filename){
            excel_interpret.splice(i,1);//从json数组里的第i个json对象开始删除，总共删除1个
        }
    }


    this_ex_interpret={"filename":file_name,"isfilter":true}



}

$("#open_config_close").click(function(){
    //$('#form-filter')[0].reset();
    //置空打开配置的内容
    document.getElementById("form-filter").reset();
    //恢复打开配置的初始模样
    $("#add-char-filter").empty();
    // 让完成按钮生效
    $("#finish").attr("disabled",false);
    $("#excel_import").modal("show");
});

$("#img-add").click(function () {
    var add_content="<div class=\"add-string-filter\"><br><span>起始文本：<input type=\"text\"  class=\"start-string\" /></span>\n" +
        "        <span>结束文本：<input type=\"text\" class=\"end-string\" /></span>\n" +
        "        <span>包含文本：<input type=\"text\" class=\"content-string\"  /></span><br></div>";

    $("#add-char-filter").append(add_content);


});

function  ex_interpret_submit(){
    var interpret_filter={};
    var index_string=[];
    var start_line=$("#start-line").val();
    var end_line=$("#end-line").val();

    var index_logic = $("#index-logic").find("label").find('input[type="radio"]:checked').val();

    interpret_filter["start_line"]=start_line;
    interpret_filter["end_line"]=end_line;
    interpret_filter["index_logic"]=index_logic;

    var start_string1=$("#index-string").find("span").find(".start-string").val();
    var end_string1=$("#index-string").find("span").find(".end-string").val();
    var content_string1=$("#index-string").find("span").find(".content-string").val();

    var index_str_filter={"start_string":start_string1,"end_string":end_string1,"content_string":content_string1};
    index_string.push(index_str_filter);


    var filter_len=$("#add-char-filter").children("div").length;
    for(var i=0;i<filter_len;i++){
        var start_string=$(".add-string-filter").eq(i).find("span").find(".start-string").val();
        var end_string=$(".add-string-filter").eq(i).find("span").find(".end-string").val();
        var content_string=$(".add-string-filter").eq(i).find("span").find(".content-string").val();

        var index_str_filter={"start_string":start_string,"end_string":end_string,"content_string":content_string};
        index_string.push(index_str_filter);
    }


    interpret_filter["index_string"]=index_string;
    this_ex_interpret["interpret_filter"]=interpret_filter;
    excel_interpret.push(this_ex_interpret);

    console.log(excel_interpret);
    alert("配置成功");
    //重置打开配置表格内容
    document.getElementById("form-filter").reset();
    //恢复打开配置的初始模样
    $("#add-char-filter").empty();
    // 让完成按钮生效
    $("#finish").attr("disabled",false);

    $("#open_config").modal("hide");
    $("#excel_import").modal("show");


}

$("#start-line").change(function(){
    var start_line=$("#start-line").val();
    var isok = isNaN(start_line);//判断是否为数值类型 bool,false为数字类型，true为字符
    if (isNaN(start_line)== true) {
        alert("请输入数字！");
        $("#finish").attr("disabled",true);   //按钮失效
    }else{
        $("#finish").attr("disabled",false);   // 按钮生效
    }
});

$("#end-line").change(function(){
    var end_line=$("#end-line").val();
    if(isNaN(end_line)==true){
        alert("请输入数字！");
        $("#finish").attr("disabled",true);   //按钮失效
    }else{
        $("#finish").attr("disabled",false);   // 按钮生效
    }
});

function judgeFilter(obj){
    var option=$(obj).val();
    //获取button的id值
    var but=$(obj).parent().parent().parent().next().find("button");
    var id=$(but).attr("id");
    console.log("id="+id);
    if(option=="yes"){
        document.getElementById(id).removeAttribute("disabled");
    }else{
        document.getElementById(id).setAttribute("disabled", true);
    }

}

function showInfo() {
    $("#datasmanage").css("color", "");
    $("#permissionmanage").css("color", "");
    $("#datemanage").css("color", "");
    $("#infomanage").css("color", "#5CB85C");

    $("#datascontent").css("display", "none");
    $("#permissioncontent").css("display", "none");
    $("#datecontent").css("display", "none");
    $("#infocontent").css("display", "block");

    $.ajax({
        type:"GET",
        url:"/TUser/userinfo",
        contentType:"application/json;charset=utf-8",
        async:false,
        success:function(result){
            console.log(result);
            if(result.loginid==null){
                $("#loginid").html("");
            }else {
                $("#loginid").html(result.loginid);
            }
            if(result.password==null){
                $("#ppwd").html("");
            }else {
                $("#ppwd").html(result.password);
            }
            if(result.nickname==null){
                $("#nickname").html("");
            }else {
                $("#nickname").html(result.nickname);
            }
            if(result.userimg==null){
                $("#userimg").html("");
            }else {
                var imgupload="<img alt='image' style='height:50px;width:50px;' src='/upload/"+result.userimg+"'/>";
                $("#userimg").html(imgupload);
            }
            if(result.remark==null){
                $("#remark").html("");
            }else {
                $("#remark").html(result.remark);
            }

            if(result.email==null){
                $("#email").html("");
            }else {
                $("#email").html(result.email);
            }
            if(result.tel==null){
                $("#tel").html("");
            }else {
                $("#tel").html(result.tel);
            }
            if(result.createTime==null){
                $("#create_time").html("");
            }else {
                var cretime=dateFormat(result.createTime,dateformat);
                $("#create_time").html(cretime);
            }
            if(result.updateTime==null){
                $("#update_time").html("");
            }else {
                var updtime=dateFormat(result.updateTime,dateformat);
                $("#update_time").html(updtime);
            }
            if(result.state==null){
                $("#state").html("");
            }else {
                $("#state").html(result.state);
            }
        }
    });
}
