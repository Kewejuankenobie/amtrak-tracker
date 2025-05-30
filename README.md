# AMV Train Tracker
## Description

The AMV Train Tracker is a web application to help users track their Amtrack and VIA Rail trains. You can visit the website, [AMV Train Tracker](amv-train-tracker.onrender.com/). This website is being hosted by a free hoster, so this site may ocassionally take a couple minutes to start showing data.

## Features

- View the status of Amtrak and VIA Rail Trains, including their location, speed, and if they're on time
- Check the schedule of train stations and find updated arrival and departure times

## Technologies

The frontend of this application is a Vite app using React and TypeScript, with the application being styled through Tailwind CSS.
The frontend displayes train locations using the Google Maps API.
The backend was build as a Java Springboot application using the REST API, and is deployed in a docker container. Station scheduling info is displayed using GTFS-RT data for both Amtrak and VIA Rail and comparing it with the static GTFS data of both railways.
Databasing is done through a PostgreSQL database. 

## Credits


## License
