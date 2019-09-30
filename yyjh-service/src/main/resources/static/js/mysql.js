$(document).ready(function(){


    //选择数据源按钮弹窗
    db_btns=$("#showDrivers_choosen").find("div").find(".modal-body").find("button");

    //MYSQL配置弹窗
    $(db_btns[2]).click(function(){
        $("#showDrivers_choosen").modal("hide");
        $("#mysql-config").modal("show");
    });

    //关闭mysql配置时开启数据源选择页面
    $("#mysql-config-close").click(function(){
        $("#showDrivers_choosen").modal("show");
    });


});

var mysql_information={};
var password="";

function mysql_connect(){
    var user=$("#username").val();
    var port=$("#port").val();
    var database_name=$("#database-name").val();
    var host=$("#host").val();
    password=$("#password").val();
    //console.log(user+" "+password+" "+port+" "+host+" "+database_name)

    var connect_param={
        "host":host,
        "databaseName":database_name,
        "port":port,
        "username":user,
        "password":password
    };

    if(user!="" && port!="" && password!=""  && database_name!="" && host!=""){
        $.ajax({
            type:"GET",
            url:"/mysql/mysqlConfig",
            contentType:"application/json;charset=utf-8",
            data:{'connect_param':JSON.stringify(connect_param)},
            dataType:"json",
            async:false,
            success:function(result){

                //连接配置成功之后要上传到t—DataSource
                if(result.code==0){

                    flag= confirm("上传成功，是否继续上传");
                    if(flag){
                        //清空配置信息
                        document.getElementById("fill_config").reset();
                    }else{
                        $("#showDrivers_choosen").modal("show");
                        $("#mysql-config").modal("hide")

                    }


                }else if(result.code==-2){
                    alert("连接异常");
                }else if(result.code==-1){
                    alert("上传失败")
                }


            }
        });//ajax
    }else {
        alert("你还没有填写配置信息");
    }

}




