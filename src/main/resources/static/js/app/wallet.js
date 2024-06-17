var stompClient = null;
var stompServerUrl = "ws://13.54.41.253:8080/sock/conn";
var walletName;
var main = {
    init : function() {
        var _this = this;
        getWalletName();
        _this.connect();

        $('#send-money').on('click', function(){
            _this.sendMoney();
        });


        $('#mine-list-btn').on('click', function(){
            _this.miningWindow();
        });

        $(document).ready(function() {
            // 모달 창 내부의 버튼에 대한 클릭 이벤트 처리
            $('#exampleModal').on('click', '.mining-button', function() {
                _this.miningBlockFunc($(this).val());
                // 클릭된 버튼의 값을 가져와서 이후의 코드로 처리합니다.
                //alert("Clicked button value: " + notMinedCode);

                // 이후의 코드는 동일합니다.
            });
        });
    },
    connect : function (){
    console.log("connect() 실행");
    stompClient = Stomp.over(new WebSocket(stompServerUrl));
    stompClient.connect({}, onConnect, onError);
    },
    sendMoney : function(){
        var sendMoneyData = {
            "amount" : $('#amount').val(),
            "sender" : walletName,
            "receiver" :  $('#receiver-name').val()
        }
        var sendMoneyJson = JSON.stringify(sendMoneyData);
        sendMessage("/app/send-money", sendMoneyJson);
        alert("송금완료 채굴을 기다립니다.");
    },
    miningWindow : function (){
        getNotMined();
    },
    miningBlockFunc : function(code){
        var notMinedCode = code;
        //alert(notMinedCode);
        var miningReq = {
            "minerName" : walletName,
            "notMinedCode" : notMinedCode
        }
        console.log(miningReq);
        postMiningBlock(miningReq);
    }
}

var getNotMined = function(){
    $.ajax({
        url: '/wallet/not-mined',
        type: 'GET',
        success: function(response) {
            // 요청이 성공했을 때 JSON 데이터를 처리하여 테이블 생성
            console.log(response);
            createTable(response);
        },
        error: function(xhr, status, error) {
            console.error('GET 요청 실패:', status, error);
        }
    });
}

createTable = function(jsonData){
    var table;
    table += '<thead class="text-center">'
    table += '<tr>';
    table += '<th scope="col"> # </th>';
    table += '<th scope="col"> data </th>';
    table += '<th scope="col"> ⛏ </th>';
    table += '</tr>';
    table += '</thead>'

    table += '<tbody>'
    for (var i = 0; i < jsonData.notMindedCode.length; i++) {
        table += '<tr>';
        table += '<th scope="row" class="text-center">' + i + '</th>';
        table += '<td>' + jsonData.notMindedData[i] + '</td>';
        table += '<td>' +
            '<button type="button" class="btn btn-outline-dark mining-button"' +
            'value="' + jsonData.notMindedCode[i] + '" ' +
            '> ⛏ </button>';

        table += '</tr>';
    }
    table += '</tbody>'
    $('#table-container').html(table);
}

var onConnect = function (){
    console.log("연결 성공");
    stompClient.subscribe('/topic',
        function (message) {
            console.log('메시지 수신:', message.body);
            var jsonData = JSON.parse(message.body);
            updateAssert(jsonData);
            updateLedger(jsonData.ledger);
        }
    );
}

var onError = function (error) {
    console.error('연결 실패:', error);
};

var sendMessage = function(destination, message){
    stompClient.send(destination, {}, message);
}

var getWalletName = function(){
    var currentURL = window.location.href;
    walletName = currentURL.substring(currentURL.lastIndexOf('/') +1);
    console.log(walletName);
}

var postMiningBlock = function (miningReqJson){
    $.ajax({
        type: 'POST',
        url: '/mining-block',
        dataType: 'json',
        contentType:'application/json; charset=utf-8',
        data: JSON.stringify(miningReqJson),
        success : function (res) {
            alert(res.result);
        },
        error:function (res){
            alert(res.result);
        }
    })

}

var updateAssert = function (data){
    var senderName = data.senderName;
    var receiverName = data.receiverName;
    var minerName = data.minerName;
    var senderAssert = data.senderAssert;
    var receiverAssert = data.receiverAssert;
    var minerAssert = data.minerAssert;

    // 현재 사용자의 이름을 가져옵니다. 여기서는 walletName이라고 가정합니다.
    var walletTitle = $('#wallet-title').text().trim();
    var walletName = walletTitle.substring(walletTitle.indexOf(' ') + 1, walletTitle.indexOf('님'));


    // 현재 사용자의 이름이 포함된 경우 해당하는 Assert 값을 가져옵니다.
    var assertValue;
    console.log("sender="+ senderName + "rcv="+ receiverName  +"miner="+ minerName + walletName);
    if (senderName === walletName) {
        console.log(senderName + walletName);
        assertValue = senderAssert;
        $('#current-asset').text(assertValue + ' mibal');
    } else if (receiverName === walletName) {
        console.log(receiverName+ walletName);
        assertValue = receiverAssert;
        $('#current-asset').text(assertValue + ' mibal');
    } else if (minerName === walletName) {
        console.log(minerName+ walletName);
        assertValue = minerAssert;
        $('#current-asset').text(assertValue + ' mibal');
    }

    console.log(assertValue);
}

function updateLedger(ledgerData) {
    // id가 ledger인 태그를 선택합니다.
    var ledgerContainer = $('#ledger');

    // ledger 배열을 순회하면서 각 요소를 <h5> 태그로 감싸서 추가합니다.
    ledgerData.forEach(function(ledgerItem) {
        var h5 = $('<h5>').text(ledgerItem);
        ledgerContainer.append(h5).append('<br>');
    });
}

main.init();
