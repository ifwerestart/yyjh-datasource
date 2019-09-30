$(document).ready(function(){

    //点击excel的修改按钮
    $("#update-excel-btn").click(function(){
        $("#excel_import1").modal("show");
        json_id["id"]=global_id;

    });


    //excel操作
   // excel_load1("#excel_upload1");


    //excel预览
    $("#e_preview1").click(function () {
        excel_url1("#excel_upload1","/excel/previewExcel");
        excel_datas1.submit();

    });

    //excel上传
    $("#e_submit1").click(function () {
        excel_url1("#excel_upload1","/excel/excelUpdate");
        excel_datas1.submit();
    });


    //关闭全部预览时开启excel上传页面
    $("#excel_preview1_close").click(function(){
        $("#excel_import1").modal("show");
    });



});

var excel_datas1=[];
var excel_index1=-1;
var excel_interpret1=[];
var json_id={}
var id=131;

function init_excel_datas1(){
    excel_datas1=[];
    excel_interpret1=[];
    excel_index1=-1;
    $("#excel_datas1").html("");
    $("#excel_datas1").bootstrapTable("destroy");

}

function excel_enter() {
    var excel_update_id=global_id;

    $.ajax({
        type:"GET",
        url:"/excel/excelEnter/"+excel_update_id,
        contentType:"application/json;charset=utf-8",
        //data:{'enter_param':JSON.stringify(uploadParam)},
        dataType:"json",
        async:false,
        success:function(result){

            if(result.code==0){
                alert("入库成功");
            }else {
                alert("表已经入库过了，入库失败");
            }

        }
    });//ajax
}

var radio_count1=0;
function excel_load1(element_id){

    $(element_id).fileupload({
        type : 'POST',
        dataType : 'json',
        autoUpload : false,
        acceptFileTypes : /(\.|\/)(xls|xlsx)$/i,
        add : function(e, data) {
            //console.log("data:"+data)
            if(excel_index1==-1){
                excel_datas1=data;
            }
            //console.log("excel_datas1:"+excel_datas1);
            excel_index1++;
            radio_count1++;

            filename=data.files[0].name;

            var row_data={};
            row_data["excel_filename"]=filename;
            row_data["excel_png"] ="<img src='../img/xlsx.png'>"
            excel_filter="<div>是否过滤？</div>"
            excel_filter+="<div>";
            excel_filter+="<label class=\"radio-inline\"><input class=\"yes-or-no\" type='radio' onclick=\"judgeFilter1(this)\"  value='yes'  name='filter"+filename+"'>&nbsp;&nbsp;&nbsp;&nbsp;是</label>"
            excel_filter+="<label class=\"radio-inline\"><input class=\"yes-or-no\" type='radio' onclick=\"judgeFilter1(this)\" value='no' name='filter"+filename+"' checked>&nbsp;&nbsp;&nbsp;否</label>"
            excel_filter+="</div>";
            row_data["excel_filter"]=excel_filter;
            row_data["open_config"]="<button id='config"+radio_count1+"' class=\"btn btn-default\" disabled=\"disabled\"   onclick=\"open_config1('"+filename+"')\">打开配置</button>"

            init_excel_table1_datas();

            $("#excel_datas1").bootstrapTable("removeAll");
            $("#excel_datas1").bootstrapTable("append",row_data);


            //初始化默认过滤条件
            this_excel_interpret1={"filename":filename,"isfilter":false}
            excel_interpret1.push(this_excel_interpret1)

            excel_datas1.files[0]=data.files[0];


        },
        done : function(e, data) {
            result=data.result;
            //console.log(result)
            if(result.code==0){
                //alert("this is done")
                if(result.hasOwnProperty("payload") && result.payload!=null){

                    $("#excel_import1").modal("hide");
                    $("#excel_preview1").modal("show");
                    //显示
                    create_excel_btngroup1(result.payload);

                }else{
                    alert("修改成功");
                    $("#excel_import1").modal("hide");
                    initTable($(elementID).bootstrapTable('getOptions').pageNumber)

                    // flag= confirm("修改成功，是否重新修改");
                    // if(flag){
                    //     init_excel_datas1();
                    // }else{
                    //     $("#excel_import1").modal("hide");
                    // }
                }

            }else{
                alert("操作失败");
            }
        }
        ,
        fail : function(e, data) {
            alert("修改失败");
        }
    });
}

