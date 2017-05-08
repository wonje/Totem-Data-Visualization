<%--
  Created by IntelliJ IDEA.
  User: wonje
  Date: 5/1/17
  Time: 10:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
      <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
      <script src="https://code.highcharts.com/highcharts.js"></script>
      <script src="https://code.highcharts.com/modules/exporting.js"></script>
      <script src="http://code.jquery.com/jquery-latest.js"> </script>
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

      <!-- Include Required Prerequisites -->
      <script type="text/javascript" src="//cdn.jsdelivr.net/jquery/1/jquery.min.js"></script>
      <script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
      <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap/3/css/bootstrap.css" />

      <!-- Include Date Range Picker -->
      <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
      <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />

      <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
      <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
      <style>
          body,h1 {font-family: "Raleway", sans-serif}
          body, html {height: 100%}
          .bgimg {
              background-image: url('https://www.w3schools.com/w3images/forestbridge.jpg');
              min-height: 100%;
              background-position: center;
              background-size: cover;
          }
      </style>

      <script type="text/javascript">
            var ajaxCall = function (startTime, endTime) {
                $.ajax({
                    url: "/deviceInfo",
                    method: "GET",
                    data: {startTime: startTime, endTime: endTime}
                }).done(function( data ) {
                    var dataSet = {};
                    for(var i in data) {
                        var obj = data[i];
                        // TODO Check the device name already exists or not
                        if(!dataSet.hasOwnProperty(obj.totemDevice)){
                            dataSet[obj.totemDevice] = {name: obj.totemDevice, data: []};
                        }

                        dataSet[obj.totemDevice].data.push([obj.timeStamp, obj.volt * obj.amp]);

                    }
                    var charSeries = [];
                    for (var key in dataSet) {
                        charSeries.push(dataSet[key]);
                    }

                    // Delete all existing data
                    var seriesLength = chartObjForCassandra.series.length;
                    for(var i = seriesLength -1; i > -1; i--) {
                        chartObjForCassandra.series[i].remove();
                    }

                    // then replace all data
                    for(var i in charSeries) {
                        chartObjForCassandra.addSeries(charSeries[i]);
                    }

                }).fail(function() {
                    alert( "Ajaxing device data failed!" );
                    $("#devices").text(":(");
                }).always(function() {
                    //alert( "complete" );
                });
            };

            // FIXME DO SOMETHING
            var ajaxCall_Postgre = function () {
                $.ajax({
                    url: "/postgre",
                    method: "GET",
                }).done(function( data ) {
                    var dataSet = {};
                    for(var i in data) {
                        var obj = data[i];
                        // TODO Check the device name already exists or not
                        if(!dataSet.hasOwnProperty(obj.totemDevice)){
                            dataSet[obj.totemDevice] = {name: obj.totemDevice, data: []};
                        }

                        dataSet[obj.totemDevice].data.push([obj.timeStamp, obj.volt * obj.amp]);

                    }
                    var charSeries = [];
                    for (var key in dataSet) {
                        charSeries.push(dataSet[key]);
                    }

                    // Delete all existing data
                    var seriesLength = chartObjForPostgre.series.length;
                    for(var i = seriesLength -1; i > -1; i--) {
                        chartObjForPostgre.series[i].remove();
                    }

                    // then replace all data
                    for(var i in charSeries) {
                        chartObjForPostgre.addSeries(charSeries[i]);
                    }

                }).fail(function() {
                    alert( "Ajaxing device data failed!" );
                    $("#devices").text(":(");
                }).always(function() {
                    //alert( "complete" );
                });
            };

            $(document).ready(function () {
                    ajaxCall(0,new Date().getTime());
                    ajaxCall_Postgre();
            });


      </script>
    <title>Display Current Data</title>
  </head>
  <body>
  <div class="bgimg w3-display-container w3-animate-opacity w3-text-grey">
      <div class="w3-display-topleft w3-padding-large w3-xlarge">
          <img src="//static1.squarespace.com/static/56104a4ee4b0ffa1f98ffcfc/t/5612fc32e4b0d85879e4f1ad/1492952904688/?format=1500w" height="60" width="200">
      </div>
      <div class="w3-display-containers">
          <h1 class="w3-jumbo w3-animate-top w3-center">Totem Internship Assignment</h1>
          <hr class="w3-border-grey" style="margin:auto;width:70%">
              <p class="w3-xxlarge w3-center">Choose Date within 7days</p>
          <div class="w3-large w3-center">
          <input type="text" name="daterange" style="width : 300px;" value="" />
          </div>
          <P></P>
          <hr class="w3-border-grey" style="margin:auto;width:70%">
              <p class="w3-xxlarge w3-center">Plot All Data Set</p>
          <h2 class="w3-large w3-center" id="devices"></h2>
      <div class="w3-large w3-center" id="container" style="height: 500px; margin: 0 auto"></div>
          <div class="w3-large w3-center" id="container_postgre" style="height: 500px; margin: 0 auto"></div>

  </div>
      <div class="w3-display-bottomleft w3-padding-large w3-large">
          <b>Wonje Kang</b>
      </div>
  </div>

    <script>
            var startDate;
            var endDate;

            $(function() {
                $('input[name="daterange"]').daterangepicker({
                    "autoApply": true,
                    "dateLimit": {
                        "days": 7
                    },
                    "showCustomRangeLabel": false,
                    "startDate": "05/08/2017",
                    "endDate": "05/09/2017"
                });
                $('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
                    startDate = picker.startDate;
                    endDate = picker.endDate;
                    alert("From " + new Date(startDate) + " to " + new Date(endDate));
                    ajaxCall(new Date(startDate).getTime(), new Date(endDate).getTime());
                });

            });




        var chartdata = [[0,0]];

        var chartObjForCassandra = Highcharts.chart('container', {

            chart: {
                type: 'area',
                spacingBottom: 30,
                zoomType: 'x'
            },
            title: {
                text: 'Totem Power : Watts over time (Cassandra)'
            },
            subtitle: {
                text: 'Time',
                floating: true,
                align: 'right',
                verticalAlign: 'bottom',
                y: 15
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                verticalAlign: 'top',
                x: 150,
                y: 100,
                floating: true,
                borderWidth: 1,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: 'Watts'
                },
//
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                        this.x + ': ' + this.y;
                }
            },
            plotOptions: {
                area: {
                    fillOpacity: 0.5
                }
            },
            credits: {
                enabled: false
            },
            series: [
                {
                name: 'wait...',
                data: []
                }
            ]
        });

            var chartObjForPostgre = Highcharts.chart('container_postgre', {

                chart: {
                    type: 'area',
                    spacingBottom: 30,
                    zoomType: 'x'
                },
                title: {
                    text: 'Totem Power : Average Watts over time (PostgreSQL)'
                },
                subtitle: {
                    text: 'Time',
                    floating: true,
                    align: 'right',
                    verticalAlign: 'bottom',
                    y: 15
                },
                legend: {
                    layout: 'vertical',
                    align: 'left',
                    verticalAlign: 'top',
                    x: 150,
                    y: 100,
                    floating: true,
                    borderWidth: 1,
                    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
                },
                xAxis: {
                    type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: 'Watts'
                    },
//
                },
                tooltip: {
                    formatter: function () {
                        return '<b>' + this.series.name + '</b><br/>' +
                            this.x + ': ' + this.y;
                    }
                },
                plotOptions: {
                    area: {
                        fillOpacity: 0.5
                    }
                },
                credits: {
                    enabled: false
                },
                series: [
                    {
                        name: 'wait...',
                        data: []
                    }
                ]
            });
    </script>
    </p>
  </body>
</html>
