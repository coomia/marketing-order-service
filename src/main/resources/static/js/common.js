/**
 * Created by Administrator on 2018/8/8 0008.
 */
var contentPath="/MeiYe";

$.ajaxSetup({
    beforeSend: function (XMLHttpRequest) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        XMLHttpRequest.setRequestHeader(header,token);
    }
});

function loadContent(params){
    if(params.appendTo==null)
        params.appendTo="#content";
    if(params.displayLoading)
        loading($(params.appendTo).parent(),params.message);
    $.ajax({
        url:params.url,
        data:params.data,
        type:"POST",
        success:function(data){
            var target=$(data).html("").appendTo(params.appendTo);
            $.parser.parse(target);
        },
        error:function(){
            displayMessage("网络错误");
        },
        complete:function(){
            endLoading();
        }
    });
}

function displayMessage(message){
        $.messager.show({
            title:"提示信息",
            msg:message,
            showType:'show',
            style:{
                left:'',
                right:0,
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
        });
}

function loading(appendTo,message){
    if(appendTo==null||appendTo=="")
        appendTo="body";
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:"100%"}).appendTo(appendTo);
    if(message!=null)
        $("<div class=\"datagrid-mask-msg\"></div>").html(message).appendTo(appendTo).css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}
function endLoading(){
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}
