ACC.paginationdate = {
    bindAll: function ()
    {
        this.bindPaginationDate();
    },
    bindPaginationDate : function () {
        with (ACC.paginationdate)
        {
            bindDateForm($('#dateForm1'));
            bindDateForm($('#dateForm2'));
        }
    },
    bindDateForm: function (dateForm)
    {
        dateForm.change(function ()
        {
            console.log("changed");
            this.submit();
        });
    }
};

$(document).ready(function ()
{
    ACC.paginationdate.bindAll();
});