function excel_url1(element_id, url) {
    $(element_id).bind('fileuploadsubmit', function(e, data) {

        data.formData = {
            excel_interpret: JSON.stringify(excel_interpret1),
            id:JSON.stringify(json_id)
        };
    });
    $(element_id).fileupload(
        'option',
        'url',
        url
    );
}

var globle_excel_datas1 = [];


function create_excel_btngroup1(excel_datas1) {
    globle_excel_datas1 = excel_datas1
    //console.log(globle_excel_datas1)
    btn_group = "<div class=\"btn-group\" role=\"group\">"
    for(i = 0; i < globle_excel_datas1.length; i++) {
        data = globle_excel_datas1[i];
        //console.log(data);
        if(i == 0) {
            btn_group += "<button onclick=\"create_excel_table1(this)\" type=\"button\" class=\"btn btn-primary\">" + data.file_name + "</button>"
        } else {
            btn_group += "<button onclick=\"create_excel_table1(this)\" type=\"button\" class=\"btn btn-default\">" + data.file_name + "</button>"
        }


    }
    btn_group += "</div>";
    $("#excel_btngroup1").html(btn_group);
    //下拉列表显示表的sheet
    select_group="<select id=\"sheet1\">";

    for(j=0;j<globle_excel_datas1[0].file_datas.length;j++){
        select_group+="<option>"+ globle_excel_datas1[0].file_datas[j].sheet_name+"</option>";
    }
    select_group+="</select>";

    $("#select-group1").html(select_group);

    $("#sheet1").change(function(){
        //下拉框一被改变要触发的事件
        var sheet_name = $("#sheet1 option:selected").text();

        //表格
        init_excel_table1(globle_excel_datas1[0].file_name,sheet_name);
    });

    //默认表格
    if(globle_excel_datas1 != []){
        init_excel_table1(globle_excel_datas1[0].file_name,globle_excel_datas1[0].file_datas[0].sheet_name)
    }
}

function create_excel_table1(btn) {
    btns = $(btn).siblings()

    for(i=0;i<btns.length;i++){
        $(btns[i]).attr("class","btn btn-default")
    }
    $(btn).attr("class","btn btn-primary");

    filename=$(btn).text();

    //下拉列表显示表的sheet
    select_group="<select id=\"sheet1\">";
    for(i = 0; i < globle_excel_datas1.length; i++) {
        data = globle_excel_datas1[i];

        if(data.file_name == filename){
            for(j=0;j<data.file_datas.length;j++){
                sheet=data.file_datas[j];
                select_group+="<option>"+ sheet.sheet_name+"</option>";
            }
            select_group+="</select>";

            break;
        }

    }
    $("#select-group1").html(select_group);
    init_excel_table1(filename,$("#sheet1 option:selected").text());

    $("#sheet1").change(function(){
        //要触发的事件
        var sheet_name = $("#sheet1 option:selected").text();
        //表格
        init_excel_table1(filename,sheet_name);
    });




}


