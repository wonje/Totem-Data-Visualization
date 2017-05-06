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

      <script type="text/javascript">
          var devicesName = {};
          var nameCount = 0;
            $(document).ready(function () {
                $.ajax({
                    url: "/deviceInfo",
                    method: "GET"
                }).done(function( data ) {
//                    $("#devices").text(JSON.stringify(data));
//                    var newData = [];
//                    var DataSet = {};
                    var dataSet = {};
                    for(var i in data) {
                        var obj = data[i];
                        // TODO Check the device name is already exists or not
                        if(!dataSet.hasOwnProperty(obj.totemDevice)){
//                            devicesName[obj.totemDevice] = nameCount++;
                            dataSet[obj.totemDevice] = {name: obj.totemDevice, data: []};
                        }

                        dataSet[obj.totemDevice].data.push([obj.timeStamp, obj.volt * obj.amp]);

//                        newData.push([obj.timeStamp, obj.volt * obj.amp])

                    }
                    var charSeries = [];
                    for (var key in dataSet) {
                        charSeries.push(dataSet[key]);
                    }

//                    devicesName.forEach(function(name){
//
////                    });
//                    for (var key in dataSet) {
//                        chartObj.series[devicesName[dataSet[key][0].totemDevice]].setData(dataSet[key], true);
//                    }

                    // Delete all existing data
                    var seriesLength = chartObj.series.length;
                    for(var i = seriesLength -1; i > -1; i--) {
                        chartObj.series[i].remove();
                    }

                    // then replace all data
                    for(var i in charSeries) {
                        chartObj.addSeries(charSeries[i]);
                    }

//                    dataSet.forEach(function(data){
//                        chartObj.series[data[0].totemDevice].setData(data, true);
//                    });


//                    chartObj.series[0].setData(newData, true);

                }).fail(function() {
                    alert( "Ajaxing device data failed!" );
                    $("#devices").text(":(");
                }).always(function() {
                    //alert( "complete" );
                });
            });
      </script>
    <title>Display Current Data</title>
  </head>
  <body>
    <h1>Totem Internship Assignment</h1><br>
    <h2>Display All data set</h2><br>
  <p id="device">
      <h2 id="devices"></h2>
    <h2 id="test"></h2>

    <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
    <script>
        var chartdata = [[0,0]];

        var chartObj = Highcharts.chart('container', {

            chart: {
                type: 'area',
                spacingBottom: 30,
                zoomType: 'x'
            },
            title: {
                text: 'Fruit consumption *'
            },
            subtitle: {
                text: '* Jane\'s banana consumption is unknown',
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
//                labels: {
//                    formatter: function () {
//                        return this.value;
//                    }
//                }
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
