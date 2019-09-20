layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#userList',
        url : '/userList',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'username', title: '用户名', minWidth:80, align:"center"},
            {field: 'sex', title: '性别', width:60, align:'center'},
            {field: 'email', title: '邮箱', minWidth:80, align:'center',templet:function(d){
                    return '<a class="layui-blue" href="mailto:'+d.email+'">'+d.email+'</a>';
                }},
            /*{field: 'email', title: '邮箱', Width:80, align:"center"},*/
            {field: 'mobile', title: '手机号码', minWidth:80, align:"center"},
            {field: 'roles', title: '角色', minWidth:50, align:'center'},
            {field: 'status', title: '状态', width:60, align:'center',templet:function(d){
                    return d.status == 0 ? "禁用" : "启用";
                }},
            {field: 'lastTime', title: '最后登录时间', align:'center',minWidth:100,templet:function (d) {
                    return (d.lastTime==null) ?"无":d.lastTime;
                }},
            {title: '操作', minWidth:190,fixed:"right",align:"center",templet:"#userListBar"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("userListTable",{
                url:"/userList",
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    username: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加用户
    function addUser(edit){
        var index = layui.layer.open({
            title : "添加用户",
            type : 2,
            content : "/toUserAdd",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    console.log(edit.sex);
                    console.log(edit.status);
                    console.log(edit.roles);
                    body.find(".username").val(edit.username);  //登录名
                    console.log(body.find(".sex input[value='" + edit.sex + "']"));
                    console.log(body.find(".status"));
                    body.find(".userSex input[value="+edit.sex+"]").prop("checked","checked");  //性别
                    // body.find(".sex input[value!='" + edit.sex + "']").prop("checked", false);
                    body.find(".userStatus").val(edit.status);
                    body.find(".email").val(edit.email);  //邮箱
                    body.find(".mobile").val(edit.mobile);  //手机
/*                    var role=edit.roles.split("/");
                    for (var i = 0; i < role.length; i++) {
                        if (role[i] == "管理员") {
                            $("#role input[value='ADMIN']").prop("checked","checked");
                        }
                        if (role[i] == "普通用户") {
                            $("#role input[value='USER']").prop("checked","checked");
                        }
                    }*/
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }
    $(".addNews_btn").click(function(){
        addUser();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            usernames = "";
        if(data.length > 0) {
            for (var i in data) {
                if(i==data.length-1){
                    usernames+=data[i].username;
                }else{
                    usernames+=data[i].username+",";
                }
            }
            layer.confirm('确定删除选中的用户？', {icon: 3, title: '提示信息'}, function (index) {
                $.get("/userDel",{
                    username : usernames //将需要删除的newsId作为参数传入
                },function(data){
                    if (data =="success") {
                        tableIns.reload();
                        layer.close(index);
                    }else if (data == "error") {
                        layer.msg("用户删除失败！");
                    }
                })
            })
        }else{
            layer.msg("请选择需要删除的用户");
        }
    })

    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addUser(data);
        }else if(layEvent === 'usable'){ //启用禁用
            alert(data);
            if (data.status==1) {
                usableText = "是否确定禁用此用户？",
                    btnText = "已禁用";
            }else{
                usableText = "是否确定启用此用户？",
                    btnText = "已启用";
            }
            var _this = $(this),
                usableText = "是否确定禁用此用户？",
                btnText = "已禁用";
            if(_this.text()=="已禁用"){
                usableText = "是否确定启用此用户？",
                btnText = "已启用";
            }
            layer.confirm(usableText,{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){
                _this.text(btnText);
                layer.close(index);
            },function(index){
                layer.close(index);
            });
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此用户？',{icon:3, title:'提示信息'},function(index){
                $.get("/userDel",{
                    username : data.username //参数传入
                },function(data){
                    if (data =="success") {
                        tableIns.reload();
                        layer.close(index);
                    }else if (data == "error") {
                        layer.msg("用户删除失败！");
                    }

                })
            });
        }
    });

})
