<%--
  Created by IntelliJ IDEA.
  User: Matteo
  Date: 18/08/2018
  Time: 18:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trova i negozi vicini a te</title>
</head>
<body>
    <p id="errText"></p>
    <div id="mapHolder" style="height:250px" ></div>
    <script src="http://www.openlayers.org/api/OpenLayers.js"></script>
    <script>
        var text = document.getElementById("errText");
        var usr_lonlat;
        var map;
        if (navigator.geolocation){
            navigator.geolocation.getCurrentPosition(showPosition, errorHandler);
        } else {
            text.innerHTML = "Geolocation is not supported";
        }

        function showPosition(position) {
            usr_lonlat = position.coords.longitude + "," + position.coords.latitude;
            map = new OpenLayers.Map("mapHolder");
            map.addLayer(new OpenLayers.Layer.OSM());
            map.setCenter(usr_lonlat, 16);
        }

        function errorHandler(error) {
            switch(error.code) {
                case error.PERMISSION_DENIED:
                    text.innerHTML = "User denied the request for Geolocation."
                    break;
                case error.POSITION_UNAVAILABLE:
                    text.innerHTML = "Location information is unavailable."
                    break;
                case error.TIMEOUT:
                    text.innerHTML = "The request to get user location timed out."
                    break;
                case error.UNKNOWN_ERR:
                    text.innerHTML = "An unknown error occurred."
                    break;
            }
        }

    </script>
</body>
</html>