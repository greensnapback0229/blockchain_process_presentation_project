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
                let walletLink = "/wallet/"+ wallet_name;
                alert(walletLink);
                // wallet_name 값을 URL 파라미터로 추가하여 새로운 페이지로 이동
                window.location.href = walletLink;
            },
            error:function (res){
                alert("지갑명이 길거나 이미 등록되었습니다.");
            }
        })
    }
};
main.init();
