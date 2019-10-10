var zh = 0;
var YJM="";
var mail=0;
var cod=0;
var newpwd=0;
var repwd=0;

var InterValObj; //timer变量，控制时间
var count = 60; //间隔函数，1秒执行
var curCount;//当前剩余秒数
$(document).ready(function(){

    //登录
    $("#submit").click(function(){
        var login_username=$("#login_username").val();
        var login_password=$("#login_password").val();
        var data={"login_username":login_username,"login_password":login_password};
        $.ajax({
            type:"GET",
            url:"/TUser/selectByLoginid",
            contentType:"application/json;charset=utf-8",
            data:{"json":JSON.stringify(data)},
            dataType:"json",
            async:false,
            success:function(result){
                if(result.code==1){
                    alert("登录成功！");
                    window.location.href='/router/datasource';
                }else if(result.code==0){
                    alert("用户名或密码错误！");
                }
            }
        });
    });

    //忘记密码
    $(".forget").click(function(){
        $(".dowebok").css("display","none");
        $("#forgetpwd").modal("show");
    });
    $("#fogmove").click(function(){
        initforgetpwd();
        $(".dowebok").css("display","block");
    });

    $("#registerback").click(function () {
        $(".dowebok").css("display","block");
        $("#register_modal").modal("hide")
    })

    $("#fogusername").blur(function(){
        //到数据库查找用户名
        var fogusername=$("#fogusername").val();
        $.ajax({
            type:"GET",
            url:"/TUser/proveusername",
            contentType:"application/json;charset=utf-8",
            //我们使用 JSON.stringify() 方法处理json，将其转换为字符串；
            data:{
                'json':JSON.stringify({'fogusername':fogusername})
            },
            dataType:"json",
            async:false,
            success:function(result){
                if(result.code==1){
                    $("#icon_username").html("<img id='m1' src='../img/ok.png'/>");
                    zh=1;
                }else if(result.code==0){
                    $("#icon_username").html("<img id='m1' src='../img/error.png'/>用户名不存在");
                    zh=0;
                }
            }
        });

    });
    $("#fogemail").blur(function () {
        if(zh==1){
            var data={"username":$("#fogusername").val(),
                "email":$("#fogemail").val()};
            $.ajax({
                type:"GET",
                url:"/email/checkemail",
                contentType:"application/json;charset=utf-8",
                //我们使用 JSON.stringify() 方法处理json，将其转换为字符串；
                data:{
                    'json':JSON.stringify(data)
                },
                dataType:"json",
                async:false,
                success:function(result){
                    if(result.code==1){
                        $("#e_email").css("display","none");
                        $("#icon_email").css("display","block");
                        mail=1;
                    }else if(result.code==0){
                        $("#icon_email").css("display","none");
                        $("#e_email").css("display","block");
                        $("#e_email").html("<img id='m1' src='../img/error.png'/>邮箱不存在");
                        mail=0;
                    }
                }
            });
        }else {
            alert("用户名错误");
        }

    });


    //发送验证码
    $("#submitemail").click(function () {
        if($("#fogemail").val() !="" && $("#fogemail").val()!=null){
            if(zh==0){
                alert("请输入正确的用户名！");
            }
            else{
                var key =parseInt((Math.random()*9+1)*100000);
                JYM=""+key;
                var data={"username":$("#fogusername").val(),
                    "email":$("#fogemail").val(),
                    "JYM":JYM};
                $.ajax({
                    type:"GET",
                    url:"/email/sendEmail",
                    contentType:"application/json;charset=utf-8",
                    data:{
                        'json':JSON.stringify(data)
                    },
                    dataType:"json",
                    async:false,
                    success:function(result){
                    }
                });
                curCount = count;
                //设置button效果，开始计时
                $("#submitemail").attr("disabled", "true");
                $("#submitemail").val(curCount + "秒后刷新");
                InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
            }
        }else{
            $("#icon_email").css("display","none");
            $("#e_email").css("display","block");
            $("#e_email").html("<img id='m1' src='../img/error.png'/>邮箱不能为空");
        }

    });


    $("#fogcode").blur(function () {
        var fogcode=$("#fogcode").val();
        if(fogcode==JYM){
            $("#icon_code").html("<img id='m1' src='../img/ok.png'/>");
            cod=1;
        }else {
            $("#icon_code").html("<img id='m1' src='../img/error.png'/>验证码错误");
            cod=0;
        }
    });

    $("#fognewpwd").blur(function () {
        var fognewpwd=$("#fognewpwd").val();
        var pwd=/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/;
        if(pwd.test(fognewpwd)){
            newpwd=1;
            $("#icon_newpwd").html("<img id='m1' src='../img/ok.png'/>");
        }else {
            $("#icon_newpwd").html("<img id='m1' src='../img/error.png'/>密码格式不正确");
            newpwd=0;
        }
    });

    $("#fogrepwd").blur(function () {
        var fogrepwd=$("#fogrepwd").val();
        var fognewpwd=$("#fognewpwd").val();
        if (fognewpwd==fogrepwd){
            $("#icon_repwd").html("<img id='m1' src='../img/ok.png'/>");
            repwd=1;
        }else {
            $("#icon_repwd").html("<img id='m1' src='../img/error.png'/>密码不一致");
            repwd=0;
        }
    });
    
    $("#forgetsubmit").click(function () {
        if(zh==1 && mail==1 && cod==1 &&newpwd==1 &&repwd==1){
            var data={"loginid":$("#fogusername").val(),
                "password":$("#fognewpwd").val()};
            $.ajax({
                type:"GET",
                url:"/TUser/changepwd",
                contentType:"application/json;charset=utf-8",
                data:{
                    'json':JSON.stringify(data)
                },
                dataType:"json",
                async:false,
                success:function(result){
                    console.log(result)
                    if(result.code==1){
                        alert("修改密码成功！");
                        $(".dowebok").css("display","block");
                        $("#forgetpwd").modal("hide");
                        initforgetpwd();
                    }else{
                        alert("修改密码失败！");
                    }
                }
            });
        }else{
            alert("请确认所填的信息是否正确！");
        }
    });


    //注册


});


function SetRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);//停止计时器
        $("#submitemail").removeAttr("disabled");//启用按钮
        $("#submitemail").val("重新发送");
        JYM="";
    }
    else {
        curCount--;
        $("#submitemail").val(curCount + "秒后刷新");
    }
}

function initforgetpwd() {
    //初始化忘记密码
    $("#fogusername").val("");
    $("#fogemail").val("");
    $("#fogcode").val("");
    $("#fognewpwd").val("");
    $("#fogrepwd").val("");
    $("#icon_username").html("");
    $("#icon_code").html("");
    $("#icon_newpwd").html("");
    $("#icon_repwd").html("");
    $("#e_email").css("display","none");
    $("#icon_email").css("display","block");
    zh=0;mail=0;cod=0;newpwd=0;repwd=0;
}