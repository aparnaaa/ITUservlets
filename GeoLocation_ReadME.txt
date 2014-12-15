GeolocationMapDB.html: Calls the servlet GetTheLocation to store the coordinates in the TRACKER_TABLE
GeolocationMapTrack.html: Calls the servlet GetTheLocation to retrieve the coordinates from the TRACKER_TABLE
GetTheLocation.java: Servlet needed by the above 2 HTMLs.

CREATE TABLE TRACKER_TABLE
  (
    DATE_TIME VARCHAR2(50 BYTE),
    LONGITUDE VARCHAR2(50 BYTE),
    LATITUDE  VARCHAR2(50 BYTE)
  )
