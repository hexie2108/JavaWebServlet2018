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
    <div id="mapHolder" style="height: 100%;" ></div>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDgVukW-2RaEvsY3lgFbKh4eiBlOZRlEgw&libraries=places"></script>
    <script>
        var text = document.getElementById("errText");
        var usr_lon;
        var usr_lat;
        var map;
        var infoWindow;

        if (navigator.geolocation){
            navigator.geolocation.getCurrentPosition(getPos, errorHandler);
        } else {
            text.innerHTML = "Geolocation is not supported";
        }

        initMap();

        function getPos(position) {
            usr_lon = position.coords.longitude;
            usr_lat = position.coords.latitude;
        }
        
        function initMap() {
            var center = new google.maps.LatLng(usr_lat, usr_lon);

            map = new google.maps.Map(document.getElementById('mapHolder'), {
                center: center,
                zoom: 15
            });

            var request = {
                location: center,
                radius: '800',
                type: ['restaurant']
            };

            infoWindow = new google.maps.InfoWindow();
            var service = new google.maps.places.PlacesService(map);
            service.nearbySearch(request, showMarker);
        }
        
        function showMarker(results, status) {
            if (status == google.maps.places.PlacesServiceStatus.OK){
                for (var i=0; i<results.length; i++){
                    createMarker(results[i]);
                }
            }
        }
        
        function createMarker(place) {
            var marker = new google.maps.Marker({
                map: map,
                position: place.geometry.location
            });

            google.maps.event.addListener(marker, 'click', function () {
                infoWindow.setContent(place.name);
                infoWindow.open(map, this);
            });
        }

        function errorHandler(error) {
            switch(error.code) {
                case error.PERMISSION_DENIED:
                    text.innerHTML = "User denied the request for Geolocation.";
                    break;
                case error.POSITION_UNAVAILABLE:
                    text.innerHTML = "Location information is unavailable.";
                    break;
                case error.TIMEOUT:
                    text.innerHTML = "The request to get user location timed out.";
                    break;
                case error.UNKNOWN_ERR:
                    text.innerHTML = "An unknown error occurred.";
                    break;
            }
        }

    </script>
</body>
</html>