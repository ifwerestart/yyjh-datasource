$(document).ready(function() {

    $(".reg").click(function(){
        $(".dowebok").css("display","none");
        $("#register_modal input").val("");
        $("#img_preview").empty();
        $("#register_modal span").html("")
		$("#showDrivers_choosen").modal("hide")
		$("#register_modal").modal("show")
	})
    img_load("#userimg");
    // $(".reg").click(function(){
    //     alert("11111")
    // });
});


$("#userimg").change(function(e) {
	$("#img_preview").empty()
    var imgBox = e.target;
	uploadImg($('#img_preview'), imgBox)
});
function uploadImg(element, tag) {
    var file = tag.files[0];

    var imgSrc;
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function() {
        imgSrc = this.result;
        var imgs = document.createElement("img");
        imgs.style.width = '70px';
        imgs.style.height = '70px';
        imgs.style.borderRadius = '50%'
        $(imgs).attr("src", imgSrc);
        element.append(imgs);
    };
}

function exchange_img() {
    $("#img_choose").css("display","block")
    $("#img").css("display","none")
    $("#img_preview").empty()
}
var imgFile_data=[]

function img_url(element_id, url) {
    $(element_id).fileupload(
        'option',
        'url',
        url
    );
}
function img_load(element_id) {

    $(element_id).fileupload({
       // url: '/img/imgUpload',
        type: 'POST',
        dataType: 'json',
        autoUpload: false,
        acceptFileTypes: /(\.|\/)(jpg)$/i,
        add: function(e, data) {
           // console.log(data.files[0])
            imgFile_data = data
            imgFile_data.files[0]=data.files[0]
           // console.log(imgFile_data)

            //显示文件全名，作业显示改成bootstrap table
            filename = data.files[0].name

			$("#img_choose").css("display","none")
            $("#img").css("display","block")
            $("#img").val(filename)
            //alert("上传成功")
            img_url("#userimg","/img/imgUpload");
            imgFile_data.submit()
        },
        done: function(e, data) {
          //  alert("1111111111")
           // console.log("1111111"+data.result)
            result = data.result
            if(result.code==0){
                //alert("this is done")
                if(result.hasOwnProperty("payload") && result.payload!=null){
                    real_filename = result.payload

                }else{
                    real_filename = null
                }

            }else{
                real_filename = null
            }
        }
    });
}

var loginId_flag = false;
var password_flag = false;
var repassword_flag = false;
var tel_flag = false;
var email_flag = false;

$("#loginId").blur(function () {
    loginId_flag = false
    var img_error = document.createElement("img");
    img_error.src = '../../img/error.png'

    var img_success = document.createElement("img");
    img_success.src = '../../img/ok.png'

    var loginId = $("#loginId").val();
    if(loginId == "" || loginId == "null"){
        $("#prompt_msg_loginId").empty()
        $("#prompt_msg_loginId").append(img_error);
        $("#prompt_msg_loginId").append("不可为空")
        $("#prompt_msg_loginId").css("color","red")
        loginId_flag = false
    }else{
        $.ajax({
            type:"POST",
            url:"/User/findTUserByLoginId/"+loginId,
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(data){
                var reg = /^[a-zA-Z]([-_a-zA-Z0-9]{3,20})$/;
                if(data!=null&&data!=""){
                    $("#prompt_msg_loginId").empty()
                    $("#prompt_msg_loginId").append(img_error);
                    $("#prompt_msg_loginId").append("账户已存在")
                    $("#prompt_msg_loginId").css("color","red")
                    loginId_flag = false
                }else if(!reg.test(loginId)){
                    $("#prompt_msg_loginId").empty()
                    $("#prompt_msg_loginId").append(img_error);
                    $("#prompt_msg_loginId").append("格式不正确")
                    $("#prompt_msg_loginId").css("color","red")
                    loginId_flag = false
                }else{
                    $("#prompt_msg_loginId").empty()
                    $("#prompt_msg_loginId").append(img_success);
                    $("#prompt_msg_loginId").append("验证成功")
                    $("#prompt_msg_loginId").css("color","green")
                    loginId_flag = true
                }

            }
        });
    }
})
$("#password").blur(function () {
    password_flag = false
    var img_error = document.createElement("img");
    img_error.src = '../../img/error.png'

    var img_success = document.createElement("img");
    img_success.src = '../../img/ok.png'

    var password =$("#password").val();
    if(password == "" || password == "null"){
        $("#prompt_msg_password").empty()
        $("#prompt_msg_password").append(img_error);
        $("#prompt_msg_password").append("不可为空")
        $("#prompt_msg_password").css("color","red")
        password_flag = false
    }else{
        var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/;
        if(!reg.test(password)){
            $("#prompt_msg_password").empty()
            $("#prompt_msg_password").append(img_error);
            $("#prompt_msg_password").append("格式不正确")
            $("#prompt_msg_password").css("color","red")
            password_flag = false
        }else {
            $("#prompt_msg_password").empty()
            $("#prompt_msg_password").append(img_success);
            $("#prompt_msg_password").append("验证成功")
            $("#prompt_msg_password").css("color","green")
            password_flag = true
        }
    }
})

