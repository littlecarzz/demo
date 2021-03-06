var form, $,areaData;
layui.config({
    base : "../../js/"
}).extend({
    "address" : "address"
});
layui.use(['form','layer','upload','laydate',"address"],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address;

    //上传头像
    upload.render({
        elem: '.userFaceBtn',
        url: '../../json/userface.json',
        method : "get",  //此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
        done: function(res, index, upload){
            var num = parseInt(4*Math.random());  //生成0-4的随机数，随机显示一个头像信息
            $('#userFace').attr('src',res.data[num].src);
            window.sessionStorage.setItem('userFace',res.data[num].src);
        }
    });

    //添加验证规则
    form.verify({
        userBirthday : function(value){
            if(!/^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value)){
                return "出生日期格式不正确！";
            }
        }
    });
    //选择出生日期
    laydate.render({
        elem: '.userBirthday',
        format: 'yyyy年MM月dd日',
        trigger: 'click',
        max : 0,
        mark : {"0-12-15":"生日"},
        done: function(value, date){
            if(date.month === 12 && date.date === 15){ //点击每年12月15日，弹出提示语
                layer.msg('今天是马哥的生日，也是layuicms2.0的发布日，快来送上祝福吧！');
            }
        }
    });

    //获取省信息
    address.provinces();

    //提交个人资料
    form.on("submit(changeUser)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var sex;
        $("input[name='sex']").each(function () {
            var ch=$(this).prop("checked");
            if (ch==true) {
                sex = $(this).val();
            }
        });
        var username=$("#username").val();
        var mobile=$("#mobile").val();
        var email=$("#email").val();
        //将填写的用户信息存到session以便下次调取
        var userInfoHtml = '';
        userInfoHtml = {
            'username' : username,
            'sex' : sex,
            'mobile' : mobile,
            'email' : email
        };
        // window.sessionStorage.setItem("userInfo",JSON.stringify(userInfoHtml));
        $.post("/userModify",{
            username: username,
            sex: sex,
            mobile: mobile,
            email: email
        },function (data) {
            if (data =="success") {
                layer.close(index);
                layer.msg("提交成功！");
            }else if (data == "error") {
                layer.close(index);
                layer.msg("修改失败！");
            }
        });
/*        setTimeout(function(){
            layer.close(index);
            layer.msg("提交成功！");
        },2000);*/
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
});
