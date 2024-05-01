var wallet_name;
var main = {
    init : function(){
        var _this = this;
        $('#btn-register').on('click', function(){
           _this.register();
        });
    },
    register : function(){
        var data = {
            wallet_name: $('#wallet-name').val()
        };

        $.ajax({
            type: 'POST',
            url: '/main/register',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data),
            success : function (res) {
                wallet_name = res.wallet_name;
                alert(res.wallet_name);
                alert("등록됨");
            },
            error:function (res){
                alert("지갑명이 길거나 이미 등록되었습니다.");
            }
        });
    }
};
main.init();
