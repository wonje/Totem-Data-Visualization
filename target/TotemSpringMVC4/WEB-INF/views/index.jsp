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
            $(document).ready(function () {
                $.ajax({
                    url: "/deviceInfo",
                    method: "GET"
                }).done(function( data ) {
                    $("#devices").text(JSON.stringify(data));
                    var newData = [];
                    for(var i in data) {
                        var obj = data[i];
                        // convert string to integer
                        var hms = obj.timeStampHours;
                        var a = hms.split(':');
                        var seconds = (+a[0]) * 60 * 60 + (+a[1]) * 60 + (+a[2]);
                        newData.push([seconds, obj.volt * obj.amp])
                    }

                    chartObj.series[0].setData(newData, true);

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
                zoomType: 'x'
            },
            title: {
                text: 'USD to EUR exchange rate over time'
            },
            subtitle: {
                text: document.ontouchstart === undefined ?
                    'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: 'Exchange rate'
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: {
                            x1: 0,
                            y1: 0,
                            x2: 0,
                            y2: 1
                        },
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 2
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                type: 'area',
                name: 'USD to EUR',
                data: chartdata
            }]
        });
    </script>
    </p>
  </body>
</html>
