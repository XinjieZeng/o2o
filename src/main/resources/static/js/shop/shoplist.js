$(function() {

    getlist();
    function getlist(e) {
        $.ajax({
            url : "/o2o/shopadmin/getshoplist",
            type : "get",
            dataType : "json",
            success : function(data) {
                if (data.success) {
                    handleList(data.shopList);
                    handleUser(data.user);
                }
            }
        });
    }

    function handleUser(data) {
        $('#user-name').text(data.name);
    }

    //concatenate the row information
    function handleList(data) {
        var html =
            '<table class="ui celled striped table">' +
            '<thead>' +
            '<tr><th>Shop Name</th>' +
            '<th>State</th>' +
            '<th>Operation</th>' +
            '</tr></thead>' +
            '<tbody>';

        data.map(function(item, index) {
            html += '<tr><td data-label="name" class="right aligned collapsing">'
                + item.shopName + '</td><td data-label="status" class="right aligned">'
                + shopStatus(item.enableStatus)
                + '</td><td data-label="operation" class="right aligned">'
                + goShop(item.enableStatus, item.shopId) + '</td></tr>';

        });
        html += '</tbody>'
        $('.shop-wrap').html(html);
    }

    function shopStatus(status) {
        if (status == 0) {
            return 'under review';
        } else if (status == -1) {
            return 'illegal shop';
        } else if (status == 1) {
            return 'pass';
        }
    }

    function goShop(status, id) {
        if (status == 1) {
            return '<a href="/o2o/shopmanagement?shopId=' + id
                + '">Enter</a>';
        } else {
            return '';
        }
    }

})