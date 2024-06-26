# MostWantedFugitives


<img src="src/main/resources/readMEImage.jpg" width="200px" height="200px">

# Description
<p>
This project aims to extract the fugitives information via selenium from 
<a href="https://www.terorarananlar.pol.tr/">Arananlar</a>.The project contains two different images
which are scraper and redis. The scraper image is responsible for extracting the information from the website and sending via RabbitMQ to the server.
The redis image is responsible for storing the extracted information so that duplicate information will not be sent via RabbitMQ. 
The server is responsible for storing the extracted information to the database and create an API to access the information.

# Table of Contents

- [Usage](#usage)
- [Future Improvement](#future-improvement)

# Usage
To run the project, you need to have git, docker and docker-compose installed on your machine. 
Then, you can run the following commands to start the project.

```bash
git clone https://github.com/saricaBugrahan/MostWantedFugitives
cd MostWantedFugitives
docker-compose up --build
```

# Future Improvement

The project can be improved in the following ways:
 - The project can be extended to scrape other websites as well.
 - The scraper image will be called via cronjob to scrape the website periodically.
 - Structure will be improved

