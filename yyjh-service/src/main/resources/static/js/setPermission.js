$(document).ready(function(){
	var userId=[];
	$.ajax({
		type:"get",
		url:"/TUser/selectTUser",
		contentType:"application/json;charset=utf-8",
        async:false,
        success:function(result) {
            console.log(result)
			var option="";
			for (i=0;i<result.length;i++){
                userId[i]=result[i].loginid;
                if(userId[i]!="admin")
                   option=option+"<option>"+userId[i]+"</option>";
			}
            $("#choose_user").html(option)

        }
	});
	var roleId=[];
    $.ajax({
        type:"get",
        url:"/TRole/selectRole",
        contentType:"application/json;charset=utf-8",
        async:false,
        success:function(result) {
            console.log(result)
            var option="";
            var opp="";
            for (i=0;i<result.length;i++){
                roleId[i]=result[i].rolename;
                if(roleId[i]!="supermanager"){
                    option=option+"<option>"+roleId[i]+"</option>";
                    opp=opp+"<div>"+roleId[i]+"&nbsp;&nbsp;&nbsp;<input name='roles' type='checkbox' value="+roleId[i]+"></div>";
                }
            }
            $("#choose_role").html(option)
            $("#set_role").html(opp)
        }
    });

    $("#role_submit").click(function () {

        $.ajax({
            type:"GET",
            url:"/TUser/operUserRole",
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(result){
                if(result.code==1){
                    var username=$("#choose_user").val();
                    var chk_roles=[];
                    $.each($("input[name='roles']:checkbox"),function(){
                        if(this.checked){
                            chk_roles.push($(this).val());
                        }
                    });
                    console.log(chk_roles)
                    var data={"chk_roles":chk_roles,
                        "username":username
                    };
                    $.ajax({
                        type:"get",
                        url:"/RoleShiro/setUserRole",
                        contentType:"application/json;charset=utf-8",
                        data:{"json":JSON.stringify(data)},
                        dataType:"json",
                        async:false,
                        success:function(result) {
                            console.log(result)
                            if(result.code==1){
                                alert("设置角色成功！");
                                $("input[name='roles']:checkbox").prop("checked",false);
                                $("#choose_user").val("");
                            }else{
                                alert("设置角色失败！")
                            }
                        }
                    });
                }
            },
            error:function () {
                alert("该账户不具有设置角色的权限！");
            }
        });


    });
    $("#permission_submit").click(function () {
        $.ajax({
            type:"GET",
            url:"/TUser/operRolePerm",
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(result){
                if(result.code==1){
                    var rolename=$("#choose_role").val();
                    var chk_permission=[];
                    $.each($("input[name='permissions']:checkbox"),function(){
                        if(this.checked){
                            chk_permission.push($(this).val());
                        }
                    });
                    console.log(chk_permission)
                    var data={"chk_permission":chk_permission,
                        "rolename":rolename
                    };
                    $.ajax({
                        type:"get",
                        url:"/RoleShiro/setRolePermission",
                        contentType:"application/json;charset=utf-8",
                        data:{"json":JSON.stringify(data)},
                        dataType:"json",
                        async:false,
                        success:function(result) {
                            console.log(result)
                            if(result.code==1){
                                alert("设置权限成功！")
                                $("input[name='permissions']:checkbox").prop("checked",false);
                                $("#choose_role").val("");
                            }else{
                                alert("设置权限失败！")
                            }
                        }
                    });
                }
            },
            error:function () {
                alert("该账户不具有设置权限的权限！");
            }
        });

    })
})
