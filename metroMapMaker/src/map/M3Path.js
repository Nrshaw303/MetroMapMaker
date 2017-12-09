/*
    M3Path.js

    This JavaScript object represents a complete path from one station to another in an M3Metro.
*/
function M3Path(initStartStation, initEndStation) {
    // THESE ARE PROVIDED EXTERNALLY AND ONCE WE HAVE THEM WE CAN BUILD OUR PATH
    this.startStation = initStartStation;
    this.endStation = initEndStation;
    this.STATION_COST = 3;
    this.TRANSFER_COST = 10;

    // OUR PATH WILL INCLUDE ALL THE DETAILS CONCERNING THE TRIP SUCH THAT IT
    // CAN BE DISPLAYED IN ALL SORTS OF CUSTOMIZED WAYS.

    // THESE ARE ALL THE LINES THAT ARE PART OF THE PATH.
    this.tripLines = new Array();
    this.tripLineNames = new Array();

    // THESE ARE ALL THE STATIONS VISITED WHEN FOLLOWING THE PATH
    this.tripStations = new Array();
    this.tripStationNames = new Array();

    // THESE ARE THE STATIONS AT WHICH TRANSFERS HAPPEN
    this.boardingStations = new Array();

    /*
        clone

        This function clones this path object, returning a path initialized with all the same data.
    */
    this.clone = function () {
        var clonedPath = new M3Path(this.startStation, this.endStation);
        for (var i = 0; i < this.tripLines.length; i++) {
            clonedPath.tripLines.push(this.tripLines[i]);
            clonedPath.tripLineNames[this.tripLines[i].name];
        }
        for (var i = 0; i < this.tripStations.length; i++) {
            clonedPath.tripStations.push(this.tripStations[i]);
            clonedPath.tripStationNames[this.tripStations[i].name];
        }
        for (var i = 0; i < this.boardingStations.length; i++) {
            clonedPath.boardingStations.push(this.boardingStations[i]);
        }
        return clonedPath;
    }

    /*
        addBoarding

        This function adds a boarding to the path. Note there is one boarding per line.
    */
    this.addBoarding = function (boardingLine, boardingStation) {
        // WE'LL NEED THE LINE AND GET THE NAMES TOO FOR QUICK LOOKUP
        this.tripLines.push(boardingLine);
        this.tripLineNames[boardingLine.name] = boardingLine.name;

        // THESE ARE THE STATIONS WHERE A PERSON WOULD BOARD A TRAIN
        this.boardingStations.push(boardingStation);
    }

    /*
        getTripStations

        This function is for getting an array with a list of all the stations to be
        visited, including boarding and passing through while on a train.
    */
    this.getTripStations = function () {
        // WE'LL RETURN AN ARRAY OF STATIONS AND WE'LL USE THE NAMES
        // FOR A QUICK LOOKUP
        this.tripStations = new Array();
        this.tripStationNames = new Array();

        // WE ONLY DO THIS IF WE HAVE A VALID TRIP
        if (this.isCompleteTrip()) {
            // IF WE MADE IT THIS FAR WE KNOW IT'S A GOOD TRIP
            var i = 0;
            while (i < this.boardingStations.length - 1) {
                var stationsToAdd = this.generateStationsForPathOnLine(
                    this.tripLines[i], this.boardingStations[i], this.boardingStations[i + 1]);
                for (var j = 0; j < stationsToAdd.length; j++) {
                    var stationToAdd = stationsToAdd[j];
                    if (!this.tripStationNames.hasOwnProperty(stationToAdd.name)) {
                        this.tripStations.push(stationToAdd);
                        this.tripStationNames[stationToAdd.name] = stationToAdd.name;
                    }
                }

                // ONTO THE NEXT LINE
                i++;
            }
            // AND NOW FOR THE LAST LINK IN THE CHAIN
            var stationsToAdd = this.generateStationsForPathOnLine(
                    this.tripLines[i], this.boardingStations[i], this.endStation);
            for (var i = 0; i < stationsToAdd.length; i++) {
                var stationToAdd = stationsToAdd[i];
                this.tripStations.push(stationToAdd);
            }
        }

        // RETURN THE STATIONS
        return this.tripStations;
    }

    /*
        isCompleteTrip

        This function tests to see if this path is complete, meaning one can get from its
        start station toe its end station using the boarding stops and trip lines.
    */
    this.isCompleteTrip = function () {
        if (this.tripLines.length == 0) {
            return false;
        }

        // THEN, IS THE END STATION ON THE LAST LINE? IF IT IS NOT THEN THE TRIP IS INCOMPLETE
        if (!this.tripLines[this.tripLines.length - 1].hasStation(this.endStation.name)) {
            return false;
        }

        // NOW, ARE ALL THE BOARDING STATIONS ON ALL THE TRIP LINES, IF NOT IT'S INCORRECT
        for (var i = 0; i < this.boardingStations.length; i++) {
            if (!this.tripLines[i].hasStation(this.boardingStations[i].name)) {
                return false;
            }
        }

        // IF WE MADE IT THIS FAR WE KNOW IT'S A COMPLETE TRIP'
        return true;
    }

    /*
        generateStationsForPathOnLine

        This function returns a list of all the stations to be visited to get from one station
        to another on the same line.
    */
    this.generateStationsForPathOnLine = function (line, station1, station2) {
        var stationsOnPath = new Array();
        var station1Index = line.getStationIndex(station1.name);
        var station2Index = line.getStationIndex(station2.name);
        
        // FOR CIRCULAR LINES WE CAN GO IN EITHER DIRECTION
        if (line.circular) {
            if (station1Index >= station2Index) {
                var forward = station1Index - station2Index;
                var reverse = station2Index + line.stations.length - station1Index;
                if (forward < reverse) {
                    for (var i = station1Index; i >= station2Index; i--) {
                        var stationToAdd = line.stations[i];
                        stationsOnPath.push(stationToAdd);
                    }
                }
                else {
                    for (var i = station1Index; i < line.stations.length; i++) {
                        var stationToAdd = line.stations[i];
                        stationsOnPath.push(stationToAdd);
                    }
                    for (var i = 0; i <= station2Index; i++) {
                        var stationToAdd = line.stations[i];
                        stationsOnPath.push(stationToAdd);
                    }
                }
            }
            // STILL CIRCULAR, BUT station1 IS BEFORE station2 IN THE ARRAY
            else {
                var forward = station2Index - station1Index;
                var reverse = station1Index + line.stations.length - station2Index;
                if (forward < reverse) {
                    for (var i = station1Index; i <= station2Index; i++) {
                        var stationToAdd = line.stations[i];
                        stationsOnPath.push(stationToAdd);
                    }
                }
                else {
                    for (var i = station1Index; i >= 0; i--) {
                        var stationToAdd = line.stations[i];
                        stationsOnPath.push(stationToAdd);
                    }
                    for (var i = line.stations.length - 1; i >= station2Index; i--) {
                        var stationToAdd = line.stations[i];
                        stationsOnPath.push(stationToAdd);
                    }
                }
            }
        }
        // NOT CIRCULAR
        else {
            if (station1Index >= station2Index) {
                for (var i = station1Index; i >= station2Index; i--) {
                    var stationToAdd = line.stations[i];
                    stationsOnPath.push(stationToAdd);
                }
            }
            else {
                for (var i = station1Index; i <= station2Index; i++) {
                    var stationToAdd = line.stations[i];
                    stationsOnPath.push(stationToAdd);
                }
            }
        }
        return stationsOnPath;
    }


    /*
        hasLine

        This function tests to see if this trip includes the testLineName. If it does, true
        is returned, otherwise false.
    */
    this.hasLine = function (testLineName) {
        return this.tripLineNames.hasOwnProperty(testLineName);
    }

    /*
        hasLineWithStation

        This function tests to see if this trip has a line with the testStationName 
        station on it. If so, true is returned, false otherwise.
    */
    this.hasLineWithStation = function (testStationName) {
        // GO THROUGH ALL THE LINES AND SEE IF IT'S IN ANY OF THEM'
        for (var i = 0; i < this.tripLines.length; i++) {
            if (this.tripLines[i].hasStation(testStationName)) {
                // YUP
                return true;
            }
        }
        // NOPE
        return false;
    }

    /*
        calculateTimeOfTrip

        This function calculates and returns the time of this trip taking into account
        the time it takes for a train to go from station to station is constant as is
        the time it takes to transfer lines.
    */
    this.calculateTimeOfTrip = function () {
        var stations = this.getTripStations();
        var stationsCost = (stations.length - 1) * this.STATION_COST;
        var transferCost = (this.tripLines.length - 1) * this.TRANSFER_COST;
        return stationsCost + transferCost;
    }
}