jQuery(document).ready(function () {
    $('#upload-form').submit(function (e) {
        e.preventDefault();

        var url = "/upload/" + $('#device-select').val();
        var fileInput = $('#file');
        var file;
        fileInput.change(function () {
            file = this.files[0];
        });

        var form = new FormData($('#upload-form')[0]);

        var beforeHandler = function () {

            }, successHandler = function () {

            },
            errorHandler = function () {

            };
        $.ajax({
            data: form,
            type: 'post',
            xhr: function () {
                var myXhr = $.ajaxSettings.xhr();
                if (myXhr.upload) {
                    myXhr.upload.addEventListener('progress', progressHandlingFunction, false)
                }
                return myXhr;
            },
            url: url,
            beforeSend: beforeHandler,
            success: successHandler,
            error: errorHandler,
            cache: false,
            contentType: false,
            processData: false
        });

        function progressHandlingFunction(e) {
            if (e.lengthComputable) {
                $('#progress').attr({value: e.loaded, max: e.total})
            }
        }
    });
    $('#temperature-table').DataTable({
        'ajax': '/dataPoints.json',
        'serverSide': true,
        columns: [{
            data: 'dateTime'
        }, {
            data: 'temperature'
        }]
    });
    var ctx = $('#temperature-chart');
    var chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            datasets: [{
                label: "Temperature",
                fill: true,
                data: [12.6, 34.6, 34.5, 87.9, 34.6, 24.6, 44.5, 76.5, 34.2, 56.4, 54, 23.8],
                borderColor: "#79D0B3",
                backgroundColor: "rgba(121, 208, 179, 0.2)"
            }]
        },
        options: {
            tooltips: {
                enabled: true,
                mode: 'label'

            }, hover: {
                mode: 'label'
            },
            legend: {
                display: false
            },
            scales: {
                xAxes: [{
                    gridLines: {
                        display: false
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Month"
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }],
                yAxes: [{
                    gridLines: {
                        display: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Temperature"
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });

});