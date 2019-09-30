$(document).ready(function(){


    //点击mysql入库按钮
    // $("#enter-mysql-btn").click(function () {
    //     $("#mysql_oper").modal("show");
    //     mysqlFindTables();
    // });

    $("#mysql_oper_close").click(function () {
        init_mysql_tables1();
    })

    //点击mysql的修改按钮
    // $("#update-mysql-btn").click(function(){
    //     $("#mysql-config1").modal("show");
    //
    //
    //     $.ajax({
    //         type:"GET",
    //         url:"/mysql/mysqlFind/"+id,
    //         contentType:"application/json;charset=utf-8",
    //         dataType:"json",
    //         async:false,
    //         success:function(result){
    //             $("#username1").val(result.username);
    //             $("#port1").val(result.port);
    //             $("#database-name1").val(result.database_name);
    //             $("#host1").val(result.ip);
    //             $("#password1").val(result.password);
    //
    //         }
    //     });//ajax
    //
    // });

    $('#mysql_all_select').click(function(){
        //如果选择全选复选框，则把所有的复选框打钩
        if ($("#mysql_all_select").is(":checked")) {
            $("input[name='select_mysql_table']:checkbox").prop("checked", true);

            uploadTableName1=[];
            $("input:checkbox[name='select_mysql_table']:checked").each(function(){
                var val=$(this).parent().parent().children().eq(2).text();
                uploadTableName1.push({"table_name":val});
            });
        }
        //如果全选复选框不被选中，就取消所有的复选框打钩
        else {
            $("input[name='select_mysql_table']:checkbox").prop("checked", false);
            //把json数据的key“tables”对应的值清空
            uploadTableName1=[];
        }

    });


});

//全局变量，用来上传入库的数据
var uploadParam1={};
//全局变量，表示选择的哪些表名
var uploadTableName1=[];
var mysql_information1={};
var password="";
var id=136;

function mysql_connect_update1(){
    var user=$("#username1").val();
    var port=$("#port1").val();
    var database_name=$("#database-name1").val();
    var host=$("#host1").val();
    password=$("#password1").val();

    var connect_param={
        "host":host,
        "databaseName":database_name,
        "port":port,
        "username":user,
        "password":password,
        "id":global_id
    };

    if(user!="" && port!="" && password!=""  && database_name!="" && host!=""){
        $.ajax({
            type:"GET",
            url:"/mysql/mysqlUpdate",
            contentType:"application/json;charset=utf-8",
            data:{'connect_param':JSON.stringify(connect_param)},
            dataType:"json",
            async:false,
            success:function(result){

                if(result.code==0){
                    alert("修改成功")
                    //$("#showDrivers_choosen").modal("show");
                    $("#mysql-config1").modal("hide")
                    initTable($(elementID).bootstrapTable('getOptions').pageNumber)
                    // flag= confirm("修改成功，是否重新修改");
                    // if(flag){
                    //     //清空配置信息
                    //     document.getElementById("fill_config1").reset();
                    // }else{
                    //     $("#showDrivers_choosen").modal("show");
                    //     $("#mysql-config1").modal("hide")
                    //
                    // }

                }else if(result.code==-2){
                    alert("连接异常");
                }else if(result.code==-1){
                    alert("数据库已经存在，修改失败")
                }


            }
        });//ajax
    }else {
        alert("你还没有填写配置信息");
    }

}



function OperTablesData1(obj) {
    //获取复选框的同一行的表名
    var val=$(obj).parent().parent().children().eq(2).text();
    if($(obj).is(":checked"))
    {
        uploadTableName1.push({"table_name":val});
    }
    //如果表里面的复选框没有选中的话，就把全选的复选框按钮设置成不被选中
    else
    {
        $("#mysql_all_select").prop("checked", false);
        //遍历json数组,根据当前table_name所在的josn数组的下标，然后删除掉这个json数据
        for(var index in uploadTableName1){
            if(uploadTableName1[index].table_name==val){
                //delete uploadTableName1[index];
                uploadTableName1.splice(index,1);
            }
        }

    }

}


function mysql_enter() {

    uploadParam1["tables"]=uploadTableName1;
    uploadParam1["id"]=global_id;

    if(uploadTableName1.length==0){
        alert("你还没有选择需要入库的表")
    }else{

        $.ajax({
            type:"GET",
            url:"/mysql/mysqlEnter",
            contentType:"application/json;charset=utf-8",
            data:{'enter_param':JSON.stringify(uploadParam1)},
            dataType:"json",
            async:false,
            success:function(result){

                if(result.code==0){
                    var flag=confirm("入库成功,是否要继续入库");
                    if(flag){
                        init_mysql_tables1();
                        mysqlFindTables();
                    }else{
                        $("#mysql_oper_close").click();
                    }

                }

            }
        });//ajax
    }


}

function init_mysql_tables1() {
    //把复选框全部变成不被选中状态
    $("input[name='select_mysql_table']:checkbox").prop("checked", false);
    $("#mysql_all_select").prop("checked", false);
    //把json数据的key“tables”对应的值清空
    uploadTableName1=[];
    $("#mysql-table").html("");

}

function mysqlFindTables() {
    $.ajax({
        type:"GET",
        url:"/mysql/mysqlFindTables/"+global_id,
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        async:false,
        success:function(result){
            mysql_information1=result.payload;
            var tables=mysql_information1.tables;
            for(var i=0;i<tables.length;i++){
                if(tables[i].state=="false"){
                    //设置这一行数据颜色为灰色，并且复选框不可用
                    var tables_data="<tr  class=\"danger\"><td><input type=\"checkbox\" name='select_mysql_table_disabled' disabled='disabled'></td><td>"+mysql_information1.databaseName+"</td><td>"+tables[i].table_name+"</td></tr>"
                    $("#mysql-table").html($("#mysql-table").html()+tables_data);
                }else if(tables[i].state=="true"){
                    var tables_data="<tr><td><input type=\"checkbox\" name='select_mysql_table' onclick='OperTablesData1(this)'></td><td>"+mysql_information1.databaseName+"</td><td>"+tables[i].table_name+"</td></tr>"
                    $("#mysql-table").html($("#mysql-table").html()+tables_data);
                }

            }
        }
    });//ajax
}


