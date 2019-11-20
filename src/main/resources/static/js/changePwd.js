var form, $,areaData;
layui.config({
    base : "../../js/"
}).extend({
    "address" : "address"
})
layui.use(['form','layer','upload','laydate',"address"],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer;
    //添加验证规则
    form.verify({
        oldPwd : function(value, item){
            var data;
            $.ajax({
                async: false,
                type: "POST",
                url: "/checkOldPwd",
                data: "oldPassword="+value,
                success: function(msg){
                    data=msg;
                }
            });
            if (data == "false") {
                return "密码错误！";
            }
        },
        newPwd : function(value, item){
            if(value.length < 6){
                return "密码长度不能小于6位！";
            }
        },
        confirmPwd : function(value, item){
            if(!new RegExp($("#oldPwd").val()).test(value)){
                return "两次输入密码不一致，请重新输入！";
            }
        }
    });
    //修改密码
    form.on("submit(changePwd)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post("/changePwd",{
            newPwd:$("#oldPwd").val(),
            username:$("#username").val()
        },function (data) {
            if (data =="success") {
                layer.close(index);
                layer.msg("密码修改成功！");
                $(".pwd").val('');
            }else if (data == "error") {
                layer.close(index);
                layer.msg("密码修改失败！");
                $(".pwd").val('');
            }
        });
        // setTimeout(function(){
        //     layer.close(index);
        //     layer.msg("密码修改成功！");
        //     $(".pwd").val('');
        // },2000);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })
});