$("#re_password").blur(function () {
    repassword_flag = false
    var img_error = document.createElement("img");
    img_error.src = '../../img/error.png'

    var img_success = document.createElement("img");
    img_success.src = '../../img/ok.png'

    var re_password =$("#re_password").val();
    var password =$("#password").val();
    if(re_password == "" || re_password == "null"){
        $("#prompt_msg_re_password").empty()
        $("#prompt_msg_re_password").append(img_error);
        $("#prompt_msg_re_password").append("不可为空")
        $("#prompt_msg_re_password").css("color","red")
        repassword_flag = false
    }else{

        if(password!=re_password){
            $("#prompt_msg_re_password").empty()
            $("#prompt_msg_re_password").append(img_error);
            $("#prompt_msg_re_password").append("密码不一致")
            $("#prompt_msg_re_password").css("color","red")
            repassword_flag = false
        }else {
            $("#prompt_msg_re_password").empty()
            $("#prompt_msg_re_password").append(img_success);
            $("#prompt_msg_re_password").append("验证成功")
            $("#prompt_msg_re_password").css("color","green")
            repassword_flag = true
        }
    }
})
$("#nickname").blur(function () {
    var nickname =$("#nickname").val();

})

$("#tel").blur(function () {
    tel_flag = false
    var img_error = document.createElement("img");
    img_error.src = '../../img/error.png'

    var img_success = document.createElement("img");
    img_success.src = '../../img/ok.png'

    var tel = $("#tel").val();
    var leg = /^1([38]\d|5[0-35-9]|7[3678])\d{8}$/;

    if(tel == "" || tel == null){
        $("#prompt_msg_tel").empty()
        $("#prompt_msg_tel").append(img_error);
        $("#prompt_msg_tel").append("不可为空")
        $("#prompt_msg_tel").css("color","red")
        tel_flag = false
    }else{
        if(!leg.test(tel)){

            $("#prompt_msg_tel").empty()
            $("#prompt_msg_tel").append(img_error);
            $("#prompt_msg_tel").append("格式不正确")
            $("#prompt_msg_tel").css("color","red")
            tel_flag = false
        }else {
            $("#prompt_msg_tel").empty()
            $("#prompt_msg_tel").append(img_success);
            $("#prompt_msg_tel").append("验证成功")
            $("#prompt_msg_tel").css("color","green")
            tel_flag = true
        }
    }

})

$("#email").blur(function () {
    email_flag = false
    var img_error = document.createElement("img");
    img_error.src = '../../img/error.png'

    var img_success = document.createElement("img");
    img_success.src = '../../img/ok.png'

    var email =$("#email").val();
    if(email == "" || email == "null"){
        $("#prompt_msg_email").empty()
        $("#prompt_msg_email").append(img_error);
        $("#prompt_msg_email").append("不可为空")
        $("#prompt_msg_email").css("color","red")
        email_flag = false
    }else{
        var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        if(!reg.test(email)){
            $("#prompt_msg_email").empty()
            $("#prompt_msg_email").append(img_error);
            $("#prompt_msg_email").append("格式不正确")
            $("#prompt_msg_email").css("color","red")
            email_flag = false
        }else {
            $("#prompt_msg_email").empty()
            $("#prompt_msg_email").append(img_success);
            $("#prompt_msg_email").append("验证成功")
            $("#prompt_msg_email").css("color","green")
            email_flag = true
        }
    }
})

function check_msg() {
    var loginId = $("#loginId").val();
    var password =$("#password").val();
    var nickname =$("#nickname").val();
    if(nickname =="" || nickname == null){
        nickname = "无";
    }
    var tel = $("#tel").val();
    var email =$("#email").val();
    var remark = $("#remark").val();
    if(remark == "" || remark == null){
        remark = "无";
    }
    var filename = real_filename.toString();

    //alert(filename)
    if(filename == "" || filename == null){
        filename = "无";
    }
    if (loginId_flag&&password_flag&&repassword_flag&&tel_flag&&email_flag) {
        //alert("全部验证成功")
        $.ajax({
            type:"POST",
            url:"/User/addTUser/"+loginId+"/"+password+"/"+nickname+"/"+filename+"/"+email+"/"+tel+"/"+remark,
            contentType:"application/json;charset=utf-8",
            async:false,
            success:function(data){
                if(data==1){
                    alert("注册成功,请登录")
                    $("#showDrivers_choosen").modal("show")
                    $("#register_modal").modal("hide")
                    $("#img_choose").css("display","block")
                    $("#img").css("display","none")
                    $("#img_preview").empty()
                    $(".dowebok").css("display","block");
                }else{
                    alert("未知原因注册失败，请稍后重试")
                    $("#showDrivers_choosen").modal("show")
                    $("#register_modal").modal("hide")
                    $("#img_choose").css("display","block")
                    $("#img").css("display","none")
                    $("#img_preview").empty()
                }

            }
        });
    }else{
        alert("请注意提示，有信息格式不正确")
    }
}