function init_excel_table1(init_filename,init_sheetname) {
    var data=[];
    for(i = 0;i<globle_excel_datas1.length;i++){
        if(globle_excel_datas1[i].file_name == init_filename){
            dd=globle_excel_datas1[i];
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


    $("#excel_table1").bootstrapTable({
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
    $("#excel_table1").bootstrapTable("load",data);
}

function  init_excel_table1_datas() {
    $("#excel_datas1").bootstrapTable({
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

var this_ex_interpret1={};

function open_config1(file_name) {

    $("#open_config1").modal("show");
    $("#excel_import1").modal("hide");

    for(var i=0;i<excel_interpret1.length;i++){
        if(file_name==excel_interpret1[i].filename){
            excel_interpret1.splice(i,1);//从json数组里的第i个json对象开始删除，总共删除1个
        }
    }


    this_ex_interpret1={"filename":file_name,"isfilter":true}



}

$("#open_config1_close").click(function(){
    //$('#form-filter1')[0].reset();
    //置空打开配置的内容
    document.getElementById("form-filter1").reset();
    //恢复打开配置的初始模样
    $("#add-char-filter1").empty();
    // 让完成按钮生效
    $("#finish1").attr("disabled",false);
    $("#excel_import1").modal("show");
});

$("#img-add1").click(function () {
    var add_content="<div class=\"add-string-filter\"><br><span>起始文本：<input type=\"text\"  class=\"start-string\" /></span>\n" +
        "        <span>结束文本：<input type=\"text\" class=\"end-string\" /></span>\n" +
        "        <span>包含文本：<input type=\"text\" class=\"content-string\"  /></span><br></div>";

    $("#add-char-filter1").append(add_content);


});

function  ex_interpret_submit1(){
    var interpret_filter={};
    var index_string=[];
    var start_line=$("#start-line1").val();
    var end_line=$("#end-line1").val();

    var index_logic = $("#index-logic1").find("label").find('input[type="radio"]:checked').val();

    interpret_filter["start_line"]=start_line;
    interpret_filter["end_line"]=end_line;
    interpret_filter["index_logic"]=index_logic;

    var start_string1=$("#index-string1").find("span").find(".start-string").val();
    var end_string1=$("#index-string1").find("span").find(".end-string").val();
    var content_string1=$("#index-string1").find("span").find(".content-string").val();

    var index_str_filter={"start_string":start_string1,"end_string":end_string1,"content_string":content_string1};
    index_string.push(index_str_filter);


    var filter_len=$("#add-char-filter1").children("div").length;
    for(var i=0;i<filter_len;i++){
        var start_string=$(".add-string-filter").eq(i).find("span").find(".start-string").val();
        var end_string=$(".add-string-filter").eq(i).find("span").find(".end-string").val();
        var content_string=$(".add-string-filter").eq(i).find("span").find(".content-string").val();

        var index_str_filter={"start_string":start_string,"end_string":end_string,"content_string":content_string};
        index_string.push(index_str_filter);
    }


    interpret_filter["index_string"]=index_string;
    this_ex_interpret1["interpret_filter"]=interpret_filter;
    excel_interpret1.push(this_ex_interpret1);
    //console.log(excel_interpret1);
    alert("配置成功");
    //重置打开配置表格内容
    document.getElementById("form-filter1").reset();
    //恢复打开配置的初始模样
    $("#add-char-filter1").empty();
    // 让完成按钮生效
    $("#finish1").attr("disabled",false);

    $("#open_config1").modal("hide");
    $("#excel_import1").modal("show");


}

$("#start-line1").change(function(){
    var start_line=$("#start-line1").val();
    var isok = isNaN(start_line);//判断是否为数值类型 bool,false为数字类型，true为字符
    if (isNaN(start_line)== true) {
        alert("请输入数字！");
        $("#finish1").attr("disabled",true);   //按钮失效
    }else{
        $("#finish1").attr("disabled",false);   // 按钮生效
    }
});

$("#end-line1").change(function(){
    var end_line=$("#end-line1").val();
    if(isNaN(end_line)==true){
        alert("请输入数字！");
        $("#finish1").attr("disabled",true);   //按钮失效
    }else{
        $("#finish1").attr("disabled",false);   // 按钮生效
    }
});

function judgeFilter1(obj){
    var option=$(obj).val();
    //获取button的id值
    var but=$(obj).parent().parent().parent().next().find("button");
    var id=$(but).attr("id");
    if(option=="yes"){
        document.getElementById(id).removeAttribute("disabled");
    }else{
        document.getElementById(id).setAttribute("disabled", true);
    }
    /*
        var id = $("label").find('input[type="radio"]:checked');
        $('input[type="radio"]').click(function() {
            if(this.value=="yes"){
                document.getElementById("config"+radio_count1).removeAttribute("disabled");
            }else{
                document.getElementById("config"+radio_count1).setAttribute("disabled", true);
            }
        });
    */

}
